package me.jesfot.gamingblockplug.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerManager
{
	private Map<UUID, GBPPlayer> players = new HashMap<>();
	
	private boolean autoRS = false;
	
	public PlayerManager() { /* Nothing here */ }
	
	public void setAutoReloadSave(final boolean value)
	{
		this.setAutoReloadSave(value, false);
	}
	
	public void setAutoReloadSave(final boolean value, final boolean applyToAll)
	{
		boolean old = this.autoRS;
		this.autoRS = value;
		if (old != value && applyToAll)
		{
			this.players.values().forEach(new Consumer<GBPPlayer>() {
				@Override
				public void accept(GBPPlayer t)
				{
					t.setAutoReloadSave(value);
				}
			});
		}
	}
	
	public void registerPlayer(GBPPlayer player)
	{
		this.players.put(player.getUniqueId(), player);
	}
	
	public GBPPlayer registerBukkitPlayer(Player player)
	{
		GBPPlayer pl = new GBPPlayer(player, this.autoRS);
		this.registerPlayer(pl);
		return pl;
	}
	
	public GBPPlayer getOffline(OfflinePlayer player)
	{
		GBPPlayer pl = new GBPPlayer(player, this.autoRS);
		return pl;
	}
	
	public GBPPlayer getPlayer(UUID uid)
	{
		return this.players.get(uid);
	}
	
	public GBPPlayer getPlayer(OfflinePlayer player)
	{
		GBPPlayer pl = this.players.get(player.getUniqueId());
		if (pl == null && (player instanceof Player))
		{
			return this.registerBukkitPlayer((Player) player);
		}
		if (pl == null)
		{
			return this.getOffline(player);
		}
		return pl;
	}
	
	public void removePlayer(GBPPlayer player)
	{
		this.players.remove(player.getUniqueId());
	}
	
	public void removePlayer(UUID uid)
	{
		this.players.remove(uid);
	}
	
	public void removePlayer(OfflinePlayer player)
	{
		this.players.remove(player.getUniqueId());
	}
	
	public int getPlayerNb()
	{
		return this.players.size();
	}
}
