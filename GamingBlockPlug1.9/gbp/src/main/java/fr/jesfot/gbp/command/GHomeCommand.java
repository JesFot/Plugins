package fr.jesfot.gbp.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.configuration.NBTConfig;
import fr.jesfot.gbp.configuration.NBTSubConfig;
import fr.jesfot.gbp.world.WorldComparator;

public class GHomeCommand extends CommandBase
{
	private GamingBlockPlug_1_9 gbp;
	
	public GHomeCommand(GamingBlockPlug_1_9 plugin)
	{
		super("ghome");
		this.gbp = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(!command.getName().equalsIgnoreCase("ghome"))
		{
			return false;
		}
		if(!(sender instanceof Player))
		{
			sender.sendMessage("Please, be a player.");
			return true;
		}
		else
		{
			Player player = (Player)sender;
			NBTConfig playerCfg = new NBTConfig(this.gbp.getConfigFolder("playerdatas"), player.getUniqueId());
			NBTSubConfig homes = new NBTSubConfig(playerCfg, "Homes");
			Location pLoc = player.getLocation();
			if(args.length >= 1 && args[0].equalsIgnoreCase("list"))
			{
				Set<String> hs = homes.readNBTFromFile().getCopy().c();
				Iterator<String> it = hs.iterator();
				if(hs.size() < 10)
				{
					sender.sendMessage(ChatColor.GOLD + "Homes: ");
					while(it.hasNext())
					{
						sender.sendMessage(ChatColor.AQUA + " - "+it.next().substring(2));
					}
				}
				else
				{
					String result = it.next();
					while(it.hasNext())
					{
						result += ", " + it.next().substring(2);
					}
					sender.sendMessage(ChatColor.GOLD + "Homes: " + ChatColor.RESET + result);
				}
				return true;
			}
			if(args.length >= 2)
			{
				if(args[1].equalsIgnoreCase("set"))
				{
					String locName = args[0];
					homes.readNBTFromFile().setLocation("h_"+locName, pLoc).writeNBTToFile();
					Command.broadcastCommandMessage(sender, "Registering new '"+locName+"' home.", true);
					return true;
				}
			}
			else if(args.length >= 1)
			{
				if(args[0].equalsIgnoreCase("set"))
				{
					homes.readNBTFromFile().setLocation("Main", pLoc).writeNBTToFile();
					Command.broadcastCommandMessage(sender, "This player registered new base home.", false);
					return true;
				}
				else
				{
					Location loc = homes.readNBTFromFile().getLocation("h_"+args[0]);
					if(loc == null)
					{
						sender.sendMessage("This location was not registered, do '/home " + args[0] + " set' to set it.");
						return true;
					}
					if(WorldComparator.compareWorlds(player.getWorld(), loc.getWorld(), gbp)<0)
					{
						sender.sendMessage("You cannot teleport your self beetween worlds.");
						return true;
					}
					player.teleport(loc, TeleportCause.PLUGIN);
					player.sendMessage("You were teleported to your home, good luck !");
					Command.broadcastCommandMessage(sender, "This player went back to home "+args[0], false);
					return true;
				}
			}
			else
			{
				Location loc = homes.readNBTFromFile().getLocation("Main");
				if(loc == null)
				{
					sender.sendMessage("This location was not registered, do '/home set' to set it.");
					return true;
				}
				if(WorldComparator.compareWorlds(player.getWorld(), loc.getWorld(), gbp)<0)
				{
					sender.sendMessage("You cannot teleport your self beetween worlds.");
					return true;
				}
				player.teleport(loc, TeleportCause.PLUGIN);
				player.sendMessage("You were teleported to your home, good luck !");
				Command.broadcastCommandMessage(sender, "This player went back to his main home.", false);
				return true;
			}
		}
		sender.sendMessage(ChatColor.RED+"Usage: /"+label+" [set] | /"+label+" <locName> [set]");
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
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

		Player player = (Player)sender;
		NBTConfig playerCfg = new NBTConfig(this.gbp.getConfigFolder("playerdatas"), player.getUniqueId());
		NBTSubConfig homes = new NBTSubConfig(playerCfg, "Homes");
		
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
			for(String name : homes.readNBTFromFile().getCopy().c())
			{
				if(name.substring(2).toLowerCase().startsWith(args[0].toLowerCase()))
				{
					result.add(name.substring(2));
				}
			}
		}
		return result;
	}
}