package fr.mppon.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerCommandEvent;

import fr.mppon.bukkit.BukkitPlugin;

public class MListener implements Listener
{
	BukkitPlugin pl;
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPluginEnable(final PluginEnableEvent event)
	{
		if (event.getPlugin().equals(pl))
		{
			if (!pl.getConfig().getBoolean("mpp_active"))
			{
				event.getPlugin().getServer().getPluginManager().disablePlugin(pl);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginDisable(final PluginDisableEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onServerCommand(final ServerCommandEvent event)
	{
		// Code ...
	}
}
