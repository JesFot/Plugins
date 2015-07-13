package fr.mpp.economy;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import fr.mpp.utils.ItemInventory;

public class MEcoMenu
{
	protected Inventory i1 = ItemInventory.createInventory("Bank Menu 1", 6);
	protected Inventory i2 = ItemInventory.createInventory("Bank Menu 2", 6);
	protected Inventory i3 = ItemInventory.createInventory("Bank Menu 3", 6);
	protected Inventory i4 = ItemInventory.createInventory("Bank Menu 4", 6);
	protected Inventory i5 = ItemInventory.createInventory("Bank Menu 5", 6);
	
	public static Inventory getInvCode()
	{
		Inventory i = ItemInventory.createItemandInv(Material.WOOD_DOOR, 53, "Quitter",
				"Cliquez ici\npour quitter ce menu", "Do your Code here", 6);
		ItemInventory.createIteminInv(Material.TORCH, i, 0, "Bank", "", 42);
		ItemInventory.createIteminInv(Material.TORCH, i, 8, "Bank", "", 42);
		ItemInventory.createIteminInv(Material.GOLD_INGOT, i, 17, "Back", "Erase code");
		ItemInventory.createIteminInv(Material.IRON_INGOT, i, 21, "1", "");
		ItemInventory.createIteminInv(Material.IRON_INGOT, i, 22, "2", "", 2);
		ItemInventory.createIteminInv(Material.IRON_INGOT, i, 23, "3", "", 3);
		ItemInventory.createIteminInv(Material.REDSTONE_BLOCK, i, 28, "Cancel", "Cancel and clear");
		ItemInventory.createIteminInv(Material.IRON_INGOT, i, 30, "4", "", 4);
		ItemInventory.createIteminInv(Material.IRON_INGOT, i, 31, "5", "", 5);
		ItemInventory.createIteminInv(Material.IRON_INGOT, i, 32, "6", "", 6);
		ItemInventory.createIteminInv(Material.EMERALD_BLOCK, i, 34, "Validate", "Validate Code and clear");
		ItemInventory.createIteminInv(Material.IRON_INGOT, i, 39, "7", "", 7);
		ItemInventory.createIteminInv(Material.IRON_INGOT, i, 40, "8", "", 8);
		ItemInventory.createIteminInv(Material.IRON_INGOT, i, 41, "9", "", 9);
		return i;
	}
	
	public class ClickOnItem implements Listener
	{
		public MEcoMenu parent;
		
		public ClickOnItem(MEcoMenu p_mem)
		{
			this.parent = p_mem;
		}
		
		@EventHandler
		public void onPlayerClick(InventoryClickEvent event)
		{
			if(event.getInventory().equals(parent.i1))
			{
				event.setCancelled(true);
			}
		}
	}
	
	public MEcoMenu()
	{
		ItemInventory.createIteminInv(Material.NETHER_STAR, this.i1, 0, "Aide", "Cliquez ici\npour voir l'aide");
		ItemInventory.createIteminInv(Material.EMERALD, this.i1, 11, "Retirer", "Cliquez ici\npour retirer\nvotre argent");
		ItemInventory.createIteminInv(Material.CHEST, this.i1, 15, "Déposer", "Cliquez ici\npour déposer de l'argent");
		ItemInventory.createIteminInv(Material.GOLD_INGOT, this.i1, 31, "Visualiser", "Cliquez ici\npour visualiser votre compte");
		ItemInventory.createIteminInv(Material.WOOD_DOOR, this.i1, 53, "Quitter", "Cliquez ici\npour quitter ce menu");
		ItemInventory.createIteminInv(Material.WOOD_DOOR, this.i2, 53, "Quitter", "Cliquez ici\npour quitter ce menu");
		ItemInventory.createIteminInv(Material.WOOD_DOOR, this.i3, 53, "Quitter", "Cliquez ici\npour quitter ce menu");
		ItemInventory.createIteminInv(Material.WOOD_DOOR, this.i4, 53, "Quitter", "Cliquez ici\npour quitter ce menu");
		ItemInventory.createIteminInv(Material.WOOD_DOOR, this.i5, 53, "Quitter", "Cliquez ici\npour quitter ce menu");
	}
}