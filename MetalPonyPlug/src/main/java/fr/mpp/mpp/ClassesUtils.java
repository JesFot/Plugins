package fr.mpp.mpp;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.mpp.MetalPonyPlug;
import fr.mpp.config.MConfig;

public class ClassesUtils
{
	private static MetalPonyPlug mpp;
	
	public static boolean isInZone(Location loc)
	{
		//395;79;-27;389;78;-27;
		int mX = MConfig.getMaxX();//395;
		int mY = MConfig.getMaxY();//79;
		int mZ = MConfig.getMaxZ();//-27;

		int miX = MConfig.getMinX();//391;
		int miY = MConfig.getMinY();//78;
		int miZ = MConfig.getMinZ();//-27;

		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();

		if (x <= mX && x >= miX)
		{
			if (y <= mY && y >= miY)
			{
				if (z <= mZ && z >= miZ)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static Classes getClasseByName(final String name)
	{
		if (name == null || name == "")
		{
			return Classes.Default;
		}
		for (Classes c : Classes.values())
		{
			if (name.equalsIgnoreCase(c.getClasse().getName()))
			{
				return c;
			}
		}
		return Classes.Default;
	}
	
	public static void addRank(String name, Player player)
	{
		Classes cl = ClassesUtils.getClasseByName(name);
		player.setDisplayName(cl.getClasse().getDisplayName() + player.getName());
		try
		{
			mpp.setMeta(player, "MPPRank", cl);
		}
		catch (Exception e)
		{
			// Code ...
		}
	}
	
	public static Classes getRank(final Player player)
	{
		return (Classes)player.getMetadata("MPPRank").get(0).value();
	}
	
	public static boolean passRank(final String name, Player player)
	{
		try
		{
			Classes clT = getRank(player);
			addRank(name, player);
			mpp.setMeta(player, "MPPRankOld", clT);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
}