package fr.jesfot.gbp.economy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import fr.jesfot.gbp.GamingBlockPlug_1_9;

public class GEconomy
{
	private GamingBlockPlug_1_9 gbp;
	protected Map<OfflinePlayer, PlayerEconomy> economies;
	
	public GEconomy(GamingBlockPlug_1_9 p_gbp)
	{
		this.economies = new HashMap<OfflinePlayer, PlayerEconomy>();
		this.gbp = p_gbp;
	}
	
	public List<String> getList(List<String> except)
	{
		List<String> result = new ArrayList<String>();
		for(String str : this.gbp.getConfigFolder("playerdatas").list())
		{
			str = str.endsWith(".dat")?str.substring(0, str.length() - ".dat".length()):str;
			UUID uid = UUID.fromString(str);
			OfflinePlayer player = this.gbp.getServer().getOfflinePlayer(uid);
			boolean flag = true;
			for(String s : except)
			{
				if(uid.toString().contentEquals(s) || player.getName().contentEquals(s))
				{
					flag = false;
				}
			}
			if(flag)
			{
				result.add(player.getName());
			}
		}
		return result;
	}
	
	public void addPlayer(OfflinePlayer player)
	{
		this.economies.put(player, new PlayerEconomy(this.gbp, player));
	}
	
	public boolean playerExists(OfflinePlayer player)
	{
		return this.economies.containsKey(player);
	}
	
	public PlayerEconomy getPEconomy(OfflinePlayer target)
	{
		if(target == null)
		{
			return null;
		}
		if(!this.playerExists(target))
		{
			this.addPlayer(target);
		}
		return this.economies.get(target);
	}
	
	public boolean resetPlayer(OfflinePlayer player)
	{
		if(this.playerExists(player))
		{
			this.economies.get(player).resetMoney();
			return true;
		}
		this.addPlayer(player);
		return false;
	}
	
	public boolean removePlayer(OfflinePlayer player)
	{
		if(this.playerExists(player))
		{
			this.economies.remove(player);
			return true;
		}
		return false;
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
			this.getPEconomy(target).add(amount);
			return true;
		}
		if(!this.getPEconomy(source).hasEnough(amount))
		{
			return false;
		}
		if(target == null)
		{
			this.getPEconomy(source).remove(amount);
			return true;
		}
		this.getPEconomy(source).remove(amount);
		this.getPEconomy(target).add(amount);
		return true;
	}
}