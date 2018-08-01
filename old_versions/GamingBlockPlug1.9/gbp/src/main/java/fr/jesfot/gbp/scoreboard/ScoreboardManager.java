package fr.jesfot.gbp.scoreboard;

import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import fr.jesfot.gbp.GamingBlockPlug_1_9;

public class ScoreboardManager
{
	GamingBlockPlug_1_9 gbp;
	
	public ScoreboardManager(GamingBlockPlug_1_9 plugin)
	{
		this.gbp = plugin;
	}
	
	public Scoreboard getMainScoreboard()
	{
		return this.gbp.getServer().getScoreboardManager().getMainScoreboard();
	}
	
	public Objective registerObjective(String name, String displayName)
	{
		try
		{
			Objective o = this.getMainScoreboard().registerNewObjective(name, "dummy");
			if(displayName != null)
			{
				o.setDisplayName(displayName);
			}
			return o;
		}
		catch(IllegalArgumentException e)
		{
			return null;
		}
	}
	
	public Objective getObjective(String name)
	{
		return this.getMainScoreboard().getObjective(name);
	}
	
	public static void setDisplay(Objective objective, DisplaySlot slot)
	{
		if(objective == null)
		{
			return;
		}
		objective.setDisplaySlot(slot);
	}
	
	public Score setScore(Objective objective, OfflinePlayer player, Integer score)
	{
		if(objective == null)
		{
			return null;
		}
		Score _score = objective.getScore(player.getName());
		if(score != null)
		{
			_score.setScore(score.intValue());
		}
		return _score;
	}
	
	public void resetScore(Objective objective, OfflinePlayer player)
	{
		if(objective == null)
		{
			return;
		}
		objective.getScoreboard().resetScores(player.getName());
	}
}