package me.jesfot.gamingblockplug.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jesfot.gamingblockplug.permission.StaticPerms;
import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;

/**
 * @author JësFot
 * @since 1.13-1.0.0
 * @version 1.0
 */
public class ForcePassnightCommand extends CommandBase
{
	private final GamingBlockPlug plugin;
	
	public ForcePassnightCommand(GamingBlockPlug plugin)
	{
		super("forcepassnight");
		
		this.plugin = plugin;
		
		super.setMinimalPermission(StaticPerms.CMD_PASSNIGHT);
		super.setRawUsageMessage("/<command>");
	}
	
	@Override
	protected boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		for(Player pl : this.plugin.getOnlinePlayers())
		{
			pl.setSleepingIgnored(true);
		}
		return true;
	}
}
