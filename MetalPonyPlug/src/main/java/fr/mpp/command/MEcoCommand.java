package fr.mpp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mpp.MetalPonyPlug;
import fr.mpp.economy.MEcoMenu;
import fr.mpp.utils.MPlayer;

public class MEcoCommand implements CommandExecutor
{
	private MetalPonyPlug mpp;
	
	public MEcoCommand(MetalPonyPlug p_mpp)
	{
		this.mpp = p_mpp;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String aliase, String[] args)
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
				player.sendMessage("You have actually "+this.mpp.getEconomy().getEco(player).getMoney()+this.mpp.getEconomy().getSym()+".");
			}
			else
			{
				if(args[0].equalsIgnoreCase("pay"))
				{
					if(args.length == 3)
					{
						double dble = Double.valueOf(args[2]);
						Player target = MPlayer.getPlayerByName(args[1]);
						if (target == null)
						{
							if(args[1].equalsIgnoreCase("console"))
							{
								if(p)
								{
									this.mpp.getEconomy().pay(player, null, dble);
									sender.sendMessage("You removed "+dble+this.mpp.getEconomy().getSym()+" from your acount.");
								}
								return true;
							}
							sender.sendMessage("Design a connected player please.");
							return true;
						}
						if(p)
						{
							this.mpp.getEconomy().pay(player, target, dble);
							target.sendMessage(player.getDisplayName()+" give you "+dble+this.mpp.getEconomy().getSym()+".");
							player.sendMessage("You gave "+dble+this.mpp.getEconomy().getSym()+" to "+target.getDisplayName()+".");
						}
						else
						{
							this.mpp.getEconomy().pay(null, target, dble);
							target.sendMessage("The Console give you "+dble+this.mpp.getEconomy().getSym()+".");
							sender.sendMessage("You gave "+dble+this.mpp.getEconomy().getSym()+" to "+target.getDisplayName()+".");
						}
						return true;
					}
					
				}
				else if(args[0].equalsIgnoreCase("inventory"))
				{
					if(!p)
						return true;
					player.openInventory(MEcoMenu.getInvCode());
				}
				else if(args[0].equalsIgnoreCase("reset"))
				{
					if(!sender.hasPermission("all"))
					{
						sender.sendMessage(ChatColor.DARK_RED + "You do not have the rights to perform this command.");
						return true;
					}
					Player tgt = MPlayer.getPlayerByName(args[1]);
					this.mpp.getEconomy().getEco(tgt).resetMoney();
				}
				else if(args[0].equalsIgnoreCase("code"))
				{
					if(!p)
					{
						sender.sendMessage("Console, please review usage ...");
						return true;
					}
					if(args.length >= 2)
					{
						int d = Integer.parseInt(args[1]);
						this.mpp.getConfig().getCustomConfig().set("eco.codes."+player.getName().toLowerCase(), d);
						this.mpp.getConfig().saveCustomConfig();
						player.sendMessage("Your code has been changed.");
					}
				}
				else
				{
					sender.sendMessage(ChatColor.DARK_RED+"Usage: /"+aliase+" [inventory]");
					sender.sendMessage(ChatColor.DARK_RED+"Usage: /"+aliase+" <pay> <Player>");
					sender.sendMessage(ChatColor.DARK_RED+"Usage: /"+aliase+" <code> <new_Code(7chars)>");
				}
			}
			/*if (args[0].equalsIgnoreCase("set"))
			{
				double dble = Double.valueOf(args[1]);
				Player cible;
				if(args.length >= 3)
				{
					String pname = args[2];
					cible = MPlayer.getPlayerByName(pname);
					if (cible == null)
					{
						sender.sendMessage("Design a connected player please.");
						return true;
					}
				}
				else if(p)
				{
					cible = (Player)sender;
				}
				else
				{
					sender.sendMessage("Give a third argument or be a player please");
					return true;
				}
				this.mpp.getEconomy().getEco(cible).setMoney(dble);
			}*/
			return true;
		}
		return false;
	}
}