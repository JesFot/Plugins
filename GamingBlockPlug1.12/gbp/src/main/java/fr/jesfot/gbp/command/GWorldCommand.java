package fr.jesfot.gbp.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import fr.jesfot.gbp.GamingBlockPlug_1_12;
import fr.jesfot.gbp.configuration.NBTConfig;
import fr.jesfot.gbp.configuration.NBTSubConfig;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.permission.PermissionsHelper;
import fr.jesfot.gbp.utils.Utils;
import fr.jesfot.gbp.world.WorldComparator;
import fr.jesfot.gbp.world.WorldLoader;
import fr.jesfot.gbp.world.WorldTeleporter;

public class GWorldCommand extends CommandBase
{
	private GamingBlockPlug_1_12 gbp;
	private Permission worldPerm;
	private String usageMessage =
			"/<com> list | /<com> tp <worldName> | /<com> set <worldName> <load|unload> [newSeed] [Env] [Type] |"
			+ " /<com> set <worldName> options <gm|group> <Value>";
	
	public GWorldCommand(GamingBlockPlug_1_12 plugin)
	{
		super("world");
		this.gbp = plugin;
		this.setRawUsageMessage(this.usageMessage);
		Permission world = plugin.getPermissionManager().addPermission("GamingBlockPlug.worlds", PermissionDefault.OP, "Worlds' permission", Permissions.globalGBP);
		plugin.getPermissionManager().addPermission("GamingBlockPlug.worlds.tp", PermissionDefault.TRUE, "Allows you to tp yourself between worlds", world);
		plugin.getPermissionManager().addPermission("GamingBlockPlug.worlds.load", PermissionDefault.OP, "Allows you to generate or unload worlds", world);
		plugin.getPermissionManager().addPermission("GamingBlockPlug.worlds.conf", PermissionDefault.OP, "Allows you to change worlds' options", world);
		this.worldPerm = world;
	}
	
	public void registerWorldsPublicPermsS(List<String> worlds)
	{
		for(String w : worlds)
		{
			this.gbp.getPermissionManager().addPermission(Permissions.GBP_PERMS + ".worlds.tpto." + w, PermissionDefault.TRUE,
					"Nothing to say", this.worldPerm);
		}
	}
	
	public void registerWorldsPublicPerms(List<World> worlds)
	{
		for(World w : worlds)
		{
			NBTSubConfig world = new NBTSubConfig(this.gbp.getConfigFolder("worldsdatas"), w.getName());
			String perm = world.readNBTFromFile().getCopy().getString("PermName");
			if(perm == null)
			{
				perm = w.getName().toLowerCase().trim();
				world.setString("PermName", w.getName().toLowerCase().trim()).writeNBTToFile();
			}
			this.gbp.getPermissionManager().addPermission(Permissions.GBP_PERMS + ".worlds.tpto." + perm, PermissionDefault.TRUE,
					"Nothing to say", this.worldPerm);
		}
	}
	
	public void registerWorldsPrivatePermsS(List<String> worlds)
	{
		for(String w : worlds)
		{
			this.gbp.getPermissionManager().addPermission(Permissions.GBP_PERMS + ".worlds.tpto." + w, PermissionDefault.OP,
					"Nothing to say", this.worldPerm);
		}
	}
	
