package fr.mpp.economy;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import fr.mpp.MetalPonyPlug;

public class MEconomy
{
	protected Map<Player, MPEconomy> eco;
	private MetalPonyPlug mpp;
	
	public MEconomy(MetalPonyPlug p_mpp)
	{
		this.eco = new HashMap<Player, MPEconomy>();
		this.mpp = p_mpp;
	}
	
	public void addPlayer(Player p_player)
	{
		this.eco.put(p_player, new MPEconomy(this.mpp, p_player));
	}
	
	public MPEconomy getEco(Player p_player)
	{
		return this.eco.get(p_player);
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