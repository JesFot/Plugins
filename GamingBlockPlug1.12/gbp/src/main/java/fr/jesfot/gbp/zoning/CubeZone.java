package fr.jesfot.gbp.zoning;

import org.bukkit.Location;

public class CubeZone implements IZone
{
	private Location startPoint, endPoint;
	
	public CubeZone()
	{
		this.startPoint = null;
		this.endPoint = null;
	}
	
	public void setStart(final Location start)
	{
		if(start == null)
		{
			this.startPoint = null;
			return;
		}
		this.startPoint = start.clone();
		this.startPoint.setX(this.startPoint.getBlockX());
		this.startPoint.setY(this.startPoint.getBlockY());
		this.startPoint.setZ(this.startPoint.getBlockZ());
		this.sortPoints();
	}
	
	public Location getStart()
	{
		return this.startPoint.clone();
	}
	
	public void setEnd(final Location end)
	{
		if(end == null)
		{
			this.endPoint = null;
			return;
		}
		this.endPoint = end.clone();
		this.endPoint.setX(this.endPoint.getBlockX());
		this.endPoint.setY(this.endPoint.getBlockY());
		this.endPoint.setZ(this.endPoint.getBlockZ());
		this.sortPoints();
	}
	
	public Location getEnd()
	{
		return this.endPoint.clone();
	}
	
	public void sortPoints()
	{
		if(startPoint != null && endPoint != null)
		{
			this.endPoint.setWorld(this.startPoint.getWorld());
			if(startPoint.getBlockX() > endPoint.getBlockX())
			{
				double tmp = startPoint.getBlockX();
				startPoint.setX(endPoint.getBlockX());
				endPoint.setX(tmp);
			}
			if(startPoint.getBlockY() > endPoint.getBlockY())
			{
				double tmp = startPoint.getBlockY();
				startPoint.setY(endPoint.getBlockY());
				endPoint.setY(tmp);
			}
			if(startPoint.getBlockZ() > endPoint.getBlockZ())
			{
				double tmp = startPoint.getBlockZ();
				startPoint.setZ(endPoint.getBlockZ());
				endPoint.setZ(tmp);
			}
		}
	}
	
	public boolean hasPoints()
	{
		if(this.startPoint == null)
		{
			return false;
		}
		if(this.endPoint == null)
		{
			return false;
		}
		return true;
	}
	
	public boolean isInZone(final Location location)
	{
		if(location.getBlockX() < this.startPoint.getBlockX() || location.getBlockX() > this.endPoint.getBlockX())
		{
			return false;
		}
		if(location.getBlockY() < this.startPoint.getBlockY() || location.getBlockY() > this.endPoint.getBlockY())
		{
			return false;
		}
		if(location.getBlockZ() < this.startPoint.getBlockZ() || location.getBlockZ() > this.endPoint.getBlockZ())
		{
			return false;
		}
		return true;
	}
}