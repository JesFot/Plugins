package fr.mpp.mpp.classes;

import org.bukkit.Material;
import org.bukkit.permissions.Permission;

import fr.mpp.mpp.CClasses;
import fr.mpp.mpp.Classes;
import fr.mpp.mpp.IClasses;

public class CChevalier extends CClasses implements IClasses
{
	private String name;
	private String displayName;
	private int maxLevel;
	private Material material;
	private Classes nextRank;
	private boolean privilege;
	private Permission perm;
	
	public CChevalier()
	{
		this.maxLevel = 1;
		this.material = null;
		this.nextRank = Classes.Null;
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
	public int getMaxLevel()
	{
		return this.maxLevel;
	}

	@Override
	public void setMaxLevel(int level)
	{
		this.maxLevel = level;
	}

	@Override
	public void addMaxLevel(int level)
	{
		this.maxLevel += level;
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

	@Override
	public boolean hasPrivilege()
	{
		return this.privilege;
	}

	@Override
	public void setPrivilege(boolean privi)
	{
		this.privilege = privi;
	}

	@Override
	public Permission getAttach()
	{
		return this.perm;
	}

	@Override
	public void setAttach(Permission attach)
	{
		this.perm = attach;
	}
}