package fr.jesfot.gbp.zoning.island;

import org.bukkit.Location;

import fr.jesfot.gbp.GamingBlockPlug_1_12;
import fr.jesfot.gbp.zoning.CubeZone;

public class IslandZone extends CubeZone
{
	private GamingBlockPlug_1_12 gbp;
	private boolean disabled = false;
	
	public IslandZone(GamingBlockPlug_1_12 plugin)
	{
		this.gbp = plugin;
	}
	
	public void readCenter()
	{
		this.setStart(this.gbp.getNBT("Island").readNBTFromFile().getLocation("StartCoords"));
		this.setEnd(this.gbp.getNBT("Island").readNBTFromFile().getLocation("EndCoords"));
		if(!this.hasPoints())
		{
			this.gbp.getNBT("Island").readNBTFromFile().setLocation("ExampleCoords",
					new Location(this.gbp.getServer().getWorlds().get(0), 0, 0, 0)).writeNBTToFile();
		}
	}
	
	public void storeCenter()
	{
		this.gbp.getNBT("Island").readNBTFromFile().setLocation("StartCoords", this.getStart()).writeNBTToFile();
		this.gbp.getNBT("Island").readNBTFromFile().setLocation("EndCoords", this.getEnd()).writeNBTToFile();
	}
	
	public void disable()
	{
		this.disabled = true;
	}
	
	public void moveUp(final int blcks)
	{
		if(this.disabled)
		{
			return;
		}
		//super.moveUp(blcks);
	}
	
	public void moveDown(final int blcks)
	{
		if(this.disabled)
		{
			return;
		}
		//super.moveDown(blcks);
	}
	
	public String getLoc()
	{
		return "W:" + this.getStart().getWorld().getName() + ", X:" + this.getStart().getBlockX() + ", Y:"
				+ this.getStart().getBlockY() + ", Z:" + this.getStart().getBlockZ();
	}
}