package fr.gbp.economy;

import fr.gbp.GamingBlockPlug;

public class Money
{
	private GamingBlockPlug gbp;
	
	protected double baseMon = 100.0;
	protected double monbyEm = 1.0;
	protected String ecoSym = "$";
	
	public Money(GamingBlockPlug p_gbp)
	{
		this.gbp = p_gbp;
	}
	
	public String getSym()
	{
		return this.ecoSym.toString();
	}

	public double getBasics()
	{
		//this.gbp.getConfig().reloadCustomConfig();
		return this.gbp.getConfig().getCustomConfig().getDouble("economy.basemoney", this.baseMon);
	}
	
	public double getManyEmerald(double money)
	{
		return money * (1 / this.gbp.getConfig().getCustomConfig().getDouble("economy.monbyem", this.monbyEm));
	}
	
	public double getManyMoney(int emerald)
	{
		return emerald * this.gbp.getConfig().getCustomConfig().getDouble("economy.monbyem", this.monbyEm);
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