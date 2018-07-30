package me.jesfot.gamingblockplug.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.jesfot.gamingblockplug.permission.PermGroup;
import me.jesfot.gamingblockplug.permission.PermissionHelper;
import me.jesfot.gamingblockplug.permission.StaticPerms;
import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;
import me.jesfot.gamingblockplug.roles.Role;
import me.unei.lang.plugin.UneiLang;

public class RoleCommand extends CommandBase
{
	private final GamingBlockPlug plugin;

	public RoleCommand(GamingBlockPlug plugin)
	{
		super("role");
		
		this.plugin = plugin;
		super.setMinimalPermission(StaticPerms.CMD_ROLE);
		super.setRawUsageMessage("/<command> list "
				+ "| /<command> add|remove <role> "
				+ "| /<command> option <role> <optname> [value] "
				+ "| /<command> set <player> <role> "
				+ "| /<command> reset <player>");
	}

	@Override
	protected boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (args.length < 1)
		{
			super.sendUsage(sender, label);
			return true;
		}
		if (args[0].equalsIgnoreCase("list"))
		{
			if (!PermissionHelper.testPermission(sender, StaticPerms.CMD_ROLE_LIST, false, null))
			{
				return true;
			}
			Set<String> rl = this.plugin.getRoleManager().getRoleList();
			Iterator<String> it = rl.iterator();
			sender.sendMessage(ChatColor.GOLD + "Roles : ");
			if (rl.size() < 10)
			{
				while (it.hasNext())
				{
					sender.sendMessage(ChatColor.AQUA + " - " + it.next());
				}
			}
			else
			{
				StringBuilder sb = new StringBuilder(it.next());
				while (it.hasNext())
				{
					sb.append(',').append(' ').append(it.next());
				}
				sender.sendMessage(ChatColor.AQUA + sb.toString());
			}
		}
		else if (args[0].equalsIgnoreCase("add") && args.length >= 2)
		{
			if (!PermissionHelper.testPermission(sender, StaticPerms.CMD_ROLE_ADD, false, null))
			{
				return true;
			}
			String roleName = args[1];
			if (this.plugin.getRoleManager().exists(roleName))
			{
				sender.sendMessage("This role already exists.");
				return true;
			}
			Role role = new Role(this.plugin, roleName);
			this.plugin.getRoleManager().create(role);
			PermGroup.registerGroupAsync(roleName);
			Command.broadcastCommandMessage(sender, "Created new role " + roleName + " with default properties", true);
		}
		else if (args[0].equalsIgnoreCase("remove") && args.length >= 2)
		{
			if (!PermissionHelper.testPermission(sender, StaticPerms.CMD_ROLE_ADD, false, null))
			{
				return true;
			}
			String roleName = args[1];
			if (!this.plugin.getRoleManager().exists(roleName))
			{
				sender.sendMessage("This role don't already exists.");
				return true;
			}
			this.plugin.getRoleManager().delete(roleName);
			PermGroup.deleteGroupAsync(roleName);
			Command.broadcastCommandMessage(sender, "Deleted role " + roleName, true);
		}
		else if (args[0].equalsIgnoreCase("set") && args.length >= 3)
		{
			if (!PermissionHelper.testPermission(sender, StaticPerms.CMD_ROLE_SET, false, null))
			{
				return true;
			}
			String roleName = args[2];
			String playerName = args[1];
			OfflinePlayer player = this.plugin.getPlayerExact(playerName);
			if (player == null)
			{
				player = this.plugin.getOfflinePlayerByName(playerName);
				if (player == null)
				{
					sender.sendMessage(UneiLang.getInstance().getPlayerManager().getDefaultLanguage().get("player.notfound", "Player <player> not found.")
							.replaceAll("<player>", playerName));
					return true;
				}
			}
			if (this.plugin.getRoleManager().setRole(player, roleName))
			{
				Command.broadcastCommandMessage(sender, "Set the role for " + playerName + " to " + roleName, true);
			}
			else
			{
				sender.sendMessage("Unknown role '" + roleName + "'");
			}
		}
		else if (args[0].equalsIgnoreCase("reset") && args.length >= 2)
		{
			if (!PermissionHelper.testPermission(sender, StaticPerms.CMD_ROLE_SET, false, null))
			{
				return true;
			}
			String playerName = args[1];
			OfflinePlayer player = this.plugin.getPlayerExact(playerName);
			if (player == null)
			{
				player = this.plugin.getOfflinePlayerByName(playerName);
				if (player == null)
				{
					sender.sendMessage(UneiLang.getInstance().getPlayerManager().getDefaultLanguage().get("player.notfound", "Player <player> not found.")
							.replaceAll("<player>", playerName));
					return true;
				}
			}
			this.plugin.getRoleManager().resetRole(player);
			Command.broadcastCommandMessage(sender, "Reset the role for " + playerName + " to default", true);
		}
		else if (args[0].equalsIgnoreCase("option") && args.length >= 3)
		{
			String roleName = args[1];
			String option = args[2];
			if (!this.plugin.getRoleManager().exists(roleName))
			{
				sender.sendMessage("Unknown role '" + roleName + "'");
				return true;
			}
			if (args.length == 3)
			{
				String res = this.plugin.getRoleManager().get(roleName).getOrSet(option, null);
				if (res != null)
				{
					sender.sendMessage("Option '" + option + "' for role '" + roleName + "': '" + res + "'");
				}
				else
				{
					sender.sendMessage("No such option '" + option + "' for role '" + roleName + "'");
				}
			}
			else
			{
				if (!PermissionHelper.testPermission(sender, StaticPerms.CMD_ROLE_MODIF, false, null))
				{
					return true;
				}
				String res = this.plugin.getRoleManager().get(roleName).getOrSet(option, args[3]);
				if (res == null)
				{
					Command.broadcastCommandMessage(sender, "Option '" + option + "' changed to '" + args[3] + "' for role '" + roleName + "'", true);
				}
				else
				{
					sender.sendMessage("No such option '" + option + "' for role '" + roleName + "'");
				}
			}
		}
		return true;
	}
	
	@Override
	protected List<String> executeTabComplete(CommandSender sender, Command command, String label, String[] args)
	{
		List<String> result = new ArrayList<>(8);
		if (args.length == 1)
		{
			result.add("option");
			if (PermissionHelper.testPermissionSilent(sender, StaticPerms.CMD_ROLE_LIST, true))
				result.add("list");
			if (PermissionHelper.testPermissionSilent(sender, StaticPerms.CMD_ROLE_ADD, true)) {
				result.add("add");
				result.add("remove");
			}
			if (PermissionHelper.testPermissionSilent(sender, StaticPerms.CMD_ROLE_SET, true)) {
				result.add("set");
				result.add("reset");
			}
		}
		else if (args.length == 2)
		{
			if (args[0].equalsIgnoreCase("remove") && PermissionHelper.testPermissionSilent(sender, StaticPerms.CMD_ROLE_ADD, true))
			{
				result.addAll(this.plugin.getRoleManager().getRoleList());
			}
			else if (args[0].equalsIgnoreCase("option"))
			{
				result.addAll(this.plugin.getRoleManager().getRoleList());
			}
			else if (PermissionHelper.testPermissionSilent(sender, StaticPerms.CMD_ROLE_SET, true)
					&& (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("reset")))
			{
				return null;
			}
		}
		else if (args.length == 3)
		{
			if (PermissionHelper.testPermissionSilent(sender, StaticPerms.CMD_ROLE_SET, true) && args[0].equalsIgnoreCase("set"))
			{
				result.addAll(this.plugin.getRoleManager().getRoleList());
			}
			else if (args[0].equalsIgnoreCase("option"))
			{
				result.add("Color");
				result.add("ChatColor");
				result.add("DisplayName");
			}
		}
		else if (args.length == 4 && args[0].equalsIgnoreCase("option"))
		{
			if (PermissionHelper.testPermissionSilent(sender, StaticPerms.CMD_ROLE_MODIF, true))
			{
				result.addAll(this.plugin.getRoleManager().get(args[1]).getOptions(args[2]));
			}
		}
		return sortStart(args[args.length - 1], result);
	}
}
