package fr.mpp.economy;

import org.bukkit.entity.Player;

import fr.mpp.MetalPonyPlug;
import fr.mpp.config.MConfig;

public class MPEconomy
{
	private MetalPonyPlug mpp;
	private Player player;
	private MConfig config;
	
	public MPEconomy(MetalPonyPlug p_mpp, Player p_player)
	{
		this.mpp = p_mpp;
		this.player = p_player;
		this.config = this.mpp.getConfig();
	}
	
	public Double getMoney()
	{
		this.config.reloadCustomConfig();
		return this.config.getCustomConfig().getDouble("eco.money."+player.getName().toLowerCase(), 0);
	}
	
	public void setMoney(Double p_money)
	{
		this.config.getCustomConfig().set("eco.money."+player.getName().toLowerCase(), p_money);
		this.config.saveCustomConfig();
	}
	
	public void resetMoney()
	{
		this.config.reloadCustomConfig();
		Double baseM = this.config.getCustomConfig().getDouble("eco.basemoney", 100);
		this.config.getCustomConfig().set("eco.money."+player.getName().toLowerCase(), baseM);
		this.config.saveCustomConfig();
	}
	
	public void removeMoney()
	{
		this.config.getCustomConfig().set("eco.money."+player.getName().toLowerCase(), null);
	}
}