package me.jesfot.gamingblockplug.command;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jesfot.gamingblockplug.permission.StaticPerms;

public class SpectateCommand extends CommandBase
{
	public SpectateCommand()
	{
		super("spectate");
		
		super.setMinimalPermission(StaticPerms.CMD_SPECTATE);
		super.setRawUsageMessage("/<command>");
	}
	
	@Override
	protected boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage("Only players can use it !");
			return true;
		}
		Player player = (Player) sender;
		if (player.getGameMode().equals(GameMode.ADVENTURE))
		{
			sender.sendMessage("You cannot use /" + label + " in adventure mode.");
			return true;
		}
		if(!player.getGameMode().equals(GameMode.SPECTATOR))
		{
			player.setGameMode(GameMode.SPECTATOR);
			Command.broadcastCommandMessage(sender, "Switched in spectator mode", true);
		}
		else
		{
			player.setGameMode(GameMode.SURVIVAL);
			Command.broadcastCommandMessage(sender, "Switched in normal mode (survival)", true);
		}
		return true;
	}
}
