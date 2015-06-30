package fr.mpp.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
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

import fr.mpp.mpp.MSecurityWallSys;
import fr.mpp.MetalPonyPlug;
import fr.mpp.mpp.ClassesUtils;
import fr.mpp.mpp.RankLevel;
import fr.mpp.mpp.Classes;

public class MBlockListener implements Listener
{
	private MSecurityWallSys sws;
	private MetalPonyPlug mpp;
	
	public MBlockListener(MetalPonyPlug mppl)
	{
		this.mpp = mppl;
		this.sws = new MSecurityWallSys(mppl);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPlace(final BlockPlaceEvent event)
	{
		if(event.getBlockPlaced().getState() instanceof Sign)
		{
			Sign s = (Sign)event.getBlockPlaced().getState();
			if(s.getLine(1).equalsIgnoreCase("[Security_wall]"))
			{
				Player p = event.getPlayer();
				ClassesUtils cu = new ClassesUtils(this.mpp.getConfig());
				Classes c = cu.getRank(p, RankLevel.STATUT);
				if(!(c == Classes.Maire || c == Classes.Prince || c == Classes.Princess))
				{
					event.setCancelled(true);
					p.sendMessage("[Security_wall] You donnot have the right access to make a wall.");
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(final BlockBreakEvent event)
	{
		this.sws.setBaseLocation(event.getBlock().getLocation());
		this.sws.updateBlocks();
		if(this.sws.isWall())
		{
			event.setCancelled(true);
		}
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
