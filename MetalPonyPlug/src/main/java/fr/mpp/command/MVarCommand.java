package fr.mpp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.mpp.MetalPonyPlug;

public class MVarCommand implements CommandExecutor
{
	private MetalPonyPlug mpp;
	
	public MVarCommand(MetalPonyPlug mppl)
	{
		this.mpp = mppl;
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
					this.mpp.getMVO().storeString(name, value);
					break;
				case "int":
				case "integer":
					this.mpp.getMVO().storeInt(name, Integer.parseInt(value));
					break;
				case "bool":
				case "boolean":
					this.mpp.getMVO().storeBool(name, Boolean.getBoolean(value.toLowerCase()));
					break;
				case "float":
					this.mpp.getMVO().storeFloat(name, Float.parseFloat(value));
					break;
				}
				sender.sendMessage("Registered.");
				this.mpp.getMVO().storeToFile();
			}
		}
		else if(args.length == 2)
		{
			if(args[0].equalsIgnoreCase("unset"))
			{
				String name = args[1];
				this.mpp.getMVO().remove(name);
				sender.sendMessage("Unseted.");
			}
		}
		else if(args.length == 1)
		{
			sender.sendMessage("Value: "+this.mpp.getMVO().getToString(args[0]));
		}
		return true;
	}
}
