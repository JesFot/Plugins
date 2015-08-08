package me.emp.command;

import me.emp.EliryumPlug;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class MHomeCommand implements CommandExecutor
{
	private EliryumPlug emp;

	public MHomeCommand(EliryumPlug emp)
	{
		this.emp = emp;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args)
	{
		if (!cmd.getName().equalsIgnoreCase("mhome"))
		{
			return false;
		}
		if (sender instanceof Player)
		{
			Player player = (Player)sender;
			Location ploc = player.getLocation();
			if(args.length >= 2)
			{
				if(args[1].equalsIgnoreCase("set"))
				{
					String locName = "mpphome."+player.getName()+"."+args[0];
					this.emp.getConfig().storeLoc(locName, ploc);
				}
			}
			else if (args.length >= 1)
			{
				if (args[0].equalsIgnoreCase("set"))
				{
					this.emp.getConfig().storeLoc("mpphome.base."+player.getName(), ploc);
				}
				else
				{
					Location loc = this.emp.getConfig().getLoc("mpphome."+player.getName()+"."+args[0]);
					if (loc == null)
					{
						return false;
					}
					player.teleport(loc, TeleportCause.PLUGIN);
					player.sendMessage("You were teleported to your home, good luck !");
				}
			}
			else
			{
				Location loc = this.emp.getConfig().getLoc("mpphome.base."+player.getName());
				if (loc == null)
				{
					return false;
				}
				player.teleport(loc, TeleportCause.PLUGIN);
				player.sendMessage("You were teleported to your home, good luck !");
			}
		}
		else
		{
			sender.sendMessage("Vous devez etre un joueur, Desole.");
		}
		return true;
	}
}