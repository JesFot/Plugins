package me.jesfot.gamingblockplug.plugin;

import java.io.File;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import me.jesfot.gamingblockplug.Infos;
import me.jesfot.gamingblockplug.command.*;
import me.jesfot.gamingblockplug.configuration.GBPConfigurations;
import me.jesfot.gamingblockplug.data.PlayerManager;
import me.jesfot.gamingblockplug.data.WorldManager;
import me.jesfot.gamingblockplug.permission.GBPPermissions;
import me.jesfot.gamingblockplug.roles.Role;
import me.jesfot.gamingblockplug.roles.RoleManager;
import me.jesfot.gamingblockplug.security.SystemManager;
import me.jesfot.gamingblockplug.utils.ServerHelper;
import me.unei.configuration.api.Configurations;
import me.unei.configuration.api.IConfiguration;
import me.unei.configuration.api.INBTConfiguration;
import me.unei.configuration.api.IYAMLConfiguration;
import me.unei.configuration.api.exceptions.FileFormatException;
import me.unei.lang.plugin.UneiLang;

/**
 * Main plugin class.
 * 
 * @author JësFot
 * @since 1.13-1.0.0
 */
public class GamingBlockPlug extends ServerHelper
{
	private static GamingBlockPlug Instance = null;
	
	private final JavaPlugin plugin;
	
	private GBPPermissions permissions = null;
	private GBPConfigurations configs = null;
	private INBTConfiguration mainDataStore = null;

	private SystemManager systemManager;
	private PlayerManager playerManager;
	private WorldManager worldManager;
	private RoleManager roleManager;
	
	GamingBlockPlug(JavaPlugin p_plugin)
	{
		super(p_plugin.getServer());
		this.plugin = p_plugin;
		
		GamingBlockPlug.Instance = this;
	}
	
	public void onLoad()
	{
		this.plugin.getLogger().info("Starting loading plugin " + Infos.GBP_NAME + " version " + Infos.GBP_VERSION + " for MC " + Infos.MC_VERSION);
		
		if (!this.plugin.getDataFolder().exists())
		{
			this.plugin.getDataFolder().mkdirs();
		}
		
		this.plugin.getLogger().info(" -> Loading permissions...");
		try
		{
			this.permissions = new GBPPermissions(this);
		}
		catch (IllegalStateException ignored)
		{
			this.plugin.getLogger().info(" W   -> Multiple instance of GBPPermissions is not permited !");
			this.permissions = GBPPermissions.getInstance();
		}
		this.permissions.load();
		this.plugin.getLogger().info("     -> Permissions loaded !");
		
		this.plugin.getLogger().info(" -> Loading configuration...");
		
		this.configs = new GBPConfigurations(this.plugin.getDataFolder());
		this.configs.setMainConfig(Configurations.newYAMLConfig(this.plugin.getDataFolder(), Infos.GBP_NAME));
		this.configs.addConfig("offlines", Configurations.newYAMLConfig(this.plugin.getDataFolder(), "offlines"));
		this.configs.addConfig("bot_discord", Configurations.newYAMLConfig(this.plugin.getDataFolder(), "discord_config"));
		this.configs.loadAll();
		
		String val = this.configs.getMainConfig().getString("lang");
		val = (val == null || val.isEmpty()) ? "fr_FR" : val;
		this.configs.getMainConfig().setString("lang", val);
		UneiLang.getInstance().getPlayerManager().setDefaultCode(val);
		
		this.mainDataStore = Configurations.newNBTConfig(this.plugin.getDataFolder(), Infos.GBP_NAME + "_Config");
		
		this.plugin.getLogger().info("     -> Configuration loaded !");
		
		this.systemManager = new SystemManager(this);
		
		this.playerManager = new PlayerManager();
		this.playerManager.setAutoReloadSave(true, true);
		
		this.worldManager = new WorldManager();
		this.worldManager.setAutoReloadSave(true, true);
		
		this.plugin.getLogger().info(" -> Registering commands...");
		CommandManager.registerCommand(new RoleCommand(this));
		CommandManager.registerCommand(new PingCommand());
		CommandManager.registerCommand(new SpawnCommand(this));
		CommandManager.registerCommand(new WarpCommand(this));
		CommandManager.registerCommand(new SetWarpCommand(this));
		CommandManager.registerCommand(new MOTDCommand(this));
		CommandManager.registerCommand(new SpectateCommand());
		CommandManager.registerCommand(new FlyCommand(this));
		CommandManager.registerCommand(new ForcePassnightCommand(this));
		CommandManager.registerCommand(new SpyChestCommand(this));
		CommandManager.registerCommand(new VarCommand(this));
		CommandManager.registerCommand(new ToolbCommand());
		this.plugin.getLogger().info("     -> Commands registered !");
	}
	
