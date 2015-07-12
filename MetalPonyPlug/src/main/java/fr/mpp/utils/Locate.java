package fr.mpp.utils;

import org.bukkit.Location;
import org.bukkit.World;

public class Locate
{
	public static boolean compare2Loc(Location loc1, Location loc2)
	{
		World w1, w2;
		double x1, x2;
		double y1, y2;
		double z1, z2;
		float yaw1, yaw2;
		float pitch1, pitch2;
		
		w1 = loc1.getWorld();
		w2 = loc2.getWorld();
		
		x1 = loc1.getX();
		y1 = loc1.getY();
		z1 = loc1.getZ();
		yaw1 = loc1.getYaw();
		pitch1 = loc1.getPitch();

		x2 = loc2.getX();
		y2 = loc2.getY();
		z2 = loc2.getZ();
		yaw2 = loc2.getYaw();
		pitch2 = loc2.getPitch();
		
		if (w1 == w2)
		{
			if (x1 == x2)
			{
				if (y1 == y2)
				{
					if (z1 == z2)
					{
						if (yaw1 == yaw2)
						{
							if (pitch1 == pitch2)
							{
								return true;
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public static boolean compare2LocB(Location loc1, Location loc2)
	{
		World w1, w2;
		int x1, x2;
		int y1, y2;
		int z1, z2;
		float yaw1, yaw2;
		float pitch1, pitch2;
		
		w1 = loc1.getWorld();
		w2 = loc2.getWorld();
		
		x1 = loc1.getBlockX();
		y1 = loc1.getBlockY();
		z1 = loc1.getBlockZ();
		yaw1 = loc1.getYaw();
		pitch1 = loc1.getPitch();

		x2 = loc2.getBlockX();
		y2 = loc2.getBlockY();
		z2 = loc2.getBlockZ();
		yaw2 = loc2.getYaw();
		pitch2 = loc2.getPitch();
		
		if (w1 == w2)
		{
			if (x1 == x2)
			{
				if (y1 == y2)
				{
					if (z1 == z2)
					{
						if (yaw1 == yaw2)
						{
							if (pitch1 == pitch2)
							{
								return true;
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public static boolean isInZone(Location loc, Location start, Location end)
	{
		World w1, w2, w3;
		int x1, x2, x3;
		int y1, y2, y3;
		int z1, z2, z3;
		
		w1 = start.getWorld();
		w2 = end.getWorld();
		w3 = loc.getWorld();
		
		x1 = start.getBlockX();
		y1 = start.getBlockY();
		z1 = start.getBlockZ();
		
		x2 = end.getBlockX();
		y2 = end.getBlockY();
		z2 = end.getBlockZ();

		x3 = loc.getBlockX();
		y3 = loc.getBlockY();
		z3 = loc.getBlockZ();
		
		if(compare2LocB(start, end))
		{
			return compare2LocB(loc, start);
		}
		
		if(((x3<=x2 && x3>=x1) || (x3>=x2 && x3<=x1)) && w2 == w3 && w1 == w2)
		{
			if((y3<=y2 && y3>=y1) || (y3>=y2 && y3<=y1))
			{
				if((z3<=z2 && z3>=z1) || (z3>=z2 && z3<=z1))
				{
					return true;
				}
			}
		}
		return false;
	}
	public static boolean isInZoneR(Location loc, Location center, double range)
	{
		double dist = getDist(loc, center);
		if(dist <= range+0.5)
		{
			return true;
		}
		return false;
	}
	public static double getDist(Location loc1, Location loc2)
	{
		Location loc3 = loc2.clone();
		if(loc1.getWorld() != loc2.getWorld())
		{
			loc3.setWorld(loc1.getWorld());
			return loc1.distance(loc3)+1000000.001;
		}
		return loc1.distance(loc3);
	}
}