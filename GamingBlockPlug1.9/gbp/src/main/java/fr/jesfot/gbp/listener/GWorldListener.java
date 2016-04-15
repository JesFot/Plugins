package fr.jesfot.gbp.listener;

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
		// Code ...
	}

	@EventHandler
	public void onWorldSave(final WorldSaveEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onWorldUnload(final WorldUnloadEvent event)
	{
		// Code ...
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
	public void onLighningStricke(final LightningStrikeEvent event)
	{
		// Code ...
	}
}