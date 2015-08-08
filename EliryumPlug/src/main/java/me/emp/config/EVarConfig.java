package me.emp.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.emp.EliryumPlug;

public class EVarConfig
{
	private final EliryumPlug emp;
	private FileConfiguration varConfig = null;
	private File varConfigFile = null;

	public EVarConfig(EliryumPlug p_emp)
	{
		this.emp = p_emp;
	}

	/**
	 * This will reload the 'save' file.
	 */
	public void reloadVarConfig()
	{
		if (this.varConfigFile == null)
		{
			this.varConfigFile = new File(this.emp.getPlugin().getDataFolder(), "varStorage.yml");
		}
		this.varConfig = YamlConfiguration.loadConfiguration(this.varConfigFile);

		Reader defConfigStream = null;
		try
		{
			defConfigStream = new InputStreamReader(this.emp.getPlugin().getResource("varStorage.yml"), "UTF8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		if (defConfigStream != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			this.varConfig.setDefaults(defConfig);
		}
	}
	/**
	 * 
	 * @return the save configuration file.
	 */
	public FileConfiguration getVarConfig()
	{
		if (this.varConfig == null)
		{
			this.reloadVarConfig();
		}
		return this.varConfig;
	}
	public void saveVarConfig()
	{
		if (this.varConfig == null || this.varConfigFile == null)
		{
			return;
		}
		try
		{
			this.getVarConfig().save(this.varConfigFile);
		}
		catch (IOException ex)
		{
			this.emp.getLogger().log(Level.SEVERE, "Could not save config to " + this.varConfigFile, ex);
		}
	}
	public void saveDefaultConfig()
	{
		if (this.varConfigFile == null)
		{
			this.varConfigFile = new File(this.emp.getPlugin().getDataFolder(), "varStorage.yml");
		}
		if (!this.varConfigFile.exists())
		{
			this.emp.getPlugin().saveResource("varStorage.yml", false);
		}
	}
}