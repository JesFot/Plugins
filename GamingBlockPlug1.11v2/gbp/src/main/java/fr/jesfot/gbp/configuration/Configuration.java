package fr.jesfot.gbp.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.jesfot.gbp.GamingBlockPlug_1_11;

public class Configuration
{
	private FileConfiguration config = null;
	private File configFile = null;
	private String lastSave = null;
	
	public Configuration(File p_configFile)
	{
		this.configFile = p_configFile;
	}
	
	public void initFile()
	{
		if(this.configFile == null)
		{
			return;
		}
		if(!this.configFile.exists())
		{
			GamingBlockPlug_1_11.getTheLogger().finer("Configuration " + this.getFileName() + " does not exists, creating it...");
			if(this.configFile.getParentFile().mkdirs())
			{
				try
				{
					if(this.configFile.createNewFile())
					{
						this.lastSave = "";
						GamingBlockPlug_1_11.getTheLogger().finer("Configuration " + this.getFileName() + " successfuly created.");
					}
					else
					{
						GamingBlockPlug_1_11.getTheLogger().warning("Configuration " + this.getFileName() + " already exists ???");
					}
				}
				catch(IOException e)
				{
					GamingBlockPlug_1_11.getTheLogger().warning("Configuration " + this.getFileName() + " cannot be created...");
				}
			}
			else
			{
				GamingBlockPlug_1_11.getTheLogger().warning("Configuration " + this.getFileName() + "'s parents folders cannot be created...");
			}
		}
	}
	
	public void reloadConfig()
	{
		if(this.configFile == null)
		{
			return;
		}
		if(this.config != null)
		{
			try
			{
				GamingBlockPlug_1_11.getTheLogger().finer("Reloading configuration " + this.getFileName() + "...");
				this.config.load(this.configFile);
				this.lastSave = this.config.saveToString();
			}
			catch(Exception e)
			{
				GamingBlockPlug_1_11.getTheLogger().warning("Error while reloading configuration " + this.getFileName() + ".");
				e.printStackTrace();
			}
			return;
		}
		GamingBlockPlug_1_11.getTheLogger().fine("Loading configuration " + this.getFileName() + "...");
		this.config = YamlConfiguration.loadConfiguration(this.configFile);
		try
		{
			this.config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(Configuration.class.getResourceAsStream("/"+this.configFile.getName()), StandardCharsets.UTF_8)));
		}
		catch(IllegalArgumentException e)
		{}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
		this.lastSave = this.config.saveToString();
	}
	
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
			GamingBlockPlug_1_11.getTheLogger().finer("Saving configuration " + this.getFileName() + "...");
			this.config.save(this.configFile);
			this.lastSave = this.config.saveToString();
		}
		catch(IOException ioe)
		{
			GamingBlockPlug_1_11.getTheLogger().warning("Error while saving configuration " + this.getFileName() + ".");
			ioe.printStackTrace();
		}
	}
	
	public boolean hasChanged()
	{
		if(this.config == null)
		{
			return false;
		}
		return this.lastSave.contentEquals(this.config.saveToString());
	}

	public String getFileName()
	{
		return this.configFile.getName().substring(0, this.configFile.getName().lastIndexOf('.'));
	}
}