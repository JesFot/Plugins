package fr.mppon;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import fr.mppon.command.MCommands;

public class MetalPonyPlug_on
{
	private MCommands coms;
	private final Server server;
	private final Logger logger;
	private final JavaPlugin plugin;
	
	public MetalPonyPlug_on(Server server, Logger logger, JavaPlugin plugin)
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
		this.coms = new MCommands(this);
		coms.regCommands();
	}
	
	public void onDisable()
	{
		logger.log(Level.INFO, "Plugin stop.");
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
}
