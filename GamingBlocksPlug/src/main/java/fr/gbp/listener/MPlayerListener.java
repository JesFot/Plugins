package fr.gbp.listener;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
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

import fr.gbp.GamingBlocksPlug;
import fr.gbp.utils.ItemInventory;

public class MPlayerListener implements Listener
{
	private GamingBlocksPlug gbp;
	
	public MPlayerListener(GamingBlocksPlug p_gbp)
	{
		this.gbp = p_gbp;
	}
	
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
		this.gbp.getConfig().reloadCustomConfig();
		int i = this.gbp.getConfig().getCustomConfig().getInt("salary.time");
		for(World world : this.gbp.getServer().getWorlds())
		{
			if(world.getTime() >= 23999 && i == 0)
			{
				i = 1;
				for(Player player : this.gbp.getServer().getOnlinePlayers())
				{
					this.gbp.getEconomy().salary("daily", player);
				}
			}
			if(world.getTime() < 24000)
			{
				i = 0;
			}
			this.gbp.getConfig().getCustomConfig().set("salary.time", i);
			this.gbp.getConfig().saveCustomConfig();
		}
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
				ItemInventory.openPlayerInv(event.getPlayer(), this.gbp.getEconomy().getPEco(event.getPlayer()).getInventory());
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
