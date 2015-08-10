package fr.mpp.economy;

import fr.mpp.MetalPonyPlug;

public class MMoney
{
	private MetalPonyPlug mpp;
	
	private double baseMoney = 100.0;
	private double emPrice = 10.0; // 10$ = 1 Emerald
	protected String ecoSym = "$";
	
	public MMoney(MetalPonyPlug p_mpp)
	{
		this.mpp = p_mpp;
	}
	
	public String getSym()
	{
		return this.ecoSym.toString();
	}
	
	public double getBase()
	{
		return this.mpp.getConfig().getCustomConfig().getDouble("economy.basemoney", this.baseMoney);
	}
	
	public double toEM(double money)
	{
		return money / this.emPrice;
	}
	
	public double toMoney(int emerald)
	{
		return emerald * this.emPrice;
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
		this.mpp.getConfig().reloadCustomConfig();
		switch(type)
		{
		case "daily":
			return this.mpp.getConfig().getCustomConfig().getDouble("salary.daily", 5.0);
		case "mensual":
			return this.mpp.getConfig().getCustomConfig().getDouble("salary.mensual", 1.0);
		}
		return 0.0;
	}
}