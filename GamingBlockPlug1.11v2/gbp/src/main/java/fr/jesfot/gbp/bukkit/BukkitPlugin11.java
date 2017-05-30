package fr.jesfot.gbp.bukkit;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.jesfot.gbp.GamingBlockPlug_1_11;
import fr.jesfot.gbp.RefString;

public class BukkitPlugin11 extends JavaPlugin
{
	private GamingBlockPlug_1_11 gbp;
	
	private boolean goodVersion = true;
	
	@Override
	public void onLoad()
	{
		Server s = this.getServer();
		Logger l = this.getLogger();
		if(!s.getBukkitVersion().equalsIgnoreCase("1.11-R0.1-SNAPSHOT"))
		{
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