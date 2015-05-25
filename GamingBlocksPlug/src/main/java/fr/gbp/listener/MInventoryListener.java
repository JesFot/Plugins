package fr.gbp.listener;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.Inventory;

import fr.gbp.GamingBlocksPlug;

public class MInventoryListener implements Listener
{
	GamingBlocksPlug gbp;
	
	public MInventoryListener(GamingBlocksPlug p_gbp)
	{
		this.gbp = p_gbp;
	}
	
	@EventHandler
	public void onCraft(CraftItemEvent event)
	{
		//
	}
	
	@EventHandler
	public void onExtractFurnace(FurnaceExtractEvent event)
	{
		//
	}
	
	@EventHandler
	public void onOpenInventory(InventoryOpenEvent event)
	{
		//
	}
	
	@EventHandler
	public void onMoveItems(InventoryMoveItemEvent event)
	{
		//
	}
	
	@EventHandler
	public void onPickupItems(InventoryPickupItemEvent event)
	{
		//
	}
	
	@EventHandler
	public void onCloseInventory(InventoryCloseEvent event)
	{
		HumanEntity human = event.getPlayer();
		Player player = (Player)human;
		Inventory inventory = event.getInventory();
		String inventoryName = inventory.getName();
		player.sendMessage("You close the "+inventoryName+" inventory.");
		if(inventoryName.toLowerCase().contains("bank"))
		{
			this.gbp.getConfig().storeInventory("banksys.inventories."+player.getName().toLowerCase(), inventory);
			player.sendMessage("Your bank has been saved");
		}
	}
}