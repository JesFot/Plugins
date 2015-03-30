package fr.mpp.mpp;

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
}
