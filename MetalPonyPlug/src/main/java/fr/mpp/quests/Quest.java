package fr.mpp.quests;

public abstract class Quest
{
	private String name;
	private String shortDescription;
	private String description;
	
	private int level;
	private int requireLevelMin;
	
	private QuestType type = QuestType.NULL;
	
	public void setQuestType(QuestType p_type)
	{
		if(type != QuestType.NULL && p_type != QuestType.NULL)
		{
			return;
		}
		this.type = p_type;
	}
	
	public QuestType setQuestType()
	{
		return this.type;
	}
	
	public String getName()
	{
		return this.name;
	}
	public String getShortDesc()
	{
		return this.shortDescription;
	}
	public String getDesc()
	{
		return this.description;
	}
	public int getLevel()
	{
		return this.level;
	}
	public int getMinPlayerLevel()
	{
		return this.requireLevelMin;
	}
	
	public void setName(String p_name)
	{
		this.name = p_name;
	}
	public void setShortDesc(String p_desc)
	{
		this.shortDescription = p_desc;
	}
	public void setDesc(String p_desc)
	{
		this.description = p_desc;
	}
	public void setLevel(int p_lvl)
	{
		this.level = p_lvl;
	}
	public void setMinPlayerLevel(int p_lvl)
	{
		this.requireLevelMin = p_lvl;
	}
}