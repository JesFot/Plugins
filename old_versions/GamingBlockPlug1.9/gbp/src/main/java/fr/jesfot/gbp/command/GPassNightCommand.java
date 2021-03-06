package fr.jesfot.gbp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.permission.PermissionsHelper;
import fr.jesfot.gbp.utils.Utils;

public class GPassNightCommand extends CommandBase
{
	private final GamingBlockPlug_1_9 gbp;
	
	public GPassNightCommand(GamingBlockPlug_1_9 plugin)
	{
		super("forcepassnight");
		this.gbp = plugin;
		plugin.getPermissionManager().addPermission("GamingBlockPlug.passnight", PermissionDefault.OP,
				"Allows you to force the day to come...", Permissions.globalGBP);
		//this.disableCommand("This command does not work correctly, so it has been desactivated.");
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(PermissionsHelper.testPermission(sender, "GamingBlockPlug.passnight", true, "You are not allowed to force"
				+ " this system !"))
		{
			GamingBlockPlug_1_9.getMyLogger().info(sender.getName() + " used /"
					+ command.getName() + " " + Utils.compile(args, 0, " "));
			for(Player pl : this.gbp.getServer().getOnlinePlayers())
			{
				pl.setSleepingIgnored(true);
			}
		}
		else
		{
			GamingBlockPlug_1_9.getMyLogger().info(sender.getName() + " tried to use /"
					+ command.getName() + " " + Utils.compile(args, 0, " "));
		}
		return true;
	}
}