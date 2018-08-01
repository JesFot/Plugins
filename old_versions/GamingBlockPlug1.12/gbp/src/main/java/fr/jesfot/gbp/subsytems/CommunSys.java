package fr.jesfot.gbp.subsytems;

import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;

import fr.jesfot.gbp.configuration.NBTSubConfig;
import fr.jesfot.gbp.utils.InventorySerializer;
import fr.jesfot.gbp.utils.ItemInventory;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

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
	
	public NBTSubConfig storeInventory(NBTSubConfig conf)
	{
		NBTTagCompound compound = conf.readNBTFromFile().getCopy();
		compound = InventorySerializer.toNBT(this.inventaireCommun, compound, "CommunChestStore");
		conf.setCopy(compound).writeNBTToFile();
		return conf;
	}
	
	public void getStoredInventory(NBTSubConfig conf)
	{
		NBTTagCompound compound = conf.readNBTFromFile().getCopy();
		this.inventaireCommun = InventorySerializer.fromNBT(compound, "CommunChestStore");
		if (this.inventaireCommun == null)
		{
			this.inventaireCommun = ItemInventory.createInventory("Commun", 6);
		}
	}
}