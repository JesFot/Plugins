package fr.jesfot.gbp.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class GPluginListener implements Listener
{
	@EventHandler
	public void onPluginEnable(final PluginEnableEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onPluginDisable(final PluginDisableEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onServerCommand(final ServerCommandEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onRconCommand(final RemoteServerCommandEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onCommandBlockActivated(final BlockRedstoneEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onPlayerCommand(final PlayerCommandPreprocessEvent event)
	{
		// Code ...
	}
}