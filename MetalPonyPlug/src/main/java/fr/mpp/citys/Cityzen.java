package fr.mpp.citys;

import org.bukkit.entity.Player;

public class Cityzen
{
	protected Player me;
	protected String myName;
	
	public Cityzen()
	{
		//
	}
	
	public Cityzen(Player p_player)
	{
		this.me = p_player;
		this.myName = p_player.getName();
	}
}