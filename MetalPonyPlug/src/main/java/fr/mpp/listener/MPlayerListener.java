package fr.mpp.listener;

import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
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
		if (event.getPlayer().hasMetadata("MPPRegistered"))
		{
			// Code ...
		}
		else
		{
			mpp.setMeta(event.getPlayer(), "MPPRegistered", true);
			event.getPlayer().setDisplayName("[" + Classes.Noobie.getName() + "]" + event.getPlayer().getDisplayName());
			mpp.setMeta(event.getPlayer(), "MPPRank", "Noobie");
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
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		ItemStack frame = event.getItem();
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
			double x = 395, y = 79, z = -27;
			if (mat == Material.DIAMOND_PICKAXE)
			{
				if (frame.getLocation().equals(new Location(frame.getWorld(), x, y, z)))
				{
					mpp.getLogger().log(Level.INFO, event.getPlayer().getName() + " clicked at the right place.");
					event.getPlayer().sendPluginMessage(mpp.getPlugin(), "You clicked right !!!", "Mwahaha MSG".getBytes());
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
