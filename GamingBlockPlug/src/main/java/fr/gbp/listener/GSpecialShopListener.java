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

import fr.gbp.utils.UPlayer;

public class GSpecialShopListener implements Listener
{
	private final int mode;
	private final String playerName;
	private final Object[] newDats;
	
	public GSpecialShopListener(int p_mode, String player, Object...dats)
	{
		this.mode = p_mode;
		this.playerName = player;
		this.newDats = dats != null ? dats : new Object[]{"shop", -1, -1, null, null};
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
				String before = line0.substring(0, ind - 1);
				String after = line0.substring(line0.indexOf(']', ind));
				String dats[] = info.split(":");
				switch (this.mode)
				{
				case 0:
					this.read(event.getPlayer(), dats);
					this.stop();
					break;
				case 1:
					dats = this.write(event.getPlayer(), new String[]{(String)newDats[0]}, (Integer)newDats[1], (Integer)newDats[2],
							(OfflinePlayer)newDats[3], (Material)newDats[4]);
					info = String.join(":", dats);
					line0 = before + info + after;
					sign.setLine(0, line0);
					event.getClickedBlock().getState().setData(sign.getData());
					event.getClickedBlock().getState().update(true);
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
	
	private String[] write(Player player, String[] datas, int price, int amount, OfflinePlayer owner, Material material)
	{
		if(owner == null)
		{
			owner = player;
		}
		if(price >= 0)
		{
			datas[1] = Integer.toString(price);
		}
		if(amount >= 0)
		{
			datas[3] = Integer.toString(amount);
		}
		if(material != null)
		{
			datas[2] = material.name().toLowerCase();
		}
		if(owner != null)
		{
			datas[4] = owner.getName();
		}
		else
		{
			if(datas[0].equalsIgnoreCase("console"))
			{
				datas[0] = "shop";
				datas[4] = "console";
			}
		}
		return datas;
	}
	
	private void stop()
	{
		PlayerInteractEvent.getHandlerList().unregister(this);
	}
}