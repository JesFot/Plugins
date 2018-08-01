package me.jesfot.gamingblockplug.permission;

import java.util.HashMap;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

import me.jesfot.gamingblockplug.Infos;
import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;

public final class GBPPermissions
{
	private static GBPPermissions Instance = null;
	
	private final GamingBlockPlug plugin;
	
	private boolean loaded = false;
	
	private HashMap<String, Permission> perms;
	
	public GBPPermissions(GamingBlockPlug plugin)
	{
		if (GBPPermissions.Instance != null)
		{
			throw new IllegalStateException("Only one instance of GBPPermissions is permitted");
		}
		this.plugin = plugin;
		this.perms = new HashMap<>();
		
		GBPPermissions.Instance = this;
	}
	
	public void load()
	{
		if (this.loaded)
		{
			return;
		}
		
		Permission tmp = null;
		
		StaticPerms.GAMINGBLOCKPLUG = this.add(Infos.GBP_NAME, PermissionDefault.OP, "Global permission for the gamingblockplug plugin");
		StaticPerms.IMPOSSIBLE = this.addGBP("impossible", PermissionDefault.FALSE, "Denied.");
		
		// System permissions :
		tmp = this.addGBP("publicchest", PermissionDefault.TRUE, "Permission to open the public chest");
		tmp = this.addGBP("hbs", PermissionDefault.TRUE, "Permission related to the 'HalfBedSys'");
		this.addGBP("hbs.ignore", PermissionDefault.FALSE, "Permission used to ignore players", tmp);
		this.addGBP("hbs.count", PermissionDefault.TRUE, "Permission checked before count in-bed players", tmp);
		tmp = this.addGBP("walls", PermissionDefault.TRUE, "Permission related to security walls");
		this.addGBP("walls.place", PermissionDefault.OP, "Permission to setup a wall", tmp);
		this.addGBP("walls.break", PermissionDefault.OP, "Permission to break a wall", tmp);
		tmp = this.addGBP("skinrestore", PermissionDefault.TRUE, "Permission to restore the original skin");
		tmp = this.addGBP("variables", PermissionDefault.TRUE, "Permission related to variables");
		this.addGBP("variables.chat", PermissionDefault.TRUE, "Permission to use variables in the normal chat", tmp);
		this.addGBP("variables.cmds", PermissionDefault.TRUE, "Permission to use variables in commands", tmp);
		tmp = this.addGBP("login", PermissionDefault.TRUE, "Permission related to login security");
		this.addGBP("login.motd", PermissionDefault.TRUE, "Permission to see the MOTD at login");
		this.addGBP("login.bypass", PermissionDefault.FALSE, "Permission to bypass the login password", tmp);
		this.addGBP("login.register", PermissionDefault.TRUE, "Permission to register the login password", tmp);
		
		// Commands permissions :
		StaticPerms.COMMANDS = this.addGBP("command", PermissionDefault.OP, "Commands");
		tmp = this.addGBP("command.fly", PermissionDefault.FALSE, "Permission to use the /fly command", StaticPerms.COMMANDS);
		this.addGBP("command.fly.other", PermissionDefault.FALSE, "Permission to use the /fly <player> command", tmp);
		tmp = this.addGBP("command.spawn", PermissionDefault.TRUE, "Permission to use the /spawn command", StaticPerms.COMMANDS);
		this.addGBP("command.spawn.set", PermissionDefault.OP, "Permission to define the spawn tp", tmp);
		this.addGBP("command.spawn.use", PermissionDefault.TRUE, "Permission to teleport to spawn", tmp);
		tmp = this.addGBP("command.warp", PermissionDefault.TRUE, "Permission to use the /warp command", StaticPerms.COMMANDS);
		this.addGBP("command.warp.set", PermissionDefault.OP, "Permission to define warps", tmp);
		this.addGBP("command.warp.list", PermissionDefault.TRUE, "Permission to define warps", tmp);
		tmp = this.addGBP("command.warp.use", PermissionDefault.TRUE, "Permission to tp to warps", tmp);
		this.addGBP("command.warp.use.other", PermissionDefault.OP, "Permission to tp someone to warps", tmp);
		this.addGBP("command.warp.use.goto", PermissionDefault.TRUE, "Permission to use tp on specific warp", tmp);
		tmp = this.addGBP("command.passnight", PermissionDefault.FALSE, "Permission to use the /forcepassnight command", StaticPerms.COMMANDS);
		// ---- > /ping command ----
		StaticPerms.CMD_PING = this.addGBP("command.ping", PermissionDefault.TRUE, "Permission to use the /ping command", StaticPerms.COMMANDS);
		// ---- < /ping command ----
		tmp = this.addGBP("command.spychest", PermissionDefault.OP, "Permission to use the /spychest command", StaticPerms.COMMANDS);
		// ---- > /role command ----
		StaticPerms.CMD_ROLE		= this.addGBP("command.role",			PermissionDefault.TRUE,		"Permission to use the /role command",		StaticPerms.COMMANDS);
		StaticPerms.CMD_ROLE_LIST	= this.addGBP("command.role.list",		PermissionDefault.TRUE,		"Permission to list roles",					StaticPerms.CMD_ROLE);
		StaticPerms.CMD_ROLE_ADD	= this.addGBP("command.role.add",		PermissionDefault.OP,		"Permission to add or remove roles",		StaticPerms.CMD_ROLE);
		StaticPerms.CMD_ROLE_MODIF	= this.addGBP("command.role.modify",	PermissionDefault.OP,		"Permission to change options of roles",	StaticPerms.CMD_ROLE);
		StaticPerms.CMD_ROLE_SET	= this.addGBP("command.role.set",		PermissionDefault.OP,		"Permission to set someone's role",			StaticPerms.CMD_ROLE);
		StaticPerms.CMD_ROLE_TEST	= this.addGBP("command.role.test",		PermissionDefault.FALSE,	"Permission to test someone's role",		StaticPerms.CMD_ROLE);
		// ---- < /role command ----
		tmp = this.addGBP("command.var", PermissionDefault.TRUE, "Permission to use the /var command", StaticPerms.COMMANDS);
		this.addGBP("command.var.set", PermissionDefault.TRUE, "Permission to define a variable", tmp);
		this.addGBP("command.var.reset", PermissionDefault.TRUE, "Permission to change a variable content", tmp);
		this.addGBP("command.var.unset", PermissionDefault.TRUE, "Permission to delete a variable", tmp);
		this.addGBP("command.var.print", PermissionDefault.TRUE, "Permission to print a variable content", tmp);
		tmp = this.addGBP("command.motd", PermissionDefault.TRUE, "Permission to use the /logmessage command", StaticPerms.COMMANDS);
		this.addGBP("command.motd.print", PermissionDefault.TRUE, "Permission to see the MOTD", tmp);
		this.addGBP("command.motd.set", PermissionDefault.OP, "Permission to set the MOTD", tmp);
		tmp = this.addGBP("command.spect", PermissionDefault.TRUE, "Permission to use the /spectate command", StaticPerms.COMMANDS);
	}
	
