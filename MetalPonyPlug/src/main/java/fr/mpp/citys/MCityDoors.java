package fr.mpp.citys;

import java.util.ArrayList;
import java.util.List;

public class MCityDoors
{
	private List<MCityDoor> doors;
	
	public void generateDoors(int howMany)
	{
		this.doors = new ArrayList<MCityDoor>(howMany);
	}
	
	public MCityDoor getDoor(int id)
	{
		return this.doors.get(id);
	}
	
	public void setDoor(int id, MCityDoor p_door)
	{
		this.doors.set(id, p_door);
	}
}