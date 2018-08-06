package me.jesfot.gamingblockplug.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.jesfot.gamingblockplug.permission.PermissionHelper;
import me.jesfot.gamingblockplug.permission.StaticPerms;
import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;
import me.jesfot.gamingblockplug.utils.DataUtils;
import me.unei.configuration.api.IYAMLConfiguration;

/**
 * @author JësFot
 * @since 1.13-1.0.0
 * @version 1.0
 */
public class MOTDCommand extends CommandBase
{
	private final GamingBlockPlug plugin;
	
	public MOTDCommand(GamingBlockPlug plugin)
	{
		super("logmsg");
		
		this.plugin = plugin;
		
		super.setMinimalPermission(StaticPerms.CMD_MOTD);
		super.setRawUsageMessage("/<command> [set <message...>]");
	}
	
	@Override
	protected boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(args.length == 0)
		{
			if(!PermissionHelper.testPermission(sender, StaticPerms.CMD_MOTD_PRINT, true, null))
			{
				return true;
			}
			IYAMLConfiguration cfg = this.plugin.getConfigurations().getMainConfig();
			DataUtils.safeReload(cfg);
			if (cfg.contains("motd") && !cfg.getString("motd").isEmpty())
			{
				String msg = cfg.getString("motd");
				String[] msgs = msg.split(" n ");
				for (String str : msgs)
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
			if(!PermissionHelper.testPermission(sender, StaticPerms.CMD_MOTD_SET, false, null))
			{
				return true;
			}
			StringBuilder msg = new StringBuilder();
			for(int i = 1; i < args.length; i++)
			{
				if(args[i].toLowerCase().startsWith("http"))
				{
					msg.append(ChatColor.BLUE).append(ChatColor.UNDERLINE).append(args[i]).append(ChatColor.RESET);
				}
				else
				{
					msg.append(args[i]);
				}
				msg.append(' ');
			}
			IYAMLConfiguration cfg = this.plugin.getConfigurations().getMainConfig();
			cfg.setString("motd", msg.toString());
			cfg.save();
			Command.broadcastCommandMessage(sender, "New login message registered", true);
		}
		else
		{
			this.sendUsage(sender, label);
		}
		return true;
	}
}
