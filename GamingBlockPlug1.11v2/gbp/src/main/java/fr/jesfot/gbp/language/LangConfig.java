package fr.jesfot.gbp.language;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.bukkit.ChatColor;

import fr.jesfot.gbp.GamingBlockPlug_1_11;

public class LangConfig
{
	public final String BASE_FOLDER;
	
	private Lang language;
	private Properties values;
	private File configFile;
	private GamingBlockPlug_1_11 gbp;
	
	public LangConfig(GamingBlockPlug_1_11 p_gbp)
	{
		this.gbp = p_gbp;
		BASE_FOLDER = this.gbp.getConfigs().getMainConfig().getConfig().getString("language.folder", "lang");
		this.values = new Properties();
		this.setLang(Lang.DEFAULT);
	}
	
	public LangConfig setLang(Lang lang)
	{
		this.gbp.getLogger().finest("Changing default language to " + lang.getName() + " (" + lang.toString() + ").");
		this.language = lang;
		this.configFile = new File(this.gbp.getPlugin().getDataFolder(), BASE_FOLDER + File.separator + this.language.getFile());
		if(!this.configFile.exists())
		{
			GamingBlockPlug_1_11.getTheLogger().finer("File " + lang.getFile() + " does not exists, creating it...");
			if(this.configFile.getParentFile().mkdirs())
			{
				try
				{
					if(this.configFile.createNewFile())
					{
						GamingBlockPlug_1_11.getTheLogger().finer("File " + lang.getFile() + " successfuly created.");
					}
					else
					{
						GamingBlockPlug_1_11.getTheLogger().warning("File " + lang.getFile() + " already exists ???");
					}
				}
				catch(IOException e)
				{
					GamingBlockPlug_1_11.getTheLogger().warning("File " + lang.getFile() + " could not be created...");
				}
			}
			else if(!this.configFile.getParentFile().exists())
			{
				GamingBlockPlug_1_11.getTheLogger().warning("File " + lang.getFile() + "'s parents folders could not be created...");
			}
		}
		try
		{
			this.gbp.getLogger().finest("Loading file for this language (" + this.configFile.getPath() + ").");
			this.values.load(new FileReader(this.configFile));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return this;
	}
	
	public LangConfig resetLang()
	{
		this.setLang(Lang.DEFAULT);
		return this;
	}
	
	public void reload()
	{
		try
		{
			this.gbp.getLogger().finest("Reloading file for this language (" + this.configFile.getPath() + ").");
			this.values.load(new FileReader(configFile));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void save()
	{
		try
		{
			this.gbp.getLogger().finest("Saving file for this language (" + this.configFile.getPath() + ").");
			this.values.store(new FileWriter(configFile), "Lang files");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public String get(String path)
	{
		return this.get(path, "");
	}
	
	public String get(String path, String defaultValue)
	{
		if(path == null || path == "" || path.contains(" "))
		{
			return defaultValue;
		}
		String path2 = path.replaceAll("-", "_");
		this.reload();
		if(!this.values.containsKey(path2))
		{
			this.values.setProperty(path2, defaultValue);
		}
		return this.values.getProperty(path2, defaultValue);
	}
	
	public String getColored(String path, String defaultValue)
	{
		return ChatColor.translateAlternateColorCodes('&', this.get(path, defaultValue));
	}
	
	public String getColored(String path)
	{
		return ChatColor.translateAlternateColorCodes('&', this.get(path));
	}
}