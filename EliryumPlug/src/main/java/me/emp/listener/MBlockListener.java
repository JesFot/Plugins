package me.emp.listener;

import me.emp.EliryumPlug;
//import me.emp.mpp.MSecurityWallSys;

//import org.bukkit.GameMode;
//import org.bukkit.Material;
//import org.bukkit.block.Sign;
//import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
//import org.bukkit.inventory.ItemStack;

public class MBlockListener implements Listener
{
	//private MSecurityWallSys sws;
	
	public MBlockListener(EliryumPlug empl)
	{
		//this.sws = new MSecurityWallSys(empl);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPlace(final BlockPlaceEvent event)
	{
		/*if(event.getBlockPlaced().getState() instanceof Sign)
		{
			Sign s = (Sign)event.getBlockPlaced().getState();
			if(s.getLine(1).equalsIgnoreCase("[Security_wall]"))
			{
				Player p = event.getPlayer();
				if(!p.isOp())
				{
					event.setCancelled(true);
					p.sendMessage("[Security_wall] You donnot have the right access to make a wall.");
				}
			}
		}*/
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onSignUpdate(final SignChangeEvent event)
	{
		/*if(event.getBlock().getState() instanceof Sign)
		{
			if(event.getLine(1).equalsIgnoreCase("[Security_wall]"))
			{
				Player p = event.getPlayer();
				if(!p.isOp())
				{
					event.setCancelled(true);
					p.sendMessage("[Security_wall] You donnot have the right access to make a wall.");
				}
			}
		}*/
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(final BlockBreakEvent event)
	{
		/*this.sws.setBaseLocation(event.getBlock().getLocation());
		this.sws.updateBlocks();
		if(this.sws.isWall() && !(event.getPlayer().getItemInHand().isSimilar(new ItemStack(Material.STICK)) && event.getPlayer().getGameMode().equals(GameMode.CREATIVE)))
		{
			event.setCancelled(true);
		}*/
	}

	@EventHandler
	public void onBlockBurn(final BlockBurnEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onBlockDispense(final BlockDispenseEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onBlockGrow(final BlockGrowEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onPistonExtend(final BlockPistonExtendEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onPistonRetract(final BlockPistonRetractEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onRedstoneChange(final BlockRedstoneEvent event)
	{
		// Code ...
	}
}
