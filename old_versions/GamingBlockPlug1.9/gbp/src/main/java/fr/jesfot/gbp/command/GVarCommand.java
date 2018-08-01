package fr.jesfot.gbp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.subsytems.VariableSys;
import fr.jesfot.gbp.utils.Utils;

public class GVarCommand extends CommandBase
{
	private GamingBlockPlug_1_9 gbp;
	
	public GVarCommand(GamingBlockPlug_1_9 plugin)
	{
		super("var");
		this.gbp = plugin;
		this.setRawUsageMessage("/<com> <VariableName> | /<com> unset <VariableName> | "
				+ "/<com> set <VariableName> <type> <Value...> | /<com> help <all|RubricName>");
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!cmd.getName().equalsIgnoreCase("var"))
		{
			return false;
		}
		if(args.length >= 4)
		{
			if(args[0].equalsIgnoreCase("set"))
			{
				/*if(!EPermissions.testPermission(sender, "Eliryum.var.set", ChatColor.RED + "You are not allowed to use the /var set command. "
						+ "Sorry, please contact an administrator if you believe that is an error."))
				{
					return true;
				}*/
				String name = args[1];
				String type = args[2].toLowerCase();
				//String value = MUtils.compile(3, " ", args);
				String value = Utils.compile(args, 3, " ");
				switch(VariableSys.getIdForTypes(type))
				{
				case 0:
					this.gbp.getVars().storeString(name, value);
					break;
				case 1:
					this.gbp.getVars().storeInt(name, Utils.toInt(value, 0));
					break;
				case 2:
					this.gbp.getVars().storeBool(name, Boolean.getBoolean(value.toLowerCase()));
					break;
				case 3:
					this.gbp.getVars().storeFloat(name, Utils.toFloat(value, 0));
					break;
				case 4:
					this.gbp.getVars().storeDouble(name, Utils.toDouble(value, 0));
					break;
				}
				sender.sendMessage("Registered.");
				this.gbp.getVars().storeToFile();
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
				/*if(!EPermissions.testPermission(sender, "Eliryum.var.set", ChatColor.RED + "You are not allowed to use the /var unset command. "
						+ "Sorry, please contact an administrator if you believe that is an error."))
				{
					return true;
				}*/
				String name = args[1];
				this.gbp.getVars().remove(name);
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
			/*if(!EPermissions.testPermission(sender, "Eliryum.var.view", ChatColor.RED
					+ "You are not allowed to use the /var <<name>> command. ".replace("<name>", args[0])
					+ "Sorry, please contact an administrator if you believe that is an error."))
			{
				return true;
			}*/
			if(this.gbp.getVars().getToString(args[0]) == null)
			{
				sender.sendMessage("'" + args[0] + "' does not exists as ley value.");
				return true;
			}
			sender.sendMessage("Value: "+ChatColor.translateAlternateColorCodes('&', this.gbp.getVars().getToString(args[0])));
		}
		else
		{
			this.sendUsage(sender, label);
		}
		return true;
	}
}