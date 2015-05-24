package fr.gbp.economy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.gbp.GamingBlocksPlug;
import fr.gbp.config.MConfig;
import fr.gbp.utils.ItemInventory;

public class PEconomy
{
	private GamingBlocksPlug gbp;
	private Inventory inv;
	private Player player;
	private String pnamelc;
	private double money;
	private MConfig config;
	
	public PEconomy(GamingBlocksPlug p_gbp, Player p_player)
	{
		this.gbp = p_gbp;
		this.player = p_player;
		this.inv = ItemInventory.createItemandInv(Material.EMERALD, 4, "Money", "", "Your Bank", 5);
		this.pnamelc = this.player.getName().toLowerCase();
		this.money = this.gbp.getMoney().getBasics();
		this.config = this.gbp.getConfig();
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