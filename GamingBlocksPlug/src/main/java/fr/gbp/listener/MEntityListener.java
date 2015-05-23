package fr.gbp.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemDespawnEvent;

public class MEntityListener implements Listener
{
	@EventHandler(priority = EventPriority.LOW)
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
