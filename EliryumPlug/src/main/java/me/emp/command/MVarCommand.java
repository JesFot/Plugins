package me.emp.command;

import me.emp.EliryumPlug;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MVarCommand implements CommandExecutor
{
	private EliryumPlug emp;
	
	public MVarCommand(EliryumPlug empl)
	{
		this.emp = empl;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!cmd.getName().equalsIgnoreCase("var"))
		{
			return false;
		}
		if(args.length == 4)
		{
			if(args[0].equalsIgnoreCase("set"))
			{
				String name = args[1];
				String type = args[2].toLowerCase();
				String value = args[3];
				switch(type)
				{
				case "str":
				case "string":
					this.emp.getMVO().storeString(name, value);
					break;
				case "int":
				case "integer":
					this.emp.getMVO().storeInt(name, Integer.parseInt(value));
					break;
				case "bool":
				case "boolean":
					this.emp.getMVO().storeBool(name, Boolean.getBoolean(value.toLowerCase()));
					break;
				case "float":
					this.emp.getMVO().storeFloat(name, Float.parseFloat(value));
					break;
				}
				sender.sendMessage("Registered.");
				this.emp.getMVO().storeToFile();
			}
		}
		else if(args.length == 2)
		{
			if(args[0].equalsIgnoreCase("unset"))
			{
				String name = args[1];
				this.emp.getMVO().remove(name);
			}
		}
		else if(args.length == 1)
		{
			sender.sendMessage("Value: "+this.emp.getMVO().getToString(args[0]));
		}
		return true;
	}
}
