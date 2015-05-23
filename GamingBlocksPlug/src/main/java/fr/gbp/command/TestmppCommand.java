package fr.mpp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TestmppCommand implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (label.equalsIgnoreCase("testmpp"))
		{
			String msg;
			if (args.length >= 1)
			{
				msg = "Et votre texte : " + args[0];
			}
			else
			{
				msg = "";
			}
			sender.sendMessage("Cette commande et donc le plugin fonctionnent." + msg);
			return true;
		}
		return false;
	}

}
