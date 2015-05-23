package fr.gbp.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class MPluginListener implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginEnable(final PluginEnableEvent event)
	{
		// Code ...
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
