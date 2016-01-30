package fr.gbp.command.helpers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.gbp.GamingBlockPlug;
import fr.gbp.perms.GPermissions;
import fr.gbp.utils.ItemInventory;
import fr.gbp.utils.UPlayer;

public class EcoHelper
{
	private GamingBlockPlug gbp;
	
	public EcoHelper(GamingBlockPlug plugin)
	{
		this.gbp = plugin;
	}
	
	public boolean economy(CommandSender sender, Command cmd, String alias, String[] args)
	{
		Player player = null;
		boolean p = false;
		if (cmd.getName().equalsIgnoreCase("economy"))
		{
			if(sender instanceof Player)
			{
				p = true;
				player = (Player)sender;
				/*if(!player.hasPermission("all"))
				{
					sender.sendMessage(ChatColor.DARK_RED + "You do not have the rights to perform this command.");
					return true;
				}*/
			}
			if(args.length <= 0)
			{
				if(!p)
				{
					sender.sendMessage(this.gbp.getLang().get("console.infmoney"));
					return true;
				}
				player.sendMessage(this.gbp.getLang().get("economy.balance").replace("<money>", this.gbp.getEconomy().getPEco(player).getBalance()+this.gbp.getMoney().getSym()));
				return true;
			}
			else
			{
				if(args[0].equalsIgnoreCase("pay"))
				{
					if(args.length == 3)
					{
						double dble = Double.valueOf(args[2]);
						OfflinePlayer target = UPlayer.getPlayerByName(args[1]);
						if(target == null)
						{
							target = UPlayer.getPlayerByNameOff(args[1]);
						}
						if (target == null)
						{
							if(args[1].equalsIgnoreCase("console"))
							{
								if(p)
								{
									this.gbp.getEconomy().pay(player, null, dble);
									sender.sendMessage(this.gbp.getLang().get("economy.autoremove").replace("<money>", dble+this.gbp.getMoney().getSym()));
								}
								return true;
							}
							sender.sendMessage(this.gbp.getLang().get("player.notfound"));
							return true;
						}
						if(p)
						{
							this.gbp.getEconomy().pay(player, target, dble);
							if(target.isOnline())
							{
								((Player)target).sendMessage(this.gbp.getLang().get("economy.receive").replace("<money>", dble+this.gbp.getMoney().getSym())
										.replace("<player>", player.getDisplayName()));
								player.sendMessage(this.gbp.getLang().get("economy.send").replace("<money>", dble+this.gbp.getMoney().getSym())
										.replace("<player>", ((Player)target).getDisplayName()));
							}
							else
								player.sendMessage(this.gbp.getLang().get("economy.send").replace("<money>", dble+this.gbp.getMoney().getSym())
										.replace("<player>", target.getName()));
						}
						else
						{
							this.gbp.getEconomy().pay(null, target, dble);
							if(target.isOnline())
							{
								((Player)target).sendMessage(this.gbp.getLang().get("economy.receivefromc").replace("<money>", dble+this.gbp.getMoney().getSym()));
								sender.sendMessage(this.gbp.getLang().get("economy.send").replace("<money>", dble+this.gbp.getMoney().getSym())
										.replace("<player>", ((Player)target).getDisplayName()));
							}
							else
								sender.sendMessage(this.gbp.getLang().get("economy.send").replace("<money>", dble+this.gbp.getMoney().getSym())
										.replace("<player>", target.getName()));
						}
						return true;
					}
					
				}
				else if(args[0].equalsIgnoreCase("inventory"))
				{
					if(!p)
						return true;
					ItemInventory.openPlayerInv(player, this.gbp.getEconomy().getPEco(player).getMenu());
					return true;
				}
				else if(args[0].equalsIgnoreCase("reset") && args.length == 2)
				{
					if(!GPermissions.testPermission(sender, "GamingBlockPlug.economy.reset", null, false))
					{
						return true;
					}
					Player tgt = UPlayer.getPlayerByName(args[1]);
					this.gbp.getEconomy().getPEco(tgt).resetMoney();
					sender.sendMessage("You reseted " + tgt.getName() + " balance.");
					return true;
				}
				else if(args[0].equalsIgnoreCase("see") && args.length == 2)
				{
					if(!GPermissions.testPermission(sender, "GamingBlockPlug.economy.see", null, true))
					{
						return true;
					}
					OfflinePlayer tgt = UPlayer.getPlayerByNameOff(args[1]);
					double dble = this.gbp.getEconomy().getPEco(tgt).getBalance();
					String tmp = this.gbp.getLang().get("economy.otherbalance")
							.replace("<money>", dble+this.gbp.getMoney().getSym())
							.replace("<player>", tgt.getName());
					sender.sendMessage(tmp);
					return true;
				}
				else if((args[0].equalsIgnoreCase("TakeEm") || args[0].equalsIgnoreCase("te")) && args.length == 2)
				{
					double dble = Double.valueOf(args[1]);
					if(!GPermissions.testPermission(sender, "GamingBlockPlug.economy.passEm", null, true))
					{
						return true;
					}
					if(!p)
					{
						sender.sendMessage(this.gbp.getLang().get("console.noinv"));
						return true;
					}
					if(this.gbp.getEconomy().getPEco(player).hasEnough(dble))
					{
						double em = this.gbp.getMoney().getManyEmerald(dble);
						int ems = this.gbp.getMoney().getEmerald(em);
						this.gbp.getEconomy().getPEco(player).remove(this.gbp.getMoney().getManyMoney(ems));
						ItemStack is = new ItemStack(Material.EMERALD, ems);
						player.getInventory().addItem(is);
						return true;
					}
					else
					{
						sender.sendMessage(this.gbp.getLang().get("economy.notenough"));
						return true;
					}
				}
				else if((args[0].equalsIgnoreCase("StoreEm") || args[0].equalsIgnoreCase("se")) && args.length == 2)
				{
					int dble;
					try
					{
						 dble = Integer.valueOf(args[1]);
					}
					catch(Exception e)
					{
						sender.sendMessage("USE INTEGER PLEASE !!!");
						return true;
					}
					if(!GPermissions.testPermission(sender, "GamingBlockPlug.economy.passEm", null, true))
					{
						return true;
					}
					if(!p)
					{
						sender.sendMessage(this.gbp.getLang().get("console.noinv"));
						return true;
					}
					double em = this.gbp.getMoney().getManyMoney(dble);
					if(player.getInventory().containsAtLeast(new ItemStack(Material.EMERALD), dble))
					{
						ItemStack is = new ItemStack(Material.EMERALD, dble);
						player.getInventory().removeItem(is);
						this.gbp.getEconomy().getPEco(player).add(em);
						return true;
					}
					else
					{
						sender.sendMessage(this.gbp.getLang().get("economy.notenough"));
						return true;
					}
				}
				String usageMsg = ChatColor.RED + "Usage: /"+alias+" [pay <player> <amount>] "
						+ " /"+alias+" TakeEm <amount>";
				if(GPermissions.testPermissionSilent(sender, "GamingBlockPlug.economy.reset", false))
				{
					usageMsg += " | /"+alias+" reset <player>";
				}
				if(GPermissions.testPermissionSilent(sender, "GamingBlockPlug.economy.see", true))
				{
					usageMsg += " | /"+alias+" see <player>";
				}
				sender.sendMessage(usageMsg);
				return true;
			}
		}
		return false;
	}
}