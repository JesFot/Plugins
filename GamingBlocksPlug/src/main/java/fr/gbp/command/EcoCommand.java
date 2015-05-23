package fr.gbp.command;

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

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(label.equalsIgnoreCase("addme"))
		{
			//
		}
		if(sender instanceof Player)
		{
			Player player = (Player)sender;
			player.sendMessage("You did the command, wait...");
			this.gbp.getEconomy().addPlayer(player);
			player.sendMessage("Done.");
			return true;
		}
		return false;
	}
}