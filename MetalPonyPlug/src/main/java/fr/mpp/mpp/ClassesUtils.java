package fr.mpp.mpp;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import fr.mpp.MetalPonyPlug;

public class ClassesUtils
{
	private static MetalPonyPlug mpp;
	
	public static boolean isInZone(Location loc)
	{
		//395;79;-27;389;78;-27;
		int mX = 395;//mpp.getConfig().getMaxX();
		int mY = 79;//mpp.getConfig().getMaxY();
		int mZ = -27;//mpp.getConfig().getMaxZ();

		int miX = 389;//mpp.getConfig().getMinX();
		int miY = 78;//mpp.getConfig().getMinY();
		int miZ = -27;//mpp.getConfig().getMinZ();

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
	
	public static void addRank(final Classes cl, final Player player)
	{
		player.setDisplayName(cl.getClasse().getDisplayName() + player.getName());
		player.setMetadata("MPPRank", new FixedMetadataValue(mpp.getPlugin(), cl));
	}
	
	public static Classes getRank(final Player player)
	{
		return (Classes)player.getMetadata("MPPRank").get(0).value();
	}
}