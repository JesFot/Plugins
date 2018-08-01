package fr.jesfot.gbp.listener;

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

import fr.jesfot.gbp.GamingBlockPlug_1_12;
import fr.jesfot.gbp.configuration.NBTSubConfig;
import fr.jesfot.gbp.subsytems.CommunSys;
import fr.jesfot.gbp.utils.ItemInventory;

public class GInventoryListener implements Listener
{
	private GamingBlockPlug_1_12 gbp;
	private CommunSys cs;
	
	public GInventoryListener(GamingBlockPlug_1_12 p_gbp)
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
		if(source.getName().equalsIgnoreCase(this.gbp.getEconomy().getPEconomy(player).getMenu().getName()))
		{
			if(item.getType().equals(Material.EMERALD))
			{
				int slot = event.getSlot();
				if(slot <= this.gbp.getEconomy().getPEconomy(player).getMenu().getSize())
				{
					event.setCancelled(true);
					ItemInventory.openPlayerInv(player, this.gbp.getEconomy().getPEconomy(player)
							.getStoredInventory().getInventory());
				}
			}
			else if(item.getType().equals(Material.APPLE))
			{
				int slot = event.getSlot();
				if(slot <= this.gbp.getEconomy().getPEconomy(player).getMenu().getSize())
				{
					event.setCancelled(true);
					if(this.cs.getCInv().getViewers().isEmpty())
					{
						this.cs.getStoredInventory(new NBTSubConfig(this.gbp.getConfigFolder(null), "Commun"));
					}
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
		if(inventoryName.toLowerCase().contains("bank"))
		{
			this.gbp.getEconomy().getPEconomy(player).setInventory(inventory).storeInventory();
		}
		else if(inventoryName.toLowerCase().contains("commun"))
		{
			if(inventory.getViewers().size() == 1)
			{
				this.cs.setCInv(inventory);
				this.cs.storeInventory(new NBTSubConfig(this.gbp.getConfigFolder(null), "Commun"));
			}
		}
		this.gbp.getEconomy().getPEconomy(player).storeInventory();
	}
}