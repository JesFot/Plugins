package fr.jesfot.gbp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class GPingCommand extends CommandBase
{
	public GPingCommand()
	{
		super("ping");
		this.setRawUsageMessage("/<com>");
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player player = (Player)sender;
			CraftPlayer craftPlayer = (CraftPlayer)player;
			int ping = craftPlayer.getHandle().ping;
			sender.sendMessage("" + ChatColor.BOLD + ChatColor.GOLD + "Pong" + ChatColor.RESET
					+ ChatColor.GOLD + ", after " + ChatColor.DARK_RED + ChatColor.ITALIC + ping + "ms");
		}
		else
		{
			sender.sendMessage("Pong ! (baka... 0ms)");
		}
		return true;
	}
}