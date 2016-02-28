package fr.gbp.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.gbp.GamingBlockPlug;
import fr.gbp.utils.UPlayer;

public class GSpecialShopListener implements Listener
{
	private GamingBlockPlug gbp;
	private final int mode;
	private final String playerName;
	
	public GSpecialShopListener(GamingBlockPlug plugin, int p_mode, String player)
	{
		this.gbp = plugin;
		this.mode = p_mode;
		this.playerName = player;
	}
	
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent event)
	{
		if(this.playerName != event.getPlayer().getName())
		{
			return;
		}
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			if(event.getClickedBlock().getType().equals(Material.SIGN_POST) || event.getClickedBlock().getType().equals(Material.WALL_SIGN))
			{
				Sign sign = (Sign)event.getClickedBlock().getState();
				String line0 = sign.getLine(0);
				int ind = line0.indexOf("[shop") + 1;
				String info = line0.substring(ind, sign.getLine(0).indexOf(']', ind));
				String dats[] = info.split(":");
				switch (this.mode)
				{
				case 0:
					this.read(event.getPlayer(), dats);
					this.stop();
					break;
				}
			}
		}
	}
	
	private void read(Player player, String[] datas)
	{
		if(datas.length == 0 || player == null)
		{
			return;
		}
		try
		{
			String plName = datas[4];
			OfflinePlayer pl = UPlayer.getPlayerByName(plName);
			if(pl == null)
			{
				pl = UPlayer.getPlayerByNameOff(plName);
			}
			if(pl == null)
			{
				//
			}
			player.sendMessage(ChatColor.DARK_PURPLE + "Shop infos :");
			player.sendMessage(ChatColor.DARK_GREEN + "  - Price: " + datas[1]);
			player.sendMessage(ChatColor.DARK_GREEN + "  - Item: " + datas[2]);
			player.sendMessage(ChatColor.DARK_GREEN + "  - Amount: " + datas[3]);
			if(plName.equalsIgnoreCase(player.getName()))
			{
				player.sendMessage(ChatColor.DARK_GREEN + "  - Owner: You");
			}
			else if(plName.equalsIgnoreCase("console"))
			{
				player.sendMessage(ChatColor.DARK_GREEN + "  - Owner: Server.");
			}
			else if(pl != null)
			{
				player.sendMessage(ChatColor.DARK_GREEN + "  - Owner: " + pl.getPlayer().getDisplayName());
			}
			else
			{
				player.sendMessage(ChatColor.DARK_GREEN + "  - Owner: undefined");
			}
		}
		catch(IndexOutOfBoundsException ex)
		{
			//
		}
	}
	
	private void stop()
	{
		PlayerInteractEvent.getHandlerList().unregister(this);
	}
}