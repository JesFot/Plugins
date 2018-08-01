package fr.jesfot.gbp.command;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionDefault;

import fr.jesfot.gbp.GamingBlockPlug_1_12;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.utils.ItemInventory;

public class GSpyChestCommand extends CommandBase
{
	private GamingBlockPlug_1_12 gbp;
	private Inventory menu;
	
	public GSpyChestCommand(GamingBlockPlug_1_12 plugin)
	{
		super("gspychest");
		plugin.getPermissionManager().addPermission("GamingBlockPlug.spychests", PermissionDefault.OP,
				"Allows to see contents of inventories", Permissions.globalGBP);
		this.gbp = plugin;
		menu = ItemInventory.createItemandInv(Material.CHEST, 0, "Normal Inv", "", "Spy Inventory", 2);
		ItemInventory.createIteminInv(Material.ENDER_CHEST, this.menu, 1, "Ender Inv", "");
		ItemInventory.createIteminInv(Material.EMERALD, this.menu, 2, "Bank Inv", "");
		this.setRawUsageMessage("/<com> <player>");
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(!command.getName().equalsIgnoreCase("gspychest"))
		{
			return false;
		}
		if(!(sender instanceof Player))
		{
			sender.sendMessage(this.color(this.gbp.getLang().get("console.onlypls")));
			return true;
		}
		if(args.length != 1)
		{
			this.sendUsage(sender, label);
			return true;
		}
		Player target = this.gbp.getPlayerExact(args[0]);
		if(target == null)
		{
			sender.sendMessage(this.color(this.gbp.getLang().get("player.notfound")));
			return true;
		}
		Player player = (Player)sender;
		Command.broadcastCommandMessage(sender, "Looking for " + args[0] + "'s Inventories", false);
		ItemInventory.createIteminInv(Material.BARRIER, this.menu, 16, "§4Data", player.getUniqueId().toString() + "_menu");
		MenuListener listner = new MenuListener(target, player.getUniqueId().toString() + "_menu", this.gbp);
		this.gbp.getPluginManager().registerEvents(listner, this.gbp.getPlugin());
		player.openInventory(this.menu);
		return true;
	}
	
	@Override
	public List<String> executeTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		if(args.length != 1 || !(sender instanceof Player))
		{
			return super.executeTabComplete(sender, command, alias, args);
		}
		return this.getPlayers(args[0]);
	}
	
	public static class MenuListener implements Listener
	{
		private Player target;
		private GamingBlockPlug_1_12 gbp;
		private final String inv_ID;
		
		public MenuListener(Player tar, final String id, GamingBlockPlug_1_12 plugin)
		{
			this.target = tar;
			this.inv_ID = id;
			this.gbp = plugin;
		}
		
		@EventHandler
		public void onInventoryClick(InventoryClickEvent event)
		{
			ItemStack item = event.getCurrentItem();
			if(event.getInventory().getItem(15).getItemMeta().getLore().get(0).contentEquals(this.inv_ID))
			{
				event.setCancelled(true);
				if(item.getType().equals(Material.CHEST))
				{
					int slot = event.getSlot();
					if(slot == 0)
					{
						ItemInventory.openPlayerInv(((Player)event.getWhoClicked()), target.getPlayer().getInventory());
					}
				}
				else if(item.getType().equals(Material.ENDER_CHEST))
				{
					if(event.getSlot() == 1)
					{
						ItemInventory.openPlayerInv(((Player)event.getWhoClicked()), target.getPlayer().getEnderChest());
					}
				}
				else if(item.getType().equals(Material.EMERALD))
				{
					if(event.getSlot() == 2)
					{
						ItemInventory.openPlayerInv(((Player)event.getWhoClicked()), this.gbp.getEconomy()
								.getPEconomy(target.getPlayer()).getStoredInventory().getInventory());
					}
				}
			}
		}
		
		@EventHandler
		public void onInventoryClose(InventoryCloseEvent event)
		{
			if(event.getInventory().getItem(15).getItemMeta().getLore().get(0).contentEquals(this.inv_ID))
			{
				InventoryClickEvent.getHandlerList().unregister(this);
				event.getHandlers().unregister(this);
			}
		}
	}
}