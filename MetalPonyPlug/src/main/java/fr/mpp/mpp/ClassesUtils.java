package fr.mpp.mpp;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.mpp.MetalPonyPlug;
import fr.mpp.config.MConfig;

public class ClassesUtils
{
	@SuppressWarnings("unused")
	private static MetalPonyPlug mpp;
	
	public static boolean isInZone(Location loc)
	{
		//mpp.getConfig();
		//395;79;-27;389;78;-27;
		int mX = MConfig.getMaxX();//395;//mpp.getConfig().getMaxX();
		int mY = MConfig.getMaxY();//79;//mpp.getConfig().getMaxY();
		int mZ = MConfig.getMaxZ();//-27;//mpp.getConfig().getMaxZ();

		int miX = MConfig.getMinX();//389;//mpp.getConfig().getMinX();
		int miY = MConfig.getMinY();//78;//mpp.getConfig().getMinY();
		int miZ = MConfig.getMinZ();//-27;//mpp.getConfig().getMinZ();

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
		//mpp.setMeta(player, "MPPRank", cl);
	}
	
	public static Classes getRank(final Player player)
	{
		return (Classes)player.getMetadata("MPPRank").get(0).value();
	}
}