package fr.mpp.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.mpp.MetalPonyPlug;
import fr.mpp.config.MConfig;

@SuppressWarnings("unused")
public class MppChestComListener implements Listener
{
	private Player pl;
	private MetalPonyPlug mpp;
	private MConfig conf;
	
	public MppChestComListener(Player pl, MetalPonyPlug mpp)
	{
		this.mpp = mpp;
		this.conf = this.mpp.getConfig();
		this.pl = pl;
	}
	
	@EventHandler
	public void onPlayerClickBlock(final PlayerInteractEvent event)
	{
		//event.getPlayer().sendMessage("[DEBUG] lolololololololol");
		Action action = event.getAction();
		Action leftClickBlock = Action.LEFT_CLICK_BLOCK;
		if (event.getPlayer() == this.pl)
		{
			//event.getPlayer().sendMessage("[DEBUG] lolilolololololol");
			if (action == leftClickBlock)
			{
				//event.getPlayer().sendMessage("[DEBUG] lolilolilolololol : " + event.getClickedBlock());
				Block block = event.getClickedBlock();
				if (block.getType().equals(Material.CHEST))
				{
					//Chest chest = (Chest)block;
					Location loc = block.getLocation();
					this.conf.storeLoc("mpp.origchest.location", loc);
					event.getPlayer().sendMessage("OK !");
					event.setCancelled(true);
					PlayerInteractEvent.getHandlerList().unregister(this);
				}
			}
		}
	}
}