package me.jesfot.gamingblockplug.command;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jesfot.gamingblockplug.permission.PermissionHelper;
import me.jesfot.gamingblockplug.permission.StaticPerms;
import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;

public class FlyCommand extends CommandBase
{
	private final GamingBlockPlug plugin;
	
	public FlyCommand(GamingBlockPlug plugin)
	{
		super("fly");
		
		this.plugin = plugin;
		
		super.setMinimalPermission(StaticPerms.CMD_FLY);
		super.setRawUsageMessage("/<command> [on/off] | /<command> <player> on/off");
	}
	
	@Override
	protected boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		Player player = null;
		if (args.length >= 2)
		{
			if (!PermissionHelper.testPermission(sender, StaticPerms.CMD_FLY_OTHER, false, null))
			{
				return true;
			}
			player = this.plugin.getPlayerExact(args[0]);
		}
		else if (sender instanceof Player)
		{
			player = (Player) sender;
		}
		if (player == null)
		{
			sender.sendMessage("Can only target players");
			return true;
		}
		
		boolean next = !player.getAllowFlight();
		if (args.length >= 1)
		{
			if (args.length == 1)
			{
				next = Boolean.parseBoolean(args[0]) || args[0].equalsIgnoreCase("on");
			}
			else
			{
				next = Boolean.parseBoolean(args[1]) || args[1].equalsIgnoreCase("on");
			}
		}
		
		if(!player.getGameMode().equals(GameMode.SURVIVAL))
		{
			sender.sendMessage("Only survival players can use /fly.");
			return true;
		}
		player.setAllowFlight(next);
		if(player.getAllowFlight())
		{
			Command.broadcastCommandMessage(sender, "Set flying to true for " + player.getName(), true);
		}
		else
		{
			Command.broadcastCommandMessage(sender, "Set flying to false for " + player.getName(), true);
		}
		return true;
	}
}
