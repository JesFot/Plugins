package fr.jesfot.gbp.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;

import fr.jesfot.gbp.event.EntityPortalEnterEvent;

public class GEntityListener implements Listener
{
	//private GamingBlockPlug_1_9 gbp;
	
	@EventHandler
	public void entityPortIn(final org.bukkit.event.entity.EntityPortalEnterEvent event)
	{
		EntityPortalEnterEvent event2 = new EntityPortalEnterEvent(event.getEntity(), event.getLocation());
		this.onEntityEnterPortal(event2);
	}
	
	public void onEntityEnterPortal(final EntityPortalEnterEvent event)
	{
		// Code ...
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDamage(final EntityDamageByEntityEvent event)
	{
		if(event.getCause().equals(DamageCause.FALL))
		{
			event.setDamage(DamageModifier.BASE, event.getDamage() - 1);
		}
	}

	@EventHandler
	public void onEntitySpawn(final CreatureSpawnEvent event)
	{
		// Code ...
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onEntityExplode(final EntityExplodeEvent event)
	{
		EntityType type = event.getEntityType();
		if(type.equals(EntityType.CREEPER))
		{
			event.blockList().clear();
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onEntityChangeBlock(final EntityChangeBlockEvent event)
	{
		if(event.getEntityType().equals(EntityType.ENDERMAN))
		{
			event.setCancelled(true);
		}
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