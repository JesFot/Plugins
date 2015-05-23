package fr.gbp.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.gbp.RefString;

public class CrashListener implements Listener
{
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		event.getPlayer().sendMessage(RefString.NAME + " failed to load.");
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerCommand(final PlayerCommandPreprocessEvent event)
	{
		if (event.getMessage().equalsIgnoreCase("gamingblocksplug") || event.getMessage().equalsIgnoreCase("/gamingblocksplug"))
		{
			event.getPlayer().sendMessage(RefString.NAME + " failed to load.");
			event.setCancelled(true);
		}
	}
}