package fr.gbp.listener;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
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
import org.bukkit.inventory.ItemStack;

import fr.gbp.GamingBlockPlug;
import fr.gbp.utils.ItemInventory;

public class GInventoryListener implements Listener
{
	GamingBlockPlug gbp;
	
	public GInventoryListener(GamingBlockPlug p_gbp)
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
	public void onOpenInventory(InventoryOpenEvent event)
	{
		//
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event)
	{
		Inventory source = event.getInventory();;
		ItemStack item = event.getCurrentItem();
		//String sItem = item!=null&&item.getType()!=null ? item.getType().toString() : "none";
		ItemStack item2 = event.getCursor();
		//String sItem2 = item2!=null&&item2.getType()!=null ? item2.getType().toString() : "none";
		HumanEntity human = event.getWhoClicked();
		Player player = (Player)human;
		if(item==null || item2==null)
		{
			return;
		}
		if(source.getName().equalsIgnoreCase(this.gbp.getEconomy().getPEco(player).getInventory().getName()))
		{
			if(item.getType().equals(Material.EMERALD))
			{
				if(item.getItemMeta().getDisplayName().equalsIgnoreCase("Money"))
				{
					int slot = event.getSlot();
					if(slot <= this.gbp.getEconomy().getPEco(player).getInventory().getSize())
					{
						if(item2.getType().equals(Material.AIR))
						{
							if(this.gbp.getEconomy().getPEco(player).getBalance() >= 10.0)
							{
								this.gbp.getEconomy().getPEco(player).remove(10.0);
								ItemStack i = ItemInventory.createItem(Material.EMERALD, "Money", null);
								i.setAmount(1);
								player.setItemOnCursor(i);
								event.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onDrag(InventoryDragEvent event)
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
		if(inventoryName.toLowerCase().contains("bank"))
		{
			this.gbp.getConfig().storeInventory("banksys.inventories."+player.getName().toLowerCase(), inventory);
			//player.sendMessage("Your bank has been saved");
		}
	}
}