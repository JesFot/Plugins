package me.jesfot.gamingblockplug.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;

public class GWorldListener implements Listener
{
	private final GamingBlockPlug plugin;
	
	public GWorldListener(GamingBlockPlug plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onWorldLoad(final WorldLoadEvent event)
	{
		this.plugin.getWorldManager().registerBukkitWorld(event.getWorld());
	}
	
	@EventHandler
	public void onWorldUnload(final WorldUnloadEvent event)
	{
		this.plugin.getWorldManager().removeWorld(event.getWorld());
	}
}
