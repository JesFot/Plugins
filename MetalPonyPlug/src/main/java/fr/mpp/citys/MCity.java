package fr.mpp.citys;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class MCity
{
	private String name;
	private List<Cityzen> cityzens;
	private Cityzen mayor;
	private Prince thePrince;
	private Prince thePrincesse;
	public MCityDoors doors;
	
	public MCity(final String p_name, World w)
	{
		this.name = p_name;
		this.doors = new MCityDoors();
		this.doors.generateDoors(4);
		MCityDoor barrage = new MCityDoor(0, 0);
		barrage.set(new Location(w, 623, 29, -159), new Location(w, 657, 50, -166));
		this.doors.setDoor(0, barrage);
	}
	
	public int addCityzen(Player p_player)
	{
		if(this.cityzens.contains(new Cityzen(p_player)))
		{
			return this.cityzens.indexOf(new Cityzen(p_player));
		}
		this.cityzens.add(new Cityzen(p_player));
		return this.cityzens.indexOf(new Cityzen(p_player));
	}
	
	public void setCityzen(int id, Cityzen p_player)
	{
		this.cityzens.set(id, p_player);
	}
	
	public Cityzen getCityzen(int id)
	{
		try
		{
			return this.cityzens.get(id);
		}
		catch(IndexOutOfBoundsException ex)
		{
			return null;
		}
	}
	
	public void setCityzens(List<Cityzen> all)
	{
		this.cityzens = all;
	}
	
	public List<Cityzen> getAllCityzens()
	{
		return this.cityzens;
	}
	
	public void setCityName(final String p_name)
	{
		this.name = p_name;
	}
	
	public String getCityName()
	{
		return this.name;
	}
	
	public Cityzen getMayor()
	{
		return this.mayor;
	}
	
	public void setMayor(Cityzen p_mayor)
	{
		this.mayor = p_mayor;
	}
	
	public void setMayor(int id)
	{
		this.mayor = this.cityzens.get(id);
	}
	
	public Prince getPrince()
	{
		return this.thePrince;
	}
	
	public Prince getPrincesse()
	{
		return this.thePrincesse;
	}
	
	public void setPrince(Prince newPrince)
	{
		this.thePrince = newPrince;
	}
	
	public void setPrincesse(Prince newPrincesse)
	{
		this.thePrincesse = newPrincesse;
	}
	
	public void setPrinces(Prince p_prince, Prince p_princesse)
	{
		this.thePrince = p_prince;
		this.thePrincesse = p_princesse;
	}
}