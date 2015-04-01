package fr.mpp.mpp;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

/*
 * Interface for all Ranks
 */
public interface IClasses
{
	public String getName();
	public void setName(String name);
	
	public Material getItem();
	public void setItem(Material mat);

	public String getReason();
	public void setReason(String reason);

	public List<Player> getPlayersConcern();
	public void setPlayerConcern(Player player);
	public void removeConcernPlayer(Player player);
	
	public int getLevel();
	public void addLevel(int level);
	public void setLevel(int level);
	public void setLevel(); //to remove
}