package fr.gbp.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
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
import org.bukkit.inventory.ItemStack;

import fr.gbp.GamingBlockPlug;
import fr.gbp.perms.GPermissions;
import fr.gbp.utils.GHalfBedSys;
import fr.gbp.utils.GUtils;
import fr.gbp.utils.ItemInventory;
import fr.gbp.utils.UPlayer;

public class GPlayerListener implements Listener
{
	private final GamingBlockPlug gbp;
	private final GHalfBedSys ghbs;
	
	public GPlayerListener(GamingBlockPlug p_gbp)
	{
		this.gbp = p_gbp;
		this.ghbs = new GHalfBedSys(p_gbp);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLogin(PlayerLoginEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		for(Player connected : this.gbp.getServer().getOnlinePlayers())
		{
			if(this.gbp.getConfig().getCustomConfig().getBoolean("hidenplayers."+connected.getName().toLowerCase(), false))
			{
				if(GPermissions.testPermissionSilent(event.getPlayer(), "GamingBlockPlug.mask.seeAll") && !connected.isOp())
				{
					continue;
				}
				if(GPermissions.testPermissionSilent(event.getPlayer(), "GamingBlockPlug.mask.seeAdmin") && connected.isOp())
				{
					continue;
				}
				event.getPlayer().hidePlayer(connected);
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
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			if(event.getClickedBlock().getType().equals(Material.SIGN_POST) || event.getClickedBlock().getType().equals(Material.WALL_SIGN))
			{
				Sign sign = (Sign)event.getClickedBlock().getState();
				if(sign.getLine(0).contains("[shop:"))
				{
					int ind = sign.getLine(0).indexOf("[shop") + 1;
					String info = sign.getLine(0).substring(ind, sign.getLine(0).indexOf(']', ind));
					String dats[] = info.split(":");
					try
					{
						double cost = Double.parseDouble(dats[1]);
						String item = dats[2].toUpperCase();
						Material mat = Material.getMaterial(item);
						int amount = Integer.parseInt(dats[3]);
						String plName = dats[4];
						OfflinePlayer pl = UPlayer.getPlayerByName(plName);
						if(pl == null)
						{
							pl = UPlayer.getPlayerByNameOff(plName);
						}
						ItemStack is = new ItemStack(mat, amount);
						BlockFace chestFace = GUtils.getDir(sign.getBlock(), Material.CHEST);
						this.gbp.broad("Shop: Before tests");
						if(this.gbp.getEconomy().getPEco(event.getPlayer()).hasEnough(cost) && (plName=="console" || pl != null))
						{
							this.gbp.broad("Shop: player.hasEnough() == true");
							if(chestFace != null)
							{
								this.gbp.broad("Shop: Chest.isPresent() == true");
								Chest chest = (Chest)sign.getBlock().getRelative(chestFace).getState();
								if(chest.getBlockInventory().containsAtLeast(is, amount))
								{
									this.gbp.broad("Shop: Chest.contains("+is.getType().name()+") == true");
									event.getPlayer().getInventory().addItem(is);
									this.gbp.getEconomy().pay(event.getPlayer(), pl, cost);
									chest.getBlockInventory().removeItem(is);
								}
								else
								{
									this.gbp.broad("Shop: Chest.contains("+is.getType().name()+") == false");
								}
							}
						}
					}
					catch(NumberFormatException ex)
					{
						this.gbp.broad("Invalid shop: str -/> int");
						this.gbp.broad(info);
						return;
					}
					catch(IndexOutOfBoundsException ex)
					{
						this.gbp.broad("Invalid shop. [shop:<price>:<itemID>:<itemAmount>:<Player>]");
						this.gbp.broad(info);
						return;
					}
					this.gbp.broad(info);
				}
			}
		}
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			if(event.getMaterial().equals(Material.DEAD_BUSH))
			{
				ItemInventory.openPlayerInv(event.getPlayer(), this.gbp.getEconomy().getPEco(event.getPlayer()).getMenu());
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerChat(final AsyncPlayerChatEvent event)
	{
		Map<String, String> status = new HashMap<String, String>();
		status.put("admin", "&4[Admin]");
		status.put("dev", "&a[Dev]");
		status.put("normal", "");
		status.put("color_name", "&1");
		if(this.gbp.getConfig().getCustomConfig().contains("chat.naming"))
		{
			for(String keys : this.gbp.getConfig().getCustomConfig().getConfigurationSection("chat.naming").getKeys(false))
			{
				String def = "";
				if(keys.equalsIgnoreCase("admin"))
				{
					def = "&4[Admin]";
				}
				if(keys.equalsIgnoreCase("dev"))
				{
					def = "&a[Dev]";
				}
				if(keys.equalsIgnoreCase("color_name"))
				{
					def = "&1";
				}
				status.put(keys, this.gbp.getConfig().getCustomConfig().getString("chat.naming."+keys, def));
			}
		}
		Map<String, List<String>> groups = new HashMap<String, List<String>>();
		if(this.gbp.getConfig().getCustomConfig().contains("groups"))
		{
			for(String key : this.gbp.getConfig().getCustomConfig().getConfigurationSection("groups").getKeys(false))
			{
				List<String> names = this.gbp.getConfig().getCustomConfig().getStringList("groups."+key);
				groups.put(key, names);
			}
		}
		List<String> usersGroups = new ArrayList<String>();
		if(!usersGroups.contains("admin") && event.getPlayer().isOp())
		{
			usersGroups.add("admin");
		}
		for(String groupName : groups.keySet())
		{
			for(String playerName : groups.get(groupName))
			{
				if(playerName.equals(event.getPlayer().getName()))
				{
					if(!usersGroups.contains(groupName))
					{
						usersGroups.add(groupName);
					}
				}
			}
		}
		String result = "";
		String teams = "#$+";
		String pseudoColor = status.get("color_name");
		List<String> usersTeams = new ArrayList<String>();
		for(String group : usersGroups)
		{
			if(group.startsWith("team_"))
			{
				usersTeams.add(group);
				usersGroups.remove(group);
			}
			else if(group.startsWith("color_"))
			{
				String color = group.substring(6).toUpperCase();
				if(color.length() == 1)
				{
					pseudoColor = "&"+color;
				}
				else
				{
					ChatColor t = ChatColor.valueOf(color);
					Player jesfot = UPlayer.getPlayerByName("JesFot");
					if(jesfot != null)
					{
						jesfot.sendMessage("[DEBUG]"+t+"test de coloration");
					}
					pseudoColor = new String(new char[]{'&', t.getChar()});
					usersGroups.remove(group);
				}
			}
			else
			{
				result = status.get(group) != null ? status.get(group) : result;
			}
		}
		for(String str : usersTeams)
		{
			if(status.containsKey(str))
			{
				teams = status.get(str) + " ";
			}
		}
		teams = teams.endsWith(" ") ? teams.substring(0, teams.length() - 1) : teams;
		String subTotal = (result + teams).replace("#$+", "");
		String total = subTotal + pseudoColor;
		if(subTotal.charAt(subTotal.length() - 2) == '&')
		{
			total = subTotal;
		}
		event.setFormat(ChatColor.translateAlternateColorCodes('&', total+"<%1$s>&r %2$s"));
		event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayergoBed(final PlayerBedEnterEvent event)
	{
		Player player = event.getPlayer();
		this.ghbs.updatePlayers().addPlayerInBed(player);
		player.getServer().broadcastMessage(this.ghbs.howManyInBedText());
		if(this.ghbs.hasHalfInBed())
		{
			this.gbp.broad(this.gbp.getLang().get("hibs.hib", "HalfInBed !!"));
			this.ghbs.passNight(this.ghbs.getPlayersInBed());
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLeaveBed(final PlayerBedLeaveEvent event)
	{
		Player player = event.getPlayer();
		this.ghbs.updatePlayers().removePlayerInBed(player).endPassNight();
	}
	
	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		if(this.gbp.getConfig().getCustomConfig().getBoolean("hidenplayers."+event.getPlayer().getName().toLowerCase(), false))
		{
			this.gbp.getConfig().getCustomConfig().set("hidenplayers."+event.getPlayer().getName().toLowerCase(), false);
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
		if(event.getNewGameMode().equals(GameMode.CREATIVE))
		{
			if(!event.getPlayer().isOp())
			{
				//event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "You are not allowed to be in creative mode.");
			}
		}
	}

	@EventHandler
	public void onPlayerTeleport(final PlayerTeleportEvent event)
	{
		// Code ...
	}
}