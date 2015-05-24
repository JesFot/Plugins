package fr.gbp.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.gbp.GamingBlocksPlug;
import fr.gbp.utils.UPlayer;

public class BankCommand implements CommandExecutor
{
	private String usageBMessage;
	private String usagePMessage;
	private GamingBlocksPlug gbp;

	public BankCommand(GamingBlocksPlug gbp)
	{
		this.gbp = gbp;
		this.usageBMessage = "/bank <pay> <player> <amount>";
		this.usagePMessage = "/pay <player> <amount>";
	}
	
	private boolean pay(CommandSender sender, boolean isPlayer, String[] args)
	{
		if(!isPlayer)
		{
			sender.sendMessage(ChatColor.RED + "You are the server, you mean give money (/bank give <Player>) ?");
			return false;
		}
		else
		{
			String targetS = "";
			double amount = 0;
			if(args[0].equalsIgnoreCase("pay"))
			{
				targetS = args[1];
				amount = Double.valueOf(args[2]);
			}
			else
			{
				targetS = args[0];
				amount = Double.valueOf(args[1]);
			}
			Player source = (Player)sender;
			Player target = UPlayer.getPlayerByName(targetS);
			if(source.equals(target))
			{
				sender.sendMessage(ChatColor.RED + "You cannot pay yourself !");
				return true;
			}
			this.gbp.getEconomy().getPEco(source).remove(amount);
			this.gbp.getEconomy().getPEco(target).add(amount);
			source.sendMessage("You payed " + amount + "$ to " + target.getDisplayName() + ".");
			target.sendMessage("You recieved from " + source.getDisplayName() + " " + amount + "$.");
			this.gbp.getLogger().info("[" + source.getName() + " performe pay command]");
		}
		return true;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		boolean isP = (sender instanceof Player);
		if(label.equalsIgnoreCase("toolb"))
		{
			if(!isP)
			{
				sender.sendMessage("You do not have an inventory");
				return false;
			}
			Player player = (Player)sender;
			player.getInventory().addItem(new ItemStack(Material.DEAD_BUSH));
			return true;
		}
		else if(label.equalsIgnoreCase("bank"))
		{
			if(args.length < 1)
			{
				sender.sendMessage(ChatColor.RED + "Usage: " + this.usageBMessage);
				return false;
			}
			else if(args[0].equalsIgnoreCase("pay"))
			{
				if(args.length < 3)
				{
					sender.sendMessage(ChatColor.RED + "Usage: " + this.usageBMessage);
					return false;
				}
				return pay(sender, isP, args);
			}
			sender.sendMessage(ChatColor.RED + "Usage: " + this.usageBMessage);
			return false;
		}
		else if(label.equalsIgnoreCase("pay"))
		{
			if(args.length < 2)
			{
				sender.sendMessage(ChatColor.RED + "Usage: " + this.usagePMessage);
				return false;
			}
			return pay(sender, isP, args);
		}
		sender.sendMessage(ChatColor.RED + "Usage: " + this.usagePMessage);
		return false;
	}
}