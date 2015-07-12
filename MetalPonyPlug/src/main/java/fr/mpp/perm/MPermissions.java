package fr.mpp.perm;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import fr.mpp.MetalPonyPlug;

public class MPermissions
{
	protected PermissionAttachment attachment;
	private static MetalPonyPlug mpp;
	protected HashMap<String, PermissionAttachment> hMap;

	/*  #mpp.reload: op
	  #mpp.timings: false

	  #bukkit.command.kill: false
	  #bukkit.command.kick: false
	  #bukkit.command.ban.player: false
	  #bukkit.command.ban.list: true
	  #bukkit.command.unban.*: false
	  #bukkit.command.ban.ip: false
	  #bukkit.command.teleport: false
	  #bukkit.command.give: false
	  #bukkit.command.list: true
	  #bukkit.command.time.*: false
	*/
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
		if (this.hMap.containsKey(player.getName()))
		{
			return this.hMap.get(player.getName());
		}
		PermissionAttachment tmp = player.addAttachment(mpp.getPlugin());
		this.hMap.put(player.getName(), tmp);
		return tmp;
	}
	public void removeAttachment(Player player)
	{
		if (!this.hMap.containsKey(player.getName()))
		{
			return;
		}
		PermissionAttachment tmp = this.hMap.get(player.getName());
		player.removeAttachment(tmp);
		this.hMap.remove(player.getName());
	}
	
	public PermissionAttachment getAttachment(Player player)
	{
		return hMap.get(player.getName());
	}
	public void setPerm(Player player, String name, boolean value)
	{
		if (this.hMap.containsKey(player.getName()))
		{
			this.hMap.get(player.getName()).setPermission(name, value);
		}
		else
		{
			this.hMap.put(player.getName(), new PermissionAttachment(mpp.getPlugin(), player)).setPermission(name, value);
		}
	}
	public void setPerm(Player player, Permission perm, boolean value)
	{
		if (this.hMap.containsKey(player.getName()))
		{
			this.hMap.get(player.getName()).setPermission(perm, value);
		}
		else
		{
			this.addAttachment(player).setPermission(perm, value);
		}
	}
	public void deletePerm(Player player, String name)
	{
		if (this.hMap.containsKey(player.getName()))
		{
			this.hMap.get(player.getName()).unsetPermission(name);
		}
	}
	public void deletePerm(Player player, Permission perm)
	{
		if (this.hMap.containsKey(player.getName()))
		{
			this.hMap.get(player.getName()).unsetPermission(perm);
		}
	}
	
	public boolean getPerm(Player player, String name)
	{
		if (this.hMap.get(player.getName()).getPermissions().get("all"))
		{
			return true;
		}
		return this.hMap.get(player.getName()).getPermissions().get(name);
	}
	public boolean getPerm(Player player, Permission perm)
	{
		String name = perm.getName();
		return getPerm(player, name);
	}
	
	public static boolean testPermission(CommandSender target, String permission, String permissionMessage)
	{
        if (testPermissionSilent(target, permission)) {
            return true;
        }

        if (permissionMessage == null) {
            target.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
        } else if (permissionMessage.length() != 0) {
            for (String line : permissionMessage.replace("<permission>", permission).split("\n")) {
                target.sendMessage(line);
            }
        }

        return false;
    }
	
	public static boolean testPermissionSilent(CommandSender target, String permission)
	{
        if ((permission == null) || (permission.length() == 0)) {
            return true;
        }

        for (String p : permission.split(";")) {
            if (target.hasPermission(p)) {
                return true;
            }
        }

        return false;
    }
	
	public class MPerm
	{
		private PermissionAttachment perm;
		
		public MPerm(PermissionAttachment att)
		{
			this.setPerm(att);
		}

		public PermissionAttachment getPerm()
		{
			return perm;
		}

		public void setPerm(PermissionAttachment perm)
		{
			this.perm = perm;
		}
	}
}