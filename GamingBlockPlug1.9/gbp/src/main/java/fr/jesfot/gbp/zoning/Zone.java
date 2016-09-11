package fr.jesfot.gbp.zoning;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;

public abstract class Zone
{
	private final int sizeX, sizeY, sizeZ;
	private Location center;
	
	public Zone(final int size_X, final int size_Y, final int size_Z, final Location p_center)
	{
		this.sizeX = size_X;
		this.sizeY = size_Y;
		this.sizeZ = size_Z;
		this.center = p_center;
	}
	
	public void setCenter(final Location loc)
	{
		if(loc == null)
		{
			this.center = null;
			return;
		}
		this.center = loc.clone();
	}
	
	public Location getCenter()
	{
		if(this.center == null)
		{
			return null;
		}
		return this.center.clone();
	}
	
	public void moveUp()
	{
		this.moveUp(1);
	}
	
	@SuppressWarnings("deprecation")
	public void moveNorth(final int blocks)
	{
		if(blocks <= 0)
		{
			return;
		}
		try
		{
			Block topN = this.getCenter().subtract(0, 0, (this.sizeZ - 1) / 2).getBlock();
			if(topN.getRelative(BlockFace.NORTH).getType().equals(Material.OBSIDIAN))
			{
				return;
			}
			Block topNorth = topN.getLocation().clone().add(0, (this.sizeY / 2) + 1, 0).getBlock();
			Block topNorthWestCorner = topNorth.getLocation().clone().subtract((this.sizeX - 1) / 2, 0, 0).getBlock();
			boolean first = true;
			for(int k = 0; k < this.sizeZ + 1; k++)
			{
				for(int j = 0; j > -this.sizeY - 1; j--)
				{
					for(int i = 0; i < this.sizeX; i++)
					{
						Block tmp = topNorthWestCorner.getLocation().clone().add(i, j, k).getBlock();
						if(first)
						{
							if(tmp.getRelative(BlockFace.NORTH).getType().equals(Material.BEDROCK))
							{
								return;
							}
							continue;
						}
						if(tmp.getRelative(BlockFace.UP).isEmpty())
						{
							Collection<Entity> es = tmp.getWorld().getNearbyEntities(
									tmp.getLocation().clone().add(0, 1, 0), 1, 1, 1);
							Collection<Entity> inside = tmp.getWorld().getNearbyEntities(
									tmp.getLocation().clone(), 1, 1, 1);
							for(Entity e : es)
							{
								if(inside.contains(e))
								{
									continue;
								}
								e.teleport(e.getLocation().clone().add(0, blocks, 0));
							}
						}
						tmp.getLocation().clone().subtract(0, 0, blocks).getBlock().setTypeIdAndData(
								tmp.getTypeId(), tmp.getData(), true);
						tmp.getLocation().clone().getBlock().setType(Material.AIR);
					}
				}
				if(first)
				{
					first = false;
					k--;
				}
			}
			this.center = this.center.clone().add(0, 0, -blocks);
		}
		catch(Throwable ignored)
		{}
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
			Block top = this.getCenter().add(0, (this.sizeY / 2) - 1, 0).getBlock();
			if(top.getRelative(BlockFace.UP).getType().equals(Material.OBSIDIAN))
			{
				return;
			}
			Block topNorthCorner = top.getLocation().clone().add(0, 0, -((this.sizeZ - 1) / 2)).getBlock();
			Block topNorthWestCorner = topNorthCorner.getLocation().clone().add(-((this.sizeX - 1) / 2), 0, 0).getBlock();
			boolean first = true;
			for(int i = 0; i > -this.sizeY - 1; i--)
			{
				for(int j = 0; j < this.sizeX; j++)
				{
					for(int k = 0; k < this.sizeZ; k++)
					{
						Block tmp = topNorthWestCorner.getLocation().clone().add(j, i, k).getBlock();
						if(first)
						{
							if(tmp.getRelative(BlockFace.UP).getType().equals(Material.BEDROCK))
							{
								return;
							}
							continue;
						}
						if(tmp.getLocation().clone().add(0, 1, 0).getBlock().isEmpty())
						{
							Collection<Entity> es = tmp.getWorld().getNearbyEntities(
									tmp.getLocation().clone().add(0, 1, 0), 1, 1, 1);
							Collection<Entity> inside = tmp.getWorld().getNearbyEntities(
									tmp.getLocation().clone(), 1, 1, 1);
							for(Entity e : es)
							{
								if(inside.contains(e))
								{
									continue;
								}
								e.teleport(e.getLocation().clone().add(0, blocks, 0));
							}
						}
						tmp.getLocation().clone().add(0, blocks, 0).getBlock().setTypeIdAndData(
								tmp.getTypeId(), tmp.getData(), true);
						tmp.getLocation().clone().getBlock().setType(Material.AIR);
					}
				}
				if(first)
				{
					first = false;
					i++;
				}
			}
			this.center = this.center.clone().add(0, blocks, 0);
		}
		catch(Throwable ignored)
		{}
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
			Block top = this.getCenter().subtract(0, (this.sizeY / 2) + 1, 0).getBlock();
			if(top.getRelative(BlockFace.DOWN).getType().equals(Material.OBSIDIAN))
			{
				return;
			}
			Block topNorthCorner = top.getLocation().clone().add(0, 0, -((this.sizeZ - 1) / 2)).getBlock();
			Block topNorthWestCorner = topNorthCorner.getLocation().clone().add(-((this.sizeX - 1) / 2), 0, 0).getBlock();
			boolean first = true;
			for(int i = 0; i < this.sizeY + 1; i++)
			{
				for(int j = 0; j < this.sizeX; j++)
				{
					for(int k = 0; k < this.sizeZ; k++)
					{
						Block tmp = topNorthWestCorner.getLocation().clone().add(j, i, k).getBlock();
						if(first)
						{
							if(tmp.getRelative(BlockFace.DOWN).getType().equals(Material.BEDROCK))
							{
								return;
							}
							continue;
						}
						if(tmp.getLocation().clone().add(0, 1, 0).getBlock().isEmpty())
						{
							Collection<Entity> es = tmp.getWorld().getNearbyEntities(
									tmp.getLocation().clone().add(0, 1, 0), 1, 1, 1);
							Collection<Entity> inside = tmp.getWorld().getNearbyEntities(
									tmp.getLocation().clone(), 1, 1, 1);
							for(Entity e : es)
							{
								if(inside.contains(e))
								{
									continue;
								}
								e.teleport(e.getLocation().clone().subtract(0, blocks, 0));
							}
						}
						tmp.getLocation().clone().subtract(0, blocks, 0).getBlock().setTypeIdAndData(
								tmp.getTypeId(), tmp.getData(), true);
						tmp.getLocation().clone().getBlock().setType(Material.AIR);
					}
				}
				if(first)
				{
					first = false;
					i--;
				}
			}
			this.center = this.center.clone().add(0, -blocks, 0);
		}
		catch(Throwable ignored)
		{}
	}
}