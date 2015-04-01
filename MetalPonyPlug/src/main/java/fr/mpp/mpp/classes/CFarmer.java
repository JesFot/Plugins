package fr.mpp.mpp.classes;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.mpp.mpp.IClasses;

public class CFarmer implements IClasses
{
	private String name;
	private String reason;
	private String displayName;
	private int level;
	private Material material;
	
	public CFarmer()
	{
		this.level = 1;
		this.material = Material.WHEAT;
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
		this.displayName = ChatColor.YELLOW + "[" + this.name + "]" + ChatColor.RESET;
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
	public void setLevel()
	{
		this.level++;
	}

	@Override
	public void setLevel(int level)
	{
		this.level = level;
	}

	@Override
	public void addLevel(int level)
	{
		this.level += level;
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