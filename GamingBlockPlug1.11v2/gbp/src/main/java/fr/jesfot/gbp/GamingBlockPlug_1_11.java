package fr.jesfot.gbp;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import fr.jesfot.gbp.utils.ServerUtils;

public class GamingBlockPlug_1_11 extends ServerUtils
{
	private static GamingBlockPlug_1_11 PLUGIN;
	
	private final Logger logger;
	private final JavaPlugin plugin;
	
	public GamingBlockPlug_1_11(Server p_server, Logger p_logger, JavaPlugin p_plugin)
	{
		super(p_server);
		this.logger = p_logger;
		this.plugin = p_plugin;
		PLUGIN = this;
	}
	
	public void onLoad()
	{
		this.logger.log(Level.INFO, "Loading plugin " + RefString.NAME + "...");
		
		this.logger.log(Level.FINEST, "Creating data configuration folder if not exists...");
		if(!this.plugin.getDataFolder().exists())
		{
			if(this.plugin.getDataFolder().mkdirs())
			{
				this.logger.log(Level.FINEST, "Successfuly created the configuration folder.");
			}
			else
			{
				this.logger.log(Level.WARNING, "Could not create the configuration directory tree.");
			}
		}
		
		//
		
		this.logger.log(Level.INFO, "Successfuly loaded plugin " + RefString.NAME + ".");
	}
	
	public void onEnable()
	{
		this.logger.log(Level.INFO, "Enabling plugin " + RefString.NAME + "...");
		//
		this.logger.log(Level.INFO, "Successfuly enabled plugin " + RefString.NAME + ".");
	}
	
	public void onDisable()
	{
		this.logger.log(Level.INFO, "Disabling plugin " + RefString.NAME + "...");
		//
		this.logger.log(Level.INFO, "Successfuly disabled plugin " + RefString.NAME + ".");
	}
	
	public Logger getLogger()
	{
		return logger;
	}
	
	public JavaPlugin getPlugin()
	{
		return plugin;
	}
	
	public static GamingBlockPlug_1_11 getMe()
	{
		return PLUGIN;
	}
}