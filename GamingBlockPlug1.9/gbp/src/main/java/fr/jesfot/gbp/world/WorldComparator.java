package fr.jesfot.gbp.world;

import org.bukkit.World;

public class WorldComparator
{
	public static int compareWorlds(World alpha, World beta)
	{
		if(alpha.getUID().equals(beta.getUID()))
		{
			return 1;
		}
		if(alpha.getName().contentEquals(beta.getName()))
		{
			return 2;
		}
		return -1;
	}
	
	public static boolean isEqualWorld(World alpha, World beta)
	{
		return WorldComparator.compareWorlds(alpha, beta) > 0;
	}
}