package me.jesfot.gamingblockplug.listeners;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

import me.jesfot.gamingblockplug.permission.PermissionHelper;
import me.jesfot.gamingblockplug.permission.StaticPerms;
import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;
import me.jesfot.gamingblockplug.security.WallSystem;
import me.jesfot.gamingblockplug.security.WallSystem.Column;

public class GBlockListener implements Listener
{
	private final GamingBlockPlug plugin;
	
	public GBlockListener(GamingBlockPlug plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onBlockPlaced(final BlockPlaceEvent event)
	{
		if (event.getBlock().getState() instanceof Sign)
		{
			for (String line : ((Sign) event.getBlock().getState()).getLines())
			{
				if (WallSystem.WALL_TEXT.equalsIgnoreCase(line))
				{
					if (!this.plugin.getSystemManager().getWallSystem().canPlace(event.getPlayer(), event.getBlock()))
					{
						event.getPlayer().sendMessage("[Security_Wall] Info: You donnot have the right to make a wall.");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onSignUpdate(final SignChangeEvent event)
	{
		if (event.getBlock().getState() instanceof Sign)
		{
			for (String line : event.getLines())
			{
				if (WallSystem.WALL_TEXT.equalsIgnoreCase(line))
				{
					if (!this.plugin.getSystemManager().getWallSystem().canPlace(event.getPlayer(), event.getBlock()))
					{
						event.getPlayer().sendMessage("[Security_Wall] Info: You donnot have the right to make a wall.");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(final BlockBreakEvent event)
	{
		Column column = this.plugin.getSystemManager().getWallSystem().getColumn(event.getBlock());
		if (!PermissionHelper.testPermissionSilent(event.getPlayer(), StaticPerms.WALLS_BREAK, false) && column.isWall())
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.DARK_RED + "[Security_Wall] Info: You cannot break this block (part of a wall)");
		}
		else if (column.isWall())
		{
			event.getPlayer().sendMessage(ChatColor.GOLD + "[Security_Wall] Warning: You broke a wall block");
		}
	}
}
