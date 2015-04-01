package fr.mpp.mpp;

import org.bukkit.Material;

/*
 * Interface for all Ranks
 */
public interface IClasses
{
	public String getName();
	public void setName(String name);
	public String getDisplayName();
	public void setDisplayName(String name);
	
	public Material getItem();
	public void setItem(Material mat);

	public String getReason();
	public void setReason(String reason);

	public boolean hasNextRank();
	public Classes getNextRank();
	public void setNextRank(Classes rank);
	
	public int getLevel();
	public void addLevel(int level);
	public int incrLevel();
	public void setLevel(int level);
}