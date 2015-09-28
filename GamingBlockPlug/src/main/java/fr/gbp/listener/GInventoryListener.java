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
import fr.gbp.utils.CommunSys;
import fr.gbp.utils.ItemInventory;

public class GInventoryListener implements Listener
{
	private GamingBlockPlug gbp;
	private CommunSys cs;
	
	public GInventoryListener(GamingBlockPlug p_gbp)
	{
		this.gbp = p_gbp;
		this.cs = new CommunSys();
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
		ItemStack item2 = event.getCursor();
		HumanEntity human = event.getWhoClicked();
		Player player = (Player)human;
		if(item==null || item2==null)
		{
			return;
		}
		if(source.getName().equalsIgnoreCase(this.gbp.getEconomy().getPEco(player).getMenu().getName()))
		{
			if(item.getType().equals(Material.EMERALD))
			{
				int slot = event.getSlot();
				if(slot <= this.gbp.getEconomy().getPEco(player).getMenu().getSize())
				{
					event.setCancelled(true);
					ItemInventory.openPlayerInv(player, this.gbp.getEconomy().getPEco(player).getInventory());
				}
			}
			else if(item.getType().equals(Material.APPLE))
			{
				int slot = event.getSlot();
				if(slot <= this.gbp.getEconomy().getPEco(player).getMenu().getSize())
				{
					event.setCancelled(true);
					this.cs.setCInv(this.gbp.getConfig().getInventory("gbp.commun.inv"));
					ItemInventory.openPlayerInv(player, this.cs.getCInv());
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
		if(inventoryName.toLowerCase().contains("bank_"))
		{
			this.gbp.getConfig().storeInventory("banksys.inventories."+player.getName().toLowerCase(), inventory);
		}
		else if(inventoryName.toLowerCase().contains("commun"))
		{
			this.gbp.getConfig().storeInventory("gbp.commun.inv", inventory);
		}
	}
}