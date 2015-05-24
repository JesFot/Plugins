package fr.mpp.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
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
import org.bukkit.inventory.ItemStack;

public class MBlockListener implements Listener
{
	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPlace(final BlockPlaceEvent event)
	{
		// Code ...
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(final BlockBreakEvent event)
	{
		if (event.getBlock().getType().equals(Material.EMERALD_ORE))
		{
			Block block = (Block)event.getBlock();
			event.setCancelled(true);
			block.setType(Material.AIR);
			ItemStack diam = new ItemStack(Material.DIAMOND, 4);
			block.getLocation().getWorld().dropItem(block.getLocation(), diam);
			event.setExpToDrop(10);
		}
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
