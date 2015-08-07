package fr.mpp.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.mpp.MetalPonyPlug;

public class MVarConfig
{
	private final MetalPonyPlug mpp;
	private FileConfiguration varConfig = null;
	private File varConfigFile = null;

	public MVarConfig(MetalPonyPlug p_mpp)
	{
		this.mpp = p_mpp;
	}

	/**
	 * This will reload the 'save' file.
	 */
	public void reloadVarConfig()
	{
		if (this.varConfigFile == null)
		{
			this.varConfigFile = new File(this.mpp.getPlugin().getDataFolder(), "varStorage.yml");
		}
		this.varConfig = YamlConfiguration.loadConfiguration(this.varConfigFile);

		Reader defConfigStream = null;
		try
		{
			defConfigStream = new InputStreamReader(this.mpp.getPlugin().getResource("varStorage.yml"), "UTF8");
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
			this.mpp.getLogger().log(Level.SEVERE, "Could not save config to " + this.varConfigFile, ex);
		}
	}
	public void saveDefaultConfig()
	{
		if (this.varConfigFile == null)
		{
			this.varConfigFile = new File(this.mpp.getPlugin().getDataFolder(), "varStorage.yml");
		}
		if (!this.varConfigFile.exists())
		{
			this.mpp.getPlugin().saveResource("varStorage.yml", false);
		}
	}
}