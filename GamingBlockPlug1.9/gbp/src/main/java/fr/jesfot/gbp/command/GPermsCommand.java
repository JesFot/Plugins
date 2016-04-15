package fr.jesfot.gbp.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.permission.PermissionsHelper;
import fr.jesfot.gbp.utils.Utils;

public class GPermsCommand extends CommandBase
{
	private GamingBlockPlug_1_9 gbp;
	
	public GPermsCommand(GamingBlockPlug_1_9 plugin)
	{
		super("perms");
		this.gbp = plugin;
		plugin.getPermissionManager().addPermission("GamingBlockPlug.perms", PermissionDefault.OP, "Allows you to manage permissions", Permissions.globalGBP);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(!PermissionsHelper.testPermission(sender, "GamingBlockPlug.perms", true, null))
		{
			return true;
		}
		if(args.length < 1)
		{
			sender.sendMessage("Usage: /perms [player] <[-]permission> | /perms &<permission>");
			return true;
		}
		if(args.length == 1)
		{
			String perm = args[0];
			if(perm.startsWith("&"))
			{
				perm = perm.substring(1);
				Permission permi = this.gbp.getPermissionManager().getPermission(perm);
				if(permi == null)
				{
					sender.sendMessage(this.gbp.getLang().get("perms.notfound", "Permission not found."));
					return true;
				}
				sender.sendMessage(ChatColor.GREEN + "-------------------------------------------");
				sender.sendMessage(ChatColor.DARK_PURPLE + "PermissionId = " + permi.getName());
				sender.sendMessage(ChatColor.BLUE + "Description: " + permi.getDescription());
				sender.sendMessage(ChatColor.BLUE + "PermDefault: " + permi.getDefault().name());
				sender.sendMessage(ChatColor.GREEN + "-------------------------------------------");
			}
			else
			{
				boolean minus = perm.startsWith("-");
				if(minus)
				{
					perm = perm.substring(1);
				}
				if(sender instanceof Player)
				{
					Player player = (Player)sender;
					if(this.gbp.getPermissionManager().getPermission(perm) == null)
					{
						sender.sendMessage(this.gbp.getLang().get("perms.notfound", "Permission not found."));
						return true;
					}
					sender.sendMessage(this.gbp.getLang().get("perms.changed", "Permission <perm> has been set to <value> for you")
							.replace("<perm>", perm)
							.replace("<value>", minus ? "false" : "true").replace("<target>", player.getName()));
					this.gbp.getPermissionManager().setPerm(player, Utils.translate(this.gbp.getPermissionManager().getPermissions(perm)), !minus);
				}
				else
				{
					sender.sendMessage("Console is not a player.");
				}
			}
		}
		else
		{
			String perm = args[1];
			boolean minus = perm.startsWith("-");
			if(minus)
			{
				perm = perm.substring(1);
			}
			Player player = this.gbp.getPlayerExact(args[0]);
			if(player == null)
			{
				sender.sendMessage(this.gbp.getLang().get("player.notfound"));
				return true;
			}
			if(perm.startsWith("+"))
			{
				perm = perm.substring(1);
				if(this.gbp.getPermissionManager().getPermission(perm) == null)
				{
					Set<String> t = new HashSet<String>();
					t.add(perm);
					this.gbp.getPermissionManager().setPermS(player, t, !minus);
				}
				sender.sendMessage(this.gbp.getLang().get("perms.changed", "Permission <perm> has been set to <value> for <target>")
						.replace("<perm>", perm)
						.replace("<value>", minus ? "false" : "true").replace("<target>", player.getName()));
				return true;
			}
			if(this.gbp.getPermissionManager().getPermission(perm) == null)
			{
				sender.sendMessage(this.gbp.getLang().get("perms.notfound"));
				return true;
			}
			sender.sendMessage(this.gbp.getLang().get("perms.changed", "Permission <perm> has been set to <value> for <target>")
					.replace("<perm>", perm)
					.replace("<value>", minus ? "false" : "true").replace("<target>", player.getName()));
			this.gbp.getPermissionManager().setPerm(player, Utils.translate(this.gbp.getPermissionManager().getPermissions(perm)), !minus);
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		if(args.length == 1 || args.length == 2)
		{
			List<String> result = new ArrayList<String>();
			for(String str : this.gbp.getPermissionManager().getAllPermNames())
			{
				if(str.startsWith(args[args.length - 1]))
				{
					result.add(str);
				}
				else if(("-"+str).startsWith(args[args.length - 1]))
				{
					result.add("-"+str);
				}
				else if(("&"+str).startsWith(args[args.length - 1]))
				{
					result.add("&"+str);
				}
				else if(("+"+str).startsWith(args[args.length - 1]))
				{
					result.add("+"+str);
				}
				else if(("-+"+str).startsWith(args[args.length - 1]))
				{
					result.add("-+"+str);
				}
			}
			if(args.length == 1)
			{
				for(Player pl : this.gbp.getOnlinePlayers())
				{
					if(pl.getName().startsWith(args[0]))
					{
						result.add(pl.getName());
					}
				}
			}
			return result;
		}
		return Collections.emptyList();
	}
}