package fr.gbp.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.gbp.GamingBlockPlug;
import fr.gbp.command.helpers.EcoHelper;

public class GEcoCommands implements CommandExecutor
{
	/*
	 * Commands :
	 *  -/addme : add to eco system
	 *  -/account
	 *  -/toolb : give deadBush
	 *  -/bank
	 *  -/pay
	 */
	private GamingBlockPlug gbp;
	private EcoHelper ecoh;
	private String usageBMessage;
	private String usagePMessage;
	private String usageAMessage;
	
	public GEcoCommands(GamingBlockPlug plugin)
	{
		this.gbp = plugin;
		this.ecoh = new EcoHelper(this.gbp);
		this.usageBMessage = "/bank <pay> <player> <amount>";
		this.usagePMessage = "/pay <player> <amount>";
		this.usageAMessage = "/account <get>";
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		boolean isPlayer = (sender instanceof Player);
		if(cmd.getName().equalsIgnoreCase("addme"))
		{
			return this.ecoh.addme(sender, isPlayer);
		}
		else if(cmd.getName().equalsIgnoreCase("toolb"))
		{
			if(!isPlayer)
			{
				sender.sendMessage("You do not have an inventory.");
				return false;
			}
			Player player = (Player)sender;
			player.getInventory().addItem(new ItemStack(Material.DEAD_BUSH));
			return true;
		}
		sender.sendMessage(ChatColor.RED + "Usage: " + this.usagePMessage);
		return true;
	}
}