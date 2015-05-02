package fr.mpp.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class MScoreBoard
{
	private final ScoreboardManager manager;
	
	public MScoreBoard()
	{
		this.manager = Bukkit.getScoreboardManager();
	}
	
	public Scoreboard getMainScoreboard()
	{
		return manager.getMainScoreboard();
	}
	
	public Scoreboard getBlankScoreBoard()
	{
		return manager.getNewScoreboard();
	}
	
	public Team createTeam(Scoreboard board, String name)
	{
		return board.registerNewTeam(name);
	}
	
	public Team createTeam(String name)
	{
		return getBlankScoreBoard().registerNewTeam(name);
	}
	
	public Objective createObjective(Scoreboard board, String name, CriteriaType type)
	{
		return board.registerNewObjective(name, type.toString());
	}
	
	public Objective createObjective(String name, CriteriaType type)
	{
		return getBlankScoreBoard().registerNewObjective(name, type.toString());
	}
	
	public Score getScore(Objective objective, String player)
	{
		return objective.getScore(player);
	}
	
	public Score getScore(Scoreboard board, String name, CriteriaType type, String player)
	{
		return createObjective(board, name, type).getScore(player);
	}
	
	public Score getScore(String name, CriteriaType type, String player)
	{
		return createObjective(name, type).getScore(player);
	}
	
	public void setScoreBoard(Player player, Scoreboard board)
	{
		player.setScoreboard(board);
	}
	
	public void removeScoreBoard(Player player)
	{
		player.setScoreboard(getBlankScoreBoard());
	}
	
	public void resetScores(Scoreboard board, String player)
	{
		board.resetScores(player);
	}
	
	public void setScore(Objective p_objective, String player, int score)
	{
		getScore(p_objective, player).setScore(score);
	}
	
	public void setObjectiveOptions(Objective objective, DisplaySlot slot, String dspName)
	{
		objective.setDisplayName(dspName);
		objective.setDisplaySlot(slot);
	}
	
	public void setTeamOptions(Team team, String prefix, String suffix, String dspName)
	{
		team.setPrefix(prefix);
		team.setSuffix(suffix);
		team.setDisplayName(dspName);
	}
	
	public void setTeamOptions(Team team, String prefix, String suffix, String dspName, boolean friendInv, boolean friendFire)
	{
		team.setPrefix(prefix);
		team.setSuffix(suffix);
		team.setDisplayName(dspName);
		team.setAllowFriendlyFire(friendFire);
		team.setCanSeeFriendlyInvisibles(friendInv);
	}
	
	public void setTeamOptions(Team team, boolean friendInv, boolean friendFire)
	{
		team.setAllowFriendlyFire(friendFire);
		team.setCanSeeFriendlyInvisibles(friendInv);
	}
}