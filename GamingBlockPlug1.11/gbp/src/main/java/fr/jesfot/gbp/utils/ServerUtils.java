package fr.jesfot.gbp.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class ServerUtils
{
	private Server server;
	
	public ServerUtils(Server p_server)
	{
		this.server = p_server;
	}
	
	public void broad(String msg)
	{
		this.server.broadcast(msg, Server.BROADCAST_CHANNEL_USERS);
	}
	
	public boolean isOnlineMode()
	{
		return this.server.getOnlineMode();
	}
	
	public void broadAdmins(String msg)
	{
		this.server.broadcast(msg, Server.BROADCAST_CHANNEL_ADMINISTRATIVE);
	}
	
	public PluginManager getPluginManager()
	{
		return this.server.getPluginManager();
	}
	
	public Collection<? extends Player> getOnlinePlayers()
	{
		return this.server.getOnlinePlayers();
	}
	
	public List<OfflinePlayer> getOfflinePlayers()
	{
		return Arrays.asList(this.server.getOfflinePlayers());
	}
	
	@SuppressWarnings("deprecation")
	public Player getPlayerExact(String name)
	{
		return this.server.getPlayerExact(name);
	}
	
	@SuppressWarnings("deprecation")
	public List<Player> matchPlayer(String name)
	{
		return this.server.matchPlayer(name);
	}
	
	public OfflinePlayer getOfflinePlayerByName(String name)
	{
		OfflinePlayer pl = null;
		for(OfflinePlayer p : this.getOfflinePlayers())
		{
			if(p.getName().equalsIgnoreCase(name))
			{
				pl = p;
				break;
			}
		}
		return pl;
	}
	
	public Server getServer()
	{
		return this.server;
	}
}