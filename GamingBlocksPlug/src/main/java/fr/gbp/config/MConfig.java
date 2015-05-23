package fr.gbp.config;

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

import fr.gbp.GamingBlocksPlug;

public class MConfig
{
	private final GamingBlocksPlug gbp;
	private FileConfiguration confFile;
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;

	public MConfig(FileConfiguration file, GamingBlocksPlug p_gbp)
	{
		this.confFile = file;
		this.gbp = p_gbp;
	}

	/**
	 * This will reload the 'save' file.
	 */
	public void reloadCustomConfig()
	{
		if (this.customConfigFile == null)
		{
			this.customConfigFile = new File(this.gbp.getPlugin().getDataFolder(), "gbpConfig.yml");
		}
		this.customConfig = YamlConfiguration.loadConfiguration(this.customConfigFile);

		Reader defConfigStream = null;
		Reader r2d2 = null;
		try
		{
			defConfigStream = new InputStreamReader(this.gbp.getPlugin().getResource("gbpConfig.yml"), "UTF8");
		}
		catch (UnsupportedEncodingException e)
		{this.gbp.broad("Unsuported format !");
			e.printStackTrace();
		}
		if (defConfigStream != r2d2)
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
			this.gbp.getLogger().log(Level.SEVERE, "Could not save config to " + this.customConfigFile, ex);
		}
	}
	public void saveDefaultConfig()
	{
		if (this.customConfigFile == null)
		{
			this.customConfigFile = new File(this.gbp.getPlugin().getDataFolder(), "gbpConfig.yml");
		}
		if (!this.customConfigFile.exists())
		{
			this.gbp.getPlugin().saveResource("gbpConfig.yml", false);
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
			this.gbp.getLogger().log(Level.WARNING, "[MConfig:116] Key is null");
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
}