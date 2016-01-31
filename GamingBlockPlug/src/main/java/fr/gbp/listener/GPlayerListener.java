package fr.gbp.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
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

import fr.gbp.GamingBlockPlug;
import fr.gbp.perms.GPermissions;
import fr.gbp.utils.GHalfBedSys;
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
		/*if(event.getPlayer().getName().equalsIgnoreCase("JesFot"))
		{
			result = "&a[Dev]";
			pseudoColor = "&1";
		}
		String name = event.getPlayer().getName();
		if(name.equalsIgnoreCase("wormsor") || name.equalsIgnoreCase("XxDiablo31xX")
				|| name.equalsIgnoreCase("_scelete_") || name.equalsIgnoreCase("purfyde"))
		{
			result = "&1[Admin]";
			teams = "&rNSWX";
			pseudoColor = "&4";
		}*/
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