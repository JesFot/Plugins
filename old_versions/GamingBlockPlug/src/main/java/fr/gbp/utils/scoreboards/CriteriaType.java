package fr.gbp.utils.scoreboards;

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
	
	@Override
	public String toString()
	{
		return this.name;
	}
}