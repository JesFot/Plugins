package fr.jesfot.gbp.zoning.island;

import org.bukkit.Location;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.zoning.Zone;

public class IslandZone extends Zone
{
	private GamingBlockPlug_1_9 gbp;
	private boolean disabled = false;
	
	public IslandZone(GamingBlockPlug_1_9 plugin)
	{
		super(101, 70, 101, null);
		this.gbp = plugin;
	}
	
	public void readCenter()
	{
		this.setCenter(this.gbp.getNBT("Island").readNBTFromFile().getLocation("coords"));
		if(this.getCenter() == null)
		{
			this.gbp.getNBT("Island").readNBTFromFile().setLocation("coordsExample",
					new Location(this.gbp.getServer().getWorlds().get(0), 0, 0, 0)).writeNBTToFile();
		}
	}
	
	public void storeCenter()
	{
		this.gbp.getNBT("Island").readNBTFromFile().setLocation("coords", this.getCenter()).writeNBTToFile();
	}
	
	public void disable()
	{
		this.disabled = true;
	}
	
	@Override
	public void moveUp(final int blcks)
	{
		if(this.disabled)
		{
			return;
		}
		super.moveUp(blcks);
	}
	
	@Override
	public void moveDown(final int blcks)
	{
		if(this.disabled)
		{
			return;
		}
		super.moveDown(blcks);
	}
	
	public String getLoc()
	{
		return "W:" + this.getCenter().getWorld().getName() + ", X:" + this.getCenter().getBlockX() + ", Y:"
				+ this.getCenter().getBlockY() + ", Z:" + this.getCenter().getBlockZ();
	}
}