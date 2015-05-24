package fr.gbp.economy;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import fr.gbp.GamingBlocksPlug;

public class MEconomy
{
	private GamingBlocksPlug gbp;
	protected Map<Player, PEconomy> eco;
	
	public MEconomy(GamingBlocksPlug p_gbp)
	{
		this.eco = new HashMap<Player, PEconomy>();
		this.gbp = p_gbp;
	}
	
	public void addPlayer(Player player)
	{
		this.eco.put(player, new PEconomy(this.gbp, player));
	}
	
	public PEconomy getPEco(Player player)
	{
		if(player == null)
		{
			return null;
		}
		if(!this.playerExists(player))
		{
			this.addPlayer(player);
		}
		return this.eco.get(player);
	}
	
	public boolean playerExists(Player player)
	{
		return this.eco.containsKey(player);
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
		this.eco.get(player).add(sal);
	}
}