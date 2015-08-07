package fr.mpp;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import fr.mpp.bukkit.BukkitPlugin;
import fr.mpp.command.MCommands;
import fr.mpp.config.MConfig;
import fr.mpp.config.MLang;
import fr.mpp.economy.MEconomy;
import fr.mpp.economy.MMensual;
import fr.mpp.perm.MPermissions;
import fr.mpp.quests.MQuest;
import fr.mpp.structs.MGeneralBuilds;
import fr.mpp.utils.MVarObject;

public class MetalPonyPlug
{
	private FileConfiguration conf;
	private MCommands coms;
	private MConfig config;
	private MLang lang;
	private MEconomy economy;
	private MPermissions perms;
	private MQuest quest;
	private final Server server;
	private final Logger logger;
	private final JavaPlugin plugin;
	public static MDebug debug;
	public BukkitTask mensual;
	private MGeneralBuilds generalBuilds;
	private MVarObject mvo;
	protected final MPP mpp = new MPP();
	
	public MetalPonyPlug(Server server, Logger logger, JavaPlugin plugin)
	{
		this.server = server;
		this.plugin = plugin;
		this.logger = logger;
	}
	
	public static void onLoad()
	{
		// Code ...
	}
	
	public void onEnable()
	{
		this.conf = plugin.getConfig();
		this.config = new MConfig(conf, this);
		this.lang = new MLang(this);
		this.coms = new MCommands(this);
		this.perms = new MPermissions();
		this.quest = new MQuest();
		this.economy = new MEconomy(this);
		coms.regCommands();
		setDebug(new MDebug(this));
		debug.setDebugLog(true);
		debug.setPlayerDebugLog(false);
		MPP.setMetalPonyPlug(this);
		this.generalBuilds = new MGeneralBuilds();
		this.mensual = new MMensual(this).start();
		this.mvo = new MVarObject(this.config.getVarConfig());
		this.mvo.getFromFile();
		this.mvo.storeBool("plugin", true);
	}
	
	public void onDisable()
	{
		logger.log(Level.INFO, "Plugin stop.");
		this.config.saveCustomConfig();
		plugin.saveConfig();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		return MCommands.onCommand(sender, cmd, label, args);
	}
	
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return MCommands.onTabComplete(sender, command, alias, args);
	}
	
	public List<String> getConfig(String name)
	{
		return this.conf.getStringList(name);
	}
	
	public Object getMeta(Player player, String name)
	{
		if (!player.hasMetadata(name))
		{
			return false;
		}
		return player.getMetadata(name).get(0).value();
	}
	
	@SuppressWarnings("unused")
	public Object getPerm(Player player, String name)
	{
		if (!player.getEffectivePermissions().contains("MPP."+name))
		{
			return false;
		}
		for (PermissionAttachmentInfo pai : player.getEffectivePermissions())
		{
			String permName = pai.getPermission();
			boolean val = pai.getAttachment().getPermissions().get(name);
			Map<String, Boolean> perms = pai.getAttachment().getPermissions();
		}
		return player.getEffectivePermissions().iterator().next().getPermission();
	}
	
	public void setMeta(Player player, String name, Object meta)
	{
		player.setMetadata(name, new FixedMetadataValue(this.getPlugin(), meta));
	}
	
	public void broad(String msg)
	{
		this.server.broadcastMessage(msg);
	}

	public MConfig getConfig()
	{
		return config;
	}
	
	public MLang getLang()
	{
		return lang;
	}
	
	public MPermissions getPerm()
	{
		return perms;
	}
	
	public MQuest getQuest()
	{
		return quest;
	}
	
	public MEconomy getEconomy()
	{
		return economy;
	}
	
	public Server getServer()
	{
		return server;
	}
	
	public Logger getLogger()
	{
		return logger;
	}
	
	public JavaPlugin getPlugin()
	{
		return plugin;
	}
	
	public static MDebug getDebug()
	{
		return debug;
	}
	
	public static void setDebug(MDebug p_debug)
	{
		debug = p_debug;
	}
	
	public MGeneralBuilds getGeneralBuilds()
	{
		return this.generalBuilds;
	}
	public MGeneralBuilds getBuilds()
	{
		return this.getGeneralBuilds();
	}
	
	public void setGeneralBuilds(MGeneralBuilds builds)
	{
		this.generalBuilds = builds;
	}
	
	public MVarObject getMVO()
	{
		return mvo;
	}
	
	public BukkitPlugin getThisPlugin()
	{
		return new BukkitPlugin();
	}
}