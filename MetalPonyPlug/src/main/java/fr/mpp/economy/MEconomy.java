package fr.mpp.economy;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import fr.mpp.MetalPonyPlug;

public class MEconomy
{
	protected Map<OfflinePlayer, MPEconomy> eco;
	private MetalPonyPlug mpp;
	protected String ecoSym = "$";
	
	public String getSym()
	{
		return this.ecoSym.toString();
	}
	
	public MEconomy(MetalPonyPlug p_mpp)
	{
		this.eco = new HashMap<OfflinePlayer, MPEconomy>();
		this.mpp = p_mpp;
	}
	
	public void addPlayer(OfflinePlayer target)
	{
		if(this.playerExists(target))
		{
			return;
		}
		this.eco.put(target, new MPEconomy(this.mpp, target));
	}
	
	public MPEconomy getEco(OfflinePlayer target)
	{
		if(target == null)
		{
			return null;
		}
		if(!this.playerExists(target))
		{
			this.addPlayer(target);
		}
		return this.eco.get(target);
	}
	
	public boolean pay(Player source, OfflinePlayer target, double amount)
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
			this.getEco(target).add(amount);
			return true;
		}
		if(target == null)
		{
			this.getEco(source).remove(amount);
			return true;
		}
		if(this.getEco(source).getMoney() < amount)
		{
			return false;
		}
		this.getEco(source).remove(amount);
		this.getEco(target).add(amount);
		return true;
	}
	
	public void removeMoneyEM(Player player, int amount)
	{
		this.getEco(player).remove(this.mpp.getMoney().toMoney(amount));
	}
	
	public boolean playerExists(OfflinePlayer target)
	{
		return this.eco.containsKey(target);
	}
	
	public boolean removePlayer(Player p_player)
	{
		if(playerExists(p_player))
		{
			this.eco.get(p_player).removeMoney();
			this.eco.remove(p_player);
			return true;
		}
		return false;
	}
	
	public MEconomy salary(String type, Player target)
	{
		double sal = this.mpp.getMoney().getSalary(type);
		if(this.playerExists(target))
		{
			this.getEco(target).add(sal);
		}
		return this;
	}
}