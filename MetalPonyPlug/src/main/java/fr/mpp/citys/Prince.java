package fr.mpp.citys;

import org.bukkit.entity.Player;

import fr.mpp.utils.MPlayer;

public class Prince extends Cityzen
{
	public Prince()
	{
		super();
	}
	
	public Prince(Player p_player)
	{
		super(p_player);
	}
	
	public Prince(String p_playerName)
	{
		super(MPlayer.getPlayerByName(p_playerName));
	}
}