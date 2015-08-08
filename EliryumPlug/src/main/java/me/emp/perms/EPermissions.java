package me.emp.perms;

import java.util.HashMap;

import me.emp.EliryumPlug;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

public class EPermissions
{
	private EliryumPlug emp;
	protected HashMap<String, PermissionAttachment> paMap;
	protected HashMap<String, Permission> pMap;
	protected Permission AllEliryumPlug = new Permission("Eliryum.all", "all EliryumPlug commands", PermissionDefault.OP, null);
	protected Permission SwitchCommand = new Permission("Eliryum.switch", "permission for the 'Switch' commands", PermissionDefault.OP, null);
	
	public EPermissions(EliryumPlug p_emp)
	{
		this.emp = p_emp;
	}
	
	public void myPerms()
	{
		this.SwitchCommand.addParent(this.AllEliryumPlug, true);
		
		this.collectPerms();
		this.pMap.put(this.AllEliryumPlug.getName(), this.AllEliryumPlug);
		this.pMap.put(this.SwitchCommand.getName(), this.SwitchCommand);
		this.registerAllPerms();
	}
	
	public void registerAllPerms()
	{
		PluginManager pm = this.emp.getServer().getPluginManager();
		for(Permission perm : this.pMap.values())
		{
			pm.addPermission(perm);
		}
	}
	
	public void unRegisterAllPerms()
	{
		PluginManager pm = this.emp.getServer().getPluginManager();
		for(Permission perm : pm.getPermissions())
		{
			pm.removePermission(perm);
		}
	}
	
	public void updateAllRegPerms()
	{
		this.unRegisterAllPerms();
		this.registerAllPerms();
	}
	
	public void collectPerms()
	{
		this.pMap.clear();
		for(Permission perm : this.emp.getServer().getPluginManager().getPermissions())
		{
			this.pMap.put(perm.getName(), perm);
		}
	}
	
	public static boolean testPermission(CommandSender target, String permission, String permissionMessage)
	{
		if(testPermissionSilent(target, permission))
		{
			return true;
		}
		
		if(permissionMessage == null)
		{
			target.sendMessage(ChatColor.RED+
					"I'm sorry, but you do not have permission to perform this command. "
					+ "Please contact the server administrators if you believe that this is in error.");
		}
		else if(permissionMessage.length() != 0)
		{
			for(String line : permissionMessage.replace("<permission>", permission).split("\n"))
			{
				target.sendMessage(line.replace("$c", ChatColor.RED+"").replace("$r", ChatColor.RESET+""));
			}
		}
		return false;
	}
	
	public static boolean testPermissionSilent(CommandSender target, String permission)
	{
		if((permission == null) || (permission.length() == 0))
		{
			return true;
		}
		
		for(String p : permission.split(";"))
		{
			if(target.hasPermission(p))
			{
				return true;
			}
		}
		return false;
	}
}