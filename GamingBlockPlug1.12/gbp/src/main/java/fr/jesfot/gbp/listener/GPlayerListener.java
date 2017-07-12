package fr.jesfot.gbp.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
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
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Sign;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import fr.jesfot.gbp.GamingBlockPlug_1_12;
import fr.jesfot.gbp.configuration.Configurations;
import fr.jesfot.gbp.configuration.NBTConfig;
import fr.jesfot.gbp.configuration.NBTSubConfig;
import fr.jesfot.gbp.economy.PlayerEconomy;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.permission.PermissionsHelper;
import fr.jesfot.gbp.secure.LoginPrompt;
import fr.jesfot.gbp.secure.RegisterPrompt;
import fr.jesfot.gbp.secure.SecurityLoginSys;
import fr.jesfot.gbp.shop.ShopObject;
import fr.jesfot.gbp.stats.PlayerStatistics;
import fr.jesfot.gbp.subsytems.HalfBedSys;
import fr.jesfot.gbp.subsytems.SkinRestoreSys;
import fr.jesfot.gbp.utils.ItemInventory;
import fr.jesfot.gbp.utils.Utils;

public class GPlayerListener implements Listener
{
	private final GamingBlockPlug_1_12 gbp;
	private final HalfBedSys hbs;
	private final SecurityLoginSys sls;
	private final SkinRestoreSys srs;
	private final PlayerStatistics stat;
	
