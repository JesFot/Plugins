package fr.jesfot.gbp.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import fr.jesfot.gbp.GamingBlockPlug_1_12;
import fr.jesfot.gbp.configuration.NBTConfig;
import fr.jesfot.gbp.configuration.NBTSubConfig;
import fr.jesfot.gbp.world.WorldComparator;

public class GHomeCommand extends CommandBase
{
	private GamingBlockPlug_1_12 gbp;
	
	public GHomeCommand(GamingBlockPlug_1_12 plugin)
	{
		super("ghome");
		this.gbp = plugin;
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
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
			if(player.getGameMode().equals(GameMode.ADVENTURE))
			{
				sender.sendMessage("Cannot use /" + label + " in adventure mode !");
				return true;
			}
			NBTConfig playerCfg = new NBTConfig(this.gbp.getConfigFolder("playerdatas"), player.getUniqueId());
			String teamName = playerCfg.readNBTFromFile().getCopy().getString("Team");
			NBTSubConfig homes = new NBTSubConfig(playerCfg, "Homes");
			int maxHomes = this.gbp.getTeams().getIfExists(teamName).getMaxHomes();
			Location pLoc = player.getLocation();
			if(args.length >= 1 && args[0].equalsIgnoreCase("list"))
			{
				Set<String> hs = homes.readNBTFromFile().getCopy().c();
				Iterator<String> it = hs.iterator();
				if(hs.size() <= 11)
				{
					sender.sendMessage(ChatColor.GOLD + "Homes: ");
					while(it.hasNext())
					{
						String r = it.next();
						if(!r.equalsIgnoreCase("Main"))
						{
							sender.sendMessage(ChatColor.AQUA + " - " + r.substring(2));
						}
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
					int hsCount = homes.readNBTFromFile().getCopy().c().size();
					if(hsCount >= maxHomes && homes.readNBTFromFile().getLocation("h_"+args[0]) == null)
					{
						sender.sendMessage("You are limited to " + maxHomes + " homes.");
						return true;
					}
					String locName = args[0];
					homes.readNBTFromFile().setLocation("h_"+locName, pLoc).writeNBTToFile();
					Command.broadcastCommandMessage(sender, "Registering new '"+locName+"' home.", true);
					return true;
				}
				else if(args[1].equalsIgnoreCase("remove"))
				{
					if(homes.readNBTFromFile().getLocation("h_"+args[0]) == null)
					{
						sender.sendMessage("No registred location for this name");
						return true;
					}
					homes.readNBTFromFile().removeTag("h_" + args[0]).writeNBTToFile();
					Command.broadcastCommandMessage(sender, "Removed old '" + args[0] + "' home.", true);
					return true;
				}
			}
			else if(args.length >= 1)
			{
				if(args[0].equalsIgnoreCase("set"))
				{
					int hsCount = homes.readNBTFromFile().getCopy().c().size();
					if(hsCount >= maxHomes && homes.readNBTFromFile().getLocation("Main") == null)
					{
						sender.sendMessage("You are limited to " + maxHomes + " homes.");
						return true;
					}
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
	public List<String> executeTabComplete(CommandSender sender, Command cmd, String label, String[] args)
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
			result.add("remove");
		}
		else if(args.length == 1)
		{
			result.add("set");
			for(String name : homes.readNBTFromFile().getCopy().c())
			{
				if(!name.equals("Main"))
				{
					result.add(name.substring(2));
				}
			}
		}
		return this.sortStart(args[args.length - 1], result);
	}
}