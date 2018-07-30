package me.jesfot.gamingblockplug.plugin;

import java.io.File;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import me.jesfot.gamingblockplug.Infos;
import me.jesfot.gamingblockplug.configuration.GBPConfigurations;
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
	
	private GBPConfigurations configs = null;
	private INBTConfiguration mainDataStore = null;
	
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
	}
	
	public void onEnable()
	{
		//
	}
	
	public void onDisable()
	{
		this.plugin.getLogger().info(" -> Saving configurations...");
		this.configs.saveAll();
		this.plugin.getLogger().info("     -> Done.");
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
}
