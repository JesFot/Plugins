package me.jesfot.gamingblockplug.security;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import me.jesfot.gamingblockplug.permission.PermissionHelper;
import me.jesfot.gamingblockplug.permission.StaticPerms;
import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;

public class HalfInBedSystem
{
	private final GamingBlockPlug plugin;
	
	private Set<Player> playersInBed;
	private Set<Player> totalPlayers;
	
	private int playersTotalCount;
	private int playersInBedCount;
	
	public HalfInBedSystem(GamingBlockPlug plugin)
	{
		this.plugin = plugin;
		this.playersInBed = new HashSet<>();
		this.totalPlayers = new HashSet<>();
		this.playersTotalCount = 0;
		this.playersInBedCount = 0;
	}
	
	public HalfInBedSystem updatePlayerList()
	{
		Collection<? extends Player> onlines = this.plugin.getServer().getOnlinePlayers();
		for (Player player : onlines)
		{
			if (!PermissionHelper.testPermissionSilent(player, StaticPerms.HBS_IGNORE, false))
			{
				this.totalPlayers.add(player);
			}
			else
			{
				player.setSleepingIgnored(true);
			}
		}
		this.playersTotalCount = this.totalPlayers.size();
		return this;
	}
	
	public HalfInBedSystem enterBed(Player player)
	{
		if (PermissionHelper.testPermissionSilent(player, StaticPerms.HBS_COUNT, false))
		{
			this.playersInBed.add(player);
			this.playersInBedCount = this.playersInBed.size();
		}
		return this;
	}
	
	public HalfInBedSystem leaveBed(Player player)
	{
		this.playersInBed.remove(player);
		this.playersInBedCount = this.playersInBed.size();
		return this;
	}
	
	public Set<Player> getPlayersInBed()
	{
		return Collections.unmodifiableSet(this.playersInBed);
	}
	
	public boolean halfInBed()
	{
		if((((double) this.playersTotalCount) / 2.0) <= ((double) this.playersInBedCount))
		{
			return true;
		}
		return false;
	}
	
	public String howManyInBedText()
	{
		if(this.playersInBedCount <= 1)
		{
			if(this.playersInBedCount == 1)
			{
				return "There is [1/<totalplayer>] player in a bed.".replace("<totalplayer>", Integer.toString(this.playersTotalCount));
			}
			return "There is no player in bed.";
		}
		return "There are [<playersinbed>/<totalplayer>] players in beds.".replace("<totalplayer>", Integer.toString(this.playersTotalCount))
				.replace("<playersinbed>", Integer.toString(this.playersInBedCount));
	}
	
	public void passNight(final Collection<? extends Player> ignored)
	{
		this.updatePlayerList();
		for(Player p : this.totalPlayers)
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
				this.plugin.broad("Night passed !");
				this.endPassNight();
			}
		}
	}
	

	public void endPassNight()
	{
		if(this.halfInBed())
		{
			return;
		}
		for(Player p : this.totalPlayers)
		{
			p.setSleepingIgnored(false);
		}
	}
}
