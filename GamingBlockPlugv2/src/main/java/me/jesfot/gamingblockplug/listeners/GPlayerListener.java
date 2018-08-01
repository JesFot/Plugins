package me.jesfot.gamingblockplug.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.jesfot.gamingblockplug.data.GBPPlayer;
import me.jesfot.gamingblockplug.permission.PermissionHelper;
import me.jesfot.gamingblockplug.permission.StaticPerms;
import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;
import me.jesfot.gamingblockplug.roles.Role;
import me.jesfot.gamingblockplug.security.HalfInBedSystem;
import me.jesfot.gamingblockplug.utils.DataUtils;
import me.unei.configuration.api.IFlatConfiguration;
import me.unei.configuration.api.IYAMLConfiguration;

@SuppressWarnings("deprecation")
public class GPlayerListener implements Listener
{
	private final GamingBlockPlug plugin;

	public GPlayerListener(GamingBlockPlug plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerLogin(final PlayerLoginEvent event)
	{
		GBPPlayer player = this.plugin.getPlayerManager().registerBukkitPlayer(event.getPlayer());
		IFlatConfiguration offlines = this.plugin.getConfigurations().getConfig("offlines");
		DataUtils.safeReload(offlines);
		offlines.setString((this.plugin.isOnlineMode() ? "official" : "cracked") + "." + player.getHandler().getName().toLowerCase(), player.getUniqueId().toString());
		offlines.save();
	}

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		final GBPPlayer player = this.plugin.getPlayerManager().getPlayer(event.getPlayer());

		if (this.plugin.isOnlineMode())
		{
			this.plugin.getLogger().config("Server is online mode, no need to restore skin nor ask password");
		}
		else
		{
			this.plugin.getLogger().info("Starting Login system for " + player.getHandler().getName() + "...");
			this.plugin.getSystemManager().getLoginSystem().autoLogin(player);
		}
		if (PermissionHelper.testPermissionSilent(event.getPlayer(), StaticPerms.LOGIN_MOTD, false))
		{
			IYAMLConfiguration cfg = this.plugin.getConfigurations().getMainConfig();
			DataUtils.safeReload(cfg);
			if (cfg.contains("motd") && !cfg.getString("motd").isEmpty())
			{
				String msg = cfg.getString("motd");
				String[] msgs = msg.split(" n ");
				for (String str : msgs)
				{
					event.getPlayer().sendMessage(ChatColor.GOLD + str);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerRespawn(final PlayerRespawnEvent event)
	{
		//
	}

	@EventHandler
	public void onPlayerMove(final PlayerMoveEvent event)
	{
		if (this.plugin.getSystemManager().getLoginSystem().isLogin(event))
		{
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onPlayerClick(final PlayerInteractEvent event)
	{
		if (this.plugin.getSystemManager().getLoginSystem().isLogin(event))
		{
			event.setUseInteractedBlock(Result.DENY);
			event.setUseItemInHand(Result.DENY);
			event.setCancelled(true);
			return;
		}
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR))
		{
			if (event.getItem().getType().equals(Material.DEAD_BUSH))
			{
				event.getPlayer().openInventory(this.plugin.getSystemManager().getSharedChestSystem().getSharedInventory());
			}
		}
	}

	@EventHandler
	public void onSyncPlayerChat(final PlayerChatEvent event)
	{
		if (this.plugin.getSystemManager().getLoginSystem().isLogin(event))
		{
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onPLayerTeleport(final PlayerTeleportEvent event)
	{
		if (this.plugin.getSystemManager().getLoginSystem().isLogin(event))
		{
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onPlayergoBed(final PlayerBedEnterEvent event)
	{
		Player player = event.getPlayer();
		HalfInBedSystem hbs = this.plugin.getSystemManager().getHalfInBedSystem();
		hbs.updatePlayerList().enterBed(player);
		player.getServer().broadcastMessage(hbs.howManyInBedText());
		if(hbs.halfInBed())
		{
			this.plugin.broad("Half of the players are in a bed.");
			hbs.passNight(hbs.getPlayersInBed());
		}
	}

	@EventHandler
	public void onPlayerLeaveBed(final PlayerBedLeaveEvent event)
	{
		HalfInBedSystem hbs = this.plugin.getSystemManager().getHalfInBedSystem();
		hbs.updatePlayerList().leaveBed(event.getPlayer()).endPassNight();
	}

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		this.plugin.getPlayerManager().removePlayer(event.getPlayer());
	}

	@EventHandler
	public void onPlayerKick(final PlayerKickEvent event)
	{
		this.plugin.getPlayerManager().removePlayer(event.getPlayer());
	}

	@EventHandler
	public void onPlayerChat(final AsyncPlayerChatEvent event)
	{
		GBPPlayer player = this.plugin.getPlayerManager().getPlayer(event.getPlayer());
		Role role = this.plugin.getRoleManager().get(player.getRoleId());

		event.setFormat(role.prependFormat(event.getFormat()));
	}

	//Login


	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDamage(final EntityDamageByEntityEvent event)
	{
		if(event.getEntity() instanceof Player)
		{
			if(this.plugin.getSystemManager().getLoginSystem().isLogin(event.getEntity().getUniqueId()))
			{
				event.setDamage(0);
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDamage(final EntityDamageByBlockEvent event)
	{
		if(event.getEntity() instanceof Player)
		{
			if(this.plugin.getSystemManager().getLoginSystem().isLogin(event.getEntity().getUniqueId()))
			{
				event.setDamage(0);
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(final PlayerDeathEvent event)
	{
		if(this.plugin.getSystemManager().getLoginSystem().isLogin(event.getEntity().getUniqueId()))
		{
			event.setKeepInventory(true);
			event.setKeepLevel(true);
			event.setDeathMessage(event.getEntity().getDisplayName() + " died while login...");
			event.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
			return;
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent event)
	{
		if(event.getWhoClicked() instanceof Player)
		{
			if(this.plugin.getSystemManager().getLoginSystem().isLogin(event.getWhoClicked().getUniqueId()))
			{
				event.setResult(Result.DENY);
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void onDrag(InventoryDragEvent event)
	{
		if(event.getWhoClicked() instanceof Player)
		{
			if(this.plugin.getSystemManager().getLoginSystem().isLogin(event.getWhoClicked().getUniqueId()))
			{
				event.setResult(Result.DENY);
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void onPlayerCommand(final PlayerCommandPreprocessEvent event)
	{
		if(this.plugin.getSystemManager().getLoginSystem().isLogin(event.getPlayer().getUniqueId()))
		{
			event.setCancelled(true);
			return;
		}
	}
}
