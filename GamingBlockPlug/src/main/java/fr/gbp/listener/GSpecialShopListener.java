package fr.gbp.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.gbp.GamingBlockPlug;

public class GSpecialShopListener implements Listener
{
	private GamingBlockPlug gbp;
	
	public GSpecialShopListener(GamingBlockPlug plugin)
	{
		this.gbp = plugin;
	}
	
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent event)
	{
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			if(event.getClickedBlock().getType().equals(Material.SIGN_POST) || event.getClickedBlock().getType().equals(Material.WALL_SIGN))
			{
				//
			}
		}
	}
}