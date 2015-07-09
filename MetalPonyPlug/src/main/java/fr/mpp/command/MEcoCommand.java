package fr.mpp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mpp.MetalPonyPlug;
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
				if(!player.hasPermission("all"))
				{
					sender.sendMessage(ChatColor.DARK_RED + "You do not have the rights to perform this command.");
					return true;
				}
			}
			if(args.length <= 0)
			{
				if(!p)
				{
					sender.sendMessage("Console, please review usage ...");
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
						Player target = MPlayer.getPlayerByName(args[1]);
						if (target == null)
						{
							sender.sendMessage("Design a connected player please.");
							return true;
						}
						double dble = Double.valueOf(args[2]);
						if(p)
						{
							this.mpp.getEconomy().pay(player, target, dble);
							target.sendMessage(player.getDisplayName()+" give you "+dble+this.mpp.getEconomy().getSym()+".");
							player.sendMessage("You gave "+dble+this.mpp.getEconomy().getSym()+" at "+target.getDisplayName()+".");
						}
						return true;
					}
					
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