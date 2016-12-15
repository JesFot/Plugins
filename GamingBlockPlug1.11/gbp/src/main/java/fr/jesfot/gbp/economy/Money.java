package fr.jesfot.gbp.economy;

import fr.jesfot.gbp.GamingBlockPlug_1_11;

public class Money
{
	protected static double baseMon = 100.0;
	protected static double monByEm = 1.0;
	protected static String ecoSym = "$";
	
	public static String getSym()
	{
		return Money.ecoSym.toString();
	}
	
	public static double getBasics()
	{
		return Money.baseMon;
	}
	
	public static double getManyEmerald(double money)
	{
		return money * (1 / Money.monByEm);
	}
	
	public static double getManyMoney(int emerald)
	{
		return emerald * Money.monByEm;
	}
	
	public static int getEmerald(double emeralds)
	{
		if(emeralds % 1 > 0)
		{
			return (int)(emeralds - 0.5);
		}
		return (int)emeralds;
	}
	
	public static void reload(GamingBlockPlug_1_11 p_gbp)
	{
		baseMon = p_gbp.getConfigs().getMainConfig().getConfig().getDouble("economy.basemoney", baseMon);
		monByEm = p_gbp.getConfigs().getMainConfig().getConfig().getDouble("economy.monbyem", monByEm);
		ecoSym = p_gbp.getConfigs().getMainConfig().getConfig().getString("economy.symbol", ecoSym);
	}
}