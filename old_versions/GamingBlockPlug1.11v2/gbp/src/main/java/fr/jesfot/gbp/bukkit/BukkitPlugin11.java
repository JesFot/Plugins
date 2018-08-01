package fr.jesfot.gbp.bukkit;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.jesfot.gbp.GamingBlockPlug_1_11;
import fr.jesfot.gbp.RefString;
import fr.jesfot.gbp.listener.GBlockListener;
import fr.jesfot.gbp.listener.GEntityListener;
import fr.jesfot.gbp.listener.GInventoryListener;
import fr.jesfot.gbp.listener.GPlayerListener;
import fr.jesfot.gbp.listener.GWorldListener;

public class BukkitPlugin11 extends JavaPlugin
{
	private GamingBlockPlug_1_11 gbp;
	
	private boolean goodVersion = true;
	
	@Override
	public void onLoad()
	{
		Server s = this.getServer();
		Logger l = this.getLogger();
		if(!s.getBukkitVersion().equalsIgnoreCase(RefString.BUKKIT_VERSION))
		{
			l.severe("The plugin cannot be loaded because of the craftbukkit version, "
					+ "must be " + RefString.BUKKIT_VERSION + " but was " + s.getBukkitVersion());
			this.goodVersion = false;
			this.stopPlugin();
			return;
		}
		this.gbp = new GamingBlockPlug_1_11(s, l, this);
		this.gbp.onLoad();
	}
	
	@Override
	public void onEnable()
	{
		if(!this.goodVersion)
		{
			this.stopPlugin();
			return;
		}
		getLogger().log(Level.INFO, "Registering listeners for plugin " + RefString.NAME + "...");
		final PluginManager pm = this.getServer().getPluginManager();
		
		getLogger().finest("Loading Block linstener...");
		pm.registerEvents(new GBlockListener(), this);
		
		getLogger().finest("Loading Entity linstener...");
		pm.registerEvents(new GEntityListener(), this);
		
		getLogger().finest("Loading Player linstener...");
		pm.registerEvents(new GPlayerListener(this.gbp), this);
		
		getLogger().finest("Loading Inventory linstener...");
		pm.registerEvents(new GInventoryListener(), this);
		
		getLogger().finest("Loading World linstener...");
		pm.registerEvents(new GWorldListener(), this);
		
		getLogger().log(Level.INFO, "Listeners registered for plugin " + RefString.NAME + ".");
		try
		{
			this.gbp.onEnable();
		}
		catch(RuntimeException ex)
		{
			getLogger().log(Level.SEVERE, "The file is broken and " + RefString.NAME + " can't open it. " + RefString.NAME + " is now disabled.");
			getLogger().log(Level.SEVERE, ex.toString());
			getLogger().log(Level.SEVERE, "", ex);
			for (Player player : getServer().getOnlinePlayers())
			{
				player.sendMessage(RefString.NAME + " failed to load, read the log file.");
			}
			this.stopPlugin();
			return;
		}
	}
	
	@Override
	public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args)
	{
		getLogger().warning("Attempting to execute a command not binded !");
		sender.getServer().broadcastMessage("Rien à voir...");
		return false;
	}
	
	@Override
	public void onDisable()
	{
		if(!this.goodVersion)
		{
			return;
		}
		this.gbp.onDisable();
	}
	
	public void stopPlugin()
	{
		this.getServer().getPluginManager().disablePlugin(this);
		this.setEnabled(false);
	}
}