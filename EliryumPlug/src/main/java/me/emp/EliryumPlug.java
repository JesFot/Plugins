package me.emp;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.emp.bukkit.BukkitPlugin;
import me.emp.command.MCommands;
import me.emp.config.MConfig;
import me.emp.perms.EPermissions;
import me.emp.utils.MVarObject;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.java.JavaPlugin;

public class EliryumPlug
{
	private FileConfiguration conf;
	private MCommands coms;
	private MConfig config;
	private EPermissions permissions;
	private final Server server;
	private final Logger logger;
	private final JavaPlugin plugin;
	private MVarObject mvo;
	
	public EliryumPlug(Server server, Logger logger, JavaPlugin plugin)
	{
		this.server = server;
		this.plugin = plugin;
		this.logger = logger;
	}
	
	public static void onLoad()
	{
		// Code ...
	}
	
	public void onEnable()
	{
		this.conf = plugin.getConfig();
		this.config = new MConfig(conf, this);
		this.coms = new MCommands(this);
		this.permissions = new EPermissions(this);
		coms.regCommands();
		this.mvo = new MVarObject(this.config.getVarConfig());
		this.mvo.getFromFile();
		this.mvo.storeBool("plugin", true);
	}
	
	public void onDisable()
	{
		logger.log(Level.INFO, "Plugin stop.");
		this.config.saveCustomConfig();
		plugin.saveConfig();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		return this.coms.onCommand(sender, cmd, label, args);
	}
	
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return this.coms.onTabComplete(sender, command, alias, args);
	}
	
	public List<String> getConfig(String name)
	{
		return this.conf.getStringList(name);
	}
	
	public Object getMeta(Player player, String name)
	{
		if (!player.hasMetadata(name))
		{
			return false;
		}
		return player.getMetadata(name).get(0).value();
	}
	
	@SuppressWarnings("unused")
	public Object getPerm(Player player, String name)
	{
		if (!player.getEffectivePermissions().contains("EMP."+name))
		{
			return false;
		}
		for (PermissionAttachmentInfo pai : player.getEffectivePermissions())
		{
			String permName = pai.getPermission();
			boolean val = pai.getAttachment().getPermissions().get(name);
			Map<String, Boolean> perms = pai.getAttachment().getPermissions();
		}
		return player.getEffectivePermissions().iterator().next().getPermission();
	}
	
	public void setMeta(Player player, String name, Object meta)
	{
		player.setMetadata(name, new FixedMetadataValue(this.getPlugin(), meta));
	}
	
	public void broad(String msg)
	{
		this.server.broadcastMessage(msg);
	}

	public MConfig getConfig()
	{
		return config;
	}
	
	public Server getServer()
	{
		return server;
	}
	
	public Logger getLogger()
	{
		return logger;
	}
	
	public JavaPlugin getPlugin()
	{
		return plugin;
	}
	
	public EPermissions getPermissions()
	{
		return permissions;
	}
	
	public EPermissions getPerms()
	{
		return getPermissions();
	}
	
	public MVarObject getMVO()
	{
		return mvo;
	}
	
	public BukkitPlugin getThisPlugin()
	{
		return new BukkitPlugin();
	}
}