	public GPlayerListener(GamingBlockPlug_1_12 plugin)
	{
		this.gbp = plugin;
		this.hbs = new HalfBedSys(plugin);
		this.sls = new SecurityLoginSys(plugin);
		this.srs = new SkinRestoreSys(plugin);
		this.regPerms();
		this.stat = new PlayerStatistics(plugin);
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
			this.gbp.getLogger().info("Player " + event.getPlayer().getName() + " spawned in a block, teleport to world's"
					+ " spawn location.");
			event.getPlayer().teleport(loc.getWorld().getSpawnLocation(), TeleportCause.PLUGIN);
		}
		this.gbp.getLogger().info(event.getPlayer().getName() + " logged in with unique id '"
				+ event.getPlayer().getUniqueId().toString() + "'");
		GamingBlockPlug_1_12.getMyLogger().info(event.getPlayer().getName() + "[/" + event.getAddress().getHostAddress()
				+ "] logged in with unique ID '" + event.getPlayer().getUniqueId().toString() + "'");
		this.gbp.getConfigs().getConfig("offlines").reloadConfig();
		this.gbp.getConfigs().getConfig("offlines").getConfig().set((this.gbp.isOnlineMode() ? "official" : "cracked") + "."
				+ event.getPlayer().getName().toLowerCase(), event.getPlayer().getUniqueId().toString());
		this.gbp.getConfigs().getConfig("offlines").saveConfig();
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		// Connection statistic Start :
		this.stat.player(player);
		// Connection statistic End !
		if(this.gbp.isOnlineMode())
		{
			this.gbp.getLogger().info("Server is running online mode, no need to restore skin or ask for password...");
		}
		else
		{
			this.gbp.getLogger().info("Starting SkinRestorerSystem....");
			Bukkit.getScheduler().runTaskAsynchronously(this.gbp.getPlugin(), new Runnable(){
				public void run()
				{
					GPlayerListener.this.srs.MAJSkin(player);
				}
			});
			this.gbp.getLogger().info("SkinRestorerSystem future task launched for " + player.getName());
			// Check login
			ConversationFactory factory = new ConversationFactory(this.gbp.getPlugin());
			final Map<Object, Object> data = new HashMap<Object, Object>();
			data.put("tries", Integer.valueOf(0));
			data.put("kick", Boolean.FALSE);
			factory.withInitialSessionData(data).withLocalEcho(false).withPrefix(new ConversationPrefix(){
				public String getPrefix(ConversationContext context)
				{
					return ChatColor.GREEN + "Login" + ChatColor.RESET + " ";
				}
			}).addConversationAbandonedListener(new ConversationAbandonedListener(){
				public void conversationAbandoned(ConversationAbandonedEvent event)
				{
					if(!event.gracefulExit() && !((Boolean)event.getContext().getSessionData("kick")))
					{
						GamingBlockPlug_1_12.getMe().getLogger().info(player.getName()
								+ " didn't login or register until 5 minutes.");
						player.kickPlayer("You must login until 5 minutes");
						GamingBlockPlug_1_12.getMyLogger().info(player.getName() + " was kicked because of no passwords until"
								+ " 5 minutes.");
					}
				}
			});
			Conversation conv;
			this.gbp.getLogger().info("Starting Login system for " + player.getName() + "...");
			this.sls.addLogin(player);
			if(this.sls.hasAccount(player))
			{
				player.sendMessage(ChatColor.RED + "Please enter your password :");
				conv = factory.withFirstPrompt(new LoginPrompt(this.sls, player, this.stat)).buildConversation(player);
				this.gbp.getLogger().info(player.getName() + " has already an account, asking for password...");
				GamingBlockPlug_1_12.getMyLogger().info(player.getName() + " has already an account, asking for password...");
			}
			else
			{
				player.sendMessage(ChatColor.RED + "Register with a new password :");
				conv = factory.withFirstPrompt(new RegisterPrompt(this.sls, player, this.stat)).buildConversation(player);
				this.gbp.getLogger().info(player.getName() + " didn't has already an account, asking for new password...");
				GamingBlockPlug_1_12.getMyLogger().info(player.getName() + " didn't has already an account, asking for new "
						+ "password...");
			}
			conv.begin();
		}
		// End check login
		// Team / Grade Start :
		String lm = event.getJoinMessage();
		NBTSubConfig playerConf = new NBTSubConfig(this.gbp.getConfigFolder("playerdatas"), player.getUniqueId());
		String tUid = playerConf.readNBTFromFile().getCopy().getString("Team");
		if(tUid.equals(""))
		{
			tUid = "default";
			playerConf.setString("Team", tUid).writeNBTToFile();
		}
		// Team / Grade End !
		boolean isMasked = playerConf.getCopy().getBoolean("Masked");
		playerConf.setString("Pseudo", player.getName());
		playerConf.setString("DisplayName", player.getDisplayName()).writeNBTToFile();
		if(isMasked)
		{
			this.gbp.getLogger().info(player.getName() + " is masked to other players.");
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
				event.getPlayer().sendRawMessage(ChatColor.GOLD + str);
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
		if(this.sls.isLogin(event.getPlayer()))
		{
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onPlayerEnchant(final EnchantItemEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onRightClick(PlayerInteractEvent event)
	{
		if(this.sls.isLogin(event.getPlayer()))
		{
			event.setCancelled(true);
			return;
		}
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
						this.gbp.getLogger().info(event.getPlayer().getName() + " tried to use a sign while not allowed.");
						event.setCancelled(true);
						return;
					}
					if(shop.isOwner(event.getPlayer()))
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
			else if(event.getClickedBlock().getType().equals(Material.CHEST))
			{
				ShopObject shop = this.gbp.getShops().getShop(event.getClickedBlock().getLocation());
				if(shop != null)
				{
					if(!shop.isOwner(event.getPlayer()))
					{
						NBTSubConfig ownerCfg = new NBTSubConfig(this.gbp.getConfigFolder("playerdatas"), shop.getOwner().getUniqueId());
						NBTSubConfig userCfg = new NBTSubConfig(this.gbp.getConfigFolder("playerdatas"), event.getPlayer().getUniqueId());
						if(PermissionsHelper.testPermissionSilent(event.getPlayer(), "GamingBlockPlug.shops.op", false))
						{
							this.gbp.getLogger().info(event.getPlayer().getName() + " Is opening an other's chest.");
							event.getPlayer().sendMessage("You are a openning chest that is not yours...");
							return;
						}
						String tN = ownerCfg.readNBTFromFile().getCopy().getString("Team");
						if(tN.equalsIgnoreCase(userCfg.readNBTFromFile().getCopy().getString("Team")))
						{
							if(this.gbp.getTeams().getIfExists(tN).canOpenGroupChest())
							{
								this.gbp.getLogger().info(event.getPlayer().getName() + " Is opening an other's chest.");
								event.getPlayer().sendMessage("You are a openning chest that is not yours...");
								return;
							}
						}
						this.gbp.getLogger().info(event.getPlayer().getName() + " tried to open chest while not allowed.");
						event.getPlayer().sendMessage("You are not allowed to open other's chests...");
						event.setCancelled(true);
						return;
					}
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerChat(final AsyncPlayerChatEvent event) // Default format: "<%1$s> %2$s"
	{
		if(this.sls.isLogin(event.getPlayer()))
		{
			event.setCancelled(true);
			return;
		}
		NBTConfig playerCfg = new NBTConfig(this.gbp.getConfigFolder(Configurations.PLAYERS_DATS), event.getPlayer().getUniqueId());
		if(playerCfg.readNBTFromFile().getCopy().getBoolean("Muted"))
		{
			event.getPlayer().sendMessage(this.gbp.getLang().getColored("player.muted", "&cYour are muted !"));
			event.setCancelled(true);
			return;
		}
		String teaming = "&7[";
		String tUid = playerCfg.readNBTFromFile().getCopy().getString("Team");
		teaming += this.gbp.getTeams().getIfExists(tUid).getChatColor();
		teaming += this.gbp.getTeams().getIfExists(tUid).getDisplayName();
		teaming += "&r&7]&r";
		teaming = ChatColor.translateAlternateColorCodes('&', teaming);
		event.setFormat(teaming + event.getFormat());
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
		else
		{
			event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
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
		if(player.getWorld().getTime() <= 2)
		{
			double nHealth = player.getHealth() + 2;
			if(nHealth < player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
			{
				this.gbp.getLogger().info("Healing " + player.getName() + ". (1 heart -> " + nHealth/2 + ")");
				player.setHealth(nHealth);
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		NBTSubConfig playerConf = new NBTSubConfig(this.gbp.getConfigFolder(Configurations.PLAYERS_DATS), player.getUniqueId());
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
		GamingBlockPlug_1_12.getMyLogger().info(player.getName() + " logged out. UID == '" + player.getUniqueId() + "'");
		this.stat.player(player);
		//this.stat.logout();
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
		if(this.sls.isLogin(event.getPlayer()))
		{
			event.setCancelled(true);
			return;
		}
	}
	
	
	
	
	
	//Login

	
	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDamage(final EntityDamageByEntityEvent event)
	{
		if(event.getEntity() instanceof Player)
		{
			if(this.sls.isLogin((Player)event.getEntity()))
			{
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
			if(this.sls.isLogin((Player)event.getEntity()))
			{
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(final PlayerDeathEvent event)
	{
		if(this.sls.isLogin(event.getEntity()))
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
			if(this.sls.isLogin((Player)event.getWhoClicked()))
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
			if(this.sls.isLogin((Player)event.getWhoClicked()))
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
		if(this.sls.isLogin(event.getPlayer()))
		{
			event.setCancelled(true);
			return;
		}
	}
}