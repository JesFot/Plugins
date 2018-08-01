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
		
		StaticPerms.GAMINGBLOCKPLUG	= this.add(Infos.GBP_NAME,		PermissionDefault.OP,		"Global permission for the gamingblockplug plugin");
		StaticPerms.IMPOSSIBLE		= this.addGBP("impossible",		PermissionDefault.FALSE,	"Denied.");
		
		// System permissions :
		// ---- > Public/Common chest ----
		StaticPerms.PUBLIC_CHEST	= this.addGBP("publicchest",	PermissionDefault.TRUE,		"Permission to open the public chest");
		// ---- < Public/Common chest ----
		// ---- > Half In Bed system ----
		StaticPerms.HBS				= this.addGBP("hbs",			PermissionDefault.TRUE,		"Permission related to the 'HalfBedSys'");
		StaticPerms.HBS_IGNORE		= this.addGBP("hbs.ignore",		PermissionDefault.FALSE,	"Permission used to ignore players",				StaticPerms.HBS);
		StaticPerms.HBS_COUNT		= this.addGBP("hbs.count",		PermissionDefault.TRUE,		"Permission checked before count in-bed players",	StaticPerms.HBS);
		// ---- < Half In Bed system ----
		// ---- > Wall system ----
		StaticPerms.WALLS			= this.addGBP("walls",			PermissionDefault.TRUE,		"Permission related to security walls");
		StaticPerms.WALLS_PLACE		= this.addGBP("walls.place",	PermissionDefault.OP,		"Permission to setup a wall",						StaticPerms.WALLS);
		StaticPerms.WALLS_BREAK		= this.addGBP("walls.break",	PermissionDefault.OP,		"Permission to break a wall",						StaticPerms.WALLS);
		// ---- < Wall system ----
		/* this.addGBP("skinrestore", PermissionDefault.FALSE, "Permission to restore the original skin"); */
		// ---- > Variables ----
		StaticPerms.VARS			= this.addGBP("variables",		PermissionDefault.TRUE,		"Permission related to variables");
		StaticPerms.VARS_CHAT		= this.addGBP("variables.chat",	PermissionDefault.TRUE,		"Permission to use variables in the normal chat",	StaticPerms.VARS);
		StaticPerms.VARS_CMD		= this.addGBP("variables.cmds",	PermissionDefault.TRUE,		"Permission to use variables in commands",			StaticPerms.VARS);
		// ---- < Variables ----
		// ---- > Login ----
		StaticPerms.LOGIN			= this.addGBP("login",			PermissionDefault.TRUE,		"Permission related to login security");
		StaticPerms.LOGIN_MOTD		= this.addGBP("login.motd",		PermissionDefault.TRUE,		"Permission to see the MOTD at login");
		StaticPerms.LOGIN_BYPASS	= this.addGBP("login.bypass",	PermissionDefault.FALSE,	"Permission to bypass the login password",			StaticPerms.LOGIN);
		StaticPerms.LOGIN_REGISTER	= this.addGBP("login.register",	PermissionDefault.TRUE,		"Permission to register the login password",		StaticPerms.LOGIN);
		// ---- < Login ----
		
		// Commands permissions :
		StaticPerms.COMMANDS			= this.addGBP("command",				PermissionDefault.OP,		"Commands");
		// ---- > /fly command ----
		StaticPerms.CMD_FLY				= this.addGBP("command.fly",			PermissionDefault.FALSE,	"Permission to use the /fly command",				StaticPerms.COMMANDS);
		StaticPerms.CMD_FLY_OTHER		= this.addGBP("command.fly.other",		PermissionDefault.FALSE,	"Permission to use the /fly <player> command",		StaticPerms.CMD_FLY);
		// ---- < /fly command ----
		// ---- > /spawn command ----
		StaticPerms.CMD_SPAWN			= this.addGBP("command.spawn",			PermissionDefault.TRUE,		"Permission to use the /spawn command",				StaticPerms.COMMANDS);
		StaticPerms.CMD_SPAWN_SET		= this.addGBP("command.spawn.set",		PermissionDefault.OP,		"Permission to define the spawn tp",				StaticPerms.CMD_SPAWN);
		StaticPerms.CMD_SPAWN_USE		= this.addGBP("command.spawn.use",		PermissionDefault.TRUE,		"Permission to teleport to spawn",					StaticPerms.CMD_SPAWN);
		// ---- < /spawn command ----
		// ---- > /warp command ----
		StaticPerms.CMD_WARP			= this.addGBP("command.warp",			PermissionDefault.TRUE,		"Permission to use the /warp command",				StaticPerms.COMMANDS);
		StaticPerms.CMD_WARP_SET		= this.addGBP("command.warp.set",		PermissionDefault.OP,		"Permission to define warps",						StaticPerms.CMD_WARP);
		StaticPerms.CMD_WARP_LIST		= this.addGBP("command.warp.list",		PermissionDefault.TRUE,		"Permission to define warps",						StaticPerms.CMD_WARP);
		StaticPerms.CMD_WARP_USE		= this.addGBP("command.warp.use",		PermissionDefault.TRUE,		"Permission to tp to warps",						StaticPerms.CMD_WARP);
		StaticPerms.CMD_WARP_USE_OTHER	= this.addGBP("command.warp.use.other",	PermissionDefault.OP,		"Permission to tp someone to warps",				StaticPerms.CMD_WARP_USE);
		StaticPerms.CMD_WARP_USE_GOTO	= this.addGBP("command.warp.use.goto",	PermissionDefault.TRUE,		"Permission to use tp on specific warp",			StaticPerms.CMD_WARP_USE);
		// ---- < /warp command ----
		// ---- > /forcepassnight command ----
		StaticPerms.CMD_PASSNIGHT		= this.addGBP("command.passnight",		PermissionDefault.FALSE,	"Permission to use the /forcepassnight command",	StaticPerms.COMMANDS);
		// ---- < /forcepassnight command ----
		// ---- > /ping command ----
		StaticPerms.CMD_PING			= this.addGBP("command.ping",			PermissionDefault.TRUE,		"Permission to use the /ping command",				StaticPerms.COMMANDS);
		// ---- < /ping command ----
		// ---- > /spychest command ----
		StaticPerms.CMD_SPYCHEST		= this.addGBP("command.spychest",		PermissionDefault.OP,		"Permission to use the /spychest command",			StaticPerms.COMMANDS);
		// ---- < /spychest command ----
		// ---- > /role command ----
		StaticPerms.CMD_ROLE			= this.addGBP("command.role",			PermissionDefault.TRUE,		"Permission to use the /role command",				StaticPerms.COMMANDS);
		StaticPerms.CMD_ROLE_LIST		= this.addGBP("command.role.list",		PermissionDefault.TRUE,		"Permission to list roles",							StaticPerms.CMD_ROLE);
		StaticPerms.CMD_ROLE_ADD		= this.addGBP("command.role.add",		PermissionDefault.OP,		"Permission to add or remove roles",				StaticPerms.CMD_ROLE);
		StaticPerms.CMD_ROLE_MODIF		= this.addGBP("command.role.modify",	PermissionDefault.OP,		"Permission to change options of roles",			StaticPerms.CMD_ROLE);
		StaticPerms.CMD_ROLE_SET		= this.addGBP("command.role.set",		PermissionDefault.OP,		"Permission to set someone's role",					StaticPerms.CMD_ROLE);
		StaticPerms.CMD_ROLE_TEST		= this.addGBP("command.role.test",		PermissionDefault.FALSE,	"Permission to test someone's role",				StaticPerms.CMD_ROLE);
		// ---- < /role command ----
		// ---- > /var command ----
		StaticPerms.CMD_VAR				= this.addGBP("command.var",			PermissionDefault.TRUE,		"Permission to use the /var command",				StaticPerms.COMMANDS);
		StaticPerms.CMD_VAR_SET			= this.addGBP("command.var.set",		PermissionDefault.TRUE,		"Permission to define a variable",					StaticPerms.CMD_VAR);
		StaticPerms.CMD_VAR_RESET		= this.addGBP("command.var.reset",		PermissionDefault.TRUE,		"Permission to change a variable content",			StaticPerms.CMD_VAR);
		StaticPerms.CMD_VAR_UNSET		= this.addGBP("command.var.unset",		PermissionDefault.TRUE,		"Permission to delete a variable",					StaticPerms.CMD_VAR);
		StaticPerms.CMD_VAR_PRINT		= this.addGBP("command.var.print",		PermissionDefault.TRUE,		"Permission to print a variable content",			StaticPerms.CMD_VAR);
		// ---- < /var command ----
		// ---- > /motd command ----
		StaticPerms.CMD_MOTD			= this.addGBP("command.motd",			PermissionDefault.TRUE,		"Permission to use the /logmessage command",		StaticPerms.COMMANDS);
		StaticPerms.CMD_MOTD_PRINT		= this.addGBP("command.motd.print",		PermissionDefault.TRUE,		"Permission to see the MOTD",						StaticPerms.CMD_MOTD);
		StaticPerms.CMD_MOTD_SET		= this.addGBP("command.motd.set",		PermissionDefault.OP,		"Permission to set the MOTD",						StaticPerms.CMD_MOTD);
		// ---- < /motd command ----
		// ---- > /spectate command ----
		StaticPerms.CMD_SPECTATE		= this.addGBP("command.spect",			PermissionDefault.TRUE,		"Permission to use the /spectate command",			StaticPerms.COMMANDS);
		// ---- < /spectate command ----
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