	public void registerWorldsPrivatePerms(List<World> worlds)
	{
		for(World w : worlds)
		{
			NBTSubConfig world = new NBTSubConfig(this.gbp.getConfigFolder("worldsdatas"), w.getName());
			String perm = world.readNBTFromFile().getCopy().getString("PermName");
			if(perm == null)
			{
				perm = w.getName().toLowerCase().trim();
				world.setString("PermName", w.getName().toLowerCase().trim()).writeNBTToFile();
			}
			this.gbp.getPermissionManager().addPermission(Permissions.GBP_PERMS + ".worlds.tpto." + perm, PermissionDefault.OP,
					"Nothing to say", this.worldPerm);
		}
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(args.length == 0)
		{
			this.sendUsage(sender, label);
			return true;
		}
		if(sender instanceof Player)
		{
			NBTConfig playerCfg = new NBTConfig(this.gbp.getConfigFolder("playerdatas"), ((Player)sender).getUniqueId());
			String teamName = playerCfg.readNBTFromFile().getCopy().getString("Team");
			if(!this.gbp.getTeams().getIfExists(teamName).canUseWorld())
			{
				sender.sendMessage("You are not allowed to use that command.");
				return true;
			}
		}
		if(args[0].equalsIgnoreCase("tp") && args.length >= 2)
		{
			String argu = args.length >= 3 ? args[2] : "";
			if(!argu.startsWith("@"))
			{
				if(argu.equalsIgnoreCase(""))
				{
					argu = sender.getName();
				}
			}
			else
			{
				Player[] pls = {};
				pls = Utils.getPlayers(sender, argu).toArray(new Player[]{});
				argu = pls[0].getName();
				for(int i = 1; i < (pls.length - 1); i++)
				{
					argu += ", " + pls[i].getName();
				}
				argu += " and " + pls[pls.length - 1].getName();
			}
			if(!PermissionsHelper.testPermission(sender, "GamingBlockPlug.worlds.tp", true, null))
			{
				GamingBlockPlug_1_12.getMyLogger().info(sender.getName() + " tried to use /"
						+ command.getName() + " " + Utils.compile(args, 0, " "));
				return true;
			}
			GamingBlockPlug_1_12.getMyLogger().info(sender.getName() + " used /"
					+ command.getName() + " " + Utils.compile(args, 0, " "));
			if(WorldTeleporter.tpToWorld(gbp, sender, args.length >= 3 ? args[2] : "", args[1]))
			{
				Command.broadcastCommandMessage(sender,"Teleported "+ argu +" to the world "+args[1],true);
			}
			else
			{
				Command.broadcastCommandMessage(sender, argu + " tried teleportation to world "+args[1]
						+ " but unseccessful",true);
			}
		}
		else if(args[0].equalsIgnoreCase("list"))
		{
			Map<String, Integer> loaded = new HashMap<String, Integer>();
			for(World tmp : this.gbp.getServer().getWorlds())
			{
				loaded.put(tmp.getName(), Integer.valueOf(tmp.getPlayers().size()));
			}
			List<String> unloaded = Arrays.asList(WorldComparator.getWorldFilesList(gbp, loaded.keySet()));
			sender.sendMessage(ChatColor.DARK_PURPLE + "All loaded worlds :");
			for(Entry<String, Integer> msg : loaded.entrySet())
			{
				sender.sendMessage(ChatColor.DARK_GREEN + " - " + msg.getKey() + " (" + msg.getValue() + "pls)");
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
					GamingBlockPlug_1_12.getMyLogger().info(sender.getName() + " tried to use /"
							+ command.getName() + " " + Utils.compile(args, 0, " "));
					return true;
				}
				GamingBlockPlug_1_12.getMyLogger().info(sender.getName() + " used /"
						+ command.getName() + " " + Utils.compile(args, 0, " "));
				String seed = null;
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
					GamingBlockPlug_1_12.getMyLogger().info(sender.getName() + " tried to use /"
							+ command.getName() + " " + Utils.compile(args, 0, " "));
					return true;
				}
				GamingBlockPlug_1_12.getMyLogger().info(sender.getName() + " used /"
						+ command.getName() + " " + Utils.compile(args, 0, " "));
				WorldLoader.unloadWorld(gbp, w);
			}
			else if(act.equalsIgnoreCase("options") && args.length >= 5)
			{
				if(!PermissionsHelper.testPermission(sender, "GamingBlockPlug.worlds.conf", true, null))
				{
					GamingBlockPlug_1_12.getMyLogger().info(sender.getName() + " tried to use /"
							+ command.getName() + " " + Utils.compile(args, 0, " "));
					return true;
				}
				GamingBlockPlug_1_12.getMyLogger().info(sender.getName() + " used /"
						+ command.getName() + " " + Utils.compile(args, 0, " "));
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
				this.sendUsage(sender, label);
			}
		}
		else
		{
			this.sendUsage(sender, label);
		}
		return true;
	}

	@Override
	public List<String> executeTabComplete(CommandSender sender, Command cmd, String alias, String[] args)
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
