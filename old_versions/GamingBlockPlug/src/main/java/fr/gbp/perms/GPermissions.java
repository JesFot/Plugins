package fr.gbp.perms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

import fr.gbp.GamingBlockPlug;
import fr.gbp.RefString;

public class GPermissions
{	
	private GamingBlockPlug gbp;
	protected HashMap<UUID, PermissionAttachment> paMap = new HashMap<UUID, PermissionAttachment>();
	protected HashMap<String, Permission> pMap = new HashMap<String, Permission>();
	
	// Permissions :
	protected Permission UseTpcCommand = new Permission("GamingBlockPlug.tpc.use", "permission for the 'tpc' command", PermissionDefault.TRUE, null);
	protected Permission TargetTpcCommand = new Permission("GamingBlockPlug.tpc.target", "permission for the 'tpc' command", PermissionDefault.TRUE, null);
	protected Permission GTpcCommand = new Permission("GamingBlockPlug.tpc.*", "permission for the 'tpc' command", PermissionDefault.TRUE, null);
	protected Permission UseGivePermsCommand = new Permission("GamingBlockPlug.giveperms", "permission for the give permissions", PermissionDefault.OP, null);
	protected Permission EcoResetCommand = new Permission("GamingBlockPlug.economy.reset", "permission for reset accounts", PermissionDefault.OP, null);
	protected Permission EcoSeeCommand = new Permission("GamingBlockPlug.economy.see", "permission for seeing other's accounts", PermissionDefault.OP, null);
	protected Permission EcoGetEmCommand = new Permission("GamingBlockPlug.economy.passEm", "permission for convert emerald to money", PermissionDefault.TRUE, null);
	protected Permission MaskCommand = new Permission("GamingBlockPlug.mask.*", "permission for all depends of the 'mask' command", PermissionDefault.OP, null);
	protected Permission UseMaskCommand = new Permission("GamingBlockPlug.mask.use", "permission for the 'mask' command", PermissionDefault.OP, null);
	protected Permission SeeMaskCommand = new Permission("GamingBlockPlug.mask.seeAll", "permission for see masked non-admin players", PermissionDefault.OP, null);
	protected Permission SeeAdminMaskCommand = new Permission("GamingBlockPlug.mask.seeAdmin", "permission for see masked admins", PermissionDefault.OP, null);
	// End permissions
	
	public GPermissions(GamingBlockPlug p_gbp)
	{
		this.gbp = p_gbp;
	}
	
	public void myPerms()
	{
		this.UseTpcCommand.addParent(GTpcCommand, true);
		this.TargetTpcCommand.addParent(GTpcCommand, true);
		this.UseMaskCommand.addParent(MaskCommand, true);
		this.SeeMaskCommand.addParent(MaskCommand, true);
		this.SeeAdminMaskCommand.addParent(MaskCommand, true);
		this.collectPerms();
		// Registering perms :
		this.regPerm(this.UseTpcCommand);
		this.regPerm(this.TargetTpcCommand);
		this.regPerm(this.UseGivePermsCommand);
		this.regPerm(this.UseMaskCommand);
		this.regPerm(this.SeeMaskCommand);
		this.regPerm(this.SeeAdminMaskCommand);
		this.regPerm(this.EcoResetCommand);
		this.regPerm(this.EcoSeeCommand);
		this.regPerm(this.EcoGetEmCommand);
		// End registering
		this.registerAllPerms();
	}
	
	public PermissionAttachment addPlayer(Player player)
	{
		this.paMap.put(player.getUniqueId(), player.addAttachment(this.gbp.getPlugin()));
		return this.paMap.get(player.getUniqueId());
	}
	
	public PermissionAttachment setPerm(Player player, Set<Permission> set, boolean value)
	{
		if(!this.paMap.containsKey(player.getUniqueId()))
		{
			this.addPlayer(player);
		}
		for(Permission p : set)
		{
			this.paMap.get(player.getUniqueId()).setPermission(p, value);
		}
		return this.paMap.get(player.getUniqueId());
	}
	
	public Set<Permission> getPerm(String name)
	{
		Set<Permission> res = new HashSet<Permission>();
		if(name == "*")
		{
			return this.gbp.getServer().getPluginManager().getPermissions();
		}
		Permission result = this.gbp.getServer().getPluginManager().getPermission(name);
		if(result == null)
		{
			/*switch(name)
			{
			case "GamingBlockPlug.economy.reset":
				result = this.EcoResetCommand;
				break;
			case "GamingBlockPlug.tpc.target":
				result = this.TargetTpcCommand;
				break;
			case "GamingBlockPlug.tpc.use":
				result = this.UseTpcCommand;
				break;
			case "GamingBlockPlug.giveperms":
				result = this.UseGivePermsCommand;
				break;
			case "GamingBlockPlug.tpc.*":
				result = this.GTpcCommand;
				break;
			default:
				return null;
			}*/
		}
		res.add(result);
		return res;
	}
	
	public void regPerm(Permission perm)
	{
		this.pMap.put(perm.getName(), perm);
	}
	
	public void registerAllPerms()
	{
		PluginManager pm = this.gbp.getServer().getPluginManager();
		for(Permission perm : this.pMap.values())
		{
			pm.addPermission(perm);
		}
	}
	
	public void unRegisterAllPerms()
	{
		PluginManager pm = this.gbp.getServer().getPluginManager();
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
		for(Permission perm : this.gbp.getServer().getPluginManager().getPermissions())
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
	
	public static boolean testPermission(CommandSender target, String permission, String permissionMessage, boolean adminByPass)
	{
		if(testPermissionSilent(target, permission, adminByPass))
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
		if((permission == null) || (permission.length() == 0))
		{
			return true;
		}
		
		if(target.isOp() && RefString.OPISALL)
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
	
	public static boolean testPermissionSilent(CommandSender target, String permission, boolean adminByPass)
	{
		if(target instanceof ConsoleCommandSender)
		{
			return true;
		}
		if((permission == null) || (permission.length() == 0))
		{
			return true;
		}
		
		if(target.isOp() && adminByPass)
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