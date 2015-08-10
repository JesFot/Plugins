package fr.mpp.perm;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.permissions.PermissionAttachment;

public class MPermissions
{
	protected PermissionAttachment attachment;
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
	
	public static boolean testPermission(CommandSender target, String permission, String permissionMessage)
	{
        if (testPermissionSilent(target, permission)) {
            return true;
        }

        if (permissionMessage == null) {
            target.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. "
            		+ "Please contact the server administrators if you believe that this is in error.");
        } else if (permissionMessage.length() != 0) {
            for (String line : permissionMessage.replace("<permission>", permission).split("\n")) {
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