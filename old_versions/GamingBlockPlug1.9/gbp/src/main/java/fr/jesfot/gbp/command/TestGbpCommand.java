package fr.jesfot.gbp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TestGbpCommand extends CommandBase
{
	public TestGbpCommand()
	{
		super("testgbp");
		this.setRawUsageMessage("/<com> [<text...>]");
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		String msg = "Cette commande et donc le plugin fonctionnent.";
		if(args != null && args.length >= 1)
		{
			msg += " Votre texte:";
			for(String arg : args)
			{
				msg += " " + arg;
			}
		}
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
		Command.broadcastCommandMessage(sender, "Player used command testGBP, text: '" + msg + "'", false);
		
		return true;
	}
}
