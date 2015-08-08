package me.emp.command;

import me.emp.EliryumPlug;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LogMessageCommand implements CommandExecutor
{
	private String usageMessage;
	private EliryumPlug emp;
	
	public LogMessageCommand(EliryumPlug empl)
	{
		this.emp = empl;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		this.usageMessage = "Usage : /"+label+" [set <text...>]";
		if(!cmd.getName().equalsIgnoreCase("logmessage"))
		{
			return false;
		}
		if(args.length == 0)
		{
			this.emp.getConfig().reloadCustomConfig();
			if(this.emp.getConfig().getCustomConfig().contains("logmsg") && this.emp.getConfig().getCustomConfig().getString("logmsg") != null)
			{
				String logmsg = this.emp.getConfig().getCustomConfig().getString("logmsg");
				String[] logmsgs = logmsg.split(" n ");
				for(String str : logmsgs)
				{
					sender.sendMessage(ChatColor.GOLD + str);
				}
			}
			else
			{
				sender.sendMessage(ChatColor.BOLD + "No log-message registered");
			}
		}
		else if(args.length >= 2)
		{
			if(!args[0].equalsIgnoreCase("set"))
			{
				sender.sendMessage(ChatColor.RED + this.usageMessage);
				return true;
			}
			String msg = "";
			for(int i = 1; i < args.length; i++)
			{
				if(args[i].toLowerCase().startsWith("http"))
				{
					args[i] = ChatColor.BLUE + "" + ChatColor.UNDERLINE + args[i] + ChatColor.RESET;
				}
				msg += args[i] + " ";
			}
			this.emp.getConfig().getCustomConfig().set("logmsg", msg);
			this.emp.getConfig().saveCustomConfig();
		}
		else
		{
			sender.sendMessage(ChatColor.RED + this.usageMessage);
			return true;
		}
		return true;
	}
}
