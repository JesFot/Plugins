package fr.mpp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mpp.MetalPonyPlug;

public class MppCommand implements CommandExecutor
{
	private final MetalPonyPlug mpp;

	public MppCommand(MetalPonyPlug mppl)
	{
		this.mpp = mppl;
	}

	private void resetPlugin()
	{
		// Code ...
	}

	private void resetPlayer(CommandSender sender)
	{
		if (sender instanceof Player)
		{
			Player player = (Player)sender;
			mpp.setMeta(player, "MPP", null); // A completer
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (args.length >= 1)
		{
			if (args[0].equalsIgnoreCase("reset"))
			{
				if (args[1].equalsIgnoreCase("all"))
				{
					resetPlugin();
				}
				else
				{
					resetPlayer(sender);
				}
			}
			return true;
		}
		else
		{
			// Usage ...
			return false;
		}
	}
}
