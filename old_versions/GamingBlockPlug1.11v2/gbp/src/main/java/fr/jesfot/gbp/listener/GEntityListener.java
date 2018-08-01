package fr.jesfot.gbp.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.ItemDespawnEvent;

public class GEntityListener implements Listener
{
	@EventHandler
	public void entityPortIn(final EntityPortalEnterEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onEntityDamage(final EntityDamageByEntityEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onEntitySpawn(final CreatureSpawnEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onEntityExplode(final EntityExplodeEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onEntityChangeBlock(final EntityChangeBlockEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onEntityDeath(final EntityDeathEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onItemDespawn(final ItemDespawnEvent event)
	{
		// Code ...
	}
}