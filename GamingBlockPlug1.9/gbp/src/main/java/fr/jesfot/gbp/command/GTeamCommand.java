package fr.jesfot.gbp.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.permission.PermissionsHelper;
import fr.jesfot.gbp.teams.GTeam;
import fr.jesfot.gbp.utils.Utils;

public class GTeamCommand extends CommandBase
{
	private GamingBlockPlug_1_9 gbp;
	
	public GTeamCommand(GamingBlockPlug_1_9 plugin)
	{
		super("gteams");
		this.gbp = plugin;
		this.setRawUsageMessage("/<com> list "
				+ "| /<com> add|remove <team> "
				+ "| /<com> option <team> <optname> <value> "
				+ "| /<com> join <team> <player> "
				+ "| /<com> leave <player>");
		plugin.getPermissionManager().addPermission("GamingBlockPlug.teams", PermissionDefault.OP, "Allows you to manage teams", Permissions.globalGBP);
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(!command.getName().equalsIgnoreCase("gteams"))
		{
			return false;
		}
		if(!PermissionsHelper.testPermission(sender, "GamingBlockPlug.teams", false, null))
		{
			return true;
		}
		if(args.length < 1)
		{
			this.sendUsage(sender, label);
			return true;
		}
		if(args[0].equalsIgnoreCase("list") && args.length == 1)
		{
			Set<String> ts = this.gbp.getTeams().getTeamList();
			Iterator<String> it = ts.iterator();
			if(ts.size() < 10)
			{
				sender.sendMessage(ChatColor.GOLD + "Registred teams : ");
				while(it.hasNext())
				{
					sender.sendMessage(ChatColor.AQUA + " - "+it.next());
				}
			}
			else
			{
				String result = it.next();
				while(it.hasNext())
				{
					result += ", " + it.next();
				}
				sender.sendMessage(ChatColor.GOLD + "Registred teams : " + ChatColor.RESET + result);
			}
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
		if(args[0].equalsIgnoreCase("options") && args.length >= 3)
		{
			String teamName = args[1];
			String opt = args[2];
			if(!this.gbp.getTeams().existsTeam(teamName))
			{
				sender.sendMessage("Cannot set options of a non existing team.");
				return true;
			}
			if(args.length == 3)
			{
				String res = this.gbp.getTeams().getIfExists(teamName).getOrSet(opt, null);
				if(res == null)
				{
					sender.sendMessage("No option of type " + opt);
					return true;
				}
				sender.sendMessage(opt + ": '" + res + "'");
			}
			else
			{
				String res = this.gbp.getTeams().getIfExists(teamName).getOrSet(opt, args[3]);
				if(res == null)
				{
					sender.sendMessage("No option of type " + opt);
					return true;
				}
				Command.broadcastCommandMessage(sender, "Changed team " + teamName + " option " + opt + " to " + res, true);
			}
			return true;
		}
		if(args[0].equalsIgnoreCase("join") && args.length == 3)
		{
			String teamName = args[1];
			String playerName = args[2];
			OfflinePlayer player = this.gbp.getPlayerExact(playerName);
			if(player == null)
			{
				player = this.gbp.getOfflinePlayerByName(playerName);
				if(player == null)
				{
					sender.sendMessage(this.gbp.getLang().get("player.notfound").replaceAll("<player>", playerName));
					return true;
				}
			}
			if(this.gbp.getTeams().join(teamName, player))
			{
				Command.broadcastCommandMessage(sender, "Added " + player.getName() + " into the team " + teamName, true);
			}
			else
			{
				sender.sendMessage("The team " + teamName + " doesn't exists...");
			}
			return true;
		}
		if(args[0].equalsIgnoreCase("leave") && args.length == 2)
		{
			String playerName = args[1];
			OfflinePlayer player = this.gbp.getOfflinePlayerByName(playerName);
			if(player == null)
			{
				sender.sendMessage(this.gbp.getLang().get("player.notfound").replaceAll("<player>", playerName));
				return true;
			}
			this.gbp.getTeams().leave(player);
			Command.broadcastCommandMessage(sender, "Added " + player.getName() + " into the default team", true);
			return true;
		}
		this.sendUsage(sender, label);
		return true;
	}
	
	@Override
	public List<String> executeTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		if(!PermissionsHelper.testPermissionSilent(sender, "GamingBlockPlug.teams", false))
		{
			return super.executeTabComplete(sender, command, alias, args);
		}
		List<String> result = new ArrayList<String>();
		if(args.length == 1)
		{
			result.add("list");
			result.add("add");
			result.add("remove");
			result.add("options");
			result.add("join");
			result.add("leave");
		}
		else
		{
			if(args[0].equalsIgnoreCase("join") && args.length == 3)
			{
				return this.getPlayers(args[2]);
			}
			if(args[0].equalsIgnoreCase("leave") && args.length == 2)
			{
				return this.getPlayers(args[1]);
			}
			if(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("leave"))
			{
				return Collections.emptyList();
			}
			if(args.length == 2)
			{
				return this.sortStart(args[1], Utils.convert(this.gbp.getTeams().getTeamList()));
			}
			if(args[0].equalsIgnoreCase("options"))
			{
				if(args.length == 3)
				{
					result.add("Color");
					result.add("ChatColor");
					result.add("DisplayName");
					result.add("MaxHomes");
					result.add("CanUseTpa");
					result.add("CanUseFly");
					result.add("CanUseWorld");
					result.add("CanOpenShops");
				}
				else if(args.length == 4)
				{
					result.addAll(this.gbp.getTeams().getIfExists(args[1]).getPossibilities(args[2]));
				}
				else
				{
					return Collections.emptyList();
				}
			}
		}
		return this.sortStart(args[args.length - 1], result);
	}
}