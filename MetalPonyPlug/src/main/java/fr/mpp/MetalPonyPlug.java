package fr.mpp;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import fr.mpp.bukkit.BukkitPlugin;
import fr.mpp.command.MCommands;

public class MetalPonyPlug
{
	private FileConfiguration conf;
	private MCommands coms;
	private MConfig config;
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
		this.conf = plugin.getConfig();
		this.config = new MConfig(conf, this);
		this.coms = new MCommands(this);
		coms.regCommands();
	}
	
	public void onDisable()
	{
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
	
	public Object getMeta(Player player, String name)
	{
		if (!player.hasMetadata(name))
		{
			return false;
		}
		return player.getMetadata(name).get(0).value();
	}
	
	public void setMeta(Player player, String name, Object meta)
	{
		player.setMetadata(name, new FixedMetadataValue(this.getPlugin(), meta));
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
	
	public BukkitPlugin getThisPlugin()
	{
		return new BukkitPlugin();
	}
}