package fr.jesfot.gbp.teams;

import java.util.HashMap;
import java.util.Map;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.configuration.NBTConfig;

public class TeamManager
{
	private Map<String, GTeam> teamList;
	private GamingBlockPlug_1_9 gbp;
	
	public TeamManager(GamingBlockPlug_1_9 plugin)
	{
		this.gbp = plugin;
		this.teamList = new HashMap<String, GTeam>();
	}
	
	public void reloadTeams()
	{
		this.teamList.clear();
		NBTConfig teamCfg = new NBTConfig(this.gbp.getConfigFolder("teams"), "TeamsData");
		for(String key : teamCfg.getCopy().c())
		{
			GTeam team = new GTeam(this.gbp, key);
			team.reloadFromConfig();
			this.teamList.put(key, team);
		}
	}
	
	public void saveAll()
	{
		for(GTeam team : this.teamList.values())
		{
			team.saveToConfig();
		}
	}
	
	public boolean existsTeam(final String id)
	{
		return this.teamList.containsKey(id);
	}
	
	public void create(GTeam team)
	{
		team.create();
		this.teamList.put(team.getId(), team);
	}
	
	public GTeam getIfExists(final String id)
	{
		if(!this.existsTeam(id))
		{
			return this.getOrCreate("default");
		}
		return this.teamList.get(id);
	}
	
	public GTeam getOrCreate(final String id)
	{
		GTeam team = this.getIfExists(id);
		if(!this.existsTeam(id))
		{
			team = new GTeam(this.gbp, id);
			this.create(team);
		}
		return team;
	}
	
	public void delete(final String id)
	{
		GTeam team = this.getIfExists(id);
		if(!this.existsTeam(id))
		{
			return;
		}
		team.delete();
		this.teamList.remove(id);
	}
}