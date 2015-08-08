package me.emp.bukkit;

import java.util.List;
import java.util.logging.Level;

import me.emp.EliryumPlug;
import me.emp.listener.*;
import me.emp.RefString;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlugin extends JavaPlugin
{
	private EliryumPlug emp;
	
	@Override
	public void onLoad()
	{
		EliryumPlug.onLoad();
	}
	
	@Override
	public void onEnable()
	{
		this.reloadConfig();
		final PluginManager pm = this.getServer().getPluginManager();
		emp = new EliryumPlug(getServer(), getLogger(), this);
		try
		{
			getLogger().log(Level.INFO, "Plugin start !!");
			emp.onEnable();
		}
		catch (RuntimeException ex)
		{
			getLogger().log(Level.SEVERE, "The file is broken and " + RefString.NAME + " can't open it. " + RefString.NAME + " is now disabled.");
			getLogger().log(Level.SEVERE, ex.toString());
			getLogger().log(Level.SEVERE, ex.getStackTrace()[0].toString());
			//pm.registerEvents(new CrashListener(), this);
			for (Player player : getServer().getOnlinePlayers())
			{
				player.sendMessage(RefString.NAME + " failed to load, read the log file.");
			}
			stopPlugin();
			return;
		}
		final MPluginListener pluginListener = new MPluginListener();
		final MBlockListener blockListener = new MBlockListener(this.emp);
		final MEntityListener entityListener = new MEntityListener();
		final MPlayerListener playerListener = new MPlayerListener(this.emp);
		final MInventoryListener inventoryListener = new MInventoryListener();
		
		pm.registerEvents(pluginListener, this);
		pm.registerEvents(blockListener, this);
		pm.registerEvents(entityListener, this);
		pm.registerEvents(playerListener, this);
		pm.registerEvents(inventoryListener, this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		return emp.onCommand(sender, cmd, label, args);
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return emp.onTabComplete(sender, command, alias, args);
	}
	
	@Override
	public void onDisable()
	{
		emp.onDisable();
	}
	
	public void stopPlugin()
	{
		this.setEnabled(false);
	}
}