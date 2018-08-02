package me.jesfot.gamingblockplug.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.jesfot.gamingblockplug.permission.PermissionHelper;
import me.jesfot.gamingblockplug.permission.StaticPerms;
import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;
import me.jesfot.gamingblockplug.security.VariablesSystem;
import me.jesfot.gamingblockplug.utils.NumberUtils;

public class VarCommand extends CommandBase
{
	private final GamingBlockPlug plugin;
	
	public VarCommand(GamingBlockPlug plugin)
	{
		super("var");
		
		this.plugin = plugin;
		
		super.setMinimalPermission(StaticPerms.CMD_VAR);
		super.setRawUsageMessage("/<command> <VariableName> | /<command> unset <VariableName> | "
				+ "/<command> set <VariableName> <type> <Value...> | /<command> help <all|RubricName>");
	}
	
	@Override
	protected boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		VariablesSystem varSystem = this.plugin.getSystemManager().getVariablesSystem();
		varSystem.loadFromFile();
		if(args.length >= 4)
		{
			if(args[0].equalsIgnoreCase("set"))
			{
				String name = args[1];
				boolean exists = (varSystem.getType(name) != null);
				if(!PermissionHelper.testPermission(sender, exists ? StaticPerms.CMD_VAR_RESET : StaticPerms.CMD_VAR_SET, false, null))
				{
					return true;
				}
				String type = args[2].toLowerCase();
				String value = compile(args, 3, " ");
				switch(VariablesSystem.getIdForTypes(type))
				{
				case 0:
					varSystem.storeString(name, value);
					break;
				case 1:
					varSystem.storeInt(name, NumberUtils.toInteger(value, 0));
					break;
				case 2:
					varSystem.storeBool(name, Boolean.getBoolean(value.toLowerCase()));
					break;
				case 3:
					varSystem.storeFloat(name, NumberUtils.toFloat(value, 0));
					break;
				case 4:
					varSystem.storeDouble(name, NumberUtils.toDouble(value, 0));
					break;
				}
				sender.sendMessage("Registered.");
				varSystem.storeToFile();
			}
			else
			{
				this.sendUsage(sender, label);
			}
		}
		else if(args.length == 2)
		{
			if(args[0].equalsIgnoreCase("unset"))
			{
				if(!PermissionHelper.testPermission(sender, StaticPerms.CMD_VAR_UNSET, false, null))
				{
					return true;
				}
				String name = args[1];
				varSystem.remove(name);
				varSystem.storeToFile();
			}
			else if(args[0].equalsIgnoreCase("help"))
			{
				if(args[1].equalsIgnoreCase("all"))
				{
					sender.sendMessage("try /<com> help types".replaceAll("<com>", label));
				}
				else if(args[1].equalsIgnoreCase("types"))
				{
					sender.sendMessage("Available types of variable are :");
					sender.sendMessage("  - String - 'string' | 'str'");
					sender.sendMessage("  - Integer - 'integer' | 'int'");
					sender.sendMessage("  - Boolean - 'boolean' | 'bool'");
					sender.sendMessage("  - Float - 'float'");
					sender.sendMessage("  - Double - 'double'");
					return true;
				}
				else
				{
					sender.sendMessage("This topic does not exists try /<com> help all".replaceAll("<com>", label));
				}
			}
			else
			{
				this.sendUsage(sender, label);
			}
		}
		else if(args.length == 1)
		{
			if(!PermissionHelper.testPermission(sender, StaticPerms.CMD_VAR_PRINT, true, ChatColor.RED
					+ "You are not allowed to use the /var <<name>> command. ".replace("<name>", args[0])
					+ "Sorry, please contact an administrator if you believe that is an error."))
			{
				return true;
			}
			if(varSystem.getToString(args[0]) == null)
			{
				sender.sendMessage("'" + args[0] + "' does not exists as key value.");
				return true;
			}
			sender.sendMessage("Value: " + ChatColor.translateAlternateColorCodes('&', varSystem.getToString(args[0])));
		}
		else
		{
			this.sendUsage(sender, label);
		}
		return true;
	}
	
	@Override
	protected List<String> executeTabComplete(CommandSender sender, Command command, String label, String[] args)
	{
		if (args.length == 1)
		{
			return CommandBase.sortStart(args[0], Arrays.asList("set", "unset", "help"));
		}
		if (args.length == 2 && args[0].equalsIgnoreCase("help"))
		{
			return CommandBase.sortStart(args[0], Arrays.asList("types", "all"));
		}
		if (args.length == 3 && args[0].equalsIgnoreCase("set"))
		{
			return CommandBase.sortStart(args[0], Arrays.asList("string", "boolean", "integer", "double", "float"));
		}
		return Collections.emptyList();
	}
	
	public static String compile(String[] list, int firstIndex, String separator)
	{
		if (firstIndex < 0 || firstIndex >= list.length)
		{
			return "";
		}
		String result = list[firstIndex];
		for (int i = firstIndex + 1; i < list.length; i++)
		{
			result += " " + list[i];
		}
		return result;
	}
}
