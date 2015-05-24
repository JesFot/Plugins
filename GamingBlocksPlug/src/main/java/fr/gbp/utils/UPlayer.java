package fr.gbp.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class UPlayer
{
	public static Player getPlayerByName(String name)
	{
		Player pl = null;
		
		for (Player player : Bukkit.getOnlinePlayers())
		{
			if (player.getName().equalsIgnoreCase(name))
			{
				pl = player;
				break;
			}
			if (player.getDisplayName().equalsIgnoreCase(name))
			{
				pl = player;
				break;
			}
			if (player.getCustomName().equalsIgnoreCase(name))
			{
				pl = player;
				break;
			}
		}
		return pl;
	}
	public static Player getPlayerByNameOff(String name)
	{
		OfflinePlayer pl = null;
		
		for (OfflinePlayer player : Bukkit.getOfflinePlayers())
		{
			if (player.getName().equalsIgnoreCase(name))
			{
				pl = player;
				break;
			}
		}
		return (Player)pl;
	}
}
