package fr.gbp.bukkit;

import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.gbp.GamingBlockPlug;
import fr.gbp.RefString;
import fr.gbp.listener.CrashListener;
import fr.gbp.listener.GBlockListener;
import fr.gbp.listener.GEntityListener;
import fr.gbp.listener.GPlayerListener;
import fr.gbp.listener.GPluginListener;

public class BukkitPlugin extends JavaPlugin
{
	private GamingBlockPlug gbp;
	
	@Override
	public void onLoad()
	{
		GamingBlockPlug.onLoad();
	}
	
	@Override
	public void onEnable()
	{
		final PluginManager pm = this.getServer().getPluginManager();
		this.gbp = new GamingBlockPlug(getServer(), getLogger(), this);
		try
		{
			getLogger().log(Level.INFO, "Plugin start !!");
			this.gbp.onEnable();
		}
		catch(RuntimeException ex)
		{
			getLogger().log(Level.SEVERE, "The file is broken and " + RefString.NAME + " can't open it. " + RefString.NAME + " is now disabled.");
			getLogger().log(Level.SEVERE, ex.toString());
			pm.registerEvents(new CrashListener(), this);
			for (Player player : getServer().getOnlinePlayers())
			{
				player.sendMessage(RefString.NAME + " failed to load, read the log file.");
			}
			stopPlugin();
			return;
		}
		final GPluginListener pluginListener = new GPluginListener();
		final GBlockListener blockListener = new GBlockListener();
		final GEntityListener entityListener = new GEntityListener();
		final GPlayerListener playerListener = new GPlayerListener(this.gbp);
		
		pm.registerEvents(pluginListener, this);
		pm.registerEvents(blockListener, this);
		pm.registerEvents(entityListener, this);
		pm.registerEvents(playerListener, this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		return this.gbp.onCommand(sender, cmd, label, args);
	}
	
	@Override
	public void onDisable()
	{
		this.gbp.onDisable();
	}
	
	public void stopPlugin()
	{
		this.setEnabled(false);
	}
}