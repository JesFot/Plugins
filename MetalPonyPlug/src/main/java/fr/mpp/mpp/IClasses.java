package fr.mpp.mpp;

import java.util.List;

import org.bukkit.entity.Player;

/*
 * Interface for all Ranks
 */
public interface IClasses
{
	public String getName();
	public void setName(String name);

	public String getReason();
	public void setReason(String reason);

	public List<Player> getPlayersConcern();
	public void setPlayerConcern(Player player);
	public void removeConcernPlayer(Player player);
	
	public int getLevel();
	public void setLevel();
	void setLevel(int level);
}
