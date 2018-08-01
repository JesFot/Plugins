package fr.gbp.utils;

import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;

public class CommunSys
{
	private Inventory inventaireCommun;
	
	public CommunSys()
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
	
	public void saveInv(Chest dchest)
	{
		dchest.getInventory().clear();
		dchest.getInventory().setContents(this.getCInv().getContents());
	}
}