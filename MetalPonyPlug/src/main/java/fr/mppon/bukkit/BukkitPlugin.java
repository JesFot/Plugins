package fr.mppon.bukkit;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.mppon.MetalPonyPlug_on;
import fr.mppon.RefString;
import fr.mppon.listener.MListener;

public class BukkitPlugin extends JavaPlugin
{
	private MetalPonyPlug_on mpp;
	
	@Override
	public void onLoad()
	{
		MetalPonyPlug_on.onLoad();
	}
	
	@Override
	public void onEnable()
	{
		this.reloadConfig();
		final PluginManager pm = this.getServer().getPluginManager();
		mpp = new MetalPonyPlug_on(getServer(), getLogger(), this);
		try
		{
			getLogger().log(Level.INFO, "Plugin start !!");
			mpp.onEnable();
		}
		catch (RuntimeException ex)
		{
			getLogger().log(Level.SEVERE, "The file is broken and " + RefString.NAME + " can't open it. " + RefString.NAME + " is now disabled.");
			getLogger().log(Level.SEVERE, ex.toString());
			for (Player player : getServer().getOnlinePlayers())
			{
				player.sendMessage(RefString.NAME + " failed to load, read the log file.");
			}
			stopPlugin();
			return;
		}
		final MListener pluginListener = new MListener();
		
		pm.registerEvents(pluginListener, this);
	}
	
	@Override
	public void onDisable()
	{
		mpp.onDisable();
	}
	
	public void stopPlugin()
	{
		this.setEnabled(false);
	}
}