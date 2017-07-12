package fr.jesfot.gbp.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public abstract class CommandBase implements CommandExecutor, TabCompleter
{
	private final String name;
	
	private final String[] othersCommad;
	
	private String usageMessage = null;
	
	private boolean enabled = true;
	
	private String disabledMessage = "Command desactivated !";
	
	protected CommandBase(final String cmdBaseName)
	{
		this.name = cmdBaseName;
		this.othersCommad = new String[]{};
	}
	
	protected void setRawUsageMessage(final String usage)
	{
		this.usageMessage = usage;
	}
	
	protected String getRawUsageMessage()
	{
		return this.usageMessage;
	}
	
	protected String getUsageMessage(ChatColor color, final String label)
	{
		if(color == null)
		{
			color = ChatColor.RESET;
		}
		return color + "Usage: " + this.getRawUsageMessage().replaceAll("<com>", label) + ChatColor.RESET;
	}
	
	protected void sendUsage(CommandSender sender, ChatColor color, String label)
	{
		sender.sendMessage(this.getUsageMessage(color, label));
	}
	
	
	protected void sendUsage(CommandSender sender, String label)
	{
		this.sendUsage(sender, ChatColor.RED, label);
	}
	
	public void disableCommand()
	{
		this.enabled = false;
	}
	
	public String color(String msg)
	{
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public void disableCommand(final String message)
	{
		this.enabled = false;
		this.disabledMessage = message;
	}
	
	protected CommandBase(final String cmdBaseName, String...othersCmds)
	{
		this.name = cmdBaseName;
		this.othersCommad = othersCmds;
	}
	
	public final List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		if(!this.enabled)
		{
			sender.sendMessage(ChatColor.DARK_RED + this.disabledMessage);
			return Collections.emptyList();
		}
		return this.executeTabComplete(sender, command, alias, args);
	}
	
	public List<String> executeTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return Collections.emptyList();
	}
	
	public final boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(!this.enabled)
		{
			sender.sendMessage(ChatColor.DARK_RED + this.disabledMessage);
			return true;
		}
		return this.executeCommand(sender, command, label, args);
	}

	public abstract boolean executeCommand(CommandSender sender, Command command, String label, String[] args);
	
	public final String getName()
	{
		return this.name;
	}
	
	public final boolean hasOtherNames()
	{
		return this.othersCommad.length>0;
	}
	
	public final String[] getOthersNames()
	{
		return this.othersCommad;
	}
	
	public final String getOtherName(final int index)
	{
		return this.othersCommad[index];
	}
	
	public final List<String> getPlayers(final String startOfName)
	{
		String name = startOfName.toLowerCase();
		List<String> pls = new ArrayList<String>();
		for(Player player : Bukkit.getOnlinePlayers())
		{
			String pl = player.getName().toLowerCase();
			if(pl.startsWith(name))
			{
				pls.add(player.getName());
			}
		}
		return pls;
	}
	
	public final List<String> sortStart(final String startOfString, List<String> listToSort)
	{
		List<String> result = new ArrayList<String>();
		for(String str : listToSort)
		{
			if(str.toLowerCase().startsWith(startOfString.toLowerCase()))
			{
				result.add(str);
			}
		}
		return result;
	}
}