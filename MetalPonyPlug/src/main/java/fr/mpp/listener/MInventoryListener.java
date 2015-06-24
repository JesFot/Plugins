package fr.mpp.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.Inventory;

import fr.mpp.MetalPonyPlug;

public class MInventoryListener implements Listener
{
	private MetalPonyPlug mpp;
	
	public MInventoryListener(MetalPonyPlug p_mpp)
	{
		this.mpp = p_mpp;
	}
	
	@EventHandler
	public void onCraft(CraftItemEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onExtractFurnace(FurnaceExtractEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onMoveItems(InventoryMoveItemEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onPickupItems(InventoryPickupItemEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onOpenInventory(InventoryOpenEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onClickInventory(InventoryClickEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onDragInventory(InventoryDragEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onCloseInventory(InventoryCloseEvent event)
	{
		//HumanEntity human = event.getPlayer();
		//Player player = (Player)human;
		Inventory inventory = event.getInventory();
		String inventoryName = inventory.getName();
		if(inventoryName.toLowerCase().contains("commun"))
		{
			this.mpp.getConfig().storeInventory("mpp.origchest.inv", inventory);
		}
	}
}