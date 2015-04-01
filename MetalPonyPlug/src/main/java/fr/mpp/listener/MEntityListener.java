package fr.mpp.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemDespawnEvent;

import fr.mpp.mpp.ClassesUtils;

public class MEntityListener implements Listener
{
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(final EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof Player)
		{
			if (ClassesUtils.isInZone(event.getEntity().getLocation()))
			{
				event.setCancelled(true);
			}
		}
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
