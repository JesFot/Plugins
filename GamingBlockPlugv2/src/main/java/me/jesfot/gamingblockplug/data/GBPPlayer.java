package me.jesfot.gamingblockplug.data;

import java.io.File;
import java.util.UUID;

import org.bukkit.OfflinePlayer;

import me.jesfot.gamingblockplug.configuration.GBPConfigurations;
import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;
import me.jesfot.gamingblockplug.utils.DataUtils;
import me.unei.configuration.api.Configurations;
import me.unei.configuration.api.INBTConfiguration;
import me.unei.configuration.api.exceptions.FileFormatException;

public class GBPPlayer
{
	private final OfflinePlayer bukkitHandler;
	
	private INBTConfiguration configFile = null;
	private boolean autoRS = false;
	
	public GBPPlayer(OfflinePlayer bukkitHandler)
	{
		this.bukkitHandler = bukkitHandler;
	}

	public GBPPlayer(OfflinePlayer bukkitHandler, boolean autoRS)
	{
		this.bukkitHandler = bukkitHandler;
		this.autoRS = autoRS;
	}
	
	public void setAutoReloadSave(boolean value)
	{
		this.autoRS = value;
	}
	
	public UUID getUniqueId()
	{
		return this.bukkitHandler.getUniqueId();
	}
	
	public OfflinePlayer getHandler()
	{
		return this.bukkitHandler;
	}
	
	public INBTConfiguration getConfig()
	{
		if (this.configFile == null)
		{
			this.configFile = Configurations.newNBTConfig(GBPPlayer.getConfigFolder(), this.bukkitHandler.getUniqueId().toString());
			this.configFile.setString("Pseudo", this.bukkitHandler.getName());
			this.configFile.save();
		}
		return this.configFile;
	}
	
	public INBTConfiguration getConfigSection(String path)
	{
		return (INBTConfiguration) this.getConfig().getSubSection(path);
	}
	
	public String getRoleId()
	{
		if (this.autoRS)
		{
			this.safeReload();
		}
		return this.getConfig().getString("Role");
	}
	
	public void setRoleId(String id)
	{
		this.getConfig().setString("Role", id);
		if (this.autoRS)
		{
			this.save();
		}
	}
	
	public void save()
	{
		this.getConfig().save();
	}
	
	public void reload() throws FileFormatException
	{
		this.getConfig().reload();
	}
	
	public boolean safeReload()
	{
		return DataUtils.safeReload(this.getConfig());
	}
	
	public static File getConfigFolder()
	{
		return GamingBlockPlug.getInstance().getConfigFolder(GBPConfigurations.PLAYERS_DATS);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (!super.equals(obj))
		{
			return false;
		}
		
		return ((GBPPlayer) obj).getHandler().equals(this.getHandler());
	}
	
	@Override
	public int hashCode()
	{
		return super.hashCode() ^ this.bukkitHandler.hashCode();
	}
}
