package fr.jesfot.gbp;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import fr.jesfot.gbp.command.CommandManager;
import fr.jesfot.gbp.command.GEcoCommand;
import fr.jesfot.gbp.command.GHomeCommand;
import fr.jesfot.gbp.command.GTpaCommand;
import fr.jesfot.gbp.command.GWorldCommand;
import fr.jesfot.gbp.command.TestGbpCommand;
import fr.jesfot.gbp.configuration.Configuration;
import fr.jesfot.gbp.configuration.Configurations;
import fr.jesfot.gbp.configuration.LangConfig;
import fr.jesfot.gbp.configuration.NBTConfig;
import fr.jesfot.gbp.configuration.NBTSubConfig;
import fr.jesfot.gbp.economy.GEconomy;
import fr.jesfot.gbp.economy.Money;
import fr.jesfot.gbp.lang.Lang;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.utils.ServerUtils;
import fr.jesfot.gbp.world.WorldLoader;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.NBTTagList;
import net.minecraft.server.v1_9_R1.NBTTagString;

public class GamingBlockPlug_1_9 extends ServerUtils
{
	private final Logger logger;
	private final JavaPlugin plugin;
	
	private Configurations configs;
	private NBTConfig mainNBTConfig;
	private Permissions permissions;
	private GEconomy economy;
	
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
		
		Money.reload(this);
		
		this.lang.setLang(Lang.getByID(cfg.getConfig().getInt("lang", -1)));
		
		this.mainNBTConfig = new NBTConfig(this.plugin.getDataFolder(), "GamingBlockPlug_Config");
		
		this.permissions = new Permissions(this);
		
		CommandManager.registerCommands(new TestGbpCommand(), new GTpaCommand(this), new GEcoCommand(this),
				new GHomeCommand(this), new GWorldCommand(this));
		
		this.logger.log(Level.INFO, "Plugin "+RefString.NAME+" loaded.");
	}
	
	public void onEnable()
	{
		this.permissions.registerPerms();
		
		this.economy = new GEconomy(this);
		
		CommandManager.loadCommands(this.plugin);
		
		this.mainNBTConfig.readNBTFromFile();
		NBTTagList list = this.mainNBTConfig.getCopy().getList("LoadedWorlds", 8);
		for(int i = 0; i < list.size(); i++)
		{
			String wName = list.getString(i);
			WorldLoader.loadWorld(this, wName);
		}
	}
	
	public void onDisable()
	{
		this.permissions.unregisterPerms();
		
		CommandManager.onPluginStopped(this.plugin);
		
		this.mainNBTConfig.readNBTFromFile();
		NBTTagList list = new NBTTagList();
		for(World w : this.getServer().getWorlds())
		{
			list.add(new NBTTagString(w.getName()));
		}
		NBTTagCompound a = this.mainNBTConfig.getCopy();
		a.set("LoadedWorlds", list);
		this.mainNBTConfig.setCopy(a).writeNBTToFile();
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
	
	public File getConfigFolder(String name)
	{
		return new File(this.plugin.getDataFolder(), name);
	}
	
	public LangConfig getLang()
	{
		return this.lang;
	}
	
	public GEconomy getEconomy()
	{
		return this.economy;
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