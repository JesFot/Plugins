package me.emp.listener;

import me.emp.bukkit.BukkitPlugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class MPluginListener implements Listener
{
	BukkitPlugin pl;
	
	@EventHandler(priority = EventPriority.HIGH)
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
