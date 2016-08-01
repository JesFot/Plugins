package fr.jesfot.gbp.listener;

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
import org.bukkit.event.block.SignChangeEvent;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.permission.PermissionsHelper;
import fr.jesfot.gbp.subsytems.SecurityWallSys;

public class GBlockListener implements Listener
{
	private GamingBlockPlug_1_9 gbp;
	private final SecurityWallSys sws;
	
	public GBlockListener(GamingBlockPlug_1_9 plugin)
	{
		this.sws = new SecurityWallSys(plugin);
		this.gbp = plugin;
	}
	
	@EventHandler
	public void onBlockPlace(final BlockPlaceEvent event)
	{
		if(event.getBlock().getState() instanceof Sign)
		{
			Sign s = (Sign)event.getBlock().getState();
			if(s.getLine(1).equalsIgnoreCase("[Security_wall]"))
			{
				Player p = event.getPlayer();
				if(!PermissionsHelper.testPermissionSilent(p, "GamingBlockPlug.secureWall.place", false))
				{
					event.setCancelled(true);
					p.sendMessage(this.gbp.getLang().get("securitywall.placedisallow", "[Security_Wall] "
							+ "You donnot have the right to make a wall."));
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onSignUpdate(final SignChangeEvent event)
	{
		if(event.getBlock().getState() instanceof Sign)
		{
			if(event.getLine(1).equalsIgnoreCase("[Security_wall]"))
			{
				Player p = event.getPlayer();
				if(!PermissionsHelper.testPermissionSilent(p, "GamingBlockPlug.secureWall.place", false))
				{
					event.setCancelled(true);
					p.sendMessage(this.gbp.getLang().get("securitywall.placedisallow", "[Security_Wall] "
							+ "You donnot have the right to make a wall."));
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(final BlockBreakEvent event)
	{
		this.sws.setBaseLocation(event.getBlock().getLocation());
		this.sws.updateBlocks();
		if(this.sws.isWall() && !PermissionsHelper.testPermissionSilent(event.getPlayer(),
				"GamingBlockPlug.secureWall.break", false))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(this.sws.message(event.getPlayer().getName()));
		}
		else if(this.sws.isWall())
		{
			event.getPlayer().sendMessage(this.sws.allowMessage(event.getPlayer().getName()));
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