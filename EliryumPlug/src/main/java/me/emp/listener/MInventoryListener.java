package me.emp.listener;

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

public class MInventoryListener implements Listener
{
	public MInventoryListener()
	{
		// Code ...
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
		// Code ...
	}
}