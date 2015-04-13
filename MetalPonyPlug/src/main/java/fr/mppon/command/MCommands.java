package fr.mppon.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.mppon.MetalPonyPlug_on;

public class MCommands
{
	private final MetalPonyPlug_on mpp;
	
	public MCommands(MetalPonyPlug_on metalpp)
	{
		this.mpp = metalpp;
	}
	
	/**
	 * Register all the MetalPonyPlug commands
	 */
	public void regCommands()
	{
		this.regCommands(mpp);
	}
	
	/**
	 * Register all the MetalPonyPlug commands
	 * 
	 * @param mppl - The main file of this plugin
	 */
	public void regCommands(MetalPonyPlug_on mppl)
	{
		mppl.getPlugin().getCommand("mpp-on").setExecutor(new MOnCommand(mppl)); // Switch off the plugin
	}
	
	public static boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) // Like command (detect cheat) listener
	{
		return false;
	}
	
	public static List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return null;
	}
}
