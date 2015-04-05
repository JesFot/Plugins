package fr.mpp.mpp;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.mpp.MetalPonyPlug;
import fr.mpp.config.MConfig;

@SuppressWarnings("unused")
public class ClassesUtils
{
	private static MetalPonyPlug mpp;
	private static MConfig mco;
	
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
	
	public static boolean iInBlockZone(Location loc)
	{
		int mX = MConfig.getMaxBX();//395;
		int mY = MConfig.getMaxBY();//79;
		int mZ = MConfig.getMaxBZ();//-27;

		int miX = MConfig.getMinBX();//391;
		int miY = MConfig.getMinBY();//78;
		int miZ = MConfig.getMinBZ();//-27;

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
		String oldCl = mco.getCustomConfig().getString("mpp.rank."+RankLevel.MAIN+"."+player.getName());
		String oldOldCl = mco.getCustomConfig().getString("mpp.rank."+RankLevel.OLD+"."+player.getName());
		mco.getCustomConfig().set("mpp.rank."+RankLevel.OLD+"."+player.getName(), oldCl);
		mco.getCustomConfig().set("mpp.rank."+RankLevel.MAIN+"."+player.getName(), cl.getAppel());
	}
	public static void addRank(String name, Player player, RankLevel level)
	{
		String lvl = level.getName();
		Classes cl = ClassesUtils.getClasseByName(name);
		player.setDisplayName(cl.getClasse().getDisplayName() + player.getName());
		String oldCl = mco.getCustomConfig().getString("mpp.rank."+lvl+"."+player.getName());
		String oldOldCl = mco.getCustomConfig().getString("mpp.rank."+lvl+"."+player.getName());
		mco.getCustomConfig().set("mpp.rank."+lvl+"."+player.getName(), oldCl);
		mco.getCustomConfig().set("mpp.rank."+lvl+"."+player.getName(), cl.getAppel());
	}
	
	public static Classes getRank(final Player player, final String level)
	{
		return getClasseByName(mco.getCustomConfig().getString("mpp.rank."+level+"."+player.getName()));
	}
	
	public static RankLevel getRankLevel(final String name, final String pName)
	{
		for (int i = 0; i < RankLevel.getHowMany(); i++)
		{
			RankLevel r = RankLevel.getRankByID(i);
			if (mco.getCustomConfig().getString("mpp.rank."+r.getName()+"."+pName) == name)
			{
				return r;
			}
		}
		return null;
	}
	
	public static boolean passRank(final String name, Player player)
	{
		RankLevel rl = getRankLevel(name, player.getName());
		Classes clT = getClasseByName(name);
		Classes nCl = clT.getClasse().getNextRank();
		mco.getCustomConfig().set("mpp.rank."+rl.getName()+"."+player.getName(), nCl.getAppel());
		return true;
	}
}