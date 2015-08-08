package me.emp.utils;

import me.emp.EliryumPlug;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MWorldUtil
{
	public static void loadWorld(EliryumPlug emp, String name)
	{
		MWorldUtil.loadWorld(emp, name, "");
	}
	
	public static void loadWorld(EliryumPlug emp, String name, String seed)
	{
		MWorldUtil.loadWorld(emp, name, seed, WorldType.NORMAL, World.Environment.NORMAL);
	}
	
	public static void loadWorld(EliryumPlug emp, String name, String seed, WorldType wType, World.Environment env)
	{
		if(seed != "")
		{
			emp.getServer().createWorld(new WorldCreator(name).environment(env).type(wType).seed(Long.parseLong(seed, 36)));
		}
		else
		{
			emp.getServer().createWorld(new WorldCreator(name));
		}
	}
	
	public static void loadWorld(EliryumPlug emp, String name, String seed, World.Environment env)
	{
		MWorldUtil.loadWorld(emp, name, seed, WorldType.NORMAL, env);
	}
	
	public static void loadWorld(EliryumPlug emp, String name, String seed, WorldType wType)
	{
		MWorldUtil.loadWorld(emp, name, seed, wType, World.Environment.NORMAL);
	}
	
	public static void unloadWorld(EliryumPlug emp, String name)
	{
		World world = emp.getServer().getWorld(name);
		if(world == null)
		{
			world = emp.getServer().getWorld(name+="_nether");
			if(world == null)
			{
				world = emp.getServer().getWorld(name+="_end");
			}
		}
		emp.getServer().unloadWorld(name, true);
	}
	
	
	
	public static void tpToWorld(EliryumPlug emp, Player[] player, String worldName)
	{
		World world = emp.getServer().getWorld(worldName);
		if(world == null)
		{
			world = emp.getServer().getWorld(worldName+"_nether");
			if(world == null)
			{
				world = emp.getServer().getWorld(worldName+"_end");
				if(world == null)
				{
					MWorldUtil.loadWorld(emp, worldName);
					world = emp.getServer().getWorld(worldName);
				}
			}
		}
		Location spawn = world.getSpawnLocation();
		if(player.length == 1)
		{
			Location old = (world.getPlayers().contains(player[0]) ? world.getPlayers().get(world.getPlayers().indexOf(player[0])).getLocation() : spawn);
			player[0].teleport(old);
		}
		else if(player.length >= 2)
		{
			for(Player p : player)
			{
				Location old = (world.getPlayers().contains(p) ? world.getPlayers().get(world.getPlayers().indexOf(p)).getLocation() : spawn);
				p.teleport(old);
			}
		}
	}
	
	public static void tpToWorld(EliryumPlug emp, CommandSender sender, String argument, String worldName)
	{
		Player[] pls = {};
		if(argument.startsWith("@"))
		{
			Location start = null;
			if(sender instanceof BlockCommandSender)
			{
				BlockCommandSender commandB = (BlockCommandSender)sender;
				start = commandB.getBlock().getLocation();
			}
			else if(sender instanceof Player)
			{
				Player myPlayer = (Player)sender;
				start = myPlayer.getLocation();
			}
			else
			{
				if(argument.startsWith("@p"))
				{
					sender.sendMessage("Nope.");
					return;
				}
				else
				{
					start = new Location(emp.getServer().getWorlds().get(0), 0, 0, 0);
				}
			}
			pls = MPlayer.getPlayerByRep(argument, start);
		}
		else
		{
			Player pl = (argument!="" ? MPlayer.getPlayerByName(argument) : ((sender instanceof Player) ? (Player)sender : null));
			pls = new Player[]{pl};
		}
		
		MWorldUtil.tpToWorld(emp, pls, worldName);
	}
}