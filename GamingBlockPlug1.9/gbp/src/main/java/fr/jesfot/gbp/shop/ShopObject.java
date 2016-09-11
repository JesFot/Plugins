package fr.jesfot.gbp.shop;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.jesfot.gbp.economy.Money;
import fr.jesfot.gbp.utils.Utils;

public class ShopObject
{
	private OfflinePlayer owner;
	private Double price;
	private Integer amount;
	private Integer timesUsed;
	private Location location;
	private Location signLocation;
	
	private ItemStack SellItem;
	
	public ShopObject(OfflinePlayer player, final double prce, final int amt, final int amtUsed, Location loc, Location Sloc, ItemStack item)
	{
		this.owner = player;
		this.price = Double.valueOf(prce);
		this.timesUsed = Integer.valueOf(amtUsed);
		this.amount = Integer.valueOf(amt);
		this.signLocation = Sloc;
		this.location = loc;
		
		this.SellItem = item;
		
		this.updateSign();
	}
	
	public Location getSignLocation()
	{
		return this.signLocation;
	}
	
	public Location getLocation()
	{
		return this.location;
	}
	
	public Inventory getInventory()
	{
		return ((Chest)this.location.getBlock().getState()).getInventory();
	}
	
	public ItemStack getItem()
	{
		return this.SellItem;
	}
	
	public OfflinePlayer getOwner()
	{
		return this.owner;
	}
	
	public double getPrice()
	{
		return this.price.doubleValue();
	}
	
	public int getAmount()
	{
		return this.amount.intValue();
	}
	
	public int getTimesUsed()
	{
		return this.timesUsed.intValue();
	}
	
	public void addUse()
	{
		this.timesUsed = Integer.valueOf(this.timesUsed.intValue() + 1);
	}
	
	public void setOwner(OfflinePlayer player)
	{
		this.owner = player;
		this.updateSign();
	}
	
	public void setPrice(double prx)
	{
		this.price = Double.valueOf(prx);
		this.updateSign();
	}
	
	public boolean canAcceptAnotherTransaction()
	{
		int validItemsShopHas = Utils.getAmountOf(this.getInventory(), this.getItem());
		if(validItemsShopHas < this.getAmount())
		{
			return false;
		}
		return true;
	}
	
	public void setAmount(int amt)
	{
		this.amount = Integer.valueOf(amt);
		this.updateSign();
	}
	
	public void updateSign()
	{
		Sign signBlock = (Sign)this.signLocation.getBlock().getState();
		
		signBlock.setLine(0, ChatColor.BOLD + "[shop]");
		signBlock.setLine(1, "Selling: " + ChatColor.BOLD + this.amount);
		signBlock.setLine(2, ChatColor.GREEN + "" + this.price + Money.getSym());
		signBlock.setLine(3, this.SellItem.getItemMeta().getDisplayName());
		
		signBlock.update(true);
	}
	
	public void delete()
	{
		Sign signBlock = (Sign)this.signLocation.getBlock().getState();
		
		signBlock.setLine(0, "");
		signBlock.setLine(1, "");
		signBlock.setLine(2, "");
		signBlock.setLine(3, "");
		
		signBlock.update(true);
	}
}