	public void onEnable()
	{
		this.plugin.getLogger().info(" -> Registering permissions...");
		this.permissions.registerPermissions();
		this.plugin.getLogger().info("     -> Permissions registered !");
		this.plugin.getLogger().info(" -> Loading role system...");
		this.roleManager = new RoleManager(this);
		this.roleManager.reloadRoles();
		Role def = this.roleManager.getOrCreate(RoleManager.DEFAULT_NAME);
		def.setChatColor("&0");
		def.setDisplayName("default");
		this.roleManager.saveAll();
		this.plugin.getLogger().info("     -> Role system loaded !");
		
		CommandManager.loadCommands(this.plugin);
	}
	
	public void onDisable()
	{
		CommandManager.onPluginStopped(this.plugin);
		
		this.plugin.getLogger().info(" -> Saving configurations...");
		this.configs.saveAll();
		this.plugin.getLogger().info("     -> Done.");
		this.plugin.getLogger().info(" -> Un-Registering permissions...");
		this.permissions.unregisterPermissions();
		this.plugin.getLogger().info("     -> Permissions un-registered !");
	}
	
	@Override
	public OfflinePlayer getOfflinePlayerByName(final String name)
	{
		try
		{
			this.configs.getConfig("offlines").reload();
		}
		catch (FileFormatException ignored)
		{
			return super.getOfflinePlayerByName(name);
		}
		IConfiguration sct = ((IYAMLConfiguration)this.configs.getConfig("offlines"))
				.getSubSection(this.isOnlineMode() ? "official" : "cracked");
		Set<String> pls = sct.getKeys();
		for (String str : pls)
		{
			if (str.equalsIgnoreCase(name))
			{
				return (this.getServer().getOfflinePlayer(UUID.fromString(sct.getString(str))));
			}
		}
		return super.getOfflinePlayerByName(name);
	}
	
	public GBPConfigurations getConfigurations()
	{
		return this.configs;
	}
	
	/**
	 * Gets a configuration folder in the right place.
	 * <p>
	 * Pass `null` as parameter to get the root folder.
	 * 
	 * @param name The path of the sub directory.
	 * @return Returns the requested directory.
	 */
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

	/**
	 * Gets a NBT sub-node.
	 * <p>
	 * Pass `null` as parameter to get the root node.
	 * 
	 * @param path The path of the sub node.
	 * @return Returns the requested node.
	 */
	public INBTConfiguration getNBT(String path)
	{
		if (path == null || path.isEmpty())
		{
			return this.mainDataStore;
		}
		return (INBTConfiguration) this.mainDataStore.getSubSection(path);
	}
	
	public JavaPlugin getPlugin()
	{
		return this.plugin;
	}
	
	public Logger getLogger()
	{
		return this.plugin.getLogger();
	}
	
	public static GamingBlockPlug getInstance()
	{
		return GamingBlockPlug.Instance;
	}

	public RoleManager getRoleManager()
	{
		return this.roleManager;
	}
	
	public PlayerManager getPlayerManager()
	{
		return this.playerManager;
	}
	
	public WorldManager getWorldManager()
	{
		return this.worldManager;
	}
	
	public SystemManager getSystemManager()
	{
		return this.systemManager;
	}
}
