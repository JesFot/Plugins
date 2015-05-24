package fr.gbp.listener;

import org.bukkit.World;
import org.bukkit.entity.Player;
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

import fr.gbp.GamingBlocksPlug;

public class MWorldListener implements Listener
{
	private GamingBlocksPlug gbp;
	
	public MWorldListener(GamingBlocksPlug p_gbp)
	{
		this.gbp = p_gbp;
	}
	
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
		World world = event.getWorld();
		long day = world.getFullTime()/24000;
		this.gbp.broad("World saved ! " + world.getName());
		long lastday = this.gbp.getConfig().getCustomConfig().getLong("worldsave.pay.lastday", 0);
		if(lastday != day)
		{
			this.gbp.broad("lastday!=day !");
			this.gbp.getConfig().getCustomConfig().set("worldsave.pay.lastday", day);
			for(Player player : world.getPlayers())
			{
				this.gbp.getEconomy().salary("daily", player);
			}
		}
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