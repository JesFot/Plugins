package fr.jesfot.gbp.bukkit;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.RefString;

public class BukkitPlugin extends JavaPlugin
{
	private GamingBlockPlug_1_9 gbp;
	
	@Override
	public void onLoad()
	{
		Server s = this.getServer();
		Logger l = this.getLogger();
		this.gbp = new GamingBlockPlug_1_9(s, l, this);
		this.gbp.onLoad();
	}
	
	@Override
	public void onEnable()
	{
		try
		{
			getLogger().info("Plugin start !!");
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
	public void onDisable()
	{
		this.gbp.onDisable();
	}
	
	public void stopPlugin()
	{
		this.setEnabled(false);
	}
}