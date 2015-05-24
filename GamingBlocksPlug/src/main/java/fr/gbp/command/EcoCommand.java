package fr.gbp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.gbp.GamingBlocksPlug;

public class EcoCommand implements CommandExecutor
{
	private GamingBlocksPlug gbp;
	
	public EcoCommand(GamingBlocksPlug p_gbp)
	{
		this.gbp = p_gbp;
	}
	
	private boolean addme(CommandSender sender, boolean isPlayer)
	{
		if(!isPlayer)
		{
			sender.sendMessage(ChatColor.RED + "You must be a player to execute this command, the server donnot need any money.");
			return true;
		}
		else
		{
			Player player = (Player)sender;
			player.sendMessage("You will be added to the money memory...");
			if(this.gbp.getEconomy().playerExists(player))
			{
				player.sendMessage("You already have an acount, to reset please type /acount reset");
				return true;
			}
			this.gbp.getEconomy().addPlayer(player);
			player.sendMessage("You have been added to money memory.");
			return true;
		}
	}
	
	private boolean resetAcount(CommandSender sender, boolean isPlayer)
	{
		if(!isPlayer)
		{
			sender.sendMessage(ChatColor.RED + "You must be a player to execute this command, the server donnot need any money.");
			return true;
		}
		else
		{
			Player player = (Player)sender;
			player.sendMessage("Your acount will be reset to base");
			if(!this.gbp.getEconomy().playerExists(player))
			{
				return addme(sender, true);
			}
			this.gbp.getEconomy().resetPlayer(player);
			player.sendMessage("Your acount have been reseted.");
			return true;
		}
	}
	
	private boolean getBalance(CommandSender sender, boolean isPlayer)
	{
		if(!isPlayer)
		{
			sender.sendMessage(ChatColor.RED + "You do not have any acount because you are the server. Do ot try again");
		}
		else
		{
			Player player = (Player)sender;
			double acount = this.gbp.getEconomy().getPEco(player).getBalance();
			player.sendMessage("You have " + acount + "$ on your acount.");
		}
		return true;
	}
	
	private boolean setBalance(CommandSender sender, boolean isPlayer, String[] args)
	{
		if(!isPlayer)
		{
			sender.sendMessage(ChatColor.RED + "You do not have any acount because you are the server.");
		}
		else
		{
			Player player = (Player)sender;
			double mon = Double.valueOf(args[1]);
			this.gbp.getEconomy().getPEco(player).setBalance(mon);
			double actual = this.gbp.getEconomy().getPEco(player).getBalance();
			player.sendMessage("You have now " + actual + "$ on your acount.");
		}
		return true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		boolean isP = (sender instanceof Player);
		if(label.equalsIgnoreCase("addme"))
		{
			return addme(sender, isP);
		}
		else if(label.equalsIgnoreCase("acount"))
		{
			if(args.length < 1)
			{
				return false;
			}
			if(args[0].equalsIgnoreCase("reset"))
			{
				return resetAcount(sender, isP);
			}
			else if(args[0].equalsIgnoreCase("get"))
			{
				return getBalance(sender, isP);
			}
			else if(args[0].equalsIgnoreCase("setb"))
			{
				if(args.length < 2)
				{
					return false;
				}
				return setBalance(sender, isP, args);
			}
		}
		return false;
	}
}