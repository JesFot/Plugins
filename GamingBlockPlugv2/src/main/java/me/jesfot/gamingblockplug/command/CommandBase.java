package me.jesfot.gamingblockplug.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.permissions.Permission;

import me.jesfot.gamingblockplug.permission.PermissionHelper;
import me.unei.lang.plugin.UneiLang;

public abstract class CommandBase implements CommandExecutor, TabCompleter
{
	private final String name;
	
	private String defaultUsageMessage = null;
	
	private boolean enabled = true;
	
	private String disabledMessage = "Command disabled !";
	
	private Permission permission = null;
	private boolean opBypass = false;
	
	protected CommandBase(final String cmdBaseName)
	{
		this.name = cmdBaseName;
	}
	
	protected final void setMinimalPermission(Permission permission)
	{
		this.permission = permission;
	}
	
	protected final void setMinimalPermission(String permission)
	{
		if (permission != null)
		{
			this.setMinimalPermission(Bukkit.getPluginManager().getPermission(permission));
		}
		else
		{
			this.permission = null;
		}
	}
	
	protected final void setMinimalPermission(Permission permission, boolean opBypass)
	{
		this.permission = permission;
		this.opBypass = opBypass;
	}
	
	protected final void setMinimalPermission(String permission, boolean opBypass)
	{
		if (permission != null)
		{
			this.setMinimalPermission(Bukkit.getPluginManager().getPermission(permission));
		}
		else
		{
			this.permission = null;
		}
		this.opBypass = opBypass;
	}
	
	public final Permission getMinimalPermission()
	{
		return this.permission;
	}
	
	public final boolean opBypass()
	{
		return this.opBypass;
	}
	
	protected final void setRawUsageMessage(final String usage)
	{
		this.defaultUsageMessage = usage;
	}
	
	public final String getRawUsageMessage()
	{
		return this.defaultUsageMessage;
	}
	
	public String getUsageMessage(UUID player)
	{
		return UneiLang.getInstance().getPlayerManager().getLanguage(player).get("command." + this.name + ".usage", this.defaultUsageMessage);
	}
	
	public String getFormattedRawUsageMessage(ChatColor color, final String label)
	{
		if(color == null)
		{
			color = ChatColor.RESET;
		}
		return color + "Usage: " + this.getRawUsageMessage().replaceAll("<command>", label) + ChatColor.RESET;
	}
	
	public String getFormattedUsageMessage(ChatColor color, final String label, UUID player)
	{
		if(color == null)
		{
			color = ChatColor.RESET;
		}
		return color + "Usage: " + this.getUsageMessage(player).replaceAll("<command>", label) + ChatColor.RESET;
	}
	
	protected void sendUsage(CommandSender sender, ChatColor color, String label)
	{
		if (sender instanceof Entity)
		{
			sender.sendMessage(this.getFormattedUsageMessage(color, label, ((Entity) sender).getUniqueId()));
		}
		else
		{
			sender.sendMessage(this.getFormattedRawUsageMessage(color, label));
		}
	}
	
	
	protected void sendUsage(CommandSender sender, String label)
	{
		this.sendUsage(sender, ChatColor.RED, label);
	}
	
	public void disableCommand()
	{
		this.enabled = false;
	}
	
	public static String color(String msg)
	{
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public void disableCommand(String message)
	{
		this.enabled = false;
		this.disabledMessage = message;
	}
	
	@Override
	public final List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		if (!this.enabled)
		{
			sender.sendMessage(ChatColor.DARK_RED + this.disabledMessage);
			return Collections.emptyList();
		}
		if (this.getMinimalPermission() != null)
		{
			if (!PermissionHelper.testPermissionSilent(sender, this.getMinimalPermission(), this.opBypass()))
			{
				return Collections.emptyList();
			}
		}
		return this.executeTabComplete(sender, command, alias, args);
	}
	
	protected List<String> executeTabComplete(CommandSender sender, Command command, String label, String[] args)
	{
		return null;
	}
	
	@Override
	public final boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (!this.enabled)
		{
			sender.sendMessage(ChatColor.DARK_RED + this.disabledMessage);
			return true;
		}
		if (this.getMinimalPermission() != null)
		{
			if (!PermissionHelper.testPermission(sender, this.getMinimalPermission(), this.opBypass(), null))
			{
				return true;
			}
		}
		return this.executeCommand(sender, command, label, args);
	}
	
	protected abstract boolean executeCommand(CommandSender sender, Command command, String label, String[] args);
	
	public final String getName()
	{
		return this.name;
	}
	
	public static List<String> sortStart(String startOfString, List<String> listToSort)
	{
		Iterator<String> it = listToSort.iterator();
		startOfString = startOfString.toLowerCase();
		while (it.hasNext())
		{
			if (!it.next().toLowerCase().startsWith(startOfString))
			{
				try
				{
					it.remove();
				}
				catch (UnsupportedOperationException ignored)
				{
					return CommandBase.sortWithRebuild(startOfString, listToSort);
				}
			}
		}
		return listToSort;
	}
	
	private static List<String> sortWithRebuild(String startOfString, List<String> listToSort)
	{
		List<String> result = new ArrayList<String>();
		Iterator<String> it = listToSort.iterator();
		startOfString = startOfString.toLowerCase();
		while (it.hasNext())
		{
			String current = it.next();
			if (current.toLowerCase().startsWith(startOfString))
			{
				result.add(current);
			}
		}
		return result;
	}
}
