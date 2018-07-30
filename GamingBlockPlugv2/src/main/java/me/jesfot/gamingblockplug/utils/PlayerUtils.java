package me.jesfot.gamingblockplug.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public final class PlayerUtils
{
	/**
	 * Gets the most proximate player.
	 * 
	 * @deprecated Do not use it if not REALLY necessary.
	 * 
	 * @param start The center location.
	 * @return Returns the found player, or null.
	 */
	@Deprecated
	public static Player getProximityPlayer(Location start)
	{
		if (start.getWorld().getPlayers().size() <= 0)
		{
			return null;
		}
		if (start.getWorld().getPlayers().size() == 1)
		{
			return start.getWorld().getPlayers().get(0);
		}
		List<Player> locations = new ArrayList<Player>();
		for (Player p : start.getWorld().getPlayers())
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
	
	/**
	 * Get a random player in the given world.
	 * 
	 * @param w The world where players are.
	 * @return Returns found player or null.
	 */
	public static Player getRandomPlayer(World w)
	{
		if (w.getPlayers().size() <= 0)
		{
			return null;
		}
		if (w.getPlayers().size() == 1)
		{
			return w.getPlayers().get(0);
		}
		int r = new Random().nextInt(w.getPlayers().size());
		return w.getPlayers().get(r);
	}
}
