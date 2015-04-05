package fr.mpp.mpp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.mpp.MetalPonyPlug;

public class MHalfBedSys
{
	private MetalPonyPlug mpp;
	private List<Player> playersInBed;
	private Player[] playersB;
	private List<Player> players;
	private int playersInBedInt;
	private int totalPlayers;
	
	public MHalfBedSys(MetalPonyPlug mppl)
	{
		this.mpp = mppl;
		this.playersInBed = new ArrayList<Player>();
		this.playersB = Bukkit.getOnlinePlayers();
		this.players = Arrays.asList(playersB);
		this.playersInBedInt = playersInBed.size();
		this.totalPlayers = players.size();
	}
	
	public List<Player> getPlayersInBed()
	{
		return this.playersInBed;
	}
	public Player[] getPlayersInBedA()
	{
		return (Player[])this.playersInBed.toArray();
	}
	public List<Player> getPlayers()
	{
		return this.players;
	}
	public Player[] getPlayersA()
	{
		return (Player[])this.players.toArray();
	}
	public void updatePlayers()
	{
		this.playersB = Bukkit.getOnlinePlayers();
		this.players = Arrays.asList(this.playersB);
		this.totalPlayers = this.players.size();
	}
	public void setPlayersInBed(List<Player> players)
	{
		this.playersInBed = players;
		this.playersInBedInt = this.playersInBed.size();
	}
	public void setPlayers(Player[] players)
	{
		this.playersB = players;
		this.players = Arrays.asList(players);
		this.totalPlayers = this.players.size();
	}
	public void addPlayerInBed(Player player)
	{
		this.playersInBed.add(player);
		this.playersInBedInt = this.playersInBed.size();
	}
	public void addPlayer(Player player)
	{
		this.players.add(player);
		this.playersB = (Player[])this.players.toArray();
		this.totalPlayers = this.players.size();
	}
	public boolean removePlayerInBed(Player player)
	{
		for (Player pl : this.playersInBed)
		{
			if (pl.getName() == player.getName())
			{
				this.playersInBed.remove(pl);
				this.playersInBedInt = this.playersInBed.size();
				return true;
			}
		}
		return false;
	}
	public boolean removePlayer(Player player)
	{
		for (Player pl : this.players)
		{
			if (pl.getName() == player.getName())
			{
				this.players.remove(pl);
				this.playersB = (Player[])this.players.toArray();
				this.totalPlayers = this.players.size();
				return true;
			}
		}
		return false;
	}
	
	public int getPlayersInBedInt()
	{
		return this.playersInBedInt;
	}
	public int getOnlinesPlayers()
	{
		return this.totalPlayers;
	}
	
	public boolean hasHalfInBed()
	{
		if ((this.totalPlayers/2) <= this.playersInBedInt)
		{
			return true;
		}
		return false;
	}
	
	public String howManyInBedText()
	{
		if (this.playersInBedInt <= 1)
		{
			if (this.playersInBedInt == 1)
			{
				return "There is [1/" + this.totalPlayers + "] player in his bed.";
			}
			return "There is no player in bed.";
		}
		return "There are [" + this.playersInBedInt + "/" + this.totalPlayers + "] players in beds.";
	}
	
	public void passNight(final List<Player> ignored)
	{
		this.updatePlayers();
		for (Player p : this.getPlayers())
		{
			if (ignored.contains(p))
			{
				continue;
			}
			else
			{
				p.setSleepingIgnored(true);
			}
			if (p.getWorld().getTime() <= 2)
			{
				this.mpp.broad("La nuit est terminée !");
				this.endPassN();
			}
		}
	}
	public void endPassN()
	{
		if (this.hasHalfInBed())
		{
			return;
		}
		for (Player p : this.getPlayers())
		{
			p.setSleepingIgnored(false);
		}
	}
}