package fr.mpp.mpp;

import org.bukkit.inventory.Inventory;

import fr.mpp.utils.ItemInventory;

public class ComunSys
{
	private Inventory inventaireCommun;
	
	public ComunSys()
	{
		this.inventaireCommun = ItemInventory.createInventory("Commun", 6);
	}
	
	public Inventory getCInv()
	{
		return this.inventaireCommun;
	}
	public void setCInv(Inventory inv)
	{
		this.inventaireCommun = inv;
	}
}