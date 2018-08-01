package fr.jesfot.gbp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import fr.jesfot.gbp.GamingBlockPlug_1_12;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.permission.PermissionsHelper;

public class LogMessageCommand extends CommandBase
{
	private GamingBlockPlug_1_12 gbp;
	
	public LogMessageCommand(GamingBlockPlug_1_12 plugin)
	{
		super("logmessage");
		this.gbp = plugin;
		this.setRawUsageMessage("/<com> [set <text...>]");
		plugin.getPermissionManager().addPermission("GamingBlockPlug.setlogmsg", PermissionDefault.OP,
				"Allows you to edit the log-message", Permissions.globalGBP);
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 0)
		{
			this.gbp.getConfigs().loadAll();
			if(this.gbp.getConfigs().getMainConfig().getConfig().contains("logmsg")
					&& this.gbp.getConfigs().getMainConfig().getConfig().getString("logmsg") != null)
			{
				String logmsg = this.gbp.getConfigs().getMainConfig().getConfig().getString("logmsg");
				String[] logmsgs = logmsg.split(" n ");
				for(String str : logmsgs)
				{
					sender.sendMessage(ChatColor.GOLD + str);
				}
			}
			else
			{
				sender.sendMessage(ChatColor.BOLD + "No log-message registred");
			}
		}
		else if(args.length >= 2)
		{
			if(!args[0].equalsIgnoreCase("set"))
			{
				this.sendUsage(sender, label);
				return true;
			}
			if(!PermissionsHelper.testPermission(sender, "GamingBlockPlug.setlogmsg", true, null))
			{
				return true;
			}
			String msg = "";
			for(int i = 1; i < args.length; i++)
			{
				if(args[i].toLowerCase().startsWith("http"))
				{
					args[i] = ChatColor.BLUE + "" + ChatColor.UNDERLINE + args[i] + ChatColor.RESET;
				}
				msg += args[i] + " ";
			}
			this.gbp.getConfigs().getMainConfig().getConfig().set("logmsg", msg);
			this.gbp.getConfigs().saveIfChanged();
		}
		else
		{
			this.sendUsage(sender, label);
		}
		return true;
	}
}