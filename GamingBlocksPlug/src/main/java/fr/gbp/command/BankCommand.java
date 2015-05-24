package fr.gbp.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.gbp.GamingBlocksPlug;

public class BankCommand implements CommandExecutor
{
	private String usageBMessage;
	private String usagePMessage;
	private GamingBlocksPlug gbp;

	public BankCommand(GamingBlocksPlug gbp)
	{
		this.gbp = gbp;
		this.usageBMessage = "/bank <pay> <player>";
		this.usagePMessage = "/pay <player>";
	}
	
	private boolean pay(CommandSender sender, boolean isPlayer, String[] args)
	{
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
				return pay(sender, isP, args);
			}
			sender.sendMessage(ChatColor.RED + "Usage: " + this.usageBMessage);
			return false;
		}
		else if(label.equalsIgnoreCase("pay"))
		{
			return pay(sender, isP, args);
		}
		sender.sendMessage(ChatColor.RED + "Usage: " + this.usagePMessage);
		return false;
	}
}