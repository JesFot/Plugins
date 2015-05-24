package fr.gbp.economy;

import fr.gbp.GamingBlocksPlug;

public class Money
{
	private GamingBlocksPlug gbp;
	
	protected double emeraldsbyMon = 0.1;
	protected double baseMon = 1000.0;
	protected double monbyEm = 10.0;
	
	public Money(GamingBlocksPlug p_gbp)
	{
		this.gbp = p_gbp;
	}

	public double getBasics()
	{
		//this.gbp.getConfig().reloadCustomConfig();
		return this.gbp.getConfig().getCustomConfig().getDouble("economy.basemoney", this.baseMon);
	}
	
	public double getManyEmerald(double money)
	{
		return money * this.emeraldsbyMon;
	}
	
	public double getManyMoney(int emerald)
	{
		return emerald * this.monbyEm;
	}
	
	public int getEmerald(double emeralds)
	{
		if(emeralds%1 > 0)
		{
			return (int)(emeralds-0.5);
		}
		return (int)emeralds;
	}

	public double getSalary(String type)
	{
		this.gbp.getConfig().reloadCustomConfig();
		if(type == "daily")
		{
			return this.gbp.getConfig().getCustomConfig().getDouble("salary.daily", 5.0);
		}
		return 0;
	}
}