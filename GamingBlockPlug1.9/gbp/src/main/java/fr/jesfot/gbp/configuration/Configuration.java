package fr.jesfot.gbp.configuration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configuration
{
	private FileConfiguration config = null;
	private String lastReload;
	protected File configFile;
	
	public Configuration(File p_configFile)
	{
		this.configFile = p_configFile;
	}
	
	/**
	 * This will relaod the 'save' file.
	 */
	public void reloadConfig()
	{
		if(this.configFile == null)
		{
			return;
		}
		this.config = YamlConfiguration.loadConfiguration(this.configFile);
		this.lastReload = this.config.saveToString();
	}
	
	/**
	 * 
	 * @return the save configuration file
	 */
	public FileConfiguration getConfig()
	{
		if(this.config == null)
		{
			this.reloadConfig();
		}
		return this.config;
	}
	
	public void saveConfig()
	{
		if(this.config == null || this.configFile == null)
		{
			return;
		}
		try
		{
			this.getConfig().save(this.configFile);
			this.lastReload = this.config.saveToString();
		}
		catch(IOException e)
		{
			LogManager.getLogManager().getLogger("[ERROR [GamingBlockPlug]]").log(Level.SEVERE, "[Configuration.java:59]"
					+ "Could not save config to " + this.configFile, e);;
		}
	}
	
	public boolean hasChanged()
	{
		String actual = this.config.saveToString();
		return !actual.equals(this.lastReload);
	}

	public String getFileName()
	{
		return this.configFile.getName().substring(0, this.configFile.getName().lastIndexOf('.'));
	}
}