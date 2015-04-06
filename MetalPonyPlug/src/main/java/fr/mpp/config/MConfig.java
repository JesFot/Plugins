package fr.mpp.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.mpp.MetalPonyPlug;

public class MConfig
{
	private final MetalPonyPlug mpp;
	private FileConfiguration confFile;
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;
	private static final int x = 0, y = 0, z = -1;

	public MConfig(FileConfiguration file, MetalPonyPlug p_mpp)
	{
		this.confFile = file;
		this.mpp = p_mpp;
	}

	/**
	 * This will reload the 'save' file.
	 */
	public void reloadCustomConfig()
	{
		if (this.customConfigFile == null)
		{
			this.customConfigFile = new File(this.mpp.getPlugin().getDataFolder(), "mppConfig.yml");
		}
		this.customConfig = YamlConfiguration.loadConfiguration(this.customConfigFile);

		Reader defConfigStream = null;
		try
		{
			defConfigStream = new InputStreamReader(this.mpp.getPlugin().getResource("mppConfig.yml"), "UTF8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		if (defConfigStream != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			this.customConfig.setDefaults(defConfig);
		}
	}
	/**
	 * 
	 * @return the save configuration file.
	 */
	public FileConfiguration getCustomConfig()
	{
	    if (this.customConfig == null)
	    {
	        this.reloadCustomConfig();
	    }
	    return this.customConfig;
	}
	public void saveCustomConfig()
	{
		if (this.customConfig == null || this.customConfigFile == null)
		{
			return;
		}
		try
		{
			this.getCustomConfig().save(this.customConfigFile);
		}
		catch (IOException ex)
		{
			this.mpp.getLogger().log(Level.SEVERE, "Could not save config to " + this.customConfigFile, ex);
		}
	}
	public void saveDefaultConfig()
	{
		if (this.customConfigFile == null)
		{
			this.customConfigFile = new File(this.mpp.getPlugin().getDataFolder(), "mppConfig.yml");
		}
		if (!this.customConfigFile.exists())
		{
			this.mpp.getPlugin().saveResource("mppConfig.yml", false);
		}
	}
	
	/**
	 * Get the main configuration file (config.yml)
	 * 
	 * @return the main configuration file
	 */
	public FileConfiguration getConf()
	{
		return this.confFile;
	}
	
	/**
	 * This will register in the save data config a location
	 * 
	 * @param name - The name of the location
	 * @param loc - The location to save
	 */
	public void storeLoc(String name, Location loc)
	{
		name = name.toLowerCase();
		String i = " , ";
		String locator = loc.getWorld().getName() +i+ loc.getX() +i+ loc.getY() +i+ loc.getZ() +i+ loc.getPitch() +i+ loc.getYaw();
		this.getCustomConfig().set(name, locator);
		if (!this.getCustomConfig().contains(name))
		{
			this.getCustomConfig().createSection(name);
		}
		this.getCustomConfig().set(name, locator);
		this.saveCustomConfig();
	}
	
	/**
	 * This will return you a stored location, registered before by {@code} storeLoc(name, loc) {@code}
	 * 
	 * @param name - The name of the location
	 * @return The location requested if exists
	 */
	public Location getLoc(String name)
	{
		name = name.toLowerCase();
		this.reloadCustomConfig();
		String key = this.getCustomConfig().getString(name);
		if (key == null || key == "")
		{
			this.mpp.getLogger().log(Level.WARNING, "[MConfig:116] Key is null");
			return null;
		}
		String[] split = key.split(" , ");
		if (split.length == 6)
		{
			Location loc = new Location(Bukkit.getWorld(split[0]),
					Double.parseDouble(split[1]),
					Double.parseDouble(split[2]),
					Double.parseDouble(split[3]),
					Float.parseFloat(split[4]),
					Float.parseFloat(split[5]));
			this.saveCustomConfig();
			return loc;
		}
		else
		{
			this.saveCustomConfig();
			return null;
		}
	}
	
	public boolean getMppActive()
	{
		return confFile.getBoolean("mpp_active");
	}

	public static int getMaxX()
	{
		return 400;//400
	}
	public static int getMaxY()
	{
		return 75;//75
	}
	public static int getMaxZ()
	{
		return -40;//-40
	}
	public static int getMinX()
	{
		return 396;//396
	}
	public static int getMinY()
	{
		return 74;//74
	}
	public static int getMinZ()
	{
		return -40;//-40
	}
	public static int getMaxBX()
	{
		return getMaxX()-x;
	}
	public static int getMaxBY()
	{
		return getMaxY()-y;
	}
	public static int getMaxBZ()
	{
		return getMaxZ()-z;
	}
	public static int getMinBX()
	{
		return getMinX()-x;
	}
	public static int getMinBY()
	{
		return getMinY()-y;
	}
	public static int getMinBZ()
	{
		return getMinZ()-z;
	}
}