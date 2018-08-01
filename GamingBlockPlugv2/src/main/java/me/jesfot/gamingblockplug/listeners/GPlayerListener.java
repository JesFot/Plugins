package me.jesfot.gamingblockplug.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.jesfot.gamingblockplug.data.GBPPlayer;
import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;
import me.jesfot.gamingblockplug.roles.Role;

public class GPlayerListener implements Listener
{
	private final GamingBlockPlug plugin;
	
	public GPlayerListener(GamingBlockPlug plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		this.plugin.getPlayerManager().removePlayer(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerKick(final PlayerKickEvent event)
	{
		this.plugin.getPlayerManager().removePlayer(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerChat(final AsyncPlayerChatEvent event)
	{
		GBPPlayer player = this.plugin.getPlayerManager().getPlayer(event.getPlayer());
		Role role = this.plugin.getRoleManager().get(player.getRoleId());
		
		event.setFormat(role.prependFormat(event.getFormat()));
	}
}
