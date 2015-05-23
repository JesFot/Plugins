package fr.gbp.economy;

import org.bukkit.entity.Player;

import fr.gbp.GamingBlocksPlug;
import fr.gbp.config.MConfig;

public class PEconomy
{
	private GamingBlocksPlug gbp;
	private Player player;
	private String pnamelc;
	private double money;
	private MConfig config;
	
	public PEconomy(GamingBlocksPlug p_gbp, Player p_player)
	{
		this.gbp = p_gbp;
		this.player = p_player;
		this.pnamelc = this.player.getName().toLowerCase();
		this.money = this.gbp.getMoney().getBasics();
		this.config = this.gbp.getConfig();
	}
	
	public double getBalance()
	{
		return this.money;
	}
	
	public void storeMoney(Player player)
	{
		this.config.getCustomConfig().set("economy."+this.pnamelc+".balance", this.money);
		this.config.saveCustomConfig();
	}
	
	public double getStoredMoney(Player player)
	{
		this.config.reloadCustomConfig();
		this.money = this.config.getCustomConfig().getDouble("economy."+this.pnamelc+".balance");
		return this.money;
	}
	
	public void resetMoney()
	{
		double baseM = this.gbp.getMoney().getBasics();
		this.config.getCustomConfig().set("economy."+this.pnamelc+".balance", baseM);
		this.config.saveCustomConfig();
	}
	
	public void removeMoney()
	{
		this.config.getCustomConfig().set("economy."+this.pnamelc+".balance", null);
		this.config.saveCustomConfig();
	}
}