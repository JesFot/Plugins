package fr.gbp;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.gbp.command.GCommands;

public class GamingBlockPlug
{
	private FileConfiguration conf;
	private GCommands coms;
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
		coms.regCommands();
	}
	
	public void onDisable()
	{
		this.plugin.saveConfig();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		return GCommands.onCommand(sender, cmd, label, args);
	}
	
	public List<String> getConfig(String name)
	{
		return this.conf.getStringList(name);
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