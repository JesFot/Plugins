package me.jesfot.gamingblockplug.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jesfot.gamingblockplug.permission.StaticPerms;
import me.jesfot.gamingblockplug.reflection.PlayerReflection;

/**
 * @author JësFot
 * @since 1.13-1.0.0
 * @version 1.0
 */
public class PingCommand extends CommandBase
{
	public PingCommand()
	{
		super("ping");
		super.setMinimalPermission(StaticPerms.CMD_PING);
		super.setRawUsageMessage("/<command>");
	}

	@Override
	protected boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			int ping = PlayerReflection.getPing((Player) sender);
			sender.sendMessage("" + ChatColor.BOLD + ChatColor.GOLD + "Pong" + ChatColor.RESET + ChatColor.GOLD
					+ ", after " + ChatColor.DARK_RED + ChatColor.ITALIC + ping + "ms");
		}
		else
		{
			sender.sendMessage("Pong ! (baka... 0ms)");
		}
		return true;
	}
}
