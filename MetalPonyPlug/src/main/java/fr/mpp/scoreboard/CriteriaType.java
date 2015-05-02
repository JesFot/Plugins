package fr.mpp.scoreboard;

public enum CriteriaType
{
	Dummy("dummy"),
	Health("health"),
	Deaths("deathCount"),
	PlayerKills("playerKillCount"),
	TotalKills("totalKillCount");
	
	private String name = "";
	
	CriteriaType(final String p_name)
	{
		this.name = p_name;
	}
	
	public String toString()
	{
		return this.name;
	}
}