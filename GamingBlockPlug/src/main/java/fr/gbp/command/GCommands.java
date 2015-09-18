package fr.gbp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.gbp.MetalPonyPlug;

public class GCommands
{
	private final GamingBlockPlug gbp;
	
	public GCommands(GamingBlockPlug gamingbp)
	{
		this.gbp = gamingbp;
	}
	
	public void regCommands()
	{
		this.regCommands(gbp);
	}
	
	public void regCommands(MetalPonyPlug gbpl)
	{
		gbpl.getPlugin().getCommand("testgbp").setExecutor(new TestmppCommand());
	}
	
	public static boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		return true;
	}
}
