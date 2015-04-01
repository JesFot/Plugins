package fr.mpp.listener;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
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
import org.bukkit.inventory.ItemStack;

import fr.mpp.MetalPonyPlug;
import fr.mpp.mpp.Classes;
import fr.mpp.mpp.ClassesUtils;

public class MPlayerListener implements Listener
{
	private final MetalPonyPlug mpp;
	
	public MPlayerListener(MetalPonyPlug mppl)
	{
		this.mpp = mppl;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLogin(PlayerLoginEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		event.getPlayer().setDisplayName(ChatColor.BLUE + "[" + Classes.Assassin.getName() + "]<" + ChatColor.RESET + event.getPlayer().getName());
		if (mpp.getMeta(event.getPlayer(), "MPPRegistered").equals(true))
		{
			mpp.setMeta(event.getPlayer(), "MPPLogTimes", ((int)mpp.getMeta(event.getPlayer(), "MPPLogTimes")+1));
			if ((int)mpp.getMeta(event.getPlayer(), "MPPLogTimes") >= 50)
			{
				mpp.setMeta(event.getPlayer(), "MPPRankB", Classes.Regular);
				event.getPlayer().setCustomName("[" + Classes.Regular.getName() + "]" + event.getPlayer().getName());
				event.getPlayer().setDisplayName("[" + Classes.Regular.getName() + "]" + event.getPlayer().getName());
			}
		}
		else
		{
			mpp.setMeta(event.getPlayer(), "MPPRegistered", true);
			mpp.setMeta(event.getPlayer(), "MPPLogTimes", 1);
			event.getPlayer().setCustomName("[" + Classes.Noobie.getName() + "]" + event.getPlayer().getName());
			event.getPlayer().setDisplayName("[" + Classes.Noobie.getName() + "]" + event.getPlayer().getName());
			mpp.setMeta(event.getPlayer(), "MPPRankB", Classes.Noobie);
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
				//player.chat("Test !!!! B : "+block.getType().toString());
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
		Entity entity = event.getRightClicked();
		if (entity instanceof ItemFrame)
		{
			ItemFrame frame = (ItemFrame)entity;
			ItemStack stack = frame.getItem();
			Material mat = stack.getType();
			Location loc = frame.getLocation();
			if (ClassesUtils.isInZone(loc))
			{
				String name = stack.getItemMeta().getDisplayName();
				Classes cl = ClassesUtils.getClasseByName(name);
				if (mat == cl.getClasse().getItem())
				{
					ClassesUtils.addRank(name, event.getPlayer());
					event.getPlayer().sendMessage("You clicked right !!!");
					mpp.getLogger().log(Level.INFO, event.getPlayer().getName() + " clicked at the right place.");
					event.setCancelled(true);
				}
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
