package fr.jesfot.gbp.world;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import fr.jesfot.gbp.GamingBlockPlug_1_11;
import fr.jesfot.gbp.configuration.NBTSubConfig;

/**
 * Class contains functions for world's loading and unloading :<br>
 * - {@link WorldLoader#loadWorld(GamingBlockPlug_1_9, String) loadWorld(plugin, worldName)}<br>
 * - {@link WorldLoader#loadWorld(GamingBlockPlug_1_9, String, String) loadWorld(plugin, worldName, seed)}<br>
 * - {@link WorldLoader#loadWorld(GamingBlockPlug_1_9, String, String, Environment) loadWorld(plugin, worldName, seed, env)}<br>
 * - {@link WorldLoader#loadWorld(GamingBlockPlug_1_9, String, String, WorldType) loadWorld(plugin, worldName, seed, type)}<br>
 * - {@link WorldLoader#loadWorld(GamingBlockPlug_1_9, String, String, WorldType, Environment) loadWorld(plugin, worldName, seed, type, env)}<br>
 * - {@link WorldLoader#unloadWorld(GamingBlockPlug_1_9, String) unloadWorld(plugin, worldName)}<br>
 * 
 * @version 1.6.3
 * @since 1.3.1
 * @author JësFot
 * @category world-management
 */
public class WorldLoader
{
	/**
	 * Load or create a world with auto-generated properties
	 * 
	 * @param gbp - The plugin
	 * @param name - The world name to load or create
	 */
	public static void loadWorld(GamingBlockPlug_1_11 gbp, String name)
	{
		WorldLoader.loadWorld(gbp, name, null);
	}
	
	/**
	 * Load or create a world with given properties
	 * 
	 * @param gbp - The plugin
	 * @param name - The world name to load or create
	 * @param seed - The seed of the new world (or null for auto-generated)
	 */
	public static void loadWorld(GamingBlockPlug_1_11 gbp, String name, String seed)
	{
		WorldLoader.loadWorld(gbp, name, seed, WorldType.NORMAL, World.Environment.NORMAL);
	}
	
	/**
	 * Load or create a world with given properties
	 * 
	 * @param gbp - The plugin
	 * @param name - The world name to load or create
	 * @param seed - The seed of the new world (or null for auto-generated)
	 * @param wType - The {@link WorldType type} of the new world
	 * @param env - The {@link Environment environment} of the new world
	 */
	public static void loadWorld(GamingBlockPlug_1_11 gbp, String name, String seed, WorldType wType, World.Environment env)
	{
		World theWorld;
		if(name.endsWith("_nether"))
		{
			env = World.Environment.NETHER;
		}
		else if(name.endsWith("_the_end"))
		{
			env = World.Environment.THE_END;
		}
		if(seed != null)
		{
			theWorld = gbp.getServer().createWorld(new WorldCreator(name).environment(env).type(wType).seed(Long.parseLong(seed, 36)));
		}
		else
		{
			theWorld = gbp.getServer().createWorld(new WorldCreator(name).environment(env));
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
	
	/**
	 * Load or create a world with given properties
	 * 
	 * @param gbp - The plugin
	 * @param name - The world name to load or create
	 * @param seed - The seed of the new world (or null for auto-generated)
	 * @param env - The {@link Environment environment} of the new world
	 */
	public static void loadWorld(GamingBlockPlug_1_11 gbp, String name, String seed, World.Environment env)
	{
		WorldLoader.loadWorld(gbp, name, seed, WorldType.NORMAL, env);
	}
	
	/**
	 * Load or create a world with given properties
	 * 
	 * @param gbp - The plugin
	 * @param name - The world name to load or create
	 * @param seed - The seed of the new world (or null for auto-generated)
	 * @param wType - The {@link WorldType type} of the new world
	 */
	public static void loadWorld(GamingBlockPlug_1_11 gbp, String name, String seed, WorldType wType)
	{
		WorldLoader.loadWorld(gbp, name, seed, wType, World.Environment.NORMAL);
	}
	
	/**
	 * Unload a world properly
	 * 
	 * @param gbp - The plugin
	 * @param name - The world name to unload
	 */
	public static void unloadWorld(GamingBlockPlug_1_11 gbp, String name)
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