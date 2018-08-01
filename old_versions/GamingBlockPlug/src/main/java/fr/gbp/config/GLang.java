package fr.gbp.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.gbp.GamingBlockPlug;
import fr.gbp.utils.GText.MLang;

public class GLang
{
	private final GamingBlockPlug gbp;
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;
	private File langFolder = null;
	private MLang language;
	
	public GLang(GamingBlockPlug p_gbp)
	{
		this.gbp = p_gbp;
		this.langFolder = new File(this.gbp.getPlugin().getDataFolder(), "lang");
		this.language = MLang.DEFAULT;
	}
	
	public GLang setLang(MLang lang)
	{
		this.language = lang;
		return this;
	}
	
	public GLang resetLang()
	{
		this.language = MLang.DEFAULT;
		return this;
	}

	/**
	 * This will reload the 'save' file.
	 */
	public GLang reloadLangConfig(MLang lang)
	{
		if (this.customConfigFile == null || !lang.getFile().contains(this.customConfigFile.getName()))
		{
			this.customConfigFile = new File(this.langFolder, lang.getFile());
		}
		this.customConfig = YamlConfiguration.loadConfiguration(this.customConfigFile);

		Reader defConfigStream = null;
		try
		{
			defConfigStream = new InputStreamReader(this.gbp.getPlugin().getResource("lang/"+lang.getFile()), "UTF8");
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
		return this;
	}
	/**
	 * 
	 * @return the save configuration file.
	 */
	public FileConfiguration getLangConfig(MLang lang)
	{
	    if (this.customConfig == null || !lang.getFile().contains(this.customConfigFile.getName()))
	    {
	        this.reloadLangConfig(lang);
	    }
	    return this.customConfig;
	}
	public GLang saveLangConfig(MLang lang)
	{
		if (this.customConfig == null || this.customConfigFile == null || !lang.getFile().contains(this.customConfigFile.getName()))
		{
			return this;
		}
		try
		{
			this.getLangConfig(lang).save(this.customConfigFile);
		}
		catch (IOException ex)
		{
			this.gbp.getLogger().log(Level.SEVERE, "Could not save config to " + this.customConfigFile, ex);
		}
		return this;
	}
	public GLang saveDefaultLangConfig(MLang lang)
	{
		if (this.customConfigFile == null || !lang.getFile().contains(this.customConfigFile.getName()))
		{
			this.customConfigFile = new File(this.langFolder, lang.getFile());
		}
		if (!this.customConfigFile.exists())
		{
			this.gbp.getPlugin().saveResource("lang/"+lang.getFile(), false);
		}
		return this;
	}
	
	public String get(String path)
	{
		return this.get(path, this.language);
	}
	
	public String get(String path, String value)
	{
		return this.get(path, value, this.language);
	}
	
	public String get(String path, MLang lang)
	{
		return this.get(path, "", lang);
	}
	
	public String get(String path, String value, MLang lang)
	{
		if(path == null || path == "" || path.contains(" "))
		{
			return null;
		}
		String path2 = path.replaceAll("-", "_");
		this.reloadLangConfig(lang);
		if(!this.getLangConfig(lang).contains(path2))
		{
			this.getLangConfig(lang).createSection(path2);
			this.getLangConfig(lang).set(path2, value);
		}
		return this.getLangConfig(lang).getString(path2, value);
	}
}