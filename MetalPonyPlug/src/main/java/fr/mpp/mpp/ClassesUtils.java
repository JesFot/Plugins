package fr.mpp.mpp;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.mpp.MetalPonyPlug;
import fr.mpp.config.MConfig;

@SuppressWarnings("unused")
public class ClassesUtils
{
	private static MetalPonyPlug mpp;
	private MConfig mco;
	
	public ClassesUtils(MConfig mo)
	{
		this.mco = mo;
	}
	
	public static boolean isInZone(Location loc)
	{
		int mX = MConfig.getMaxX();
		int mY = MConfig.getMaxY();
		int mZ = MConfig.getMaxZ();

		int miX = MConfig.getMinX();
		int miY = MConfig.getMinY();
		int miZ = MConfig.getMinZ();

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
		int mX = MConfig.getMaxBX();
		int mY = MConfig.getMaxBY();
		int mZ = MConfig.getMaxBZ();

		int miX = MConfig.getMinBX();
		int miY = MConfig.getMinBY();
		int miZ = MConfig.getMinBZ();

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
	
	public static Classes getClasseByAppelName(final String name)
	{
		if (name == null || name == "")
		{
			return Classes.Default;
		}
		for (Classes c : Classes.values())
		{
			if (name.equalsIgnoreCase(c.getAppel()))
			{
				return c;
			}
		}
		return Classes.Default;
	}
	
	public void addRank(Classes cl, Player player)
	{
		player.setDisplayName(cl.getClasse().getDisplayName() + player.getName());
		String oldCl = mco.getCustomConfig().getString("mpp.rank."+RankLevel.MAIN.getName()+"."+player.getName().toLowerCase());
		String oldOldCl = mco.getCustomConfig().getString("mpp.rank."+RankLevel.OLD.getName()+"."+player.getName().toLowerCase());
		mco.getCustomConfig().set("mpp.rank."+RankLevel.OLD.getName()+"."+player.getName().toLowerCase(), oldCl);
		mco.getCustomConfig().set("mpp.rank."+RankLevel.MAIN.getName()+"."+player.getName().toLowerCase(), cl.getAppel());
		mco.saveCustomConfig();
	}
	public void addRank(Classes cl, Player player, RankLevel level)
	{
		String lvl = level.getName();
		if (level == RankLevel.MAIN)
		{
			addRank(cl, player);
			return;
		}
		String oldCl = mco.getCustomConfig().getString("mpp.rank."+lvl+"."+player.getName().toLowerCase());
		String oldOldCl = mco.getCustomConfig().getString("mpp.rank."+RankLevel.OLD.getName()+"."+player.getName().toLowerCase());
		mco.getCustomConfig().set("mpp.rank."+RankLevel.OLD.getName()+"."+player.getName().toLowerCase(), oldCl);
		mco.getCustomConfig().set("mpp.rank."+lvl+"."+player.getName().toLowerCase(), cl.getAppel());
		mco.saveCustomConfig();
	}
	
	public Classes getRank(final Player player, final RankLevel level)
	{
		String lvl = level.getName();
		return getClasseByName(mco.getCustomConfig().getString("mpp.rank."+lvl+"."+player.getName().toLowerCase()));
	}
	
	public RankLevel getRankLevel(final String name, final String pName)
	{
		for (int i = 0; i < RankLevel.getHowMany(); i++)
		{
			RankLevel r = RankLevel.getRankByID(i);
			if (mco.getCustomConfig().getString("mpp.rank."+r.getName()+"."+pName.toLowerCase()) == name)
			{
				return r;
			}
		}
		return null;
	}
	
	public boolean passRank(final String name, Player player)
	{
		RankLevel rl = getRankLevel(name, player.getName().toLowerCase());
		Classes clT = getClasseByName(name);
		Classes nCl = clT.getClasse().getNextRank();
		mco.getCustomConfig().set("mpp.rank."+rl.getName()+"."+player.getName().toLowerCase(), nCl.getAppel());
		mco.saveCustomConfig();
		return true;
	}
}