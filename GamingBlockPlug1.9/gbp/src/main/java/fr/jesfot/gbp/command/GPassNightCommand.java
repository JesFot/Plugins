package fr.jesfot.gbp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.permission.PermissionsHelper;
import fr.jesfot.gbp.subsytems.HalfBedSys;

public class GPassNightCommand extends CommandBase
{
	private final HalfBedSys hbs;
	
	public GPassNightCommand(GamingBlockPlug_1_9 plugin)
	{
		super("forcepassnight");
		this.hbs = new HalfBedSys(plugin);
		plugin.getPermissionManager().addPermission("GamingBlockPlug.passnight", PermissionDefault.OP,
				"Allows you to force the day to come...", Permissions.globalGBP);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(PermissionsHelper.testPermission(sender, "GamingBlockPlug.passnight", true, "You are not allowed to force"
				+ " this system !"))
		{
			this.hbs.passNight(this.hbs.getPlayersInBed());
		}
		return true;
	}
}