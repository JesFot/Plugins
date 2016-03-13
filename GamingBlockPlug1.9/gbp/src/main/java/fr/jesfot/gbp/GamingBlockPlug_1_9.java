package fr.jesfot.gbp;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import fr.jesfot.gbp.command.CommandManager;
import fr.jesfot.gbp.command.TestGbpCommand;
import fr.jesfot.gbp.configuration.Configuration;
import fr.jesfot.gbp.configuration.Configurations;
import fr.jesfot.gbp.configuration.LangConfig;
import fr.jesfot.gbp.configuration.NBTConfig;
import fr.jesfot.gbp.configuration.NBTSubConfig;
import fr.jesfot.gbp.lang.Lang;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.utils.ServerUtils;

public class GamingBlockPlug_1_9 extends ServerUtils
{
	private final Logger logger;
	private final JavaPlugin plugin;
	
	private Configurations configs;
	private NBTConfig mainNBTConfig;
	private Permissions permissions;
	
	private LangConfig lang;
	
	public GamingBlockPlug_1_9(Server p_server, Logger p_logger, JavaPlugin p_plugin)
	{
		super(p_server);
		this.logger = p_logger;
		this.plugin = p_plugin;
	}
	
	public void onLoad()
	{
		this.logger.log(Level.INFO, "Starting loading plugin " + RefString.NAME + " for CraftBukkit " + RefString.MCVERSION);
		
		if(!this.plugin.getDataFolder().exists())
		{
			this.plugin.getDataFolder().mkdirs();
		}
		
		this.lang = new LangConfig(this);
		
		this.configs = new Configurations(this.plugin.getDataFolder());
		Configuration cfg = new Configuration(new File(this.plugin.getDataFolder(), "GamingBlockPlug.yml"));
		this.configs.setMainConfig(cfg);
		this.configs.loadAll();
		this.lang.setLang(Lang.getByID(cfg.getConfig().getInt("lang", -1)));
		
		this.mainNBTConfig = new NBTConfig(this.plugin.getDataFolder(), "GamingBlockPlug_Config");
		
		this.permissions = new Permissions(this);
		
		CommandManager.registerCommands(new TestGbpCommand());
		
		this.logger.log(Level.INFO, "Plugin "+RefString.NAME+" loaded.");
	}
	
	public void onEnable()
	{
		this.permissions.registerPerms();
		
		CommandManager.loadCommands(this.plugin);
	}
	
	public void onDisable()
	{
		this.permissions.unregisterPerms();
		
		CommandManager.onPluginStopped(this.plugin);
	}
	
	// Getters :
	
	/**
	 * Put key to 'null' to get the parent tag.
	 * 
	 * @param key
	 * @return
	 */
	public NBTSubConfig getNBT(String key)
	{
		return new NBTSubConfig(this.mainNBTConfig, key);
	}
	
	public Permissions getPermissionManager()
	{
		return this.permissions;
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
}