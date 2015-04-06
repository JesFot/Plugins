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