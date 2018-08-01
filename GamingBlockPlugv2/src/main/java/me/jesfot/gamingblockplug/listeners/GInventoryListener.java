package me.jesfot.gamingblockplug.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;
import me.jesfot.gamingblockplug.security.CommonChestSystem;
import me.jesfot.gamingblockplug.utils.DataUtils;

public class GInventoryListener implements Listener
{
	private final GamingBlockPlug plugin;
	
	public GInventoryListener(GamingBlockPlug plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onInventoryOpen(final InventoryOpenEvent event)
	{
		if (event.getInventory() == this.plugin.getSystemManager().getSharedChestSystem().getSharedInventory())
		{
			if (event.getViewers().size() == 0 || (event.getViewers().size() == 1 && event.getViewers().contains(event.getPlayer())))
			{
				CommonChestSystem scs = this.plugin.getSystemManager().getSharedChestSystem();
				DataUtils.safeReload(this.plugin.getNBT(null));
				scs.loadFromConfig(this.plugin.getNBT("CommonChest"));
			}
		}
	}
	
	@EventHandler
	public void onInventoryClose(final InventoryCloseEvent event)
	{
		if (event.getViewers().size() < 2 && event.getInventory() == this.plugin.getSystemManager().getSharedChestSystem().getSharedInventory())
		{
			CommonChestSystem scs = this.plugin.getSystemManager().getSharedChestSystem();
			DataUtils.safeReload(this.plugin.getNBT(null));
			scs.saveToConfig(this.plugin.getNBT("CommonChest"));
			this.plugin.getNBT(null).save();
		}
	}
}
