package fr.jesfot.gbp;

import java.io.File;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import fr.jesfot.gbp.command.CommandManager;
import fr.jesfot.gbp.command.GEcoCommand;
import fr.jesfot.gbp.command.GFlyCommand;
import fr.jesfot.gbp.command.GHomeCommand;
import fr.jesfot.gbp.command.GIslandCommand;
import fr.jesfot.gbp.command.GMuteCommand;
import fr.jesfot.gbp.command.GPassNightCommand;
import fr.jesfot.gbp.command.GPermsCommand;
import fr.jesfot.gbp.command.GPingCommand;
import fr.jesfot.gbp.command.GSalaryCommand;
import fr.jesfot.gbp.command.GSecurityWallCommand;
import fr.jesfot.gbp.command.GSeedCommand;
import fr.jesfot.gbp.command.GShopCommand;
import fr.jesfot.gbp.command.GSpyChestCommand;
import fr.jesfot.gbp.command.GTeamCommand;
import fr.jesfot.gbp.command.GTestForCommand;
import fr.jesfot.gbp.command.GTpaCommand;
import fr.jesfot.gbp.command.GTpcCommand;
import fr.jesfot.gbp.command.GVarCommand;
import fr.jesfot.gbp.command.GWorldCommand;
import fr.jesfot.gbp.command.LogMessageCommand;
import fr.jesfot.gbp.command.SpectateCommand;
import fr.jesfot.gbp.command.TestGbpCommand;
import fr.jesfot.gbp.configuration.Configuration;
import fr.jesfot.gbp.configuration.Configurations;
import fr.jesfot.gbp.configuration.LangConfig;
import fr.jesfot.gbp.configuration.NBTConfig;
import fr.jesfot.gbp.configuration.NBTSubConfig;
import fr.jesfot.gbp.economy.GEconomy;
import fr.jesfot.gbp.economy.Money;
import fr.jesfot.gbp.lang.Lang;
import fr.jesfot.gbp.logging.GSecurityLogger;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.scoreboard.ScoreboardManager;
import fr.jesfot.gbp.shop.Shops;
import fr.jesfot.gbp.subsytems.VariableSys;
import fr.jesfot.gbp.teams.GTeam;
import fr.jesfot.gbp.teams.TeamManager;
import fr.jesfot.gbp.utils.ServerUtils;
import fr.jesfot.gbp.world.WorldLoader;
import fr.jesfot.gbp.zoning.island.IslandZone;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NBTTagString;

public class GamingBlockPlug_1_12 extends ServerUtils
{
	private static GamingBlockPlug_1_12 PLUGIN;
	
	private static GSecurityLogger secureLogger;
	
	private final Logger logger;
	private final JavaPlugin plugin;
	
	private Configurations configs;
	private NBTConfig mainNBTConfig;
	private ScoreboardManager scoreManager;
	private Permissions permissions;
	private GEconomy economy;
	private Shops shops;
	
	private LangConfig lang;
	
	private VariableSys vars;
	
	private TeamManager teams;
	
	private IslandZone island;
	
	private GWorldCommand worldCmd;
	
	public GamingBlockPlug_1_12(Server p_server, Logger p_logger, JavaPlugin p_plugin)
	{
		super(p_server);
		this.logger = p_logger;
		this.plugin = p_plugin;
		GamingBlockPlug_1_12.PLUGIN = this;
		GamingBlockPlug_1_12.secureLogger = new GSecurityLogger(this.getConfigFolder("logs"), "GBP_logs.log");
	}
	
	public void onLoad()
	{
		this.logger.log(Level.INFO,
				"Starting loading plugin " + RefString.NAME + " for CraftBukkit " + RefString.MCVERSION);
		
		if (!this.plugin.getDataFolder().exists())
		{
			this.plugin.getDataFolder().mkdirs();
		}
		
		this.lang = new LangConfig(this);
		
		this.getLogger().info("Loading configuration...");
		
		this.configs = new Configurations(this.plugin.getDataFolder());
		Configuration cfg = new Configuration(new File(this.plugin.getDataFolder(), "GamingBlockPlug.yml"));
		Configuration cfgplayerslist = new Configuration(new File(this.plugin.getDataFolder(), "offlines.yml"));
		this.configs.setMainConfig(cfg);
		this.configs.addConfig("offlines", cfgplayerslist);
		this.configs.addConfig("bot_discord", "discord_config.yml");
		this.configs.loadAll();
		
		Money.reload(this);
		
		this.lang.setLang(Lang.getByID(cfg.getConfig().getInt("lang", -1)));
		
		this.mainNBTConfig = new NBTConfig(this.plugin.getDataFolder(), "GamingBlockPlug_Config");
		
		this.getLogger().info("Configuration loaded !");
		
		this.scoreManager = new ScoreboardManager(this);
		
		this.permissions = new Permissions(this);
		
		this.shops = new Shops(this);
		
		this.worldCmd = new GWorldCommand(this);
		CommandManager.registerCommands(new TestGbpCommand(), new GTpaCommand(this), new GEcoCommand(this),
				new GHomeCommand(this), this.worldCmd, new GPermsCommand(this), new GVarCommand(this),
				new GIslandCommand(this), new GPassNightCommand(this), new GSeedCommand(),
				new GSecurityWallCommand(this), new LogMessageCommand(this), new GShopCommand(this), new GPingCommand(),
				new GFlyCommand(this), new GTpcCommand(this), new GTeamCommand(this), new SpectateCommand(this),
				new GSpyChestCommand(this), new GSalaryCommand(this), new GMuteCommand(this),
				new GTestForCommand(this));
		
		this.worldCmd.registerWorldsPublicPermsS(Arrays.asList("hub", "crea", "gallifrey"));
		this.worldCmd.registerWorldsPrivatePermsS(Arrays.asList("worms"));
		
		this.worldCmd.registerWorldsPublicPerms(this.getServer().getWorlds());
		
		this.logger.log(Level.INFO, "Plugin " + RefString.NAME + " loaded.");
	}
	
