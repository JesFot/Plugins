package fr.mpp.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class MPlayer
{
	public static Player getPlayerByName(String name)
	{
		Player pl = null;
		
		if(name.contains("@"))
		{
			return null;
		}
		
		for (Player player : Bukkit.getOnlinePlayers())
		{
			if (player.getName().equalsIgnoreCase(name))
			{
				pl = player;
				break;
			}
			if (player.getDisplayName().equalsIgnoreCase(name))
			{
				pl = player;
				break;
			}
			if (player.getCustomName().equalsIgnoreCase(name))
			{
				pl = player;
				break;
			}
		}
		return pl;
	}
	public static Player getPlayerByNameOff(String name)
	{
		OfflinePlayer pl = null;
		
		for (OfflinePlayer player : Bukkit.getOfflinePlayers())
		{
			if (player.getName().equalsIgnoreCase(name))
			{
				pl = player;
				break;
			}
		}
		return (Player)pl;
	}
	public static Player getProximityPlayer(Location start)
	{
		if(start.getWorld().getPlayers().size() <= 0)
		{
			return null;
		}
		if(start.getWorld().getPlayers().size() == 1)
		{
			return start.getWorld().getPlayers().get(0);
		}
		List<Player> locations = new ArrayList<Player>();
		for(Player p : start.getWorld().getPlayers())
		{
			locations.add(p);
		}
		Location myLocation = start;
		Player closest = locations.get(0);
		double closestDist = closest.getLocation().distance(myLocation);
		for (Player loc : locations)
		{
			if (loc.getLocation().distance(myLocation) < closestDist)
			{
				closestDist = loc.getLocation().distance(myLocation);
				closest = loc;
			}
		}
		return closest;
	}
	public static Player getRandomPlayer(World w)
	{
		if(w.getPlayers().size() <= 0)
		{
			return null;
		}
		if(w.getPlayers().size() == 1)
		{
			return w.getPlayers().get(0);
		}
		int r = new Random().nextInt(w.getPlayers().size());
		return w.getPlayers().get(r);
	}
	public static Player[] getPlayerByRep(String rep, Location start)
	{
		Player pls[] = {};
		MParseCommandTarget mpct = new MParseCommandTarget(rep, start);
		switch(mpct.type.charac)
		{
		case 'p':
			pls = new Player[]{getProximityPlayer(start)};
			return pls;
		case 'r':
			pls = new Player[]{getRandomPlayer(start.getWorld())};
			return pls;
		case 'e':
		case 'a':
			pls = Bukkit.getOnlinePlayers();
			return pls;
		default:
			return pls;
		}
	}
	public static String concateTable(String[] args)
	{
		String result = "[";
		int i = 0;
		for(String str : args)
		{
			i++;
			result += "\""+str+"\"";
			if(!(i == args.length))
			{
				result += ", ";
			}
		}
		return (result + "]");
	}
}