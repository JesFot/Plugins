package me.emp.listener;

import me.emp.EliryumPlug;
import me.emp.mpp.MHalfBedSys;
//import me.emp.utils.ItemInventory;

import me.emp.perms.EPermissions;

import org.bukkit.ChatColor;
//import org.bukkit.CropState;
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.block.Block;
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
import org.bukkit.event.player.PlayerInteractEntityEvent;
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
	private EliryumPlug emp;
	private final MHalfBedSys mhbs;
	
	public MPlayerListener(EliryumPlug empl)
	{
		this.emp = empl;
		this.mhbs = new MHalfBedSys(empl);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLogin(PlayerLoginEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		/*if(event.getPlayer().getName().equals("JesFot"))
		{
			event.getPlayer().addAttachment(this.emp.getPlugin()).setPermission("Eliryum.switch", true);
		}*/
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

	//@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		Action rightClickAir = Action.RIGHT_CLICK_AIR;
		Action rightClickBlock = Action.RIGHT_CLICK_BLOCK;
		Action leftClickAir = Action.LEFT_CLICK_AIR;
		Action leftClickBlock = Action.LEFT_CLICK_BLOCK;
		if (action == rightClickAir || action == rightClickBlock)
		{
			if (action == rightClickAir)
			{
				// Code ...
			}
			else
			{
				/*if(ItemInventory.isEqual(event.getClickedBlock().getType(), new Material[]{Material.CROPS, Material.CARROT, Material.POTATO, Material.NETHER_WARTS}))
				{
					if(!event.hasItem())
					{
						//
					}
					else if(ItemInventory.isHoe(event.getItem().getType()))
					{
						Block block = event.getClickedBlock();
						Location loc = block.getLocation();
						boolean cr = block.getType()!=Material.NETHER_WARTS?CropState.getByData(block.getData()).equals(CropState.RIPE):
							CropState.getByData(block.getData()).equals(CropState.SMALL);
						
						if(cr)
						{
							Material m = block.getType();
							block.breakNaturally();
							loc.getWorld().getBlockAt(loc).setType(m);
						}
					}
				}*/
			}
		}
		else if (action == leftClickAir || action == leftClickBlock)
		{
			if (action == leftClickAir)
			{
				// Code ...
			}
			else
			{
				// Code ...
			}
		}
	}
	
	@EventHandler
	public void onPlayerClickEntity(final PlayerInteractEntityEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onPlayerChat(final AsyncPlayerChatEvent event)
	{
		String msg = "";
		if(event.getMessage().contains("${") && event.getMessage().contains("}"))
		{
			if(!EPermissions.testPermission(event.getPlayer(), "Eliryum.var.use", ChatColor.RED + "You are not allowed to use any variables. "
					+ "Sorry, please contact an administrator if you believe that is an error."))
			{
				return;
			}
			for(String arg : event.getMessage().split("\\$"))
			{
				if(arg.startsWith("{") && arg.contains("}"))
				{
					String var = arg.substring(1, arg.indexOf("}"));
					arg = this.emp.getMVO().getToString(var)+((arg.endsWith("}") && arg.indexOf("}")==arg.length()-1) ? "" : arg.substring(arg.indexOf("}")+1));
				}
				msg += arg + "";
			}
			event.setMessage(msg);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayergoBed(final PlayerBedEnterEvent event)
	{
		Player player = event.getPlayer();
		this.mhbs.updatePlayers();
		this.mhbs.addPlayerInBed(player);
		//player.getServer().broadcastMessage(this.mhbs.howManyInBedText());
		/*if (this.mhbs.hasHalfInBed())
		{
			this.mhbs.passNight(this.mhbs.getPlayersInBed());
		}*/
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLeaveBed(final PlayerBedLeaveEvent event)
	{
		Player player = event.getPlayer();
		this.mhbs.updatePlayers();
		this.mhbs.removePlayerInBed(player);
		//this.mhbs.endPassN();
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
