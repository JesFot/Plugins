package fr.mpp.listener;

import org.bukkit.Material;
import org.bukkit.block.Chest;
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
import org.bukkit.inventory.InventoryHolder;

import fr.mpp.MetalPonyPlug;
import fr.mpp.economy.MMemory;

public class MInventoryListener implements Listener
{
	private MetalPonyPlug mpp;
	private MMemory memo;
	
	public MInventoryListener(MetalPonyPlug p_mpp)
	{
		this.mpp = p_mpp;
		memo = new MMemory(p_mpp);
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
		InventoryHolder holder = event.getInventory().getHolder();
		if(holder instanceof Chest)
		{
			Chest block = (Chest)holder;
			if(block.hasMetadata("Lock")/* && block.getMetadata("Lock").get(0).value() != ""*/)
			{
				this.mpp.broad("coucou");
				if(event.getPlayer().getInventory().getItemInHand().getItemMeta().getDisplayName().equals("MasterKey_w0hr654g6q5rg6f546er"))
				{
					event.getPlayer().openInventory(event.getInventory());
				}
			}
		}
	}
	
	@EventHandler
	public void onClickInventory(InventoryClickEvent event)
	{
		if(event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null ||
				event.getCurrentItem().getItemMeta().getDisplayName() == null)
		{
			return;
		}
		if(event.getInventory().contains(Material.TORCH, 42) &&
				event.getInventory().getItem(0).getItemMeta().getDisplayName().equalsIgnoreCase("Bank"))
		{
			this.memo.setInventory(event.getInventory());
			if(event.getCurrentItem().getAmount()<=1 && event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("1"))
			{
				this.memo.enterCodeDigit(1);
			}
			else if(event.getCurrentItem().getAmount() <= 9 && event.getCurrentItem().getAmount() > 1)
			{
				this.memo.enterCodeDigit(event.getCurrentItem().getAmount());
			}
			if(event.getCurrentItem().getAmount() == 1)
			{
				String name = event.getCurrentItem().getItemMeta().getDisplayName();
				if(name.equalsIgnoreCase("Cancel"))
				{
					this.memo.cancel();
				}
				else if(name.equalsIgnoreCase("Validate"))
				{
					this.memo.validate(event.getWhoClicked().getName());
				}
				else if(name.equalsIgnoreCase("Back"))
				{
					this.memo.back();
				}
				else if(name.equalsIgnoreCase("quitter"))
				{
					this.memo.cancel();
					event.getWhoClicked().closeInventory();
				}
			}
			event.setCancelled(true);
		}
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