package fr.mpp.economy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.mpp.MetalPonyPlug;
import fr.mpp.config.MConfig;
import fr.mpp.utils.ItemInventory;
import fr.mpp.utils.MPlayer;

public class MMemory
{
	private MConfig config;
	protected Inventory inv;
	protected List<Integer> ints = new ArrayList<Integer>();
	protected boolean noMore = false;
	protected boolean modeCentered = false;
	
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
		if(!this.modeCentered)
		{
			this.enterCodeDigit(digit);
			return;
		}
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
	
	public void enterCodeDigit(int digit)
	{
		if(this.modeCentered)
		{
			this.enterCodedigit(digit);
			return;
		}
		if(this.noMore)
		{
			return;
		}
		ints.add(digit);
		switch(this.ints.size())
		{
		case 1:
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 1, "1: "+digit, "", digit);
			break;
		case 2:
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 1, "1: "+this.ints.get(0), "", ints.get(0));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 2, "2: "+digit, "", digit);
			break;
		case 3:
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 1, "1: "+this.ints.get(0), "", ints.get(0));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 2, "2: "+this.ints.get(1), "", ints.get(1));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 3, "3: "+digit, "", digit);
			break;
		case 4:
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 1, "1: "+this.ints.get(0), "", ints.get(0));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 2, "2: "+this.ints.get(1), "", ints.get(1));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 3, "3: "+this.ints.get(2), "", this.ints.get(2));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 4, "4: "+digit, "", digit);
			break;
		case 5:
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 1, "1: "+this.ints.get(0), "", ints.get(0));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 2, "2: "+this.ints.get(1), "", ints.get(1));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 3, "3: "+this.ints.get(2), "", this.ints.get(2));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 4, "4: "+this.ints.get(3), "", this.ints.get(3));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 5, "5: "+digit, "", digit);
			break;
		case 6:
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 1, "1: "+this.ints.get(0), "", ints.get(0));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 2, "2: "+this.ints.get(1), "", ints.get(1));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 3, "3: "+this.ints.get(2), "", this.ints.get(2));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 4, "4: "+this.ints.get(3), "", this.ints.get(3));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 5, "5: "+this.ints.get(4), "", this.ints.get(4));
			ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, 6, "6: "+digit, "", digit);
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
		if(!this.config.getCustomConfig().contains("eco.codes."+player.toLowerCase()))
		{
			this.config.getCustomConfig().createSection("eco.codes."+player.toLowerCase());
			this.config.getCustomConfig().set("eco.codes."+player.toLowerCase(), 123456);
		}
		String code = this.config.getCustomConfig().getString("eco.codes."+player.toLowerCase(), "00");
		if(code.length() == this.ints.size())
		{
			if(this.compare2List(this.ints, code))
			{
				MPlayer.getPlayerByName(player).sendMessage("Code Correct !");
				return;
			}
			MPlayer.getPlayerByName(player).sendMessage("Code Incorrect !");
			return;
		}
		if(code.length() > this.ints.size())
		{
			MPlayer.getPlayerByName(player).sendMessage("You didn't enter enought digits.");
		}
		else
		{
			MPlayer.getPlayerByName(player).sendMessage("You entered too digits !! ...");
		}
		this.cancel();
	}
	
	public void cancel()
	{
		this.ints.clear();
		this.ints = new ArrayList<Integer>();
		this.eraseAllScreenField();
		this.noMore = false;
	}
	
	public void back()
	{
		this.ints.remove(this.ints.size()-1);
		this.eraseScreenField(this.ints.size());
		this.noMore = false;
	}
	
	public void eraseScreenField(int index)
	{
		if(index < 0 || index >= 7)
		{
			return;
		}
		index++;
		ItemInventory.addItemtoInventory(new ItemStack(Material.AIR), this.inv, index);
		this.inv.clear(index);
	}
	public void eraseAllScreenField()
	{
		for(int i = 0; i <= 7; i++)
		{
			this.eraseScreenField(i);
		}
	}
	
	public void setSreen(int index, int digit)
	{
		String d = Integer.toString(digit);
		ItemInventory.createIteminInv(Material.REDSTONE_TORCH_ON, this.inv, index+1, d, "", digit);
	}
	
	public boolean compare2List(List<Integer> a, String b)
	{
		List<Integer> lst = new ArrayList<Integer>();
		Bukkit.broadcastMessage("Hy, \""+MPlayer.concateTable(b.split(""))+"\"");
		for(String ch : b.split(""))
		{
			lst.add(Integer.parseInt(ch));
		}
		for(int c = 0; c < a.size(); c++)
		{
			if(!a.get(c).equals(lst.get(c)))
			{
				return false;
			}
		}
		return true;
	}
}