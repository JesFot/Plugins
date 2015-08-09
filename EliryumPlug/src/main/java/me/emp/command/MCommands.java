package me.emp.command;

import java.util.List;

import me.emp.EliryumPlug;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MCommands
{
	private final EliryumPlug emp;
	
	public MCommands(EliryumPlug metalpp)
	{
		this.emp = metalpp;
	}
	
	/**
	 * Register all the MetalPonyPlug commands
	 */
	public void regCommands()
	{
		this.regCommands(emp);
	}
	
	/**
	 * Register all the MetalPonyPlug commands
	 * 
	 * @param mppl - The main file of this plugin
	 */
	public void regCommands(EliryumPlug empl)
	{
		//empl.getPlugin().getCommand("testmpp").setExecutor(new TestmppCommand()); // Command to test that the plugin is active
		SwitchCommand switchCommand = new SwitchCommand(empl);
		empl.getPlugin().getCommand("switch").setExecutor(switchCommand); // Switch on or off a plugin
		empl.getPlugin().getCommand("switch").setTabCompleter(switchCommand);
		//empl.getPlugin().getCommand("mhome").setExecutor(new MHomeCommand(empl)); // Like /home of essentials with '/mhome set' to set
		//empl.getPlugin().getCommand("mtpa").setExecutor(new MtpaCommand(empl)); // Like /tpa of essentials but, ask method change
		//empl.getPlugin().getCommand("logmessage").setExecutor(new LogMessageCommand(empl));
		//empl.getPlugin().getCommand("seed").setExecutor(new MSeedCommand());
		//empl.getPlugin().getCommand("seed").setTabCompleter(new MSeedCommand());
		//empl.getPlugin().getCommand("world").setExecutor(new MWorldCommand(empl));
		MVarCommand varCommand = new MVarCommand(empl);
		empl.getPlugin().getCommand("var").setExecutor(varCommand);
		MMaskCommand maskCommand = new MMaskCommand(empl);
		empl.getPlugin().getCommand("mask").setExecutor(maskCommand);
		//empl.getPlugin().getCommand("mtpc").setExecutor(new MTpcCommand());
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) // Like command (detect cheat) listener
	{
		return false;
	}
	
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return null;
	}
}
