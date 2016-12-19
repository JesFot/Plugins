package fr.jesfot.gbp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.jesfot.gbp.GamingBlockPlug_1_11;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.permission.PermissionsHelper;

public class GBotCommand extends CommandBase
{
	private GamingBlockPlug_1_11 gbp;
	
	public GBotCommand(GamingBlockPlug_1_11 plugin)
	{
		super("gbpbotdiscordcontrol");
		this.gbp = plugin;
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(!PermissionsHelper.testPermission(sender, Permissions.impossiblePermission, true, "This command is only for JesFot & wormsor !"))
		{
			return true;
		}
		if(args.length == 1)
		{
			if(args[0].equalsIgnoreCase("start"))
			{
				this.gbp.startDiscord();
			}
			else if(args[0].equalsIgnoreCase("stop"))
			{
				this.gbp.stopDiscord();
			}
			else if(args[0].equalsIgnoreCase("restart"))
			{
				this.gbp.restartDiscord();
			}
			else
			{
				return false;
			}
			return true;
		}
		return false;
	}
}