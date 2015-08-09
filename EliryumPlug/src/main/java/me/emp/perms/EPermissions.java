package me.emp.perms;

import java.util.HashMap;

import me.emp.EliryumPlug;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

public class EPermissions
{
	private EliryumPlug emp;
	protected HashMap<String, PermissionAttachment> paMap;
	protected HashMap<String, Permission> pMap;
	protected Permission SwitchCommand = new Permission("Eliryum.switch", "permission for the 'Switch' command", PermissionDefault.OP, null);
	protected Permission ViewVarCommand = new Permission("Eliryum.var.view", "permission for the 'var' command", PermissionDefault.TRUE, null);
	protected Permission SetVarCommand = new Permission("Eliryum.var.set", "permission for the 'var set' command", PermissionDefault.TRUE, null);
	protected Permission UseVarCommand = new Permission("Eliryum.var.use", "permission for use defined variables", PermissionDefault.TRUE, null);
	protected Permission UseMaskCommand = new Permission("Eliryum.mask.use", "permission for the 'mask' command", PermissionDefault.OP, null);
	protected Permission SeeMaskCommand = new Permission("Eliryum.mask.seeAll", "permission for see masked non-admin players", PermissionDefault.OP, null);
	protected Permission SeeAdminMaskCommand = new Permission("Eliryum.mask.seeAdmin", "permission for see masked admins", PermissionDefault.OP, null);
	
	public EPermissions(EliryumPlug p_emp)
	{
		this.emp = p_emp;
	}
	
	public void myPerms()
	{
		this.collectPerms();
		this.pMap.put(this.SwitchCommand.getName(), this.SwitchCommand);
		this.pMap.put(this.ViewVarCommand.getName(), this.ViewVarCommand);
		this.pMap.put(this.SetVarCommand.getName(), this.SetVarCommand);
		this.pMap.put(this.UseVarCommand.getName(), this.UseVarCommand);
		this.pMap.put(this.UseMaskCommand.getName(), this.UseMaskCommand);
		this.pMap.put(this.SeeMaskCommand.getName(), this.SeeMaskCommand);
		this.pMap.put(this.SeeAdminMaskCommand.getName(), this.SeeAdminMaskCommand);
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
					+ "Please contact the server administrators if you believe that this is an error.");
		}
		else if(permissionMessage.length() != 0)
		{
			for(String line : permissionMessage.replace("<permission>", permission).split("\n"))
			{
				target.sendMessage(line);
			}
		}
		return false;
	}
	
	public static boolean testPermissionSilent(CommandSender target, String permission)
	{
		if(target instanceof ConsoleCommandSender)
		{
			return true;
		}
		if(target.isOp())
		{
			return true;
		}
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