package fr.gbp.utils;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Objective;
import org.bukkit.util.NumberConversions;

import fr.gbp.GamingBlockPlug;
import fr.gbp.utils.scoreboards.CriteriaType;
import fr.gbp.utils.scoreboards.GScoreBoard;

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
		gbp.getConfig().reloadCustomConfig();
		int last = gbp.getConfig().getCustomConfig().getInt("worlds.last", 0);
		if(!gbp.getConfig().getCustomConfig().contains("worlds.list."+name))
		{
			gbp.getConfig().getCustomConfig().createSection("worlds.list."+name);
			gbp.getConfig().getCustomConfig().set("worlds.list."+name, last);
		}
		gbp.getWorldsConfig().reloadWorldConfig(name+".yml");
		if(!gbp.getWorldsConfig().getWorldConfig(name+".yml").contains("id"))
		{
			gbp.getWorldsConfig().getWorldConfig(name+".yml").createSection("id");
			gbp.getWorldsConfig().getWorldConfig(name+".yml").set("id", last);
			gbp.getWorldsConfig().saveWorldConfig(name+".yml");
		}
		if(!gbp.getConfig().getCustomConfig().contains("worlds.last"))
		{
			gbp.getConfig().getCustomConfig().createSection("worlds.last");
		}
		gbp.getConfig().getCustomConfig().set("worlds.last", last+1);
		gbp.getConfig().saveCustomConfig();
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
		boolean keepInventories = false;
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
		List<String> ws = gbp.getWorldsConfig().getWorldConfig(world.getName()+".yml").getStringList("sameinv_worlds");
		List<String> pws;
		if(player.length == 1)
		{
			pws = gbp.getWorldsConfig().getWorldConfig(player[0].getWorld().getName()+".yml").getStringList("sameinv_worlds");
			if(ws.contains(player[0].getWorld().getName().toLowerCase()))
			{
				keepInventories = true;
			}
			if(pws.contains(world.getName().toLowerCase()))
			{
				keepInventories = true;
			}
			String exWorld = player[0].getWorld().getName();
			//Location pre_old = gbp.getWorldsConfig().getLoc("locations.last."+player[0].getName().toLowerCase(), world.getName()+".yml");
			Inventory ender = gbp.getWorldsConfig().getInventory("inventories.ender."+player[0].getName().toLowerCase(), world.getName()+".yml");
			Inventory norm = gbp.getWorldsConfig().getInventory("inventories.norm."+player[0].getName().toLowerCase(), world.getName()+".yml");
			Inventory bank = gbp.getWorldsConfig().getInventory("inventories.bank."+player[0].getName().toLowerCase(), world.getName()+".yml");
			float xp = NumberConversions.toFloat(gbp.getWorldsConfig().getWorldConfig(world.getName()+".yml").get("levels."+player[0].getName().toLowerCase()+".exp", player[0].getExp()));
			int lvl = gbp.getWorldsConfig().getWorldConfig(world.getName()+".yml").getInt("levels."+player[0].getName().toLowerCase()+".lvl", player[0].getLevel());
			if(!keepInventories)
			{
				if(norm != null)
				{
					player[0].getInventory().setContents(norm.getContents());
				}
				if(ender != null)
				{
					player[0].getEnderChest().setContents(ender.getContents());
				}
				if(bank != null)
				{
					gbp.getEconomy().getPEco(player[0]).getInventory().setContents(bank.getContents());
				}
				if(gbp.getWorldsConfig().getWorldConfig(world.getName()+".yml").contains("gamemode"))
				{
					player[0].setGameMode(GameMode.valueOf(gbp.getWorldsConfig().getWorldConfig(world.getName()+".yml").getString("gamemode", GameMode.SURVIVAL.toString())));
				}
				player[0].setExp(xp);
				player[0].setLevel(lvl);
			}
			Location old = (world.getPlayers().contains(player[0]) ? world.getPlayers().get(world.getPlayers().indexOf(player[0])).getLocation() : spawn);
			player[0].teleport(/*pre_*/old);
			if(player[0].getServer().getWorld(exWorld).getPlayers().isEmpty())
			{
				unloadWorld(gbp, exWorld);
			}
		}
		else if(player.length >= 2)
		{
			for(Player p : player)
			{
				pws = gbp.getWorldsConfig().getWorldConfig(p.getWorld().getName()+".yml").getStringList("sameinv_worlds");
				if(ws.contains(p.getWorld().getName().toLowerCase()))
				{
					keepInventories = true;
				}
				if(pws.contains(world.getName().toLowerCase()))
				{
					keepInventories = true;
				}
				String exWorld = p.getWorld().getName();
				//Location pre_old = gbp.getWorldsConfig().getLoc("locations.last."+p.getName().toLowerCase(), world.getName()+".yml");
				if(!keepInventories)
				{
					Inventory ender = gbp.getWorldsConfig().getInventory("inventories.ender."+p.getName().toLowerCase(), world.getName()+".yml");
					Inventory norm = gbp.getWorldsConfig().getInventory("inventories.norm."+p.getName().toLowerCase(), world.getName()+".yml");
					Inventory bank = gbp.getWorldsConfig().getInventory("inventories.bank."+p.getName().toLowerCase(), world.getName()+".yml");
					float xp = NumberConversions.toFloat(gbp.getWorldsConfig().getWorldConfig(world.getName()+".yml").get("levels."+p.getName().toLowerCase()+".exp", p.getExp()));
					int lvl = gbp.getWorldsConfig().getWorldConfig(world.getName()+".yml").getInt("levels."+p.getName().toLowerCase()+".lvl", p.getLevel());
					if(norm != null)
					{
						p.getInventory().setContents(norm.getContents());
					}
					if(ender != null)
					{
						p.getEnderChest().setContents(ender.getContents());
					}
					if(bank != null)
					{
						gbp.getEconomy().getPEco(p).getInventory().setContents(bank.getContents());
					}
					if(gbp.getWorldsConfig().getWorldConfig(world.getName()+".yml").contains("gamemode"))
					{
						p.setGameMode(GameMode.valueOf(gbp.getWorldsConfig().getWorldConfig(world.getName()+".yml").getString("gamemode", GameMode.SURVIVAL.toString())));
					}
					p.setExp(xp);
					p.setLevel(lvl);
				}
				Location old = (world.getPlayers().contains(p) ? world.getPlayers().get(world.getPlayers().indexOf(p)).getLocation() : spawn);
				p.teleport(/*pre_*/old);
				if(p.getServer().getWorld(exWorld).getPlayers().isEmpty())
				{
					unloadWorld(gbp, exWorld);
				}
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
		Objective worldObj;
		if(!GScoreBoard.getInstance().objectiveExists("world_identifics"))
		{
			worldObj = GScoreBoard.getInstance().createObjective("world_identifics", CriteriaType.Dummy);
		}
		else
		{
			worldObj = GScoreBoard.getInstance().getMainScoreboard().getObjective("world_identifics");
		}
		for(Player pl : pls)
		{
			gbp.getConfig().reloadCustomConfig();
			String tmp = pl.getWorld().getName();
			gbp.getWorldsConfig().storeInventory("inventories.ender."+pl.getName().toLowerCase(), pl.getEnderChest(), tmp+".yml");
			gbp.getWorldsConfig().storeInventory("inventories.bank."+pl.getName().toLowerCase(), gbp.getEconomy().getPEco(pl).getInventory(), tmp+".yml");
			gbp.getWorldsConfig().storeInventory("inventories.norm."+pl.getName().toLowerCase(), pl.getInventory(), tmp+".yml");
			gbp.getWorldsConfig().getWorldConfig(tmp+".yml").set("levels."+pl.getName().toLowerCase()+".exp", pl.getExp());
			gbp.getWorldsConfig().getWorldConfig(tmp+".yml").set("levels."+pl.getName().toLowerCase()+".lvl", pl.getLevel());
			gbp.getWorldsConfig().storeLoc("locations.last."+pl.getName().toLowerCase(), pl.getLocation(), tmp+".yml");
			GScoreBoard.setScore(worldObj, pl.getName(), gbp.getConfig().getCustomConfig().getInt("worlds.list."+tmp, -1));
		}
		
		GWorldUtil.tpToWorld(gbp, pls, worldName);
	}
}