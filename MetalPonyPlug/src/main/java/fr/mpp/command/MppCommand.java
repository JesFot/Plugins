package fr.mpp.command;

import org.bukkit.Bukkit;
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
			mpp.setMeta(player, "MPPRankB", null);
			mpp.setMeta(player, "MPPLogTimes", 0);
			mpp.setMeta(player, "MPPRegistered", false);
		}
		else
		{
			resetPlugin();
		}
	}
	
	private void set(String[] args)
	{
		if (args[1] != "")
		{
			Player target = null;
			for (Player player : Bukkit.getOnlinePlayers())
			{
				if (player.getName() == args[1])
				{
					target = player;
					break;
				}
				if (player.getDisplayName() == args[1])
				{
					target = player;
					break;
				}
			}
			if (!(target instanceof Player))
			{
				return;
			}
			mpp.setMeta(target, "MPP" + args[2], args[3]);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (args.length >= 1)
		{
			if (args[0].equalsIgnoreCase("reset"))
			{
				if (args.length >= 2)
				{
					resetPlugin();
				}
				else
				{
					resetPlayer(sender);
				}
			}
			else if (args[0].equalsIgnoreCase("set"))
			{
				set(args);
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