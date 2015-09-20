package fr.gbp;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.gbp.bukkit.BukkitPlugin;
import fr.gbp.command.GCommands;
import fr.gbp.config.GConfig;
import fr.gbp.config.GLang;
import fr.gbp.economy.GEconomy;
import fr.gbp.economy.Money;
import fr.gbp.perms.GPermissions;
import fr.gbp.utils.GText.MLang;

public class GamingBlockPlug
{
	private FileConfiguration conf;
	private GCommands coms;
	private GConfig config;
	private GLang lang;
	private GPermissions permissions;
	private Money money;
	private GEconomy economy;
	private final Server server;
	private final Logger logger;
	private final JavaPlugin plugin;
	
	public GamingBlockPlug(Server p_server, Logger p_logger, JavaPlugin p_plugin)
	{
		this.server = p_server;
		this.logger = p_logger;
		this.plugin = p_plugin;
	}
	
	public static void onLoad()
	{
		// Code ...
	}
	
	public void onEnable()
	{
		this.conf = this.plugin.getConfig();
		this.coms = new GCommands(this);
		this.config = new GConfig(this);
		this.lang = new GLang(this);
		this.permissions = new GPermissions(this);
		this.money = new Money(this);
		this.economy = new GEconomy(this);
		
		this.permissions.myPerms();
		this.config.reloadCustomConfig();
		this.lang.setLang(MLang.getByID(this.config.getCustomConfig().getInt("lang", -1)));
		coms.regCommands();
	}
	
	public void onDisable()
	{
		logger.log(Level.INFO, "Plugin stop.");
		this.plugin.saveConfig();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		return GCommands.onCommand(sender, cmd, label, args);
	}
	
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return GCommands.onTabComplete(sender, command, alias, args);
	}
	
	public List<String> getConfig(String name)
	{
		return this.conf.getStringList(name);
	}
	
	public void broad(String msg)
	{
		this.server.broadcastMessage(msg);
	}

	public GConfig getConfig()
	{
		return config;
	}
	
	public GLang getLang()
	{
		return lang;
	}
	
	public GPermissions getPermissions()
	{
		return permissions;
	}
	
	public GPermissions getPerms()
	{
		return getPermissions();
	}
	
	public Money getMoney()
	{
		return money;
	}
	
	public GEconomy getEconomy()
	{
		return economy;
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
	
	public BukkitPlugin getThisPlugin()
	{
		return new BukkitPlugin();
	}
}