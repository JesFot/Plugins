package fr.jesfot.gbp.command;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.permissions.PermissionDefault;

import fr.jesfot.gbp.GamingBlockPlug_1_11;
import fr.jesfot.gbp.economy.GEconomy;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.permission.PermissionsHelper;
import fr.jesfot.gbp.teams.GTeam;
import fr.jesfot.gbp.teams.TeamManager;

public class GSalaryCommand extends CommandBase
{
	private GamingBlockPlug_1_11 gbp;
	
	public GSalaryCommand(GamingBlockPlug_1_11 plugin)
	{
		super("salary");
		this.setRawUsageMessage("/<com>");
		this.gbp = plugin;
		plugin.getPermissionManager().addPermission("GamingBlockPlug.salary", PermissionDefault.FALSE,
				"Salary system whole permission", Permissions.globalGBP);
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(!(sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender))
		{
			if(!PermissionsHelper.testPermission(sender, "GamingBlockPlug.salary", false, null))
			{
				return true;
			}
		}
		if(args.length == 0)
		{
			GEconomy economy = this.gbp.getEconomy();
			TeamManager teams = this.gbp.getTeams();
			List<String> players = economy.getList(Collections.<String>emptyList());
			for(String player : players)
			{
				GTeam team = teams.getTeamOf(this.gbp.getOfflinePlayerByName(player));
				economy.getPEconomy(this.gbp.getOfflinePlayerByName(player)).add(team.getSalary());
			}
			return true;
		}
		this.sendUsage(sender, label);
		return true;
	}
}