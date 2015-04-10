package fr.mpp.perm;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import fr.mpp.MetalPonyPlug;

public class MPermissions
{
	PermissionAttachment attachment;
	static MetalPonyPlug mpp;
	HashMap<UUID, PermissionAttachment> hMap;
	
	public MPermissions()
	{
		//
	}
	
	public static void setPerm(Player player, String name, boolean perm, Plugin plug)
	{
		player.addAttachment(plug).setPermission(name, perm);
	}
	public static void setPerm(Player player, Permission name, boolean perm, Plugin plug)
	{
		player.addAttachment(plug).setPermission(name, perm);
	}
	
	public PermissionAttachment addAttachment(Player player)
	{
		if (this.hMap.containsKey(player.getUniqueId()))
		{
			return this.hMap.get(player.getUniqueId());
		}
		PermissionAttachment tmp = player.addAttachment(mpp.getPlugin());
		this.hMap.put(player.getUniqueId(), tmp);
		return tmp;
	}
	public void removeAttachment(Player player)
	{
		if (!this.hMap.containsKey(player.getUniqueId()))
		{
			return;
		}
		PermissionAttachment tmp = this.hMap.get(player.getUniqueId());
		player.removeAttachment(tmp);
		this.hMap.remove(player.getUniqueId());
	}
	
	public void setPerm(Player player, String name, boolean value)
	{
		if (this.hMap.containsKey(player.getUniqueId()))
		{
			this.hMap.get(player.getUniqueId()).setPermission(name, value);
		}
		else
		{
			this.hMap.put(player.getUniqueId(), new PermissionAttachment(mpp.getPlugin(), player)).setPermission(name, value);
		}
	}
	public void setPerm(Player player, Permission perm, boolean value)
	{
		if (this.hMap.containsKey(player.getUniqueId()))
		{
			this.hMap.get(player.getUniqueId()).setPermission(perm, value);
		}
		else
		{
			this.addAttachment(player).setPermission(perm, value);
		}
	}
	public void deletePerm(Player player, String name)
	{
		if (this.hMap.containsKey(player.getUniqueId()))
		{
			this.hMap.get(player.getUniqueId()).unsetPermission(name);
		}
	}
	public void deletePerm(Player player, Permission perm)
	{
		if (this.hMap.containsKey(player.getUniqueId()))
		{
			this.hMap.get(player.getUniqueId()).unsetPermission(perm);
		}
	}
	
	public boolean getPerm(Player player, String name)
	{
		return this.hMap.get(player.getUniqueId()).getPermissions().get(name);
	}
	public boolean getPerm(Player player, Permission perm)
	{
		String name = perm.getName();
		return this.hMap.get(player.getUniqueId()).getPermissions().get(name);
	}
}