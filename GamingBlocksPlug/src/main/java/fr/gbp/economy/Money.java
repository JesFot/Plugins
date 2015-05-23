package fr.gbp.economy;

import fr.gbp.GamingBlocksPlug;

public class Money
{
	private GamingBlocksPlug gbp;
	
	protected double emeraldsbyMon = 1;
	protected double baseMon = 1000.0;
	protected double monbyEm = 1;
	
	public Money(GamingBlocksPlug p_gbp)
	{
		this.gbp = p_gbp;
	}

	public double getBasics()
	{
		//this.gbp.getConfig().reloadCustomConfig();
		return this.gbp.getConfig().getCustomConfig().getDouble("economy.basemoney", this.baseMon);
	}
}