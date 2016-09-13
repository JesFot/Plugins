package fr.jesfot.gbp.command;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Sign;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.shop.ShopObject;

public class GShopCommand extends CommandBase
{
	private GamingBlockPlug_1_9 gbp;
	private SpecialListener sL;
	
	public GShopCommand(GamingBlockPlug_1_9 plugin)
	{
		super("shop");
		this.gbp = plugin;
		this.setRawUsageMessage("/<com> list | /<com> add <Player>");
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 1)
		{
			if(args[0].equalsIgnoreCase("list"))
			{
				sender.sendMessage(color(this.gbp.getLang().get("shop.list", "There are &6<amount>&r "
						+ "shops registered on the server.").replaceAll("<amount>",
								"" + this.gbp.getShops().getNumberOfShops())));
				return true;
			}
		}
		else if(args.length == 2)
		{
			if(args[0].equalsIgnoreCase("add") && sender instanceof Player)
			{
				String colName = args[1];
				OfflinePlayer collab = this.gbp.getOfflinePlayerByName(colName);
				if(collab == null)
				{
					sender.sendMessage(this.gbp.getLang().get("player.notfound").replaceAll("<player>", colName));
					return true;
				}
				this.sL = new SpecialListener(collab, (Player)sender, this.gbp);
				this.gbp.getPluginManager().registerEvents(this.sL, this.gbp.getPlugin());
			}
		}
		this.sendUsage(sender, label);
		return true;
	}
	
	public static class SpecialListener implements Listener
	{
		private OfflinePlayer collab;
		private Player user;
		private GamingBlockPlug_1_9 gbp;
		
		public SpecialListener(OfflinePlayer col, Player u, GamingBlockPlug_1_9 plugin)
		{
			this.collab = col;
			this.user = u;
			this.gbp = plugin;
		}
		
		@EventHandler(priority = EventPriority.HIGHEST)
		public void onClickShop(PlayerInteractEvent event)
		{
			if(event.getPlayer().getUniqueId().equals(this.user.getUniqueId()))
			{
				if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
				{
					if(event.getClickedBlock().getType().equals(Material.WALL_SIGN))
					{
						Sign sign = (Sign)event.getClickedBlock().getState().getData();
						Block ch = event.getClickedBlock().getRelative(sign.getAttachedFace());
						ShopObject shop = this.gbp.getShops().getShop(ch.getLocation());
						if(shop != null)
						{
							shop.addCollab(this.collab);
							this.user.sendMessage("Collab Added.");
						}
					}
					event.getHandlers().unregister(this);
				}
			}
		}
	}
}