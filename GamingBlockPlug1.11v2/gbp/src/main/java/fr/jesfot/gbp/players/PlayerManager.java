package fr.jesfot.gbp.players;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import fr.jesfot.gbp.GamingBlockPlug_1_11;

public class PlayerManager
{
	private final GamingBlockPlug_1_11 gbp;
	private final Map<UUID, GBPPlayer> playerList;
	
	public PlayerManager(GamingBlockPlug_1_11 plugin)
	{
		this.gbp = plugin;
		this.playerList = new HashMap<UUID, GBPPlayer>();
	}
	
	public GBPPlayer getPlayer(UUID uid)
	{
		return this.playerList.get(uid);
	}
	
	public GBPPlayer getPlayer(Player player)
	{
		return this.playerList.get(player.getUniqueId());
	}
	
	public GBPPlayer addPlayer(Player player)
	{
		if(this.playerList.containsKey(player.getUniqueId()))
		{
			return this.getPlayer(player);
		}
		GBPPlayer to_add = new GBPPlayer(player);
		to_add.setConfigFolder(this.gbp.getPlayerDataFolder());
		this.playerList.put(player.getUniqueId(), to_add);
		return to_add;
	}
	
	public GBPPlayer login(Player player)
	{
		GBPPlayer sel = this.addPlayer(player);
		sel.load();
		sel.setLogState(GBPPlayer.LogState.Identifying);
		return sel;
	}
	
	public GBPPlayer join(Player player)
	{
		GBPPlayer sel = this.addPlayer(player);
		sel.setLogState(GBPPlayer.LogState.Joining);
		return sel;
	}
	
	public GBPPlayer removePlayer(UUID uid)
	{
		return this.playerList.remove(uid);
	}
	
	public GBPPlayer removePlayer(Player player)
	{
		return this.playerList.remove(player.getUniqueId());
	}
	
	public GBPPlayer logout(Player player)
	{
		GBPPlayer sel = this.getPlayer(player);
		if(sel == null)
		{
			return null;
		}
		sel.save();
		sel.setLogState(GBPPlayer.LogState.OffLine);
		return sel;
	}
	
	public void addAll(Collection<? extends Player> collection, GBPPlayer.LogState state)
	{
		for(Player pl : collection)
		{
			this.addPlayer(pl).setLogState(state);
		}
	}
	
	public void loadAll()
	{
		for(GBPPlayer pl : this.playerList.values())
		{
			pl.load();
		}
	}
	
	public void saveAll()
	{
		for(GBPPlayer pl : this.playerList.values())
		{
			pl.save();
		}
	}
	
	public int size()
	{
		return this.playerList.size();
	}
	
	public Map<UUID, GBPPlayer> getPlayerList()
	{
		return this.playerList;
	}
}
