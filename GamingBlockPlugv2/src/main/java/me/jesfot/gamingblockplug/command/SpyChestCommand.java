package me.jesfot.gamingblockplug.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jesfot.gamingblockplug.permission.StaticPerms;
import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;

public class SpyChestCommand extends CommandBase
{
	private final GamingBlockPlug plugin;
	
	public SpyChestCommand(GamingBlockPlug plugin)
	{
		super("spychest");
		
		this.plugin = plugin;
		
		super.setMinimalPermission(StaticPerms.CMD_SPYCHEST);
		super.setRawUsageMessage("/<command> <player> [normal|ender]");
	}
	
	@Override
	protected boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage("You must be a player");
			return true;
		}
		if (args.length < 1 || args.length > 2)
		{
			return false;
		}
		Player target = this.plugin.getPlayerExact(args[0]);
		if (target == null)
		{
			sender.sendMessage("Target is null or offline.");
			return true;
		}
		String mode = (args.length == 1) ? "normal" : args[1];
		Player player = (Player) sender;
		if (mode.equalsIgnoreCase("normal"))
		{
			player.openInventory(target.getInventory());
		}
		else if (mode.equalsIgnoreCase("ender"))
		{
			player.openInventory(target.getEnderChest());
		}
		else if (mode.equalsIgnoreCase("view"))
		{
			player.openInventory(target.getOpenInventory());
		}
		else
		{
			sender.sendMessage("Bad mode (normal or ender)");
			return false;
		}
		return true;
	}
	
	@Override
	protected List<String> executeTabComplete(CommandSender sender, Command command, String label, String[] args)
	{
		if (args.length == 1)
		{
			return null;
		}
		else if (args.length == 2)
		{
			return CommandBase.sortStart(args[1], Arrays.asList("normal", "ender"));
		}
		return Collections.emptyList();
	}
}
