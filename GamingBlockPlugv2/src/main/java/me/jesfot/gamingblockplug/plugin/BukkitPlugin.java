package me.jesfot.gamingblockplug.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.jesfot.gamingblockplug.listeners.GBlockListener;
import me.jesfot.gamingblockplug.listeners.GPlayerListener;
import me.jesfot.gamingblockplug.listeners.GWorldListener;

/**
 * Plugin CraftBukkit bootstrap.
 * 
 * @author JësFot
 * @since 1.13-1.0.0
 */
public final class BukkitPlugin extends JavaPlugin
{
	private GamingBlockPlug gbpPlugin = null;
	
	public BukkitPlugin()
	{}
	
	@Override
	public void onLoad()
	{
		this.gbpPlugin = new GamingBlockPlug(this);
		this.gbpPlugin.onLoad();
	}
	
	@Override
	public void onEnable()
	{
		this.gbpPlugin.onEnable();
		
		getLogger().info(" -> Registering listeners...");
		final PluginManager pm = this.getServer().getPluginManager();
		
		//final GPluginListener pluginListener = new GPluginListener(this.gbpPlugin);
		final GBlockListener blockListener = new GBlockListener(this.gbpPlugin);
		//final GEntityListener entityListener = new GEntityListener();
		final GPlayerListener playerListener = new GPlayerListener(this.gbpPlugin);
		final GWorldListener worldListener = new GWorldListener(this.gbpPlugin);
		//final GInventoryListener inventoryListener = new GInventoryListener(this.gbpPlugin);
		
		//pm.registerEvents(pluginListener, this);
		pm.registerEvents(blockListener, this);
		//pm.registerEvents(entityListener, this);
		pm.registerEvents(playerListener, this);
		pm.registerEvents(worldListener, this);
		//pm.registerEvents(inventoryListener, this);
		getLogger().info("     -> Listeners registered !");
	}
	
	@Override
	public void onDisable()
	{
		this.gbpPlugin.onDisable();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		sender.sendMessage("Bad or unregistered command.");
		return super.onCommand(sender, command, label, args);
	}
}
