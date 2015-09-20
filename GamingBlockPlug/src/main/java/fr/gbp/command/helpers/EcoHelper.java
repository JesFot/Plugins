package fr.gbp.command.helpers;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.gbp.GamingBlockPlug;
import fr.gbp.utils.UPlayer;

public class EcoHelper
{
	private GamingBlockPlug gbp;
	
	public EcoHelper(GamingBlockPlug plugin)
	{
		this.gbp = plugin;
	}
	
	public boolean addme(CommandSender sender, boolean isPlayer)
	{
		if(!isPlayer)
		{
			sender.sendMessage(ChatColor.RED + "You must be a player to execute this command, the server do not need any money.");
			return true;
		}
		else
		{
			Player player = (Player)sender;
			player.sendMessage("You will be added to the money memory...");
			if(this.gbp.getEconomy().playerExists(player))
			{
				player.sendMessage("You already have an account.");
				return true;
			}
			this.gbp.getEconomy().addPlayer(player);
			player.sendMessage("You have been added to money memory.");
			return true;
		}
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
					sender.sendMessage("Console, You have infinity money.");
					return true;
				}
				player.sendMessage("You have actually "+this.gbp.getEconomy().getPEco(player).getBalance()+this.gbp.getMoney().getSym()+".");
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
									sender.sendMessage("You removed "+dble+this.gbp.getMoney().getSym()+" from your acount.");
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
								((Player)target).sendMessage(player.getDisplayName()+" give you "+dble+this.gbp.getMoney().getSym()+".");
								player.sendMessage("You gave "+dble+this.gbp.getMoney().getSym()+" to "+((Player)target).getDisplayName()+".");
							}
							else
								player.sendMessage("You gave "+dble+this.gbp.getMoney().getSym()+" to "+target.getName()+".");
						}
						else
						{
							this.gbp.getEconomy().pay(null, target, dble);
							if(target.isOnline())
							{
								((Player)target).sendMessage("The Console give you "+dble+this.gbp.getMoney().getSym()+".");
								sender.sendMessage("You gave "+dble+this.gbp.getMoney().getSym()+" to "+((Player)target).getDisplayName()+".");
							}
							else
								sender.sendMessage("You gave "+dble+this.gbp.getMoney().getSym()+" to "+target.getName()+".");
						}
						return true;
					}
					
				}
				else if(args[0].equalsIgnoreCase("reset"))
				{
					if(!sender.hasPermission("GamingBlockPlug.economy.reset"))
					{
						sender.sendMessage(ChatColor.DARK_RED + "You do not have the rights to perform this command.");
						return true;
					}
					Player tgt = UPlayer.getPlayerByName(args[1]);
					this.gbp.getEconomy().getPEco(tgt).resetMoney();
				}
				else
				{
					sender.sendMessage(ChatColor.DARK_RED+"Usage: /"+alias);
					sender.sendMessage(ChatColor.DARK_RED+"Usage: /"+alias+" <pay> <Player>");
				}
			}
			return true;
		}
		return false;
	}
}