package fr.gbp.economy;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;

import fr.gbp.GamingBlockPlug;
import fr.gbp.config.GConfig;
import fr.gbp.utils.ItemInventory;

public class PEconomy
{
	private GamingBlockPlug gbp;
	private Inventory menu;
	private Inventory inv;
	private OfflinePlayer player;
	private String pnamelc;
	private double money;
	private GConfig config;
	
	public PEconomy(GamingBlockPlug p_gbp, OfflinePlayer target)
	{
		this.gbp = p_gbp;
		this.player = target;
		this.menu = ItemInventory.createItemandInv(Material.EMERALD, 0, "Bank", "", "Bank Menu", 2);
		ItemInventory.createIteminInv(Material.APPLE, this.menu, 9, "Commun Chest", "");
		if(this.gbp.getConfig().getCustomConfig().contains("banksys.inventories."+this.player.getName().toLowerCase()))
		{
			this.inv = this.gbp.getConfig().getInventory("banksys.inventories."+this.player.getName().toLowerCase());
		}
		else
		{
			this.inv = ItemInventory.createItemandInv(Material.EMERALD, 4, "Money", "", "Your Bank", 5);
		}
		this.pnamelc = this.player.getName().toLowerCase();
		this.money = this.gbp.getMoney().getBasics();
		this.config = this.gbp.getConfig();
		if(this.gbp.getConfig().getCustomConfig().contains("economy."+this.pnamelc+".balance"))
		{
			this.getStoredMoney();
		}
		this.storeMoney();
	}
	
	public double getBalance()
	{
		this.getStoredMoney();
		return this.money;
	}
	
	public void setBalance(double p_money)
	{
		this.money = p_money;
		this.storeMoney();
	}
	
	public void add(double p_money)
	{
		this.money += p_money;
		this.storeMoney();
	}
	
	public void remove(double p_money)
	{
		this.money -= p_money;
		this.storeMoney();
	}
	
	public void storeMoney()
	{
		this.config.getCustomConfig().set("economy."+this.pnamelc+".balance", this.money);
		this.config.saveCustomConfig();
	}
	
	public void getStoredMoney()
	{
		this.config.reloadCustomConfig();
		this.money = this.config.getCustomConfig().getDouble("economy."+this.pnamelc+".balance");
	}
	
	public Inventory getInventory()
	{
		return this.inv;
	}
	
	public Inventory getMenu()
	{
		return this.menu;
	}
	
	public void resetMoney()
	{
		double baseM = this.gbp.getMoney().getBasics();
		this.money = baseM;
		this.config.getCustomConfig().set("economy."+this.pnamelc+".balance", baseM);
		this.config.saveCustomConfig();
	}
	
	public void removeMoney()
	{
		this.config.getCustomConfig().set("economy."+this.pnamelc+".balance", null);
		this.config.saveCustomConfig();
	}
}