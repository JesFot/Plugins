package fr.mpp;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.mppon.bukkit.BukkitPlugin;
import fr.mppon.command.MCommands;

public class MetalPonyPlug
{
	private MCommands coms;
	private final Server server;
	private final Logger logger;
	private final JavaPlugin plugin;
	
	public MetalPonyPlug(Server server, Logger logger, JavaPlugin plugin)
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
