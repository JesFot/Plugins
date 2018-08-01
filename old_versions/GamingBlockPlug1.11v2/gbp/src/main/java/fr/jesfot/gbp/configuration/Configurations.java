package fr.jesfot.gbp.configuration;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configurations
{
	public final static String PLAYERS_DATS = "playerdatas";
	
	private Map<String, Configuration> configs;
	private File mainDir;
	private Configuration mainConfig;
	
	public Configurations(File directory)
	{
		this.configs = new HashMap<String, Configuration>();
		this.mainDir = directory;
	}
	
	public Configurations setMainConfig(Configuration p_conf)
	{
		this.mainConfig = p_conf;
		return this;
	}
	
	public Configurations setMainConfig(String fileName)
	{
		this.mainConfig = new Configuration(new File(this.mainDir, fileName));
		return this;
	}
	
	public Configurations addConfig(String name, Configuration config)
	{
		this.configs.put(name, config);
		return this;
	}
	
	public Configurations addConfig(Configuration config)
	{
		this.configs.put(config.getFileName(), config);
		return this;
	}
	
	public Configurations addConfig(String name, String fileName)
	{
		this.configs.put(name, new Configuration(new File(this.mainDir, fileName)));
		return this;
	}
	
	public Configurations addConfig(String fileName)
	{
		this.configs.put(fileName, new Configuration(new File(this.mainDir, fileName)));
		return this;
	}
	
	public Configurations removeConfig(String name)
	{
		this.configs.remove(name);
		return this;
	}
	
	public Configurations removeConfig(Configuration config)
	{
		this.configs.remove(config.getFileName());
		return this;
	}
	
	@Deprecated
	public List<String> getAllConfigNames()
	{
		if(this.configs.isEmpty())
		{
			return Collections.emptyList();
		}
		return Arrays.asList(this.configs.keySet().toArray(new String[]{}));
	}
	
	@Deprecated
	public List<Configuration> getAllConfigs()
	{
		if(this.configs.isEmpty())
		{
			return Collections.emptyList();
		}
		return Arrays.asList(this.configs.values().toArray(new Configuration[]{}));
	}
	
	public void initAll()
	{
		this.mainConfig.initFile();
		for(Configuration cfg : this.configs.values())
		{
			cfg.initFile();
		}
	}
	
	public void loadAll()
	{
		this.mainConfig.reloadConfig();
		for(Configuration cfg : this.configs.values())
		{
			cfg.reloadConfig();
		}
	}
	
	public void saveAll()
	{
		this.mainConfig.saveConfig();
		for(Configuration cfg : this.configs.values())
		{
			cfg.saveConfig();
		}
	}
	
	public void saveIfChanged()
	{
		if(this.mainConfig.hasChanged())
		{
			this.mainConfig.saveConfig();
		}
		for(Configuration cfg : this.configs.values())
		{
			if(cfg.hasChanged())
			{
				cfg.saveConfig();
			}
		}
	}
	
	public Configuration getMainConfig()
	{
		return this.mainConfig;
	}
	
	public Configuration getConfig(String configName)
	{
		return this.configs.get(configName);
	}
}