package fr.jesfot.gbp.listener;

import org.bukkit.command.Command;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.SpawnChangeEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import fr.jesfot.gbp.GamingBlockPlug_1_11;

public class GWorldListener implements Listener
{
	@EventHandler(priority = EventPriority.LOWEST)
	public void onWorldInit(final WorldInitEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onWorldLoad(final WorldLoadEvent event)
	{
		GamingBlockPlug_1_11.getMe().getLogger().info("World " + event.getWorld().getName() + " loaded succesfully.");
		Command.broadcastCommandMessage(GamingBlockPlug_1_11.getMe().getServer().getConsoleSender(),
				"Loaded world " + event.getWorld().getName(), false);
	}

	@EventHandler
	public void onWorldSave(final WorldSaveEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onWorldUnload(final WorldUnloadEvent event)
	{
		GamingBlockPlug_1_11.getMe().getLogger().info("World " + event.getWorld().getName() + " unloaded succesfuly.");
		Command.broadcastCommandMessage(GamingBlockPlug_1_11.getMe().getServer().getConsoleSender(),
				"Unloaded world " + event.getWorld().getName(), false);
	}

	@EventHandler
	public void onPortalCreate(final PortalCreateEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onSpawnChange(final SpawnChangeEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onWeatherChange(final WeatherChangeEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onLightningStrike(final LightningStrikeEvent event)
	{
		// Code ...
	}
}