package fr.mpp.mpp.classes;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.mpp.mpp.IClasses;

public class CNoobie implements IClasses
{
	private String name;
	private String reason;
	private int level;
	
	
	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String getReason()
	{
		return this.reason;
	}

	@Override
	public void setReason(String reason)
	{
		this.reason = reason;
	}

	@Override
	public List<Player> getPlayersConcern()
	{
		return null;
	}

	@Override
	public void setPlayerConcern(Player player)
	{
		// Code ...
	}

	@Override
	public void removeConcernPlayer(Player player)
	{
		// Code ...
	}

	@Override
	public int getLevel()
	{
		return this.level;
	}

	@Override
	public void setLevel(int level)
	{
		this.level = level;
	}

	@Override
	public Material getItem()
	{
		return null;
	}
	
	@Override
	public void addLevel(int level)
	{
		this.level += level;
	}

	@Override
	public void setItem(Material mat)
	{}

	@Override
	public void setLevel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDisplayName(String name) {
		// TODO Auto-generated method stub
		
	}
}