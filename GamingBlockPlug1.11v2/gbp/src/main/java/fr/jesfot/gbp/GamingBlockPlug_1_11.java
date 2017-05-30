package fr.jesfot.gbp;

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
		//
	}
	
	public void onEnable()
	{
		//
	}
	
	public void onDisable()
	{
		//
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