package fr.jesfot.gbp.bukkit;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.jesfot.gbp.GamingBlockPlug_1_12;
import fr.jesfot.gbp.RefString;
import fr.jesfot.gbp.listener.GBlockListener;
import fr.jesfot.gbp.listener.GEntityListener;
import fr.jesfot.gbp.listener.GInventoryListener;
import fr.jesfot.gbp.listener.GPlayerListener;
import fr.jesfot.gbp.listener.GPluginListener;

public class BukkitPlugin12 extends JavaPlugin
{
	private GamingBlockPlug_1_12 gbp;
	
	private GPlayerListener playerListener;
	
	private boolean goodVersion = true;
	
	@Override
	public void onLoad()
	{
		Server s = this.getServer();
		Logger l = this.getLogger();
		if(!s.getBukkitVersion().equalsIgnoreCase("1.12-R0.1-SNAPSHOT"))
		{
			this.goodVersion = false;
			this.stopPlugin();
			return;
		}
		this.gbp = new GamingBlockPlug_1_12(s, l, this);
		this.gbp.onLoad();
		
		playerListener = new GPlayerListener(this.gbp);
	}
	
	@Override
	public void onEnable()
	{
		if(!this.goodVersion)
		{
			this.stopPlugin();
			return;
		}
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
		getLogger().info("Registering listeners...");
		final PluginManager pm = this.getServer().getPluginManager();
		
		final GPluginListener pluginListener = new GPluginListener(this.gbp);
		final GBlockListener blockListener = new GBlockListener(this.gbp);
		final GEntityListener entityListener = new GEntityListener();
		final GInventoryListener inventoryListener = new GInventoryListener(this.gbp);
		
		pm.registerEvents(pluginListener, this);
		pm.registerEvents(blockListener, this);
		pm.registerEvents(entityListener, this);
		pm.registerEvents(playerListener, this);
		pm.registerEvents(inventoryListener, this);
		getLogger().info("Listeners registered !");
	}
	
	@Override
	public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args)
	{
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
		this.setEnabled(false);
	}
}