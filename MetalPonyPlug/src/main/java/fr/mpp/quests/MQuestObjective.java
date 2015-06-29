package fr.mpp.quests;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class MQuestObjective
{
	protected final String playerID;
	
	protected final String instructions;
	
	protected final String conditions;
	
	protected final String events;
	
	protected final String tag;
	
	public MQuestObjective(String p_playerID, String p_instructions)
	{
		this.playerID = p_playerID;
		this.instructions = p_instructions;
		String tempTag = "";
		String tempEvents = "";
		String tempConditions = "";
		
		for(String part : p_instructions.split(" "))
		{
			if(part.contains("label:"))
			{
				tempTag = part.substring(6);
			}
			if(part.contains("events:"))
			{
				tempEvents = part;
			}
			if(part.contains("conditions:"))
			{
				tempConditions = part;
			}
		}
		this.tag = tempTag;
		this.events = tempEvents;
		this.conditions = tempConditions;
	}
	
	protected final void completeObjective()
	{
		String[] parts = this.instructions.split(" ");
		String rawEvents = null;
		
		for(String part : parts)
		{
			if(part.contains("events:"))
			{
				rawEvents = part.substring(7);
				break;
			}
		}
		
		if(rawEvents != null && !rawEvents.equalsIgnoreCase(""))
		{
			final String[] events = rawEvents.split(",");
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					for(String eventID : events)
					{
						if(!eventID.contains("."))
						{
							//
							continue;
						}
						//
					}
				}
			}.runTask(null);
		}
	}
}