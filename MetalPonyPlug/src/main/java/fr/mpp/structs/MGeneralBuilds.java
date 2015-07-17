package fr.mpp.structs;

import org.bukkit.Location;

import fr.mpp.MPP;

public class MGeneralBuilds
{
	private MBank bank;
	
	public MGeneralBuilds()
	{
		this.bank = new MBank().setBankLoc(new Location(MPP.getmpp().getServer().getWorlds().get(0), 0, 0, 0), 0.5);
	}
	
	public MBank getBank()
	{
		return this.bank;
	}
}