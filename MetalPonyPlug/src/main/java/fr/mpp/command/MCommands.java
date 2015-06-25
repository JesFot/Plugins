package fr.mpp.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.mpp.MetalPonyPlug;

public class MCommands
{
	private final MetalPonyPlug mpp;
	
	public MCommands(MetalPonyPlug metalpp)
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
	public void regCommands(MetalPonyPlug mppl)
	{
		mppl.getPlugin().getCommand("testmpp").setExecutor(new TestmppCommand(mppl)); // Comand to test that the plugin is active
		//mppl.getPlugin().getCommand("mpp").setExecutor(new MppCommand(mppl)); // Main command, will add more fonctionalities
		mppl.getPlugin().getCommand("mpp-off").setExecutor(new MppOffCommand(mppl)); // Switch off the plugin
		mppl.getPlugin().getCommand("mhome").setExecutor(new MHomeCommand(mppl)); // Like /home of essentials with '/mhome set' to set
		//mppl.getPlugin().getCommand("mppsetchest").setExecutor(new MppChestCommand(mppl)); // Set the commun place chest
		mppl.getPlugin().getCommand("mtpa").setExecutor(new MtpaCommand(mppl)); // Like /tpa of essentials but, ask method change
		//mppl.getPlugin().getCommand("election").setExecutor(new MelectionsCommand(mppl)); // This make an election conversation
		mppl.getPlugin().getCommand("economy").setExecutor(new MEcoCommand(mppl));
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