	public void onEnable()
	{
		this.logger.info("Starting enabling plugin " + RefString.NAME + "...");
		this.logger.info("Loading Variables system...");
		this.vars = new VariableSys(new NBTSubConfig(this.mainNBTConfig));
		this.vars.getFromFile();
		this.vars.storeBool("plugin", true);
		this.vars.storeToFile();
		this.logger.info("Loaded !");
		
		this.logger.info("Registering Permissions...");
		this.permissions.registerPerms();
		this.logger.info("Registred !");
		
		this.logger.info("Loading economy & shop systems...");
		this.economy = new GEconomy(this);
		
		this.shops.loadShops();
		this.logger.info("Loaded !");
		
		CommandManager.loadCommands(this.plugin);
		
		this.logger.info("Loading worlds...");
		this.mainNBTConfig.readNBTFromFile();
		NBTTagList list = this.mainNBTConfig.getCopy().getList("LoadedWorlds", 8);
		for (int i = 0; i < list.size(); i++)
		{
			String wName = list.getString(i);
			WorldLoader.loadWorld(this, wName);
		}
		this.logger.info("Loaded " + list.size() + " worlds");
		
		this.logger.info("Initializing floating island...");
		this.island = new IslandZone(this);
		this.island.readCenter();
		if (!this.island.hasPoints())
		{
			this.logger.warning("No location stored for the island....");
			this.island.disable();
		}
		this.logger.info("Island enabled !");
		this.island.disable();
		this.logger.info("Island disabled !");
		
		this.logger.info("Loading team system...");
		this.teams = new TeamManager(this);
		this.teams.reloadTeams();
		GTeam def = this.teams.getOrCreate("default");
		def.setCanOpenShops(false);
		def.setCanUseTpa(false);
		def.setCanUseWorld(false);
		def.setChatColor("&0");
		def.setDisplayName("default");
		def.setMaxHomes(2);
		this.teams.saveAll();
		this.logger.info("Team system: ok !");
		
		this.logger.info("Setupping scoreboard for Economy...");
		this.scoreManager.registerObjective("money", "Money:");
		this.logger.info("Scoreboard: ok !");
		this.logger.log(Level.INFO, "Plugin " + RefString.NAME + " enabled.");
	}
	
	public void onDisable()
	{
		this.logger.info("Starting disabling plugin " + RefString.NAME + "...");
		
		this.logger.info("Unregistering Permissions...");
		this.permissions.unregisterPerms();
		this.logger.info("Done !");
		
		CommandManager.onPluginStopped(this.plugin);
		
		this.logger.info("Save & delete shops...");
		this.shops.saveShops();
		this.shops.subDeleteAll();
		this.logger.info("Done !");
		
		this.logger.info("Saving loaded worlds...");
		this.mainNBTConfig.readNBTFromFile();
		NBTTagList list = new NBTTagList();
		for (World w : this.getServer().getWorlds())
		{
			list.add(new NBTTagString(w.getName()));
		}
		NBTTagCompound a = this.mainNBTConfig.getCopy();
		a.set("LoadedWorlds", list);
		this.mainNBTConfig.setCopy(a).writeNBTToFile();
		this.logger.info("Saved " + list.size() + " worlds");
		
		this.teams.saveAll();
		
		this.logger.info("Saving and turn off variables system...");
		this.vars.storeToFile();
		this.logger.info("Done !");
		this.logger.log(Level.INFO, "Plugin " + RefString.NAME + " disabled.");
	}
	
	// Getters :
	
	@Override
	public OfflinePlayer getOfflinePlayerByName(final String name)
	{
		this.configs.getConfig("offlines").reloadConfig();
		ConfigurationSection sct = this.configs.getConfig("offlines").getConfig()
				.getConfigurationSection(this.isOnlineMode() ? "official" : "cracked");
		Set<String> pls = sct.getKeys(false);
		for (String str : pls)
		{
			if (str.equalsIgnoreCase(name))
			{
				return (this.getServer().getOfflinePlayer(UUID.fromString(sct.getString(str))));
			}
		}
		return super.getOfflinePlayerByName(name);
	}
	
	/**
	 * Put key to 'null' to get the parent tag.
	 * 
	 * @param key
	 * @return The configuration section
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
	
	public ScoreboardManager getScoreboardManager()
	{
		return this.scoreManager;
	}
	
	public File getConfigFolder(String name)
	{
		if (name == null)
		{
			return this.plugin.getDataFolder();
		}
		File file = new File(this.plugin.getDataFolder(), name);
		if (!file.exists())
		{
			file.mkdirs();
		}
		return file;
	}
	
	public IslandZone getIsland()
	{
		return this.island;
	}
	
	public LangConfig getLang()
	{
		return this.lang;
	}
	
	public VariableSys getVars()
	{
		return this.vars;
	}
	
	public TeamManager getTeams()
	{
		return this.teams;
	}
	
	public GEconomy getEconomy()
	{
		return this.economy;
	}
	
	public Shops getShops()
	{
		return this.shops;
	}
	
	public Logger getLogger()
	{
		return this.logger;
	}
	
	public JavaPlugin getPlugin()
	{
		return this.plugin;
	}
	
	public static GamingBlockPlug_1_12 getMe()
	{
		return GamingBlockPlug_1_12.PLUGIN;
	}
	
	public static GSecurityLogger getMyLogger()
	{
		return GamingBlockPlug_1_12.secureLogger;
	}
}