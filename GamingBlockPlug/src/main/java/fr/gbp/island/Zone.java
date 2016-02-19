package fr.gbp.island;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import fr.gbp.GamingBlockPlug;

public class Zone
{
	private final int sizeX;
	private final int sizeY;
	private final int sizeZ;
	private Location center;
	private GamingBlockPlug gbp;
	
	public Zone(GamingBlockPlug plugin)
	{
		this.sizeX = 101;
		this.sizeY = 70;
		this.sizeZ = 101;
		this.gbp = plugin;
	}
	
	public void readCenter()
	{
		this.center = this.gbp.getConfig().getLoc("island.center.coords");
	}
	
	public Location getCenter()
	{
		return this.center.clone();
	}
	
	public void storeCenter()
	{
		this.gbp.getConfig().storeLoc("island.center.coords", this.center);
	}
	
	public void moveUp()
	{
		this.moveUp(1);
	}
	
	@SuppressWarnings("deprecation")
	public void moveUp(final int blocks)
	{
		if(blocks <= 0)
		{
			return;
		}
		try
		{
			Block top = this.center.clone().add(0, (sizeY / 2) - 1, 0).getBlock();
			Block topNorthCorner = top.getLocation().clone().add(0, 0, -((sizeZ - 1) / 2)).getBlock();
			Block topNorthWestCorner = topNorthCorner.getLocation().clone().add(-((sizeX - 1) / 2), 0, 0).getBlock();
			for(int i = 0; i > -this.sizeY-1; i--)
			{
				for(int j = 0; j < this.sizeX; j++)
				{
					for(int k = 0; k < this.sizeZ; k++)
					{
						Block tmp = topNorthWestCorner.getLocation().clone().add(j, i, k).getBlock();
						if(tmp.getLocation().clone().add(0, 1, 0).equals(null))
						{
							Collection<Entity> es = tmp.getWorld().getNearbyEntities(tmp.getLocation().clone().add(0, 1, 0), 
									1, 1, 1);
							for(Entity e : es)
							{
								e.teleport(tmp.getLocation().clone().add(0, blocks, 0));
							}
						}
						tmp.getLocation().clone().add(0, blocks, 0).getBlock().setTypeIdAndData(tmp.getTypeId(), tmp.getData(), true);
						tmp.getLocation().clone().getBlock().setType(Material.AIR);
					}
				}
			}
			this.center = this.center.clone().add(0, blocks, 0);
			this.storeCenter();
		}
		catch(Exception e)
		{
			//
		}
	}
	
	public void moveDown()
	{
		this.moveDown(1);
	}
	
	@SuppressWarnings("deprecation")
	public void moveDown(final int blocks)
	{
		if(blocks <= 0)
		{
			return;
		}
		try
		{
			Block top = this.center.clone().subtract(0, (sizeY / 2) - 1, 0).getBlock();
			Block topNorthCorner = top.getLocation().clone().add(0, 0, -((sizeZ - 1) / 2)).getBlock();
			Block topNorthWestCorner = topNorthCorner.getLocation().clone().add(-((sizeX - 1) / 2), 0, 0).getBlock();
			for(int i = 0; i < this.sizeY+1; i++)
			{
				for(int j = 0; j < this.sizeX; j++)
				{
					for(int k = 0; k < this.sizeZ; k++)
					{
						Block tmp = topNorthWestCorner.getLocation().clone().add(j, i, k).getBlock();
						if(tmp.getLocation().clone().add(0, 1, 0).equals(null))
						{
							Collection<Entity> es = tmp.getWorld().getNearbyEntities(tmp.getLocation().clone().add(0, 1, 0), 
									1, 1, 1);
							for(Entity e : es)
							{
								e.teleport(tmp.getLocation().clone().subtract(0, blocks, 0));
							}
						}
						tmp.getLocation().clone().add(0, blocks, 0).getBlock().setTypeIdAndData(tmp.getTypeId(), tmp.getData(), true);
						tmp.getLocation().clone().getBlock().setType(Material.AIR);
					}
				}
			}
			this.center = this.center.clone().add(0, blocks, 0);
			this.storeCenter();
		}
		catch(Exception e)
		{
			//
		}
	}
}