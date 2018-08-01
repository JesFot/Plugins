package fr.gbp.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.gbp.GamingBlockPlug;
import fr.gbp.utils.GWorldUtil;
import fr.gbp.utils.UPlayer;

public class GWorldCommand implements CommandExecutor, TabCompleter
{
	private GamingBlockPlug gbp;
	private String usageMessage = ChatColor.RED +
			"/<com> list | /<com> tp <worldName> | /<com> set <worldName> <load|unload> [newSeed] [Env] [Type] | /<com> set <worldName> options <gm> <GameMode>";
	
	public GWorldCommand(GamingBlockPlug p_gbp)
	{
		this.gbp = p_gbp;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!cmd.getName().equalsIgnoreCase("world"))
		{
			return false;
		}
		if(args.length == 0)
		{
			sender.sendMessage(this.usageMessage.replaceAll("<com>", label));
		}
		else
		{
			if(args[0].equalsIgnoreCase("set") && args.length >= 3)
			{
				String w = args[1];
				String act = args[2];
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
				}
				if(act.equalsIgnoreCase("load"))
				{
					GWorldUtil.loadWorld(this.gbp, w, seed, wType, env);
				}
				else if(act.equalsIgnoreCase("unload"))
				{
					GWorldUtil.unloadWorld(this.gbp, w);
				}
				else if(act.equalsIgnoreCase("options"))
				{
					if(args[3].equalsIgnoreCase("gm"))
					{
						GameMode gm;
						if(args[4].equalsIgnoreCase("survival") || args[4].equalsIgnoreCase("0"))
						{
							gm = GameMode.SURVIVAL;
						}
						else if(args[4].equalsIgnoreCase("creative") || args[4].equalsIgnoreCase("1"))
						{
							gm = GameMode.CREATIVE;
						}
						else if(args[4].equalsIgnoreCase("adventure") || args[4].equalsIgnoreCase("2"))
						{
							gm = GameMode.ADVENTURE;
						}
						else if(args[4].equalsIgnoreCase("spectator") || args[4].equalsIgnoreCase("3"))
						{
							gm = GameMode.SPECTATOR;
						}
						else
						{
							gm = GameMode.SURVIVAL;
						}
						this.gbp.getWorldsConfig().reloadWorldConfig(w+".yml");
						this.gbp.getWorldsConfig().getWorldConfig(w+".yml").set("gamemode", gm.toString());
					}
				}
				else
				{
					sender.sendMessage(this.usageMessage.replaceAll("<com>", label));
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("tp") && args.length >= 2)
			{
				GWorldUtil.tpToWorld(gbp, sender, args.length >= 3 ? args[2] : "", args[1]);
			}
			else if(args[0].equalsIgnoreCase("list"))
			{
				List<String> all = new ArrayList<String>();
				for(World tmp : this.gbp.getServer().getWorlds())
				{
					all.add(tmp.getName());
				}
				sender.sendMessage(ChatColor.DARK_PURPLE + "All loaded worlds :");
				for(String msg : all)
				{
					sender.sendMessage(ChatColor.DARK_GREEN + " - " + msg);
				}
			}
			else
			{
				sender.sendMessage(this.usageMessage.replaceAll("<com>", label));
			}
		}
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args)
	{
		List<String> result = new ArrayList<String>();
		List<String> all = new ArrayList<String>();
		List<String> allP = new ArrayList<String>();
		for(World tmp : this.gbp.getServer().getWorlds())
		{
			all.add(tmp.getName());
		}
		for(Player tmp : UPlayer.getOnlinePlayers())
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
			result.add("tp");
			break;
		case 2:
			if(args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("set"))
			{
				result.addAll(all);
			}
			break;
		case 3:
			if(args[0].equalsIgnoreCase("set"))
			{
				result.add("load");
				result.add("unload");
				result.add("options");
			}
			else if(args[0].equalsIgnoreCase("tp"))
			{
				result.addAll(allP);
			}
			break;
		case 4:
			if(args[0].equalsIgnoreCase("set") && args[2].equalsIgnoreCase("options"))
			{
				result.add("gm");
			}
			break;
		case 5:
			if(args[0].equalsIgnoreCase("set") && args[2].equalsIgnoreCase("load"))
			{
				result.add("nether");
				result.add("end");
				result.add("normal");
			}
			else if(args[0].equalsIgnoreCase("set") && args[2].equalsIgnoreCase("options") && args[3].equalsIgnoreCase("gm"))
			{
				result.add("Adventure");
				result.add("Spectator");
				result.add("Creative");
				result.add("Survival");
			}
			break;
		case 6:
			if(args[0].equalsIgnoreCase("set") && args[2].equalsIgnoreCase("load"))
			{
				result.add("large");
				result.add("flat");
				result.add("normal");
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