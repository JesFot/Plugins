package fr.gbp.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.gbp.GamingBlockPlug;

public class GHalfBedSys
{
	private GamingBlockPlug gbp;
	private List<Player> playersInBed;
	private Player[] playersB;
	private List<Player> players;
	private int playersInBedInt;
	private int totalPlayers;
	
	public GHalfBedSys(GamingBlockPlug p_gbp)
	{
		this.gbp = p_gbp;
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
	
	public GHalfBedSys updatePlayers()
	{
		this.playersB = Bukkit.getOnlinePlayers();
		this.players = Arrays.asList(this.playersB);
		this.totalPlayers = this.players.size();
		return this;
	}
	
	public GHalfBedSys setPlayersInBed(List<Player> p_players)
	{
		this.playersInBed = p_players;
		this.playersInBedInt = this.playersInBed.size();
		return this;
	}
	
	public GHalfBedSys setPlayers(Player[] p_players)
	{
		this.playersB = p_players;
		this.players = Arrays.asList(this.playersB);
		this.totalPlayers = this.players.size();
		return this;
	}
	
	public GHalfBedSys addPlayerInBed(Player player)
	{
		this.playersInBed.add(player);
		this.playersInBedInt = this.playersInBed.size();
		return this;
	}
	
	public GHalfBedSys removePlayerInBed(Player player)
	{
		for(Player pl : this.playersInBed)
		{
			if(pl.getUniqueId() == player.getUniqueId())
			{
				this.playersInBed.remove(pl);
				this.playersInBedInt = this.playersInBed.size();
			}
		}
		return this;
	}
	
	public GHalfBedSys removePlayer(Player player)
	{
		for(Player pl : this.players)
		{
			if(pl.getUniqueId() == player.getUniqueId())
			{
				this.players.remove(pl);
				this.playersB = this.getPlayersA();
				this.totalPlayers = this.players.size();
			}
		}
		return this;
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
		if(((double)this.totalPlayers / 2) <= (double)this.playersInBedInt)
		{
			return true;
		}
		return false;
	}
	
	public String howManyInBedText()
	{
		if(this.playersInBedInt <= 1)
		{
			if(this.playersInBedInt == 1)
			{
				return this.gbp.getLang().get("hibs.onep", "There is [1/<totalplayer>] player in a bed.").replace("<totalplayer>", "" + this.totalPlayers);
			}
			return this.gbp.getLang().get("hibs.nop", "There is no player in bed.");
		}
		return this.gbp.getLang().get("hibs.pp", "There are [<playersinbed>/<totalplayer>] players in beds.").replace("<totalplayer>", ""+this.totalPlayers)
				.replace("<playersinbed>", ""+this.playersInBedInt);
	}
	
	public void passNight(final List<Player> ignored)
	{
		this.updatePlayers();
		for(Player p : this.getPlayers())
		{
			if(ignored.contains(p))
			{
				continue;
			}
			else
			{
				p.setSleepingIgnored(true);
			}
			if(p.getWorld().getTime() <= 2)
			{
				this.gbp.broad(this.gbp.getLang().get("hibs.np", "Night passed !"));
				this.endPassNight();
			}
		}
	}
	
	public void endPassNight()
	{
		if(this.hasHalfInBed())
		{
			return;
		}
		for(Player p : this.getPlayers())
		{
			p.setSleepingIgnored(false);
		}
	}
}