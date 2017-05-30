package fr.jesfot.gbp.listener;

import org.bukkit.event.EventHandler;
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

public class GBlockListener implements Listener
{
	@EventHandler
	public void onBlockPlace(final BlockPlaceEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onSignUpdate(final SignChangeEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onBlockBreak(final BlockBreakEvent event)
	{
		// Code ...
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