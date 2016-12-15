package fr.jesfot.gbp.command.helpers;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.jesfot.gbp.GamingBlockPlug_1_11;
import fr.jesfot.gbp.economy.Money;
import fr.jesfot.gbp.permission.PermissionsHelper;
import fr.jesfot.gbp.utils.ItemInventory;
import fr.jesfot.gbp.utils.Utils;

/**
 * Requested perms :<br>
 *  - '{@code GamingBlockPlug.economy.reset}'<br>
 *  - '{@code GamingBlockPlug.economy.see}'<br>
 *  - '{@code GamingBlockPlug.economy.passEm}'<br>
 * 
 * 
 * @author JësFot
 */
public class EcoHelper
{
	private GamingBlockPlug_1_11 gbp;
	
	public EcoHelper(GamingBlockPlug_1_11 plugin)
	{
		this.gbp = plugin;
	}
	
	public boolean view(CommandSender sender, Command cmd, String alias, String[] args, boolean isPlayer)
	{
		if(args.length != 0)
		{
			sender.sendMessage(this.usage(sender, cmd, alias, args));
			return true;
		}
		Player player = isPlayer?(Player)sender:null;
		if(!isPlayer)
		{
			sender.sendMessage(this.gbp.getLang().get("console.infmoney"));
			return true;
		}
		player.sendMessage(this.gbp.getLang().get("economy.balance")
				.replace("<money>", this.gbp.getEconomy().getPEconomy(player).getBalance() + Money.getSym()));
		return true;
	}
	
	public boolean pay(CommandSender sender, Command cmd, String alias, String[] args, boolean isPlayer)
	{
		Player player = null;
		if(isPlayer)
		{
			player = (Player)sender;
		}
		if(args.length == 3)
		{
			double dble = Utils.toDouble(args[2], 0);
			OfflinePlayer target = this.gbp.getPlayerExact(args[1]);
			if(target == null)
			{
				target = this.gbp.getOfflinePlayerByName(args[1]);
			}
			if(target == null)
			{
				if(args[1].equalsIgnoreCase("console"))
				{
					if(isPlayer)
					{
						this.gbp.getEconomy().pay(player, null, dble);
						sender.sendMessage(this.gbp.getLang().get("economy.autoremove")
								.replace("<money>", dble + Money.getSym()));
						Command.broadcastCommandMessage(sender, "This player throw " + dble + " by the window", false);
					}
					return true;
				}
				sender.sendMessage(this.gbp.getLang().get("player.notfound"));
				return true;
			}
			if(isPlayer)
			{
				this.gbp.getEconomy().pay(player, target, dble);
				Command.broadcastCommandMessage(sender, "This player gave " + dble + " to " + target.getName(), false);
				String targetName = target.getName();
				if(target.isOnline())
				{
					((Player)target).sendMessage(this.gbp.getLang().get("economy.receive")
							.replace("<money>", dble + Money.getSym())
							.replace("<player>", player.getDisplayName()));
					targetName = ((Player)target).getDisplayName();
				}
				player.sendMessage(this.gbp.getLang().get("economy.send")
						.replace("<money>", dble + Money.getSym())
						.replace("<player>", targetName));
			}
			else
			{
				this.gbp.getEconomy().pay(null, target, dble);
				Command.broadcastCommandMessage(sender, dble + "$ appear from nowhere and arrives to " + target.getName(), false);
				String targetName = target.getName();
				if(target.isOnline())
				{
					((Player)target).sendMessage(this.gbp.getLang().get("economy.receivefromc")
							.replace("<money>", dble + Money.getSym()));
					targetName = ((Player)target).getDisplayName();
				}
				sender.sendMessage(this.gbp.getLang().get("economy.send")
						.replace("<money>", dble + Money.getSym())
						.replace("<player>", targetName));
			}
			return true;
		}
		sender.sendMessage(this.usage(sender, cmd, alias, args));
		return true;
	}
	
	public boolean reset(CommandSender sender, Command cmd, String alias, String[] args)
	{
		if(!PermissionsHelper.testPermission(sender, "GamingBlockPlug.economy.reset", false, null) || args.length != 2)
		{
			sender.sendMessage(this.usage(sender, cmd, alias, args));
			return true;
		}
		OfflinePlayer target = this.gbp.getPlayerExact(args[1]);
		if(target == null)
		{
			target = this.gbp.getOfflinePlayerByName(args[1]);
		}
		this.gbp.getEconomy().getPEconomy(target).resetMoney();
		if(target.isOnline())
		{
			((Player)target).sendMessage(sender.getName() + " reseted your account, you have now " + this.gbp.getEconomy().getPEconomy(target).getBalance()
					+ Money.getSym());
		}
		sender.sendMessage("You reseted " + target.getName() + " balance.");
		Command.broadcastCommandMessage(sender, "This player reseted " + target.getName() + " account", false);
		return true;
	}
	
