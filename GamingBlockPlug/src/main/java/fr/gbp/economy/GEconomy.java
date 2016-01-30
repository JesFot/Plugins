package fr.gbp.economy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import fr.gbp.GamingBlockPlug;

public class GEconomy
{
	private GamingBlockPlug gbp;
	protected Map<OfflinePlayer, PEconomy> eco;
	
	public GEconomy(GamingBlockPlug p_gbp)
	{
		this.eco = new HashMap<OfflinePlayer, PEconomy>();
		this.gbp = p_gbp;
	}
	
	public List<String> getList()
	{
		List<String> result = new ArrayList<String>();
		for(Entry<OfflinePlayer, PEconomy> e : this.eco.entrySet())
		{
			result.add(e.getKey().getName());
		}
		for(String str : this.gbp.getConfig().getCustomConfig().getConfigurationSection("banksys.inventories")
				.getKeys(false))
		{
			if(!result.contains(str))
			{
				result.add(str);
			}
		}
		return result;
	}
	
	public void addPlayer(OfflinePlayer target)
	{
		this.eco.put(target, new PEconomy(this.gbp, target));
	}
	
	public PEconomy getPEco(OfflinePlayer target)
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
			this.getPEco(target).add(amount);
			return true;
		}
		if(target == null)
		{
			this.getPEco(source).remove(amount);
			return true;
		}
		if(this.getPEco(source).getBalance() < amount)
		{
			return false;
		}
		this.getPEco(source).remove(amount);
		this.getPEco(target).add(amount);
		return true;
	}
	
	public boolean playerExists(OfflinePlayer target)
	{
		return this.eco.containsKey(target);
	}
	
	public boolean resetPlayer(Player player)
	{
		if(this.playerExists(player))
		{
			this.eco.get(player).resetMoney();
			return true;
		}
		this.addPlayer(player);
		return false;
	}
	
	public boolean removePlayer(Player player)
	{
		if(this.playerExists(player))
		{
			this.eco.get(player);
			this.eco.remove(player);
			return true;
		}
		return false;
	}

	public void salary(String type, Player player)
	{
		double sal = this.gbp.getMoney().getSalary(type);
		if(this.playerExists(player))
		{
			this.eco.get(player).add(sal);
		}
	}
}