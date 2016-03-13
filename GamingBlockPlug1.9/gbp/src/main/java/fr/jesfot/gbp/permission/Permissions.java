package fr.jesfot.gbp.permission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

import fr.jesfot.gbp.GamingBlockPlug_1_9;

public class Permissions
{
	private GamingBlockPlug_1_9 gbp;
	
	protected HashMap<String, Permission> perms;
	protected HashMap<UUID, PermissionAttachment> permsAttachment;
	
	public static Permission globalGBP = null;
	
	public static Permission impossiblePermission;
	
	public Permissions(GamingBlockPlug_1_9 p_gbp)
	{
		this.gbp = p_gbp;
		this.perms = new HashMap<String, Permission>();
		this.permsAttachment = new HashMap<UUID, PermissionAttachment>();
		globalGBP = this.addPermission("GamingBlockPlug", PermissionDefault.OP, "Global permission for the gamingblockplug plugin.");
		impossiblePermission = this.addPermission("GamingBlockPlug.impossible", PermissionDefault.FALSE, "Denied.");
	}
	
	public Permission addPermission(String id, PermissionDefault p_default, String description)
	{
		Permission tmp = new Permission(id, description, p_default, null);
		this.perms.put(id, tmp);
		return tmp;
	}
	
	public Permission addPermission(String id, PermissionDefault p_default, String description, Permission parent)
	{
		Permission tmp = new Permission(id, description, p_default, null);
		tmp.addParent(parent, true);
		this.perms.put(id, tmp);
		return tmp;
	}
	
	public Permission removePermission(String id)
	{
		return this.perms.remove(id);
	}
	
	public List<Permission> getAllPermissions()
	{
		return Arrays.asList(this.gbp.getPluginManager().getPermissions().toArray(new Permission[]{}));
	}
	
	public List<String> getAllPermNames()
	{
		List<String> s = new ArrayList<String>();
		Iterator<Permission> it = this.getAllPermissions().iterator();
		while(it.hasNext())
		{
			Permission p = it.next();
			s.add(p.getName());
		}
		return s;
	}
	
	public void registerPerms()
	{
		PluginManager pm = this.gbp.getPluginManager();
		for(Permission perm : this.perms.values())
		{
			try
			{
				pm.addPermission(perm);
			}
			catch(IllegalArgumentException e)
			{
				this.gbp.broadAdmins("IllegalStateException -> trying to re-register permissions");
				e.printStackTrace();
			}
		}
	}
	
	public void unregisterPerms()
	{
		PluginManager pm = this.gbp.getPluginManager();
		for(Permission perm : this.perms.values())
		{
			pm.removePermission(perm);
		}
	}
}