	public boolean see(CommandSender sender, Command cmd, String alias, String[] args)
	{
		if(!PermissionsHelper.testPermission(sender, "GamingBlockPlug.economy.see", true, null) || args.length != 2)
		{
			sender.sendMessage(this.usage(sender, cmd, alias, args));
			return true;
		}
		OfflinePlayer target = this.gbp.getOfflinePlayerByName(args[1]);
		if(target == null)
		{
			sender.sendMessage(this.gbp.getLang().get("player.notfound"));
			return false;
		}
		double dble = this.gbp.getEconomy().getPEconomy(target).getBalance();
		String tmp = this.gbp.getLang().get("economy.otherbalance")
				.replace("<money>", dble + Money.getSym())
				.replace("<player>", target.getName());
		sender.sendMessage(tmp);
		Command.broadcastCommandMessage(sender, "This player is looking for " + target.getName() + " account", false);
		return true;
	}
	
	public boolean inventory(CommandSender sender, Command cmd, String alias, String[] args, boolean isPlayer)
	{
		if(!isPlayer)
			return false;
		ItemInventory.openPlayerInv((Player)sender, this.gbp.getEconomy().getPEconomy((Player)sender).getMenu());
		return true;
	}
	
	public boolean takeEm(CommandSender sender, Command cmd, String alias, String[] args, boolean isPlayer)
	{
		Player player = (Player)sender;
		if(args.length != 2 || !PermissionsHelper.testPermission(sender, "GamingBlockPlug.economy.passEm", true, null))
		{
			sender.sendMessage(this.usage(sender, cmd, alias, args));
			return true;
		}
		if(!isPlayer)
		{
			sender.sendMessage(this.gbp.getLang().get("console.noinv", "The console do not have any inventory, so you cannot have emeralds..."));
			return false;
		}
		double dble = Utils.toDouble(args[1], 0);
		if(this.gbp.getEconomy().getPEconomy(player).hasEnough(dble))
		{
			double em = Money.getManyEmerald(dble);
			int ems = Money.getEmerald(em);
			this.gbp.getEconomy().getPEconomy(player).remove(Money.getManyMoney(ems));
			ItemStack is = new ItemStack(Material.EMERALD_BLOCK, ems);
			player.getInventory().addItem(is);
		}
		else
		{
			sender.sendMessage(this.gbp.getLang().get("economy.notenough"));
		}
		return true;
	}
	
	public boolean storeEm(CommandSender sender, Command cmd, String alias, String[] args, boolean isPlayer)
	{
		Player player = (Player)sender;
		if(args.length != 2 || !PermissionsHelper.testPermission(sender, "GamingBlockPlug.economy.passEm", true, null))
		{
			sender.sendMessage(this.usage(sender, cmd, alias, args));
			return true;
		}
		if(!isPlayer)
		{
			sender.sendMessage(this.gbp.getLang().get("console.noinv", "The console do not have any inventory, so you cannot have emeralds..."));
			return false;
		}
		int itn = Utils.toInt(args[1], 0);
		double money = Money.getManyMoney(itn);
		if(player.getInventory().containsAtLeast(new ItemStack(Material.EMERALD_BLOCK), itn))
		{
			ItemStack is = new ItemStack(Material.EMERALD_BLOCK, itn);
			player.getInventory().removeItem(is);
			this.gbp.getEconomy().getPEconomy(player).add(money);
		}
		else
		{
			sender.sendMessage(this.gbp.getLang().get("economy.notenough"));
		}
		return true;
	}
	
	public String usage(CommandSender sender, Command cmd, String alias, String[] args)
	{
		String usageMsg = "Usage: /" + alias + " [pay <player> <amount>]";
		if(PermissionsHelper.testPermissionSilent(sender, "GamingBlockPlug.economy.passEm", true))
		{
			usageMsg += " | /"+alias+" TakeEm|StoreEm <amount>";
		}
		if(PermissionsHelper.testPermissionSilent(sender, "GamingBlockPlug.economy.reset", false))
		{
			usageMsg += " | /"+alias+" reset <player>";
		}
		if(PermissionsHelper.testPermissionSilent(sender, "GamingBlockPlug.economy.see", true))
		{
			usageMsg += " | /"+alias+" see <player>";
		}
		return usageMsg;
	}
}