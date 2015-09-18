package fr.gbp.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.gbp.GamingBlockPlug;

public class GCommands
{
	private final GamingBlockPlug gbp;
	
	public GCommands(GamingBlockPlug p_gbp)
	{
		this.gbp = p_gbp;
	}
	
	public void regCommands()
	{
		this.regCommands(this.gbp);
	}
	
	public void regCommands(GamingBlockPlug gbpl)
	{
		gbpl.getPlugin().getCommand("testgbp").setExecutor(new TestgbpCommand());
	}
	
	public static boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		return true;
	}
	
	public static List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return null;
	}
}