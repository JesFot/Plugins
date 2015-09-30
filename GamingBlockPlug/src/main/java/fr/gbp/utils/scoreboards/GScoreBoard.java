package fr.gbp.utils.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class GScoreBoard
{
	private final ScoreboardManager manager;
	
	public GScoreBoard()
	{
		this.manager = Bukkit.getScoreboardManager();
	}
	
	public static GScoreBoard getInstance()
	{
		return new GScoreBoard();
	}
	
	public Scoreboard getMainScoreboard()
	{
		return manager.getMainScoreboard();
	}
	
	public Scoreboard getBlankScoreboard()
	{
		return manager.getNewScoreboard();
	}
	
	public Team createTeam(String name)
	{
		return this.getMainScoreboard().registerNewTeam(name);
	}
	
	public Objective createObjective(String name, CriteriaType type)
	{
		return this.getMainScoreboard().registerNewObjective(name, type.toString());
	}
	
	public void removeScoreBoard(Player player)
	{
		player.setScoreboard(getMainScoreboard());
	}
	
	public boolean objectiveExists(String name)
	{
		for(Objective obj : this.manager.getMainScoreboard().getObjectives())
		{
			if(obj.getName().equals(name))
			{
				return true;
			}
		}
		return false;
	}
	
	//
	public static Team createTeam(Scoreboard board, String name)
	{
		return board.registerNewTeam(name);
	}
	
	public static Objective createObjective(Scoreboard board, String name, CriteriaType type)
	{
		return board.registerNewObjective(name, type.toString());
	}
	
	public static Score getScore(Objective objective, String player)
	{
		return objective.getScore(player);
	}
	
	public static void setScoreBoard(Player player, Scoreboard board)
	{
		player.setScoreboard(board);
	}
	
	public static void removeScoreBoard(Player player, ScoreboardManager p_manager)
	{
		player.setScoreboard(getMainScoreboard(p_manager));
	}
	
	public static void resetScore(Scoreboard board, String player)
	{
		board.resetScores(player);
	}
	
	public static void setScore(Objective p_objective, String player, int score)
	{
		GScoreBoard.getScore(p_objective, player).setScore(score);
	}
	
	public static void setObjectiveOptions(Objective objective, DisplaySlot slot, String dspName)
	{
		objective.setDisplaySlot(slot);
		objective.setDisplayName(dspName);
	}
	
	public static void setTeamOptions(Team team, String prefix, String suffix, String dspName)
	{
		team.setPrefix(prefix);
		team.setSuffix(suffix);
		team.setDisplayName(dspName);
	}
	
	public static void setTeamOptions(Team team, boolean friendInv, boolean friendFire)
	{
		team.setAllowFriendlyFire(friendFire);
		team.setCanSeeFriendlyInvisibles(friendInv);
	}
	
	public static void setTeamOptions(Team team, String prefix, String suffix, String dspName, boolean friendInv, boolean friendFire)
	{
		GScoreBoard.setTeamOptions(team, prefix, suffix, dspName);
		GScoreBoard.setTeamOptions(team, friendInv, friendFire);
	}
	//
	
	public static Scoreboard getMainScoreboard(ScoreboardManager p_manager)
	{
		return p_manager.getMainScoreboard();
	}
	
	public static Scoreboard getBlankScoreboard(ScoreboardManager p_manager)
	{
		return p_manager.getNewScoreboard();
	}
}