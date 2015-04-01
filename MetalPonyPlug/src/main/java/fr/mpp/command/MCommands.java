package fr.mpp.command;

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
	
	public void regCommands()
	{
		this.regCommands(mpp);
	}
	
	public void regCommands(MetalPonyPlug mppl)
	{
		mppl.getPlugin().getCommand("testmpp").setExecutor(new TestmppCommand());
		mppl.getPlugin().getCommand("mpp").setExecutor(new MppCommand(mppl));
		mppl.getPlugin().getCommand("mpp-off").setExecutor(new MppOffCommand(mppl));
	}
	
	public static boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		return false;
	}
}
