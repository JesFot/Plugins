package fr.gbp.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class MPlayerListener implements Listener
{
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLogin(PlayerLoginEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onPlayerRespawn(final PlayerRespawnEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onPlayerEnchant(final EnchantItemEvent event)
	{
		// Code ...
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRightClick(PlayerInteractEvent event)
	{
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			if(event.getMaterial().equals(Material.DEAD_BUSH))
			{
				event.getPlayer().sendMessage("[DEBUG] ok. Right click with dead bush.");
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerChat(final AsyncPlayerChatEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onPlayerKick(final PlayerKickEvent event)
	{
		// Code ..
	}

	@EventHandler
	public void onPlayerChangeWorld(final PlayerChangedWorldEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onPlayerGMChange(final PlayerGameModeChangeEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onPlayerTeleport(final PlayerTeleportEvent event)
	{
		// Code ...
	}
}
