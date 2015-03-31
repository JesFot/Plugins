package fr.mpp.mpp.classes;

import java.util.List;

import org.bukkit.entity.Player;

import fr.mpp.mpp.IClasses;

public class CAssassin implements IClasses
{
	private String name;
	private String reason;
	private int level;

	public CAssassin()
	{
		this.level = 1;
	}
	
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
		// Code ...
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
	public void setLevel() {
		// TODO Auto-generated method stub
		
	}
}