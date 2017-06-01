package fr.jesfot.gbp;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import fr.jesfot.gbp.configuration.Configurations;
import fr.jesfot.gbp.language.Lang;
import fr.jesfot.gbp.language.LangConfig;
import fr.jesfot.gbp.players.PlayerManager;
import fr.jesfot.gbp.utils.ServerUtils;

public class GamingBlockPlug_1_11 extends ServerUtils
{
	private static GamingBlockPlug_1_11 PLUGIN;
	
	private final Logger logger;
	private final JavaPlugin plugin;
	
	private PlayerManager playerManager;
	
	private LangConfig lang;
	
	private Configurations configs;
	
	public GamingBlockPlug_1_11(Server p_server, Logger p_logger, JavaPlugin p_plugin)
	{
		super(p_server);
		this.logger = p_logger;
		this.plugin = p_plugin;
		PLUGIN = this;
	}
	
	public void onLoad()
	{
		this.logger.log(Level.INFO, "Loading plugin " + RefString.NAME + "...");
		
		this.logger.log(Level.FINEST, "Creating data configuration folder if not exists...");
		if(!this.plugin.getDataFolder().exists())
		{
			if(this.plugin.getDataFolder().mkdirs())
			{
				this.logger.log(Level.FINEST, "Successfuly created the configuration folder.");
			}
			else
			{
				this.logger.log(Level.WARNING, "Could not create the configuration directory tree.");
			}
		}
		
		this.logger.info("Loading configuration files...");
		this.configs = new Configurations(this.plugin.getDataFolder());
		
		this.configs.setMainConfig("GamingBlockPlug.yml");
		this.configs.addConfig("offlines", "offlines.yml");
		this.configs.addConfig("bot_discord", "discord_config.yml");
		
		this.configs.initAll();
		this.configs.loadAll();
		this.configs.saveAll();
		this.logger.info("Successfuly loaded configuration files.");

		this.logger.info("Loading language configuration....");
		this.lang = new LangConfig(this);
		this.lang.setLang(Lang.getByID(this.configs.getMainConfig().getConfig().getInt("language.id", -1)));
		this.logger.info("Successfuly loaded language configuration.");
		
		this.playerManager = new PlayerManager(this);
		
		this.logger.log(Level.INFO, "Successfuly loaded plugin " + RefString.NAME + ".");
	}
	
	public void onEnable()
	{
		this.logger.log(Level.INFO, "Enabling plugin " + RefString.NAME + "...");
		//
		this.logger.log(Level.INFO, "Successfuly enabled plugin " + RefString.NAME + ".");
	}
	
	public void onDisable()
	{
		this.logger.log(Level.INFO, "Disabling plugin " + RefString.NAME + "...");

		this.logger.info("Saving configuration files...");
		this.configs.saveAll();
		this.lang.save();
		this.logger.info("Successfuly saved configuration files.");
		
		this.logger.log(Level.INFO, "Successfuly disabled plugin " + RefString.NAME + ".");
	}
	
	public PlayerManager getPlayerManager()
	{
		return this.playerManager;
	}
	
	public File getPlayerDataFolder()
	{
		String fName = this.getConfigs().getMainConfig().getConfig().getString("players.folder", "playerdatas");
		return new File(this.getPlugin().getDataFolder(), fName);
	}
	
	public Configurations getConfigs()
	{
		return this.configs;
	}
	
	public LangConfig getLang()
	{
		return this.lang;
	}
	
	public Logger getLogger()
	{
		return logger;
	}
	
	public JavaPlugin getPlugin()
	{
		return plugin;
	}
	
	public static GamingBlockPlug_1_11 getMe()
	{
		return PLUGIN;
	}
	
	public static Logger getTheLogger()
	{
		return GamingBlockPlug_1_11.getMe().getLogger();
	}
}