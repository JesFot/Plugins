package me.jesfot.gamingblockplug.listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.MerchantInventory;

public class GInventoryListener implements Listener
{
	@EventHandler
	public void onInventoryOpen(final InventoryOpenEvent event)
	{
		if (event.getInventory().getType() == InventoryType.MERCHANT)
		{
			Player player = (Player) event.getPlayer();
			MerchantInventory inventory = (MerchantInventory) event.getInventory();
			Villager vil = (Villager) inventory.getMerchant();
		}
	}
	
	@EventHandler
	public void onInventoryClose(final InventoryCloseEvent event)
	{
		//
	}
}
