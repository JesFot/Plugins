package fr.mpp.economy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import fr.mpp.MetalPonyPlug;
import fr.mpp.config.MConfig;
import fr.mpp.utils.ItemInventory;
import fr.mpp.utils.MPlayer;

public class MMemory
{
	private MConfig config;
	protected Inventory inv;
	public List<Integer> ints = new ArrayList<Integer>();
	protected boolean noMore = false;
	
	public MMemory(MConfig config)
	{
		this.config = config;
		this.ints.clear();
	}
	
	public MMemory(MetalPonyPlug mpp)
	{
		this.config = mpp.getConfig();
		this.ints.clear();
	}
	
	public MMemory(MConfig config, Inventory onv)
	{
		this.config = config;
		this.inv = onv;
		this.ints.clear();
	}
	
	public MMemory(MetalPonyPlug mpp, Inventory onv)
	{
		this.config = mpp.getConfig();
		this.inv = onv;
		this.ints.clear();
	}
	
	public void setInventory(Inventory onv)
	{
		this.inv = onv;
	}
	
	public void enterCodedigit(int digit)
	{
		if(this.noMore)
		{
			return;
		}
		ints.add(digit);
		switch(this.ints.size())
		{
		case 1:
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 4, "1: "+digit, "", digit);
			break;
		case 2:
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 4, "1: "+this.ints.get(0), "", ints.get(0));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 5, "2: "+digit, "", digit);
			break;
		case 3:
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 3, "1: "+this.ints.get(0), "", ints.get(0));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 4, "2: "+this.ints.get(1), "", ints.get(1));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 5, "3: "+digit, "", digit);
			break;
		case 4:
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 3, "1: "+this.ints.get(0), "", ints.get(0));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 4, "2: "+this.ints.get(1), "", ints.get(1));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 5, "3: "+this.ints.get(2), "", this.ints.get(2));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 6, "4: "+digit, "", digit);
			break;
		case 5:
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 2, "1: "+this.ints.get(0), "", ints.get(0));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 3, "2: "+this.ints.get(1), "", ints.get(1));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 4, "3: "+this.ints.get(2), "", this.ints.get(2));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 5, "4: "+this.ints.get(3), "", this.ints.get(3));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 6, "5: "+digit, "", digit);
			break;
		case 6:
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 2, "1: "+this.ints.get(0), "", ints.get(0));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 3, "2: "+this.ints.get(1), "", ints.get(1));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 4, "3: "+this.ints.get(2), "", this.ints.get(2));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 5, "4: "+this.ints.get(3), "", this.ints.get(3));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 6, "5: "+this.ints.get(4), "", this.ints.get(4));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 7, "6: "+digit, "", digit);
			break;
		case 7:
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 1, "1: "+this.ints.get(0), "", ints.get(0));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 2, "2: "+this.ints.get(1), "", ints.get(1));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 3, "3: "+this.ints.get(2), "", this.ints.get(2));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 4, "4: "+this.ints.get(3), "", this.ints.get(3));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 5, "5: "+this.ints.get(4), "", this.ints.get(4));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 6, "6: "+this.ints.get(5), "", this.ints.get(5));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 7, "7: "+digit, "", digit);
			break;
		}
		if(this.ints.size()==7)
		{
			this.noMore = true;
		}
	}
	
	public void validate(String player)
	{
		List<Integer> code = this.config.getCustomConfig().getIntegerList("eco.codes."+player.toLowerCase());
		if(code.size() == this.ints.size())
		{
			if(code.equals(this.ints))
			{
				MPlayer.getPlayerByName(player).sendRawMessage("Code Correct !");
				return;
			}
			MPlayer.getPlayerByName("Code Incorrect !");
			return;
		}
		MPlayer.getPlayerByName(player).sendRawMessage("You didn't enter enought digits.");
		this.cancel();
	}
	
	public void cancel()
	{
		this.ints.clear();
		this.inv = MEcoMenu.getInvCode();
	}
	
	public void back()
	{
		this.ints.remove(this.ints.size()-1);
		int it = this.ints.get(this.ints.size()-1);
		this.ints.remove(this.ints.size()-1);
		this.enterCodedigit(it);
	}
}