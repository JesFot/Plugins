package fr.mpp.economy;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import fr.mpp.MetalPonyPlug;
import fr.mpp.config.MConfig;

public class MPEconomy
{
	private MetalPonyPlug mpp;
	private OfflinePlayer player;
	private String playerNameLc;
	private MConfig config;
	private double money;
	
	public MPEconomy(MetalPonyPlug p_mpp, OfflinePlayer p_player)
	{
		this.mpp = p_mpp;
		this.player = p_player;
		this.playerNameLc = this.player.getName().toLowerCase();
		this.money = this.mpp.getMoney().getBase();
		this.config = this.mpp.getConfig();
		this.money = 100;
	}
	
	public OfflinePlayer getPlayer()
	{
		return this.player;
	}
	
	public Player getOnlinePlayer()
	{
		if(this.player.isOnline())
		{
			return (Player)this.player;
		}
		return null;
	}
	
	public Double getMoney()
	{
		this.getStoredMoney();
		return this.money;
	}
	
	public void setMoney(Double p_money)
	{
		this.money = p_money;
		this.storeMoney();
	}
	
	public void add(double amount)
	{
		this.money += amount;
		this.storeMoney();
	}
	
	public void remove(double amount)
	{
		this.money -= amount;
		this.storeMoney();
	}
	
	public void resetMoney()
	{
		Double baseM = this.mpp.getMoney().getBase();
		this.money = baseM;
		this.storeMoney();
	}
	
	public void removeMoney()
	{
		this.config.getCustomConfig().set("economy."+this.playerNameLc+".money", null);
		this.config.saveCustomConfig();
	}
	
	public void storeMoney()
	{
		this.config.getCustomConfig().set("economy."+this.playerNameLc+".money", this.money);
		this.config.saveCustomConfig();
	}
	
	public void getStoredMoney()
	{
		this.config.reloadCustomConfig();
		this.money = this.config.getCustomConfig().getDouble("economy."+this.playerNameLc+".money");
	}
}