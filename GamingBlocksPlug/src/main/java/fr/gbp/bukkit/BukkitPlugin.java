package fr.mpp.bukkit;

import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.mpp.MetalPonyPlug;
import fr.mpp.listener.*;
import fr.mpp.RefString;

public class BukkitPlugin extends JavaPlugin
{
	private MetalPonyPlug mpp;
	
	@Override
	public void onLoad()
	{
		MetalPonyPlug.onLoad();
	}
	
	@Override
	public void onEnable()
	{
		final PluginManager pm = this.getServer().getPluginManager();
		mpp = new MetalPonyPlug(getServer(), getLogger(), this);
		try
		{
			mpp.onEnable();
		}
		catch (RuntimeException ex)
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
		final MPluginListener pluginListener = new MPluginListener();
		final MBlockListener blockListener = new MBlockListener();
		final MEntityListener entityListener = new MEntityListener();
		final MPlayerListener playerListener = new MPlayerListener();
		
		pm.registerEvents(pluginListener, this);
		pm.registerEvents(blockListener, this);
		pm.registerEvents(entityListener, this);
		pm.registerEvents(playerListener, this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		return mpp.onCommand(sender, cmd, label, args);
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