	public Permission add(Permission perm)
	{
		return this.perms.put(perm.getName(), perm);
	}
	
	public Permission add(String id, PermissionDefault def, String description)
	{
		Permission tmp = this.perms.get(id);
		if (tmp != null)
		{
			return tmp;
		}
		tmp = GBPPermissions.build(id, def, description);
		this.add(tmp);
		return tmp;
	}
	
	public Permission add(String id, PermissionDefault def, String description, Permission parent)
	{
		Permission tmp = this.perms.get(id);
		if (tmp != null)
		{
			return tmp;
		}
		tmp = GBPPermissions.build(id, def, description, parent);
		this.add(tmp);
		return tmp;
	}
	
	public Permission addGBP(String id, PermissionDefault def, String description)
	{
		id = Infos.GBP_NAME + "." + id;
		Permission tmp = this.perms.get(id);
		if (tmp != null)
		{
			return tmp;
		}
		tmp = GBPPermissions.build(id, def, description, StaticPerms.GAMINGBLOCKPLUG);
		this.add(tmp);
		return tmp;
	}
	
	public Permission addGBP(String id, PermissionDefault def, String description, Permission parent)
	{
		id = Infos.GBP_NAME + "." + id;
		Permission tmp = this.perms.get(id);
		if (tmp != null)
		{
			return tmp;
		}
		tmp = GBPPermissions.build(id, def, description, parent);
		this.add(tmp);
		return tmp;
	}
	
	public static Permission build(String id, PermissionDefault def, String description)
	{
		return new Permission(id, description, def, null);
	}

	public static Permission build(String id, PermissionDefault def, String description, Permission parent)
	{
		Permission tmp = build(id, def, description);
		if (parent != null)
		{
			tmp.addParent(parent, true);
		}
		return tmp;
	}
	
	public void registerPermissions()
	{
		PluginManager pm = this.plugin.getPluginManager();
		for (Permission perm : this.perms.values())
		{
			try
			{
				pm.addPermission(perm);
			}
			catch (IllegalArgumentException iae)
			{
				this.plugin.broadAdmins("IllegalStateException -> trying to re-register permissions");
				iae.printStackTrace();
			}
		}
	}
	
	public void unregisterPermissions()
	{
		PluginManager pm = this.plugin.getPluginManager();
		for (Permission perm : this.perms.values())
		{
			pm.removePermission(perm);
		}
	}
	
	public void reloadPermissions()
	{
		this.unregisterPermissions();
		this.registerPermissions();
	}
	
	public static GBPPermissions getInstance()
	{
		return GBPPermissions.Instance;
	}
}
