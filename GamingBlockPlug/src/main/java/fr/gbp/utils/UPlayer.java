package fr.gbp.utils;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class UPlayer
{
	public static List<Player> getOnlinePlayers()
	{
		return Arrays.asList(Bukkit.getOnlinePlayers().toArray(new Player[]{}));
	}
	public static List<OfflinePlayer> getOfflinePlayers()
	{
		return Arrays.asList(Bukkit.getOfflinePlayers());
	}
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
			if (player.getCustomName() != null && player.getCustomName().equalsIgnoreCase(name))
			{
				pl = player;
				break;
			}
		}
		return pl;
	}
	public static OfflinePlayer getPlayerByNameOff(String name)
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
		return pl;
	}
	/*public static Player getProximityPlayer(Location start)
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
	}*/
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