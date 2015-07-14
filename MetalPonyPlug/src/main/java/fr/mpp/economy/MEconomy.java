package fr.mpp.economy;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import fr.mpp.MetalPonyPlug;

public class MEconomy
{
	protected Map<Player, MPEconomy> eco;
	private MetalPonyPlug mpp;
	protected String ecoSym = "$";
	
	public String getSym()
	{
		return this.ecoSym.toString();
	}
	
	public MEconomy(MetalPonyPlug p_mpp)
	{
		this.eco = new HashMap<Player, MPEconomy>();
		this.mpp = p_mpp;
	}
	
	public void addPlayer(Player p_player)
	{
		if(this.playerExists(p_player))
		{
			return;
		}
		this.eco.put(p_player, new MPEconomy(this.mpp, p_player));
	}
	
	public MPEconomy getEco(Player p_player)
	{
		if(p_player == null)
		{
			return null;
		}
		if(!playerExists(p_player))
		{
			addPlayer(p_player);
		}
		return this.eco.get(p_player);
	}
	
	public boolean pay(Player source, Player target, double amount)
	{
		if(source == target)
		{
			return false;
		}
		if((Double)amount == null || amount <= 0.1)
		{
			return false;
		}
		if(source == null)
		{
			this.getEco(target).setMoney(this.getEco(target).getMoney()+amount);
			return true;
		}
		if(target == null)
		{
			this.getEco(source).setMoney(this.getEco(source).getMoney()-amount);
			return true;
		}
		if(this.getEco(source).getMoney() < amount)
		{
			return false;
		}
		this.getEco(source).setMoney(this.getEco(source).getMoney()-amount);
		this.getEco(target).setMoney(this.getEco(target).getMoney()+amount);
		return true;
	}
	
	public int toEM(double amount)
	{
		return (int)(amount/10);
	}
	
	public double toMY(int emerald)
	{
		return emerald*10;
	}
	
	public void removeMoney(Player player, double amount)
	{
		this.getEco(player).setMoney(this.getEco(player).getMoney()-amount);
	}
	
	public void removeMoneyEM(Player player, int amount)
	{
		this.getEco(player).setMoney(this.getEco(player).getMoney()-(this.toMY(amount)));
	}
	
	public boolean playerExists(Player p_player)
	{
		return this.eco.containsKey(p_player);
	}
	
	public void removePlayer(Player p_player)
	{
		if(playerExists(p_player))
		{
			this.eco.get(p_player).removeMoney();
			this.eco.remove(p_player);
		}
	}
}