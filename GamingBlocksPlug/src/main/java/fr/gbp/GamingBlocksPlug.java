package fr.gbp;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.gbp.bukkit.BukkitPlugin;
import fr.gbp.command.MCommands;
import fr.gbp.config.MConfig;
import fr.gbp.economy.Money;

public class GamingBlocksPlug
{
	private FileConfiguration conf;
	private MCommands coms;
	private MConfig config;
	private Money money;
	private final Server server;
	private final Logger logger;
	private final JavaPlugin plugin;
	
	public GamingBlocksPlug(Server server, Logger logger, JavaPlugin plugin)
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
		this.money = new Money(this);
		coms.regCommands();
	}
	
	public void onDisable()
	{
		this.config.saveCustomConfig();
		plugin.saveConfig();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		return MCommands.onCommand(sender, cmd, label, args);
	}
	
	public List<String> getConfig(String name)
	{
		return this.conf.getStringList(name);
	}
	
	public void broad(String msg)
	{
		this.server.broadcastMessage(msg);
	}

	public MConfig getConfig()
	{
		return config;
	}
	
	public Money getMoney()
	{
		return this.money;
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
