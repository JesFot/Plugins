package fr.gbp.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.gbp.GamingBlockPlug;
import fr.gbp.command.helpers.EcoHelper;
import fr.gbp.listener.GSpecialShopListener;
import fr.gbp.perms.GPermissions;
import fr.gbp.utils.UPlayer;

public class GEcoCommands implements CommandExecutor, TabCompleter
{
	/*
	 * Commands :
	 *  -/addme : add to eco system
	 *  -/account
	 *  -/toolb : give deadBush
	 *  -/bank
	 *  -/pay
	 */
	private GamingBlockPlug gbp;
	private EcoHelper ecoh;
	
	public GEcoCommands(GamingBlockPlug plugin)
	{
		this.gbp = plugin;
		this.ecoh = new EcoHelper(this.gbp);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		boolean isPlayer = (sender instanceof Player);
		if(cmd.getName().equalsIgnoreCase("toolb"))
		{
			if(!isPlayer)
			{
				sender.sendMessage(this.gbp.getLang().get("console.noinv"));
				return false;
			}
			Player player = (Player)sender;
			player.getInventory().addItem(new ItemStack(Material.DEAD_BUSH));
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("shop"))
		{
			if(args.length == 0)
			{
				if(!isPlayer)
				{
					sender.sendMessage("Please provide a location or be a player please.");
					return false;
				}
				Player player = (Player)sender;
				GSpecialShopListener listener = new GSpecialShopListener(0, player.getName(), "shop", -1, -1, null, null);
				this.gbp.getServer().getPluginManager().registerEvents(listener, this.gbp.getPlugin());
				return true;
			}
			else if(args.length == 2)
			{
				if(!isPlayer)
				{
					sender.sendMessage("Please provide a location or be a player please.");
					return false;
				}
				Player player = (Player)sender;
				if(args[0].equalsIgnoreCase("price"))
				{
					try
					{
						int a = Integer.parseInt(args[1]);
						GSpecialShopListener listener = new GSpecialShopListener(1, player.getName(), "shop", a, -1, null, null);
						this.gbp.getServer().getPluginManager().registerEvents(listener, this.gbp.getPlugin());
						return true;
					}
					catch(NumberFormatException ex)
					{
						//
					}
				}
				else if(args[0].equalsIgnoreCase("amount"))
				{
					try
					{
						int a = Integer.parseInt(args[1]);
						GSpecialShopListener listener = new GSpecialShopListener(1, player.getName(), "shop", -1, a, null, null);
						this.gbp.getServer().getPluginManager().registerEvents(listener, this.gbp.getPlugin());
						return true;
					}
					catch(NumberFormatException ex)
					{
						//
					}
				}
				else if(args[0].equalsIgnoreCase("item"))
				{
					Material mat = Material.getMaterial(args[1]);
					GSpecialShopListener listener = new GSpecialShopListener(1, player.getName(), "shop", -1, -1, null, mat);
					this.gbp.getServer().getPluginManager().registerEvents(listener, this.gbp.getPlugin());
					return true;
				}
				else if(args[0].equalsIgnoreCase("owner"))
				{
					OfflinePlayer playeroff = UPlayer.getPlayerByName(args[1]);
					if(playeroff == null){playeroff = UPlayer.getPlayerByNameOff(args[1]);};
					GSpecialShopListener listener = new GSpecialShopListener(1, player.getName(),
							playeroff==null&&args[1].equalsIgnoreCase("console")?"console":"shop",
									-1, -1, playeroff, null);
					this.gbp.getServer().getPluginManager().registerEvents(listener, this.gbp.getPlugin());
					return true;
				}
			}
			else if(args.length == 4)
			{
				if(!isPlayer)
				{
					sender.sendMessage("Please provide a location or be a player please.");
					return false;
				}
				Player player = (Player)sender;
				try
				{
					int p = Integer.parseInt(args[0]);
					int a = Integer.parseInt(args[1]);
					Material mat = Material.getMaterial(args[2]);
					OfflinePlayer playeroff = UPlayer.getPlayerByName(args[3]);
					if(playeroff == null){playeroff = UPlayer.getPlayerByNameOff(args[3]);};
					GSpecialShopListener listener = new GSpecialShopListener(1, player.getName(),
							playeroff==null&&args[3].equalsIgnoreCase("console")?"console":"shop", p, a, playeroff, mat);
					this.gbp.getServer().getPluginManager().registerEvents(listener, this.gbp.getPlugin());
					return true;
				}
				catch(NumberFormatException ex)
				{
					//
				}
			}
			sender.sendMessage(ChatColor.DARK_RED + "shop | shop <price> <amount> <item> <owner> | shop <owner|item|amount|price> <value>");
			return false;
		}
		else if(cmd.getName().equalsIgnoreCase("economy"))
		{
			return ecoh.economy(sender, cmd, label, args);
		}
		
		
		sender.sendMessage("what ?");
		return true;
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
	{
		List<String> result = new ArrayList<String>();
		List<String> allP = new ArrayList<String>();
		for(Player tmp : UPlayer.getOnlinePlayers())
		{
			allP.add(tmp.getName());
		}
		if(cmd.getName().equalsIgnoreCase("toolb"))
		{
			return Collections.emptyList();
		}
		else if(cmd.getName().equalsIgnoreCase("economy"))
		{
			switch(args.length)
			{
			case 1:
				result.add("pay");
				result.add("inventory");
				result.add("TakeEm");
				result.add("StoreEm");
				if(GPermissions.testPermissionSilent(sender, "GamingBlockPlug.economy.reset", false))
				{
					result.add("reset");
				}
				if(GPermissions.testPermissionSilent(sender, "GamingBlockPlug.economy.see", true))
				{
					result.add("see");
				}
				break;
			case 2:
				if(args[0].equalsIgnoreCase("pay"))
				{
					result.addAll(allP);
					result.add("console");
				}
				else if(GPermissions.testPermissionSilent(sender, "GamingBlockPlug.economy.reset", false) 
						&& args[0].equalsIgnoreCase("reset"))
				{
					result.addAll(allP);
				}
				else if(GPermissions.testPermissionSilent(sender, "GamingBlockPlug.economy.see", true) 
						&& args[0].equalsIgnoreCase("see"))
				{
					result.addAll(allP);
					result.addAll(this.gbp.getEconomy().getList(allP));
				}
				break;
			}
			List<String> response = new ArrayList<String>();
			for(String res : result)
			{
				if(res.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
				{
					response.add(res);
				}
			}
			return response;
		}
		return null;
	}
}