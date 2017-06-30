package fr.jesfot.gbp.permissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import fr.jesfot.gbp.GamingBlockPlug_1_11;
import fr.jesfot.gbp.players.GBPPlayer;

public class PermissionGroup
{
	private static final PermissionGroup DEFAULT_GROUP = new PermissionGroup("default");
	
	private static final HashMap<String, PermissionGroup> listPermGroups = new HashMap<>();
	
	private String id = "";
	private final Map<String, Boolean> permissions;
	private final Map<UUID, Map<String, Boolean>> perWorldPermissions;
	
	private final List<UUID> players;
	
	protected PermissionGroup()
	{
		this.permissions = new HashMap<String, Boolean>();
		this.perWorldPermissions = new HashMap<UUID, Map<String, Boolean>>();
		this.players = new ArrayList<UUID>();
	}

	public PermissionGroup(final String name)
	{
		this.id = name;
		if(PermissionGroup.listPermGroups.containsKey(name))
		{
			PermissionGroup orig = PermissionGroup.getGroup(name);
			this.permissions = orig.permissions;
			this.perWorldPermissions = orig.perWorldPermissions;
			this.players = orig.players;
		}
		else
		{
			this.permissions = new HashMap<String, Boolean>();
			this.perWorldPermissions = new HashMap<UUID, Map<String, Boolean>>();
			this.players = new ArrayList<UUID>();
			PermissionGroup.listPermGroups.put(name, this);
		}
	}
	
	public void addPlayer(GBPPlayer player)
	{
		if(!this.players.contains(player.getUniqueId()))
		{
			this.players.add(player.getUniqueId());
			player.setPermissionGroup(this);
		}
	}
	
	public boolean hasPermission(String permission, Player player)
	{
		if(this.perWorldPermissions.containsKey(player.getWorld().getUID()))
		{
			Map<String, Boolean> perms = this.perWorldPermissions.get(player.getWorld().getUID());
			if(perms.containsKey(permission))
			{
				return perms.get(permission).booleanValue();
			}
		}
		if(this.permissions.containsKey(permission))
		{
			return this.permissions.get(permission).booleanValue();
		}
		return GamingBlockPlug_1_11.getMe().getServer().getPlayer(player.getUniqueId()).hasPermission(permission);
	}
	
	public boolean hasPermission(Permission permission, Player player)
	{
		if(this.perWorldPermissions.containsKey(player.getWorld().getUID()))
		{
			Map<String, Boolean> perms = this.perWorldPermissions.get(player.getWorld().getUID());
			if(perms.containsKey(permission.getName()))
			{
				return perms.get(permission.getName()).booleanValue();
			}
		}
		if(this.permissions.containsKey(permission.getName()))
		{
			return this.permissions.get(permission.getName()).booleanValue();
		}
		return GamingBlockPlug_1_11.getMe().getServer().getPlayer(player.getUniqueId()).hasPermission(permission);
	}
	
	public String getId()
	{
		return this.id;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == null || !(o instanceof PermissionGroup))
		{
			return false;
		}
		PermissionGroup oth = (PermissionGroup)o;
		return oth.id.equals(this.id);
	}
	
	public static PermissionGroup getGroup(String name)
	{
		return PermissionGroup.listPermGroups.get(name);
	}
	
	public static PermissionGroup getDefaultGroup()
	{
		return PermissionGroup.DEFAULT_GROUP;
	}
}
