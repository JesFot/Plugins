package fr.jesfot.gbp.world;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.World;

import fr.jesfot.gbp.GamingBlockPlug_1_11;
import fr.jesfot.gbp.configuration.NBTSubConfig;

/**
 * Class contains functions for world's comparing and data management :<br>
 * 
 * @version 1.6.3
 * @since 1.3.1
 * @author JësFot
 * @category world-management
 */
public class WorldComparator
{
	public static int compareWorlds(World alpha, World beta, GamingBlockPlug_1_11 gbp)
	{
		if(gbp != null)
		{
			NBTSubConfig walpha = new NBTSubConfig(gbp.getConfigFolder("worldsdatas"), alpha.getName()).readNBTFromFile();
			NBTSubConfig wbeta = new NBTSubConfig(gbp.getConfigFolder("worldsdatas"), beta.getName()).readNBTFromFile();
			
			if(walpha.getCopy().getString("Group").contentEquals(wbeta.getCopy().getString("Group")))
			{
				if(walpha.getCopy().getString("Group") != "_undifined_" && walpha.getCopy().getString("Group") != "")
				{
					return 0;
				}
			}
		}
		
		if(alpha.getUID().equals(beta.getUID()))
		{
			return 1;
		}
		if(alpha.getName().contentEquals(beta.getName()))
		{
			return 2;
		}
		return -1;
	}
	
	public static boolean isEqualWorld(World alpha, World beta, GamingBlockPlug_1_11 gbp)
	{
		return WorldComparator.compareWorlds(alpha, beta, gbp) > 0;
	}
	
	public static String[] getWorldFilesList(GamingBlockPlug_1_11 gbp, Collection<String> exclude)
	{
		List<String> result = new ArrayList<String>();
		
		File folder = gbp.getConfigFolder("worldsdatas");
		for(String fileName : folder.list())
		{
			if(!exclude.contains(fileName.substring(0, fileName.length() - ".dat".length())))
			{
				result.add(fileName.substring(0, fileName.length() - ".dat".length()));
			}
		}
		
		return result.toArray(new String[]{});
	}
	
	public static void setDefaultGamemode(GamingBlockPlug_1_11 gbp, String worldName, String gameMode)
	{
		NBTSubConfig world = new NBTSubConfig(gbp.getConfigFolder("worldsdatas"), worldName).readNBTFromFile();
		String gm;
		if(gameMode.equalsIgnoreCase("survival") || gameMode.equalsIgnoreCase("0"))
		{
			gm = GameMode.SURVIVAL.toString();
		}
		else if(gameMode.equalsIgnoreCase("creative") || gameMode.equalsIgnoreCase("1"))
		{
			gm = GameMode.CREATIVE.toString();
		}
		else if(gameMode.equalsIgnoreCase("adventure") || gameMode.equalsIgnoreCase("2"))
		{
			gm = GameMode.ADVENTURE.toString();
		}
		else if(gameMode.equalsIgnoreCase("spectator") || gameMode.equalsIgnoreCase("3"))
		{
			gm = GameMode.SPECTATOR.toString();
		}
		else
		{
			gm = "NaN";
		}
		world.setString("Option.Gamemode", gm).writeNBTToFile();
	}
	
	public static String getDefaultGamemode(GamingBlockPlug_1_11 gbp, String worldName)
	{
		NBTSubConfig world = new NBTSubConfig(gbp.getConfigFolder("worldsdatas"), worldName).readNBTFromFile();
		return world.getCopy().getString("Option.Gamemode")!=""?world.getCopy().getString("Option.Gamemode"):"NaN";
	}
	
	public static void setKeepInventory(GamingBlockPlug_1_11 gbp, String worldName, boolean value)
	{
		NBTSubConfig world = new NBTSubConfig(gbp.getConfigFolder("worldsdatas"), worldName).readNBTFromFile();
		world.setBoolean("Option.KeepInventory", value).writeNBTToFile();
	}
	
	public static boolean getKeepInventory(GamingBlockPlug_1_11 gbp, String worldName)
	{
		NBTSubConfig world = new NBTSubConfig(gbp.getConfigFolder("worldsdatas"), worldName).readNBTFromFile();
		return world.getCopy().getBoolean("Option.KeepInventory");
	}
	
	public static void setChangeBedSpawn(GamingBlockPlug_1_11 gbp, String worldName, boolean value)
	{
		NBTSubConfig world = new NBTSubConfig(gbp.getConfigFolder("worldsdatas"), worldName).readNBTFromFile();
		world.setBoolean("Option.ChangeBedSpawn", value).writeNBTToFile();
	}
	
	public static boolean getChangeBedSpawn(GamingBlockPlug_1_11 gbp, String worldName)
	{
		NBTSubConfig world = new NBTSubConfig(gbp.getConfigFolder("worldsdatas"), worldName).readNBTFromFile();
		return world.getCopy().getBoolean("Option.ChangeBedSpawn");
	}
	
	public static void setKeepLocation(GamingBlockPlug_1_11 gbp, String worldName, boolean value)
	{
		NBTSubConfig world = new NBTSubConfig(gbp.getConfigFolder("worldsdatas"), worldName).readNBTFromFile();
		world.setBoolean("Option.KeepLocation", value).writeNBTToFile();
	}
	
	public static boolean getKeepLocation(GamingBlockPlug_1_11 gbp, String worldName)
	{
		NBTSubConfig world = new NBTSubConfig(gbp.getConfigFolder("worldsdatas"), worldName).readNBTFromFile();
		return world.getCopy().getBoolean("Option.KeepLocation");
	}
	
	public static void setAutoUnload(GamingBlockPlug_1_11 gbp, String worldName, boolean value)
	{
		NBTSubConfig world = new NBTSubConfig(gbp.getConfigFolder("worldsdatas"), worldName).readNBTFromFile();
		world.setBoolean("Option.AutoUnload", value).writeNBTToFile();
	}
	
	public static boolean getAutoUnload(GamingBlockPlug_1_11 gbp, String worldName)
	{
		NBTSubConfig world = new NBTSubConfig(gbp.getConfigFolder("worldsdatas"), worldName).readNBTFromFile();
		return world.getCopy().getBoolean("Option.AutoUnload");
	}
}