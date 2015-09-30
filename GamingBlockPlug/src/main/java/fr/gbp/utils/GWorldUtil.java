package fr.gbp.utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.gbp.GamingBlockPlug;

public class GWorldUtil
{
	public static void loadWorld(GamingBlockPlug gbp, String name)
	{
		GWorldUtil.loadWorld(gbp, name, "");
	}
	
	public static void loadWorld(GamingBlockPlug gbp, String name, String seed)
	{
		GWorldUtil.loadWorld(gbp, name, seed, WorldType.NORMAL, World.Environment.NORMAL);
	}
	
	public static void loadWorld(GamingBlockPlug gbp, String name, String seed, WorldType wType, World.Environment env)
	{
		if(seed != "")
		{
			gbp.getServer().createWorld(new WorldCreator(name).environment(env).type(wType).seed(Long.parseLong(seed, 36)));
		}
		else
		{
			gbp.getServer().createWorld(new WorldCreator(name));
		}
	}
	
	public static void loadWorld(GamingBlockPlug gbp, String name, String seed, World.Environment env)
	{
		GWorldUtil.loadWorld(gbp, name, seed, WorldType.NORMAL, env);
	}
	
	public static void loadWorld(GamingBlockPlug gbp, String name, String seed, WorldType wType)
	{
		GWorldUtil.loadWorld(gbp, name, seed, wType, World.Environment.NORMAL);
	}
	
	public static void unloadWorld(GamingBlockPlug gbp, String name)
	{
		World world = gbp.getServer().getWorld(name);
		if(world == null)
		{
			world = gbp.getServer().getWorld(name+="_nether");
			if(world == null)
			{
				world = gbp.getServer().getWorld(name+="_end");
			}
		}
		gbp.getServer().unloadWorld(name, true);
	}
	
	
	
	public static void tpToWorld(GamingBlockPlug gbp, Player[] player, String worldName)
	{
		World world = gbp.getServer().getWorld(worldName);
		if(world == null)
		{
			world = gbp.getServer().getWorld(worldName+"_nether");
			if(world == null)
			{
				world = gbp.getServer().getWorld(worldName+"_end");
				if(world == null)
				{
					GWorldUtil.loadWorld(gbp, worldName);
					world = gbp.getServer().getWorld(worldName);
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
	
	public static void tpToWorld(GamingBlockPlug gbp, CommandSender sender, String argument, String worldName)
	{
		Player[] pls = {};
		if(argument.startsWith("@"))
		{
			pls = GParseCommandTarget.getPlayers(sender, argument).toArray(new Player[]{});
			if(pls == null)
			{
				sender.sendMessage(ChatColor.RED + gbp.getLang().get("player.notfound"));
			}
		}
		else
		{
			Player pl = (argument!="" ? UPlayer.getPlayerByName(argument) : ((sender instanceof Player) ? (Player)sender : null));
			pls = new Player[]{pl};
		}
		
		GWorldUtil.tpToWorld(gbp, pls, worldName);
	}
}