package me.jesfot.gamingblockplug.configuration;

import static me.unei.configuration.api.Configurations.newConfig;
import static me.unei.configuration.api.Configurations.newYAMLConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import me.unei.configuration.SavedFile;
import me.unei.configuration.api.Configurations.ConfigurationType;
import me.unei.configuration.api.IFlatConfiguration;
import me.unei.configuration.api.IYAMLConfiguration;
import me.unei.configuration.api.exceptions.FileFormatException;

/**
 * Multiple configuration files holder.
 * 
 * @author JësFot
 * @since 1.13-1.0.0
 */
public final class GBPConfigurations
{
	/** The folder where player data should be in. */
	public static final String PLAYERS_DATS = "gbp_playerdata";
	
	private Map<String, IFlatConfiguration> configs = new HashMap<>();
	private File mainDir;
	private IYAMLConfiguration mainConfig;
	
	/**
	 * Create a new Configuration holder.
	 * 
	 * @param directory The main configuration directory.
	 * @throws IllegalArgumentException If `directory` is not a directory.
	 */
	public GBPConfigurations(File directory)
	{
		if (directory.exists() && !directory.isDirectory())
		{
			throw new IllegalArgumentException("'directory' argument must be a directory on file system");
		}
		this.mainDir = directory;
	}
	
	public GBPConfigurations setMainConfig(IYAMLConfiguration config)
	{
		this.mainConfig = config;
		return this;
	}
	
	public GBPConfigurations setMainConfig(String fileName)
	{
		this.mainConfig = newYAMLConfig(new SavedFile(new File(this.mainDir, fileName)));
		return this;
	}
	
	public GBPConfigurations addConfig(String name, IFlatConfiguration config)
	{
		this.configs.put(name, config);
		return this;
	}
	
	public GBPConfigurations addConfig(IFlatConfiguration config)
	{
		this.addConfig(config.getFileName(), config);
		return this;
	}
	
	public GBPConfigurations addConfig(String name, ConfigurationType type, String fileName)
	{
		this.configs.put(name, newConfig(type, new SavedFile(new File(this.mainDir, fileName)), null));
		return this;
	}
	
	public GBPConfigurations addConfig(ConfigurationType type, String fileName)
	{
		this.addConfig(fileName, newConfig(type, new SavedFile(new File(this.mainDir, fileName)), null));
		return this;
	}
	
	public GBPConfigurations removeConfig(String name)
	{
		this.configs.remove(name);
		return this;
	}
	
	public GBPConfigurations removeConfig(IFlatConfiguration config)
	{
		this.configs.remove(config.getFileName());
		return this;
	}
	
	public void loadAll()
	{
		try
		{
			this.mainConfig.reload();
		}
		catch (FileFormatException ignored) { /* ignored */ }
		for (IFlatConfiguration cfg : this.configs.values())
		{
			try
			{
				cfg.reload();
			}
			catch (FileFormatException ignored) { /* ignored */ }
		}
	}
	
	public void saveAll()
	{
		this.mainConfig.save();
		for (IFlatConfiguration cfg : this.configs.values())
		{
			cfg.save();
		}
	}
	
	public IYAMLConfiguration getMainConfig()
	{
		return this.mainConfig;
	}
	
	public IFlatConfiguration getConfig(String configName)
	{
		return this.configs.get(configName);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getConfigOfType(String configName, Class<T> type)
	{
		IFlatConfiguration tmp = this.getConfig(configName);
		if (tmp != null && type.isAssignableFrom(tmp.getClass()))
		{
			return (T) tmp;
		}
		return null;
	}
}
