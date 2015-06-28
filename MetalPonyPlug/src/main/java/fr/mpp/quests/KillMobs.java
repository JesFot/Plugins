package fr.mpp.quests;

import org.bukkit.entity.EntityType;

public class KillMobs extends Quest
{
	private int mobsNumber;
	private EntityType mob;
	
	public KillMobs()
	{
		this.setQuestType(QuestType.KILLMOB);
	}
	
	@SuppressWarnings("deprecation")
	public void setMob(EntityType p_mob)
	{
		if(p_mob.getTypeId()>=50 && p_mob.getTypeId()<= 68 && p_mob != EntityType.ENDER_DRAGON && p_mob != EntityType.WITHER)
		{
			this.mob = p_mob;
		}
	}
	
	public EntityType getMob()
	{
		return this.mob;
	}
	
	public void setHowManyMobs(int p_nb)
	{
		this.mobsNumber = p_nb;
	}
	
	public int getHowManyMobs(int p_nb)
	{
		return this.mobsNumber;
	}
	
	public void setName(String p_name)
	{
		super.setName("[KM]"+p_name);
	}
}