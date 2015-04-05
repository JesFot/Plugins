package fr.mpp.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import fr.mpp.MetalPonyPlug;

public class MHomeCommand implements CommandExecutor
{
	private MetalPonyPlug mpp;

	public MHomeCommand(MetalPonyPlug mpp)
	{
		this.mpp = mpp;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args)
	{
		if (!alias.equalsIgnoreCase("mhome"))
		{
			return false;
		}
		if (sender instanceof Player)
		{
			Player player = (Player)sender;
			Location ploc = player.getLocation();
			if (args.length >= 1)
			{
				if (args[0].equalsIgnoreCase("set"))
				{
					this.mpp.getConfig().storeLoc("mpphome."+player.getName(), ploc);
				}
			}
			else
			{
				Location loc = this.mpp.getConfig().getLoc("mpphome."+player.getName());
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