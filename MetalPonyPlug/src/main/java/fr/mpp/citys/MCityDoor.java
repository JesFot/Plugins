package fr.mpp.citys;

import org.bukkit.Location;
import org.bukkit.Material;

public class MCityDoor
{
	int id;
	boolean useCommandBlock;
	boolean justPlaceRed;
	Location redstoneBlockPlace;
	Location blockToTest;
	
	public MCityDoor(int p_id, int p_n)
	{
		this.id = p_id;
		this.useCommandBlock = true;
		this.justPlaceRed = true;
	}
	
	public void set(Location redstoneB, Location testB)
	{
		this.redstoneBlockPlace = redstoneB;
		this.blockToTest = testB;
	}
	
	public boolean canOpenDoor()
	{
		return true;
	}
	
	public boolean canCloseDoor()
	{
		return true;
	}
	
	public boolean isOpen()
	{
		return this.blockToTest.getWorld().getBlockAt(this.blockToTest).getType().equals(Material.AIR);
	}
	
	public void open()
	{
		if(this.isOpen() || !this.canOpenDoor())
		{
			return;
		}
		if(this.useCommandBlock)
		{
			this.redstoneBlockPlace.getWorld().getBlockAt(this.redstoneBlockPlace).setType(Material.REDSTONE_BLOCK);
		}
		if(this.justPlaceRed)
		{
			return;
		}
	}
	
	public void close()
	{
		if(!this.isOpen() || !this.canCloseDoor())
		{
			return;
		}
		if(this.useCommandBlock)
		{
			this.redstoneBlockPlace.getWorld().getBlockAt(this.redstoneBlockPlace).setType(Material.REDSTONE_BLOCK);
		}
		if(this.justPlaceRed)
		{
			return;
		}
	}
}