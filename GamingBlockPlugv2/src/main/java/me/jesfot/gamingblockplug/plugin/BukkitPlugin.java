package me.jesfot.gamingblockplug.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

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
