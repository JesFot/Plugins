package fr.jesfot.gbp.teams;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.OfflinePlayer;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.configuration.NBTConfig;
import fr.jesfot.gbp.configuration.NBTSubConfig;

public class TeamManager
{
	public static final String DEFAULT_NAME = "default";
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
		for(String key : teamCfg.readNBTFromFile().getCopy().c())
		{
			GTeam team = new GTeam(this.gbp, key);
			team.reloadFromConfig();
			this.teamList.put(key, team);
		}
	}
	
	public Set<String> getTeamList()
	{
		return this.teamList.keySet();
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
			return this.getOrCreate(DEFAULT_NAME);
		}
		return this.teamList.get(id);
	}
	
	public GTeam getOrCreate(final String id)
	{
		GTeam team;
		if(!this.existsTeam(id))
		{
			team = new GTeam(this.gbp, id);
			this.create(team);
		}
		else
		{
			team = this.getIfExists(id);
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
	
	public boolean join(final String id, final OfflinePlayer player)
	{
		if(!this.existsTeam(id))
		{
			return false;
		}
		NBTSubConfig cfg = new NBTSubConfig(this.gbp.getConfigFolder("playerdatas"), player.getUniqueId());
		cfg.readNBTFromFile().setString("Team", id).writeNBTToFile();
		return true;
	}
	
	public void leave(final OfflinePlayer player)
	{
		NBTSubConfig cfg = new NBTSubConfig(this.gbp.getConfigFolder("playerdatas"), player.getUniqueId());
		cfg.readNBTFromFile().setString("Team", DEFAULT_NAME).writeNBTToFile();
	}
	
	public GTeam getTeamOf(final OfflinePlayer player)
	{
		NBTSubConfig cfg = new NBTSubConfig(this.gbp.getConfigFolder("playerdatas"), player.getUniqueId());
		String team = cfg.readNBTFromFile().getCopy().getString("Team");
		GTeam result = this.getIfExists(team);
		return result;
	}
}