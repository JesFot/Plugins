package fr.jesfot.gbp.permission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.entity.Player;
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
	
	public static Permission SecureWallPerm = null;
	
	public static Permission impossiblePermission;
	
	private static Permission allPermRepresentator = new Permission("*", "All game's permissions");
	
	public Permissions(GamingBlockPlug_1_9 p_gbp)
	{
		this.gbp = p_gbp;
		this.perms = new HashMap<String, Permission>();
		this.permsAttachment = new HashMap<UUID, PermissionAttachment>();
		globalGBP = this.addPermission("GamingBlockPlug", PermissionDefault.OP, "Global permission for the gamingblockplug plugin.");
		impossiblePermission = this.addPermission("GamingBlockPlug.impossible", PermissionDefault.FALSE, "Denied.");
		SecureWallPerm = this.addPermission("GamingBlockPlug.secureWall", PermissionDefault.OP, "Managing security walls", Permissions.globalGBP);
	}
	
	public Permission addPermission(String id, PermissionDefault p_default, String description)
	{
		if(this.perms.containsKey(id))
		{
			return this.perms.get(id);
		}
		Permission tmp = new Permission(id, description, p_default, null);
		this.perms.put(id, tmp);
		return tmp;
	}
	
	public Permission addPermission(String id, PermissionDefault p_default, String description, Permission parent)
	{
		if(this.perms.containsKey(id))
		{
			return this.perms.get(id);
		}
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
	
	public List<Permission> getChildsOf(String permName)
	{
		if(permName.contentEquals("*"))
		{
			return this.getAllPermissions();
		}
		if(permName.endsWith(".*"))
		{
			permName = permName.substring(0, permName.length() - 2);
		}
		Permission perm = this.gbp.getPluginManager().getPermission(permName);
		if(perm == null)
		{
			return Collections.emptyList();
		}
		List<Permission> result = new ArrayList<Permission>();
		for(Entry<String, Boolean> e : perm.getChildren().entrySet())
		{
			result.add(this.gbp.getPluginManager().getPermission(e.getKey()));
		}
		return result;
	}
	
	public Permission getPermission(String permName)
	{
		if(permName.contentEquals("*"))
		{
			return allPermRepresentator;
		}
		if(permName.endsWith(".*"))
		{
			permName = permName.substring(0, permName.length() - 2);
		}
		return this.gbp.getPluginManager().getPermission(permName);
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
	
	public List<Permission> getPermissions(String perms)
	{
		List<Permission> result = new ArrayList<Permission>();
		if(!perms.contains(";"))
		{
			result.add(this.getPermission(perms));
			return result;
		}
		for(String perm : perms.split(";"))
		{
			result.add(this.gbp.getPluginManager().getPermission(perm));
		}
		return result;
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
	
	public PermissionAttachment addPlayer(Player player)
	{
		this.permsAttachment.put(player.getUniqueId(), player.addAttachment(this.gbp.getPlugin()));
		return this.permsAttachment.get(player.getUniqueId());
	}
	
	public PermissionAttachment setPerm(Player player, Set<Permission> set, boolean value)
	{
		if(!this.permsAttachment.containsKey(player.getUniqueId()))
		{
			this.addPlayer(player);
		}
		for(Permission p : set)
		{
			this.permsAttachment.get(player.getUniqueId()).setPermission(p, value);
		}
		return this.permsAttachment.get(player.getUniqueId());
	}
	
	public PermissionAttachment setPermS(Player player, Set<String> set, boolean value)
	{
		if(!this.permsAttachment.containsKey(player.getUniqueId()))
		{
			this.addPlayer(player);
		}
		for(String p : set)
		{
			this.permsAttachment.get(player.getUniqueId()).setPermission(p, value);
		}
		return this.permsAttachment.get(player.getUniqueId());
	}
}