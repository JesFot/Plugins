package fr.jesfot.gbp.permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.permissions.Permission;

public class PermissionsHelper
{
	public static boolean testPermission(CommandSender sender, Permission permission, boolean opByPass, String permissionMessage)
	{
		if(testPermissionSilent(sender, permission, opByPass))
		{
			return true;
		}
		
		if(permissionMessage == null)
		{
			sender.sendMessage(ChatColor.RED +
			"I'm sorry, but you do not have permission to perform this command. "
			+ "Please contact the server administrators if you believe that this is an error.");
		}
		else if(permissionMessage.length() != 0)
		{
			for(String line : permissionMessage.replace("<permission>", permission.getName()).split("\n"))
			{
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
			}
		}
		return false;
	}
	
	public static boolean testPermission(CommandSender sender, String permissions, boolean opByPass, String permissionMessage)
	{
		if(testPermissionSilent(sender, permissions, opByPass))
		{
			return true;
		}
		
		if(permissionMessage == null)
		{
			sender.sendMessage(ChatColor.RED +
			"I'm sorry, but you do not have permissions to perform this command. "
			+ "Please contact the server administrators if you believe that this is an error.");
		}
		else if(permissionMessage.length() != 0)
		{
			for(String line : permissionMessage.replace("<permission>", permissions).split("\n"))
			{
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
			}
		}
		return false;
	}
	
	public static boolean testPermissionSilent(CommandSender sender, Permission permission, boolean opByPass)
	{
		if(permission == null)
		{
			return false;
		}
		if(sender instanceof ConsoleCommandSender)
		{
			return true;
		}
		else if(sender instanceof RemoteConsoleCommandSender)
		{
			return true;
		}
		else
		{
			if(sender.isOp() && opByPass)
			{
				return true;
			}
			return sender.hasPermission(permission);
		}
	}
	
	public static boolean testPermissionSilent(CommandSender sender, String permissions, boolean opByPass)
	{
		if(permissions == null || permissions.length() == 0)
		{
			return false;
		}
		for(String p : permissions.split(";"))
		{
			if(testPermissionSilent(sender, Bukkit.getPluginManager().getPermission(p), opByPass))
			{
				return true;
			}
		}
		return false;
	}
}