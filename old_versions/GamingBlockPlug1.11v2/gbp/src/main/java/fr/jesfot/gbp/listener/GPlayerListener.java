package fr.jesfot.gbp.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import fr.jesfot.gbp.GamingBlockPlug_1_11;
import fr.jesfot.gbp.configuration.Configuration;

public class GPlayerListener implements Listener
{
	private final GamingBlockPlug_1_11 gbp;
	
	public GPlayerListener(GamingBlockPlug_1_11 plugin)
	{
		this.gbp = plugin;
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent event)
	{
		this.gbp.getPlayerManager().login(event.getPlayer());
		Configuration offlineList = this.gbp.getConfigs().getConfig("offlines");
		offlineList.reloadConfig();
		offlineList.getConfig().set((this.gbp.isOnlineMode() ? "official" : "cracked") + "." + event.getPlayer().getName().toLowerCase(),
				event.getPlayer().getUniqueId().toString());
		offlineList.saveConfig();
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		this.gbp.getPlayerManager().join(event.getPlayer());
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

	@EventHandler
	public void onRightClick(PlayerInteractEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onPlayerChat(final AsyncPlayerChatEvent event) // Default format: "<%1$s> %2$s"
	{
		// Code ...
	}
	
	@EventHandler
	public void onPlayergoBed(final PlayerBedEnterEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onPlayerLeaveBed(final PlayerBedLeaveEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		// Code ...
		this.gbp.getPlayerManager().logout(event.getPlayer());
		this.gbp.getPlayerManager().removePlayer(event.getPlayer());
	}

	@EventHandler
	public void onPlayerKick(final PlayerKickEvent event)
	{
		// Code ..
		this.gbp.getPlayerManager().logout(event.getPlayer());
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
	
	@EventHandler
	public void onPlayerDeath(final PlayerDeathEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onPlayerCommand(final PlayerCommandPreprocessEvent event)
	{
		// Code ...
	}
}