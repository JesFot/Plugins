package fr.mpp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.mpp.MetalPonyPlug;

public class LogMessageCommand implements CommandExecutor
{
	private String usageMessage;
	private MetalPonyPlug mpp;
	
	public LogMessageCommand(MetalPonyPlug mppl)
	{
		this.mpp = mppl;
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
			this.mpp.getConfig().reloadCustomConfig();
			sender.sendMessage(this.mpp.getConfig().getCustomConfig().getString("logmsg", ""));
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
			this.mpp.getConfig().getCustomConfig().set("logmsg", msg);
			this.mpp.getConfig().saveCustomConfig();
		}
		else
		{
			sender.sendMessage(ChatColor.RED + this.usageMessage);
			return true;
		}
		return true;
	}
}
