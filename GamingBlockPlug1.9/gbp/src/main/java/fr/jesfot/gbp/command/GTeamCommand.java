package fr.jesfot.gbp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.teams.GTeam;

public class GTeamCommand extends CommandBase
{
	private GamingBlockPlug_1_9 gbp;
	
	public GTeamCommand(GamingBlockPlug_1_9 plugin)
	{
		super("gteams");
		this.gbp = plugin;
		this.setRawUsageMessage("/<com> add|remove|option|join|leave <Team> <...>");
		plugin.getPermissionManager().addPermission("GamingBlockPlug.teams", PermissionDefault.OP, "Allows you to manage teams", Permissions.globalGBP);
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(!command.getName().equalsIgnoreCase("gteams"))
		{
			return false;
		}
		if(args.length <= 1)
		{
			this.sendUsage(sender, label);
			return true;
		}
		if(args[0].equalsIgnoreCase("add") && args.length == 2)
		{
			String teamName = args[1];
			if(this.gbp.getTeams().existsTeam(teamName))
			{
				sender.sendMessage("This team already exists.");
				return true;
			}
			GTeam team = new GTeam(this.gbp, teamName);
			this.gbp.getTeams().create(team);
			Command.broadcastCommandMessage(sender, "Created new team " + teamName + " with default properties", true);
			return true;
		}
		if(args[0].equalsIgnoreCase("remove") && args.length == 2)
		{
			String teamName = args[1];
			if(!this.gbp.getTeams().existsTeam(teamName))
			{
				sender.sendMessage("This team didn't exists.");
				return true;
			}
			this.gbp.getTeams().delete(teamName);
			Command.broadcastCommandMessage(sender, "Removed team " + teamName, true);
			return true;
		}
		return true;
	}
}