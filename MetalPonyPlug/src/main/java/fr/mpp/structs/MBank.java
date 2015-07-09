package fr.mpp.structs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.mpp.utils.Locate;

public class MBank
{
	protected Location inZoneStart;
	protected Location inZoneEnd;
	
	public MBank()
	{
		//
	}
	
	public int howManyPlayersIn()
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
	
	public Player[] getPlayers()
	{
		List<Player> players = new ArrayList<Player>();
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(Locate.isInZone(player.getLocation(), this.inZoneStart, this.inZoneEnd))
			{
				players.add(player);
			}
		}
		return (Player[])players.toArray();
	}
}