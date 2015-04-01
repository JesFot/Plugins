package fr.mpp.mpp.classes;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import fr.mpp.mpp.Classes;
import fr.mpp.mpp.IClasses;

public class CRedstoner implements IClasses
{
	private String name;
	private String reason;
	private String displayName;
	private int level;
	private Material material;
	private Classes nextRank;
	
	public CRedstoner()
	{
		this.level = 1;
		this.material = Material.REDSTONE;
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
		this.displayName = ChatColor.RED + "[" + this.name + "]" + ChatColor.RESET;
	}
	
	@Override
	public String getDisplayName()
	{
		return this.displayName;
	}
	
	@Override
	public void setDisplayName(String name)
	{
		this.displayName = name;
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
	public boolean hasNextRank()
	{
		if (this.nextRank != Classes.Null)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public Classes getNextRank()
	{
		return this.nextRank;
	}
	
	@Override
	public void setNextRank(Classes rank)
	{
		this.nextRank = rank;
	}

	@Override
	public int getLevel()
	{
		return this.level;
	}
	
	@Override
	public void addLevel(int level)
	{
		this.level += level;
	}
	
	@Override
	public int incrLevel()
	{
		this.level += 1;
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
		return this.material;
	}

	@Override
	public void setItem(Material mat)
	{
		this.material = mat;
	}
}