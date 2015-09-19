package fr.gbp.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import fr.gbp.GamingBlockPlug;

public class GHomeCommand implements CommandExecutor, TabCompleter
{
	private GamingBlockPlug gbp;
	
	public GHomeCommand(GamingBlockPlug p_gbp)
	{
		this.gbp = p_gbp;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!cmd.getName().equalsIgnoreCase("ghome"))
		{
			return false;
		}
		
		if(sender instanceof Player)
		{
			this.gbp.getConfig().reloadCustomConfig();
			Player player = (Player)sender;
			Location ploc = player.getLocation();
			if(args.length >= 2)
			{
				if(args[1].equalsIgnoreCase("set"))
				{
					String locName = "gbphome." + player.getName().toLowerCase() + "." + args[0];
					this.gbp.getConfig().storeLoc(locName, ploc);
					this.gbp.getConfig().saveCustomConfig();
					return true;
				}
			}
			else if(args.length >= 1)
			{
				if(args[0].equalsIgnoreCase("set"))
				{
					this.gbp.getConfig().storeLoc("gbphome.base." + player.getName().toLowerCase(), ploc);
					this.gbp.getConfig().saveCustomConfig();
				}
				else
				{
					Location loc = this.gbp.getConfig().getLoc("gbphome." + player.getName().toLowerCase() + "." + args[0]);
					if(loc == null)
					{
						sender.sendMessage("This location do not exists.");
						return true;
					}
					player.teleport(loc, TeleportCause.PLUGIN);
					player.sendMessage("You were teleported to your home, good luck !");
					return true;
				}
			}
			else
			{
				Location loc = this.gbp.getConfig().getLoc("gbphome.base." + player.getName().toLowerCase());
				if(loc == null)
				{
					sender.sendMessage("You did not register any base home !");
					return true;
				}
				player.teleport(loc, TeleportCause.PLUGIN);
				player.sendMessage("You were teleported to your home, good luck !");
				return true;
			}
		}
		else
		{
			sender.sendMessage("Please, be a player.");
			return true;
		}
		sender.sendMessage(ChatColor.RED + "Usage: /" + label + " [set] | /" + label + " <locName> [set]");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args)
	{
		List<String> result = new ArrayList<String>();
		if(!cmd.getName().equalsIgnoreCase("ghome"))
		{
			return null;
		}
		if(!(sender instanceof Player))
		{
			sender.sendMessage("Please, be a player.");
			return null;
		}
		
		this.gbp.getConfig().reloadCustomConfig();
		Player player = (Player)sender;
		
		if(args.length == 2)
		{
			result.add("set");
		}
		else if(args.length == 1)
		{
			if("set".startsWith(args[0].toLowerCase()))
			{
				result.add("set");
			}
			for(String name : this.gbp.getConfig().getCustomConfig().getConfigurationSection("gbphome." + player.getName().toLowerCase()).getKeys(false))
			{
				if(name.toLowerCase().startsWith(args[0].toLowerCase()))
				{
					result.add(name);
				}
			}
		}
		return result;
	}
}