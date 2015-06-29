package fr.mpp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mpp.MetalPonyPlug;
import fr.mpp.citys.Cityzen;

public class OpenCityCommand implements CommandExecutor
{
	private MetalPonyPlug mpp;
	private String usageMessage;
	
	public OpenCityCommand(MetalPonyPlug mppl)
	{
		this.mpp = mppl;
		this.usageMessage = ChatColor.DARK_RED + "/opencity <doorID> [open|close]";
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		boolean hasRight = false;
		this.usageMessage.replaceAll("opencity", label.toLowerCase());
		
		if(!cmd.getName().equalsIgnoreCase("opencity"))
		{
			return false;
		}
		if(!(sender instanceof Player))
		{
			hasRight = true;
		}
		else
		{
			hasRight = false;//this.mpp.mainCity.getAllCityzens().contains(new Cityzen((Player)sender));
		}
		
		if(args.length <= 0 || args.length >= 3)
		{
			sender.sendMessage(this.usageMessage);
			return true;
		}
		
		if(!hasRight)
		{
			sender.sendMessage(ChatColor.DARK_PURPLE + "You are not alowed to access to that city");
			return true;
		}
		return true;
	}
}
