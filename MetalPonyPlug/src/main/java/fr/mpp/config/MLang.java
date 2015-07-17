package fr.mpp.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.mpp.MText;
import fr.mpp.MetalPonyPlug;

public class MLang
{
	private final MetalPonyPlug mpp;
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;
	private File langFolder = null;
	private MText.MLang language;

	public MLang(MetalPonyPlug p_mpp)
	{
		this.mpp = p_mpp;
		this.langFolder = new File(this.mpp.getPlugin().getDataFolder(), "lang");
		language = MText.MLang.DEFAULT;
	}
	
	public MLang setLang(MText.MLang lang)
	{
		this.language = lang;
		return this;
	}
	
	public MLang resetLang()
	{
		this.language = MText.MLang.DEFAULT;
		return this;
	}

	/**
	 * This will reload the 'save' file.
	 */
	public MLang reloadLangConfig(MText.MLang lang)
	{
		if (this.customConfigFile == null || !lang.getFile().contains(this.customConfigFile.getName()))
		{
			this.customConfigFile = new File(this.langFolder, lang.getFile());
		}
		this.customConfig = YamlConfiguration.loadConfiguration(this.customConfigFile);

		Reader defConfigStream = null;
		try
		{
			defConfigStream = new InputStreamReader(this.mpp.getPlugin().getResource("lang/"+lang.getFile()), "UTF8");
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
	public FileConfiguration getLangConfig(MText.MLang lang)
	{
	    if (this.customConfig == null || !lang.getFile().contains(this.customConfigFile.getName()))
	    {
	        this.reloadLangConfig(lang);
	    }
	    return this.customConfig;
	}
	public MLang saveLangConfig(MText.MLang lang)
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
			this.mpp.getLogger().log(Level.SEVERE, "Could not save config to " + this.customConfigFile, ex);
		}
		return this;
	}
	public MLang saveDefaultLangConfig(MText.MLang lang)
	{
		if (this.customConfigFile == null || !lang.getFile().contains(this.customConfigFile.getName()))
		{
			this.customConfigFile = new File(this.langFolder, lang.getFile());
		}
		if (!this.customConfigFile.exists())
		{
			this.mpp.getPlugin().saveResource("lang/"+lang.getFile(), false);
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
	
	public String get(String path, MText.MLang lang)
	{
		return this.get(path, "", lang);
	}
	
	public String get(String path, String value, MText.MLang lang)
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