package fr.jesfot.gbp.listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
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
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.configuration.NBTSubConfig;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.permission.PermissionsHelper;
import fr.jesfot.gbp.subsytems.HalfBedSys;
import fr.jesfot.gbp.utils.ItemInventory;

public class GPlayerListener implements Listener
{
	private final GamingBlockPlug_1_9 gbp;
	private final HalfBedSys hbs;
	
	public GPlayerListener(GamingBlockPlug_1_9 plugin)
	{
		this.gbp = plugin;
		this.hbs = new HalfBedSys(plugin);
		this.regPerms();
	}
	
	private void regPerms()
	{
		Permission mask = this.gbp.getPermissionManager().addPermission("GamingBlockPlug.mask", PermissionDefault.OP,
				"Permission for all part of the vanishing", Permissions.globalGBP);
		this.gbp.getPermissionManager().addPermission("GamingBlockPlug.mask.seeAll", PermissionDefault.OP,
				"Allows you to see not-op masked players", mask);
		this.gbp.getPermissionManager().addPermission("GamingBlockPlug.mask.seeAdmin", PermissionDefault.FALSE,
				"Allows you to see op masked players", mask);
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent event)
	{
		Location loc = event.getPlayer().getLocation();
		if(!loc.getWorld().getBlockAt(loc).isEmpty())
		{
			event.getPlayer().teleport(loc.getWorld().getSpawnLocation(), TeleportCause.PLUGIN);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		String lm = event.getJoinMessage();
		NBTSubConfig playerConf = new NBTSubConfig(this.gbp.getConfigFolder("playerdatas"), player.getUniqueId());
		boolean isMasked = playerConf.readNBTFromFile().getCopy().getBoolean("Masked");
		if(isMasked)
		{
			event.setJoinMessage("");
			for(Player connected : this.gbp.getOnlinePlayers())
			{
				if(PermissionsHelper.testPermissionSilent(connected, "GamingBlockPlug.mask.seeAll", false) && !player.isOp())
				{
					connected.sendRawMessage(lm);
					continue;
				}
				if(PermissionsHelper.testPermissionSilent(connected, "GamingBlockPlug.mask.seeAdmin", false) && player.isOp())
				{
					connected.sendRawMessage(lm);
					continue;
				}
				connected.hidePlayer(player);
			}
		}
		boolean seeAll = PermissionsHelper.testPermissionSilent(player, "GamingBlockPlug.mask.seeAll", false) && !player.isOp();
		boolean seeAdm = PermissionsHelper.testPermissionSilent(player, "GamingBlockPlug.mask.seeAdmin", false) && player.isOp();
		for(Player connected : this.gbp.getOnlinePlayers())
		{
			NBTSubConfig conConf = new NBTSubConfig(this.gbp.getConfigFolder("playerdatas"), connected.getUniqueId());
			boolean hiden = conConf.readNBTFromFile().getCopy().getBoolean("Masked");
			if(hiden)
			{
				if(seeAll || seeAdm)
				{
					continue;
				}
				player.hidePlayer(connected);
			}
		}
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
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR))
		{
			if(event.getMaterial().equals(Material.DEAD_BUSH))
			{
				ItemInventory.openPlayerInv(event.getPlayer(),
						this.gbp.getEconomy().getPEconomy(event.getPlayer()).getMenu());
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerChat(final AsyncPlayerChatEvent event)
	{
		event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayergoBed(final PlayerBedEnterEvent event)
	{
		Player player = event.getPlayer();
		this.hbs.updatePlayers().addPlayerInBed(player);
		player.getServer().broadcastMessage(this.hbs.howManyInBedText());
		if(this.hbs.hasHalfInBed())
		{
			this.gbp.broad(this.gbp.getLang().get("hibs.hib", "HalfInBed !!"));
			this.hbs.passNight(this.hbs.getPlayersInBed());
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLeaveBed(final PlayerBedLeaveEvent event)
	{
		Player player = event.getPlayer();
		this.hbs.updatePlayers().removePlayerInBed(player).endPassNight();
	}
	
	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		NBTSubConfig playerConf = new NBTSubConfig(this.gbp.getConfigFolder("playerdatas"), player.getUniqueId());
		boolean isMasked = playerConf.readNBTFromFile().getCopy().getBoolean("Masked");
		if(isMasked)
		{
			playerConf.setBoolean("Masked", false).writeNBTToFile();
			for(Player connected : this.gbp.getOnlinePlayers())
			{
				if(PermissionsHelper.testPermissionSilent(connected, "GamingBlockPlug.mask.seeAll", false) && !player.isOp())
				{
					connected.sendRawMessage(event.getQuitMessage());
					continue;
				}
				if(PermissionsHelper.testPermissionSilent(connected, "GamingBlockPlug.mask.seeAdmin", false) && player.isOp())
				{
					connected.sendRawMessage(event.getQuitMessage());
					continue;
				}
			}
			event.setQuitMessage("");
		}
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