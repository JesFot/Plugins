package fr.jesfot.gbp.listener;

import java.util.HashMap;
import java.util.Map.Entry;

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
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Sign;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.configuration.NBTSubConfig;
import fr.jesfot.gbp.economy.PlayerEconomy;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.permission.PermissionsHelper;
import fr.jesfot.gbp.shop.ShopObject;
import fr.jesfot.gbp.subsytems.HalfBedSys;
import fr.jesfot.gbp.utils.ItemInventory;
import fr.jesfot.gbp.utils.Utils;

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
		playerConf.setString("Pseudo", player.getName());
		playerConf.setString("DisplayName", player.getDisplayName()).writeNBTToFile();
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
		this.gbp.getConfigs().loadAll();
		if(this.gbp.getConfigs().getMainConfig().getConfig().contains("logmsg")
				&& this.gbp.getConfigs().getMainConfig().getConfig().getString("logmsg") != null)
		{
			String logmsg = this.gbp.getConfigs().getMainConfig().getConfig().getString("logmsg");
			String[] logmsgs = logmsg.split(" n ");
			for(String str : logmsgs)
			{
				event.getPlayer().sendMessage(ChatColor.GOLD + str);
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
		else if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			if(event.getClickedBlock().getType().equals(Material.WALL_SIGN))
			{
				Sign sign = (Sign)event.getClickedBlock().getState().getData();
				ShopObject shop = this.gbp.getShops().getShop(event.getClickedBlock().getRelative(sign.getAttachedFace())
						.getLocation());
				if(shop != null)
				{
					shop.updateSign();
					if(!PermissionsHelper.testPermission(event.getPlayer(), "GamingBlockPlug.shops.use", true,
							"&cYou are not allowed to use shops."))
					{
						event.setCancelled(true);
						return;
					}
					if(shop.getOwner().getUniqueId().equals(event.getPlayer().getUniqueId()))
					{
						event.getPlayer().sendMessage(Utils.color(this.gbp.getLang().get("shop.uses",
								"&7This shop has been used a total of &r<amount> &7time(s).")
								.replaceAll("<amount>", shop.getTimesUsed() + "")));
					}
					else
					{
						if(!shop.canAcceptAnotherTransaction())
						{
							event.getPlayer().sendMessage(Utils.color(this.gbp.getLang().get("shop.out",
								"&cThis shop is out of items.")));
							event.setCancelled(true);
							return;
						}
						PlayerEconomy eco = this.gbp.getEconomy().getPEconomy(event.getPlayer());
						if(!eco.hasEnough(shop.getPrice()))
						{
							event.getPlayer().sendMessage(Utils.color(this.gbp.getLang()
									.get("economy.notenough", "Not enought money !")));
							return;
						}
						this.gbp.getEconomy().pay(event.getPlayer(), shop.getOwner(), shop.getPrice());
						ItemStack is = new ItemStack(shop.getItem());
						is.setAmount(shop.getAmount());
						shop.getInventory().removeItem(is);
						HashMap<Integer, ItemStack> excess = event.getPlayer().getInventory().addItem(is);
						for(Entry<Integer, ItemStack> me : excess.entrySet())
						{
							event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), me.getValue());
						}
						String pName;
						if(!is.getItemMeta().hasDisplayName())
						{
							pName = is.getType().name().replace('_', ' ').toLowerCase() + "(s)";
						}
						else
						{
							pName = is.getItemMeta().getDisplayName() + "(s)";
						}
						event.getPlayer().sendMessage(Utils.color(this.gbp.getLang()
								.get("shop.send").replaceAll("<item>", pName).replaceAll("<money>", shop.getPrice()+"")
								.replaceAll("<player>", shop.getOwner().getName())));
						if(shop.getOwner().isOnline())
						{
							shop.getOwner().getPlayer().sendMessage(Utils.color(this.gbp.getLang()
								.get("shop.receive").replaceAll("<item>", pName).replaceAll("<money>", shop.getPrice()+"")
								.replaceAll("<player>", event.getPlayer().getName())));
						}
						shop.addUse();
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerChat(final AsyncPlayerChatEvent event)
	{
		String msg = "";
		if(event.getMessage().contains("${") && event.getMessage().contains("}"))
		{
			for(String arg : event.getMessage().split("\\$"))
			{
				if(arg.startsWith("{") && arg.contains("}"))
				{
					String var = arg.substring(1, arg.indexOf("}"));
					arg = this.gbp.getVars().getToString(var)+((arg.endsWith("}") && arg.indexOf("}")==arg.length()-1)
							? "" : arg.substring(arg.indexOf("}") + 1));
				}
				msg += arg + "";
			}
			event.setMessage(ChatColor.translateAlternateColorCodes('&', msg));
		}
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
		if(player.getName().equalsIgnoreCase("JesFot"))
		{
			player.sendMessage("Wake Full time: " + player.getWorld().getFullTime());
			player.sendMessage("Wake Relative time: " + player.getWorld().getTime());
		}
		if(player.getWorld().getTime() <= 2)
		{
			player.setHealth(player.getHealth() + 2);
		}
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