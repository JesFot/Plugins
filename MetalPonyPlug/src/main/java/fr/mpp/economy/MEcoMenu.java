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