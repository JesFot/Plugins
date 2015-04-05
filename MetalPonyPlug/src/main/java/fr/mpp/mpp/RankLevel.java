package fr.mpp.mpp;

public enum RankLevel
{
	MAIN("main", 0),
	HAB("hab", 1),
	OLD("old", 2),
	STATUT("statut", 3),
	RIGHT("right", 4),
	MASK("mask", 5);
	
	private String name;
	private int ID;
	private static int howMany = 6;
	
	RankLevel(String name, int id)
	{
		this.ID = id;
		this.setName(name);
	}
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public int getID()
	{
		return this.ID;
	}
	public static int getHowMany()
	{
		return howMany;
	}
	public static RankLevel getRankByName(String name)
	{
		for (RankLevel c : RankLevel.values())
		{
			if (name.equalsIgnoreCase(c.getName()))
			{
				return c;
			}
		}
		return null;
	}
	public static RankLevel getRankByID(int id)
	{
		for (RankLevel c : RankLevel.values())
		{
			if (id == c.getID())
			{
				return c;
			}
		}
		return null;
	}
}