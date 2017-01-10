package fr.jesfot.gbp.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.bukkit.ChatColor;

import fr.jesfot.gbp.GamingBlockPlug_1_11;
import fr.jesfot.gbp.lang.Lang;

public class LangConfig
{
	private Lang language;
	private Properties values;
	private File configFile;
	private GamingBlockPlug_1_11 gbp;
	
	public LangConfig(GamingBlockPlug_1_11 p_gbp)
	{
		this.gbp = p_gbp;
		this.values = new Properties();
		this.setLang(Lang.DEFAULT);
	}
	
	public LangConfig setLang(Lang lang)
	{
		this.language = lang;
		this.configFile = new File(this.gbp.getPlugin().getDataFolder(), "lang" + File.separator + this.language.getFile());
		try
		{
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