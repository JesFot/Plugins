package fr.jesfot.gbp.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.configuration.NBTSubConfig;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.permission.PermissionsHelper;
import fr.jesfot.gbp.world.WorldComparator;
import fr.jesfot.gbp.world.WorldLoader;
import fr.jesfot.gbp.world.WorldTeleporter;

public class GWorldCommand extends CommandBase
{
	private GamingBlockPlug_1_9 gbp;
	private String usageMessage = ChatColor.RED +
			"/<com> list | /<com> tp <worldName> | /<com> set <worldName> <load|unload> [newSeed] [Env] [Type] |"
			+ " /<com> set <worldName> options <gm|group> <Value>";
	
	public GWorldCommand(GamingBlockPlug_1_9 plugin)
	{
		super("world");
		this.gbp = plugin;
		Permission world = plugin.getPermissionManager().addPermission("GamingBlockPlug.worlds", PermissionDefault.OP, "Worlds' permission", Permissions.globalGBP);
		plugin.getPermissionManager().addPermission("GamingBlockPlug.worlds.tp", PermissionDefault.TRUE, "Allows you to tp yourself between worlds", world);
		plugin.getPermissionManager().addPermission("GamingBlockPlug.worlds.load", PermissionDefault.OP, "Allows you to generate or unload worlds", world);
		plugin.getPermissionManager().addPermission("GamingBlockPlug.worlds.conf", PermissionDefault.OP, "Allows you to change worlds' options", world);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(args.length == 0)
		{
			sender.sendMessage(this.usageMessage.replaceAll("<com>", label));
			return true;
		}
		if(args[0].equalsIgnoreCase("tp") && args.length >= 2)
		{
			if(!PermissionsHelper.testPermission(sender, "GamingBlockPlug.worlds.tp", true, null))
			{
				return true;
			}
			WorldTeleporter.tpToWorld(gbp, sender, args.length >= 3 ? args[2] : "", args[1]);
			Command.broadcastCommandMessage(sender,"Teleported "+sender.getName()+" to the world "+args[1],true);
		}
		else if(args[0].equalsIgnoreCase("list"))
		{
			List<String> loaded = new ArrayList<String>();
			for(World tmp : this.gbp.getServer().getWorlds())
			{
				loaded.add(tmp.getName());
			}
			List<String> unloaded = Arrays.asList(WorldComparator.getWorldFilesList(gbp, loaded));
			sender.sendMessage(ChatColor.DARK_PURPLE + "All loaded worlds :");
			for(String msg : loaded)
			{
				sender.sendMessage(ChatColor.DARK_GREEN + " - " + msg);
			}
			sender.sendMessage(ChatColor.DARK_PURPLE + "All unloaded worlds :");
			for(String msg : unloaded)
			{
				sender.sendMessage(ChatColor.DARK_GREEN + " - " + msg);
			}
		}
		else if(args[0].equalsIgnoreCase("set") && args.length >= 3)
		{
			String w = args[1];
			String act = args[2];
			if(act.equalsIgnoreCase("load"))
			{
				if(!PermissionsHelper.testPermission(sender, "GamingBlockPlug.worlds.load", false, null))
				{
					return true;
				}
				String seed = "";
				WorldType wType = WorldType.NORMAL;
				World.Environment env = World.Environment.NORMAL;
				if(args.length >= 4)
				{
					seed = args[3];
				}
				if(args.length >= 5)
				{
					if(args[4].equalsIgnoreCase("nether"))
					{
						env = World.Environment.NETHER;
						w += w.endsWith("_nether") ? "" : "_nether";
					}
					else if(args[4].equalsIgnoreCase("end"))
					{
						env = World.Environment.THE_END;
						w += w.endsWith("_end") ? "" : "_end";
					}
					else if(args[4].equalsIgnoreCase("normal"))
					{
						env = World.Environment.NORMAL;
					}
				}
				if(args.length >= 6)
				{
					if(args[5].equalsIgnoreCase("normal"))
					{
						wType = WorldType.NORMAL;
					}
					else if(args[5].equalsIgnoreCase("flat"))
					{
						wType = WorldType.FLAT;
					}
					else if(args[5].equalsIgnoreCase("large"))
					{
						wType = WorldType.LARGE_BIOMES;
					}
					else if(args[5].equalsIgnoreCase("amplified"))
					{
						wType = WorldType.AMPLIFIED;
					}
				}
				WorldLoader.loadWorld(gbp, w, seed, wType, env);
			}
			else if(act.equalsIgnoreCase("unload"))
			{
				if(!PermissionsHelper.testPermission(sender, "GamingBlockPlug.worlds.load", true, null))
				{
					return true;
				}
				WorldLoader.unloadWorld(gbp, w);
			}
			else if(act.equalsIgnoreCase("options") && args.length >= 5)
			{
				if(!PermissionsHelper.testPermission(sender, "GamingBlockPlug.worlds.conf", true, null))
				{
					return true;
				}
				String opt = args[3];
				String val = args[4];
				if(opt.equalsIgnoreCase("gm") || opt.equalsIgnoreCase("gamemode"))
				{
					WorldComparator.setDefaultGamemode(gbp, w, val);
				}
				else if(opt.equalsIgnoreCase("autounload") || opt.equalsIgnoreCase("aul"))
				{
					WorldComparator.setAutoUnload(gbp, w, new Boolean(val).booleanValue());
				}
				else if(opt.equalsIgnoreCase("group"))
				{
					NBTSubConfig world = new NBTSubConfig(gbp.getConfigFolder("worldsdatas"), w).readNBTFromFile();
					world.setString("Group", val).writeNBTToFile();
				}
				else if(opt.equalsIgnoreCase("keepinventory") || opt.equalsIgnoreCase("keepinv"))
				{
					WorldComparator.setKeepInventory(gbp, w, new Boolean(val).booleanValue());
				}
				else if(opt.equalsIgnoreCase("keeplastlocation") || opt.equalsIgnoreCase("keeploc"))
				{
					WorldComparator.setKeepLocation(gbp, w, new Boolean(val).booleanValue());
				}
				else if(opt.equalsIgnoreCase("changespawnpoint") || opt.equalsIgnoreCase("changesp"))
				{
					WorldComparator.setChangeBedSpawn(gbp, w, new Boolean(val).booleanValue());
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "Options available: GameMode, Group, autoUnload, keepInventory, keepLastLocation"
							+ " and changeSpawnPoint");
				}
			}
			else
			{
				sender.sendMessage(this.usageMessage.replaceAll("<com>", label));
			}
		}
		else
		{
			sender.sendMessage(this.usageMessage.replaceAll("<com>", label));
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args)
	{
		List<String> result = new ArrayList<String>();
		List<String> loaded = new ArrayList<String>();
		for(World tmp : this.gbp.getServer().getWorlds())
		{
			loaded.add(tmp.getName());
		}
		List<String> unloaded = Arrays.asList(WorldComparator.getWorldFilesList(gbp, loaded));
		List<String> allP = new ArrayList<String>();
		for(Player tmp : this.gbp.getOnlinePlayers())
		{
			allP.add(tmp.getName());
		}
		if(args.length == 0)
		{
			return null;
		}
		switch(args.length)
		{
		case 1:
			result.add("list");
			result.add("set");
			if(PermissionsHelper.testPermissionSilent(sender, "GamingBlockPlug.worlds.tp", true))
			{
				result.add("tp");
			}
			break;
		case 2:
			if(args[0].equalsIgnoreCase("tp") && PermissionsHelper.testPermissionSilent(sender, "GamingBlockPlug.worlds.tp", true))
			{
				result.addAll(loaded);
			}
			else if(args[0].equalsIgnoreCase("set"))
			{
				result.addAll(loaded);
				result.addAll(unloaded);
			}
			break;
		case 3:
			if(args[0].equalsIgnoreCase("set"))
			{
				if(PermissionsHelper.testPermissionSilent(sender, "GamingBlockPlug.worlds.load", false))
				{
					result.add("load");
				}
				if(PermissionsHelper.testPermissionSilent(sender, "GamingBlockPlug.worlds.load", true))
				{
					result.add("unload");
				}
				if(PermissionsHelper.testPermissionSilent(sender, "GamingBlockPlug.worlds.conf", true))
				{
					result.add("options");
				}
			}
			else if(args[0].equalsIgnoreCase("tp") && PermissionsHelper.testPermissionSilent(sender, "GamingBlockPlug.worlds.tp", true))
			{
				result.addAll(allP);
			}
			break;
		case 4:
			if(args[0].equalsIgnoreCase("set") && args[2].equalsIgnoreCase("options") && PermissionsHelper.testPermissionSilent(sender, "GamingBlockPlug.worlds.conf", true))
			{
				result.add("GameMode");
				result.add("Group");
				result.add("autoUnload");
				result.add("keepInventory");
				result.add("keepLastLocation");
				result.add("changeSpawnPoint");
			}
			break;
		case 5:
			if(args[0].equalsIgnoreCase("set") && args[2].equalsIgnoreCase("load") && PermissionsHelper.testPermissionSilent(sender, "GamingBlockPlug.worlds.load", false))
			{
				result.add("nether");
				result.add("end");
				result.add("normal");
			}
			else if(args[0].equalsIgnoreCase("set") && args[2].equalsIgnoreCase("options") && PermissionsHelper.testPermissionSilent(sender, "GamingBlockPlug.worlds.conf", true))
			{
				if(args[3].equalsIgnoreCase("gamemode") || args[3].equalsIgnoreCase("gm"))
				{
					result.add("Adventure");
					result.add("Spectator");
					result.add("Creative");
					result.add("Survival");
				}
				else if(args[3].equalsIgnoreCase("autounload") || args[3].equalsIgnoreCase("aul") ||
						args[3].equalsIgnoreCase("keepinventory") || args[3].equalsIgnoreCase("keepinv") ||
						args[3].equalsIgnoreCase("keeplastlocation") || args[3].equalsIgnoreCase("keeploc") ||
						args[3].equalsIgnoreCase("changespawnpoint") || args[3].equalsIgnoreCase("changesp"))
				{
					result.add("false");
					result.add("true");
				}
			}
			break;
		case 6:
			if(args[0].equalsIgnoreCase("set") && args[2].equalsIgnoreCase("load") && PermissionsHelper.testPermissionSilent(sender, "GamingBlockPlug.worlds.load", false))
			{
				result.add("large");
				result.add("flat");
				result.add("normal");
				result.add("amplified");
			}
			break;
		}
		List<String> r2 = new ArrayList<String>();
		for(String str : result)
		{
			if(str.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
			{
				r2.add(str);
			}
		}
		return r2;
	}
}
