package fr.gbp.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BankCommand implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(label.equalsIgnoreCase("toolb"))
		{
			if(!(sender instanceof Player))
			{
				sender.sendMessage("You do not have an inventory");
				return false;
			}
			Player player = (Player)sender;
			player.getInventory().addItem(new ItemStack(Material.DEAD_BUSH));
			return true;
		}
		return false;
	}
}