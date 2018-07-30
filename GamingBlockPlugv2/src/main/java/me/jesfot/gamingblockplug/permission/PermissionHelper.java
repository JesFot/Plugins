package me.jesfot.gamingblockplug.permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;

import me.unei.lang.api.Language;
import me.unei.lang.plugin.UneiLang;

public final class PermissionHelper
{
	/**
	 * Tests if a permissible has a specific permission.
	 * 
	 * @param object The permissible to check.
	 * @param permission The permission to lookup.
	 * @param opByPass If an operator permissible should bypass the permission.
	 * @return Returns `true` if the permissible has this permission, `false` otherwise.
	 */
	public static boolean testPermissionSilent(Permissible object, Permission permission, boolean opByPass)
	{
		if (permission == null)
		{
			return true;
		}
		if (object instanceof ConsoleCommandSender
				|| object instanceof RemoteConsoleCommandSender
				|| object instanceof BlockCommandSender)
		{
			return true;
		}
		else
		{
			if (object.isOp() && opByPass)
			{
				return true;
			}
			return object.hasPermission(permission);
		}
	}

	/**
	 * Tests if a permissible has some specific(s) permission(s).
	 * <p>
	 * To test multiple permissions, separate them using ';'.
	 * 
	 * @param object The permissible to check.
	 * @param permissions The permission(s) to lookup.
	 * @param opByPass If an operator permissible should bypass the permission(s).
	 * @return Returns `true` if the permissible has one of the permission(s), `false` otherwise.
	 */
	public static boolean testPermissionSilent(Permissible object, String permissions, boolean opByPass)
	{
		if (permissions == null || permissions.isEmpty())
		{
			return false;
		}
		for (String p : permissions.split(";"))
		{
			if (testPermissionSilent(object, Bukkit.getPluginManager().getPermission(p), opByPass))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean testPermission(Permissible object, Permission permission, boolean opByPass, String permissionMessage)
	{
		if(testPermissionSilent(object, permission, opByPass))
		{
			return true;
		}
		
		if (object instanceof CommandSender)
		{
			if(permissionMessage == null)
			{
				Language lang = UneiLang.getInstance().getPlayerManager().getDefaultLanguage();
				if (object instanceof Entity)
				{
					lang = UneiLang.getInstance().getPlayerManager().getLanguage(((Entity) object).getUniqueId());
				}
				((CommandSender) object).sendMessage(ChatColor.translateAlternateColorCodes('&',
						lang.get("general.badperm", ChatColor.RED +
						"I'm sorry, but you do not have permission to perform this command. "
						+ "Please contact the server administrators if you believe that this is an error.")));
			}
			else if(permissionMessage.length() != 0)
			{
				for(String line : permissionMessage.replace("<permission>", permission.getName()).split("\n"))
				{
					((CommandSender) object).sendMessage(ChatColor.translateAlternateColorCodes('&', line));
				}
			}
		}
		return false;
	}
	
	public static boolean testPermission(Permissible object, String permissions, boolean opByPass, String permissionMessage)
	{
		if(testPermissionSilent(object, permissions, opByPass))
		{
			return true;
		}
		
		if (object instanceof CommandSender)
		{
			if(permissionMessage == null)
			{
				Language lang = UneiLang.getInstance().getPlayerManager().getDefaultLanguage();
				if (object instanceof Entity)
				{
					lang = UneiLang.getInstance().getPlayerManager().getLanguage(((Entity) object).getUniqueId());
				}
				((CommandSender) object).sendMessage(ChatColor.translateAlternateColorCodes('&',
						lang.get("general.badperm", ChatColor.RED +
						"I'm sorry, but you do not have permission to perform this command. "
						+ "Please contact the server administrators if you believe that this is an error.")));
			}
			else if(permissionMessage.length() != 0)
			{
				for(String line : permissionMessage.replace("<permission>", permissions).split("\n"))
				{
					((CommandSender) object).sendMessage(ChatColor.translateAlternateColorCodes('&', line));
				}
			}
		}
		return false;
	}
}
