package fr.jesfot.gbp.world;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.configuration.NBTSubConfig;

public class WorldLoader
{
	//
	public static void loadWorld(GamingBlockPlug_1_9 gbp, String name)
	{
		WorldLoader.loadWorld(gbp, name, "");
	}
	
	public static void loadWorld(GamingBlockPlug_1_9 gbp, String name, String seed)
	{
		WorldLoader.loadWorld(gbp, name, seed, WorldType.NORMAL, World.Environment.NORMAL);
	}
	
	public static void loadWorld(GamingBlockPlug_1_9 gbp, String name, String seed, WorldType wType, World.Environment env)
	{
		World theWorld;
		if(seed != "")
		{
			theWorld = gbp.getServer().createWorld(new WorldCreator(name).environment(env).type(wType).seed(Long.parseLong(seed, 36)));
		}
		else
		{
			theWorld = gbp.getServer().createWorld(new WorldCreator(name));
		}
		NBTSubConfig world = new NBTSubConfig(gbp.getConfigFolder("worldsdatas"), name);
		
		String groupName = world.readNBTFromFile().getCopy().getString("Group");
		if(groupName == "")
		{
			groupName = "_undifined_";
			world.setString("Group", groupName);
		}
		
		WorldComparator.setKeepInventory(gbp, name, true);
		WorldComparator.setKeepLocation(gbp, name, false);
		WorldComparator.setChangeBedSpawn(gbp, name, true);
		WorldComparator.setAutoUnload(gbp, name, true);
		theWorld.setGameRuleValue("keepLastLocation", "false");
		theWorld.setGameRuleValue("changeBedSpawn", "true");
		theWorld.setGameRuleValue("autoUnLoad", "true");
		
		world.setBoolean("isLoaded", true);
	}
	
	public static void loadWorld(GamingBlockPlug_1_9 gbp, String name, String seed, World.Environment env)
	{
		WorldLoader.loadWorld(gbp, name, seed, WorldType.NORMAL, env);
	}
	
	public static void loadWorld(GamingBlockPlug_1_9 gbp, String name, String seed, WorldType wType)
	{
		WorldLoader.loadWorld(gbp, name, seed, wType, World.Environment.NORMAL);
	}
	
	public static void unloadWorld(GamingBlockPlug_1_9 gbp, String name)
	{
		World world = gbp.getServer().getWorld(name);
		NBTSubConfig w = new NBTSubConfig(gbp.getConfigFolder("worldsdatas"), name);
		if(world == null)
		{
			if(!w.existFile())
			{
				return;
			}
			if(w.readNBTFromFile().getCopy().getBoolean("isLoaded"))
			{
				w.setBoolean("isLoaded", false).writeNBTToFile();
				return;
			}
			return;
		}
		gbp.getServer().unloadWorld(name, true);
		w.readNBTFromFile().setBoolean("isLoaded", false).writeNBTToFile();
	}
}