package fr.gbp.listener;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemDespawnEvent;

import fr.gbp.utils.GWorldUtil;

public class GEntityListener implements Listener
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
		if(event.getEntityType().equals(EntityType.PLAYER))
		{
			Player player = (Player)event.getEntity();
			event.setDroppedExp(player.getTotalExperience() - Math.round(player.getExp()) - (player.getExpToLevel()*(3/4)));
			World world = player.getWorld();
			Location respawn = player.getBedSpawnLocation();
			if(GWorldUtil.compareWorlds(respawn.getWorld(), world))
			{
				return;
			}
			player.setBedSpawnLocation(world.getSpawnLocation());
		}
	}

	@EventHandler
	public void onItemDespawn(final ItemDespawnEvent event)
	{
		// Code ...
	}
}