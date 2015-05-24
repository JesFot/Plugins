package fr.gbp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.gbp.GamingBlocksPlug;

public class MCommands
{
	private final GamingBlocksPlug gbp;
	
	public MCommands(GamingBlocksPlug gamingbp)
	{
		this.gbp = gamingbp;
	}
	
	public void regCommands()
	{
		this.regCommands(gbp);
	}
	
	public void regCommands(GamingBlocksPlug gbpl)
	{
		gbpl.getPlugin().getCommand("testgbp").setExecutor(new TestgbpCommand());
		gbpl.getPlugin().getCommand("addme").setExecutor(new EcoCommand(gbpl));
		gbpl.getPlugin().getCommand("acount").setExecutor(new EcoCommand(gbpl));
		//gbpl.getPlugin().getCommand("bank").setExecutor(new BankCommand());
		gbpl.getPlugin().getCommand("toolb").setExecutor(new BankCommand());
	}
	
	public static boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		return true;
	}
}