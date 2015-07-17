package fr.mpp.structs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.mpp.utils.Locate;

public class MBank
{
	protected Location inZoneStart;
	protected Location inZoneEnd;
	protected double range;
	protected boolean useRange;
	
	protected List<Player> inBank = new ArrayList<Player>();
	
	public MBank()
	{
		this.inZoneStart = null;
		this.inZoneEnd = null;
		this.range = 0.0;
		this.useRange = false;
	}
	
	public MBank setBankLoc(Location start, Location end)
	{
		this.inZoneStart = start;
		this.inZoneEnd = end;
		this.useRange = false;
		return this;
	}
	
	public MBank setBankLoc(Location start, double p_range)
	{
		this.setBankLoc(start, null);
		this.useRange = true;
		return this;
	}
	
	public MBank setRange(double p_range)
	{
		if(this.inZoneStart == null)
		{
			return this;
		}
		this.range = p_range;
		this.useRange = true;
		return this;
	}
	
	public MBank setLocEnd(Location end)
	{
		if(this.inZoneStart == null)
		{
			return this;
		}
		this.inZoneEnd = end;
		this.useRange = false;
		return this;
	}
	
	public int howManyPlayersIn()
	{
		if(!this.useRange)
		{
			int tmp = 0;
			for(Player player : Bukkit.getOnlinePlayers())
			{
				if(Locate.isInZone(player.getLocation(), this.inZoneStart, this.inZoneEnd))
				{
					tmp++;
				}
			}
			return tmp;
		}
		else
		{
			int tmp = 0;
			for(Player player : Bukkit.getOnlinePlayers())
			{
				if(Locate.isInZoneR(player.getLocation(), this.inZoneStart, this.range))
				{
					tmp++;
				}
			}
			return tmp;
		}
	}
	
	public Player[] getPlayers()
	{
		List<Player> players = new ArrayList<Player>();
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(this.useRange)
			{
				if(Locate.isInZoneR(player.getLocation(), this.inZoneStart, this.range))
				{
					players.add(player);
				}
			}
			else
			{
				if(Locate.isInZone(player.getLocation(), this.inZoneStart, this.inZoneEnd))
				{
					players.add(player);
				}
			}
		}
		return (Player[])players.toArray();
	}
	
	public MBank updatePlayers()
	{
		this.inBank = Arrays.asList(this.getPlayers());
		return this;
	}
	
	public boolean isNewInBank(Player player)
	{
		if(this.isInBank(player) && !this.inBank.contains(player))
		{
			return true;
		}
		return false;
	}
	
	public boolean isOldInBank(Player player)
	{
		if(!this.isInBank(player) && this.inBank.contains(player))
		{
			return true;
		}
		return false;
	}
	
	public boolean isInBank(Player player)
	{
		if(this.useRange)
		{
			if(Locate.isInZoneR(player.getLocation(), this.inZoneStart, this.range))
			{
				return true;
			}
		}
		else
		{
			if(Locate.isInZone(player.getLocation(), this.inZoneStart, this.inZoneEnd))
			{
				return true;
			}
		}
		return false;
	}
	
	public MBank playerEnter(Player player)
	{
		this.updatePlayers();
		player.sendMessage("You ");
		return this;
	}
	
	public MBank playerLeave(Player player)
	{
		this.updatePlayers();
		return this;
	}
}