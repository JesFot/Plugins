package me.emp.command;

import java.util.ArrayList;
import java.util.List;

import me.emp.EliryumPlug;
import me.emp.utils.MWorldUtil;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MWorldCommand implements CommandExecutor
{
	private EliryumPlug emp;
	private String usageMessage = ChatColor.DARK_RED+
			"/<com> <list> or /<com> <set|tp> <worldName> [load|unload] [newSeed] [Env] [Type]";
	
	public MWorldCommand(EliryumPlug empl)
	{
		this.emp = empl;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!cmd.getName().equalsIgnoreCase("world"))
		{
			return false;
		}
		if(args.length == 0)
		{
			sender.sendMessage(this.usageMessage);
		}
		else
		{
			this.usageMessage = this.usageMessage.replaceAll("/<com>", "/"+label);
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
						w += "";
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
					MWorldUtil.loadWorld(this.emp, w, seed, wType, env);
				}
				else if(act.equalsIgnoreCase("unload"))
				{
					MWorldUtil.unloadWorld(this.emp, w);
				}
			}
			else if(args[0].equalsIgnoreCase("tp") && args.length >= 2)
			{
				MWorldUtil.tpToWorld(this.emp, sender, args.length>=3 ? args[2] : "", args[1]);
			}
			else if(args[0].equalsIgnoreCase("list"))
			{

				List<String> all = new ArrayList<String>();
				for(World tmp : this.emp.getServer().getWorlds())
				{
					all.add(tmp.getName());
				}
				sender.sendMessage(ChatColor.DARK_PURPLE+"All loaded worlds :");
				for(String msg : all)
				{
					sender.sendMessage(ChatColor.DARK_GREEN+" - "+msg);
				}
			}
			else
			{
				sender.sendMessage(this.usageMessage);
			}
			this.usageMessage = this.usageMessage.replaceAll("/"+label, "/<com>");
		}
		return true;
	}
}