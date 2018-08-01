package me.jesfot.gamingblockplug.data;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;

import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;
import me.jesfot.gamingblockplug.utils.DataUtils;
import me.unei.configuration.api.Configurations;
import me.unei.configuration.api.INBTConfiguration;
import me.unei.configuration.api.exceptions.FileFormatException;

public class GBPWorld
{
	private final World bukkitHandler;
	
	private INBTConfiguration configFile = null;
	private boolean autoRS = false;
	
	public GBPWorld(World bukkitHandler)
	{
		this.bukkitHandler = bukkitHandler;
	}

	public GBPWorld(World bukkitHandler, boolean autoRS)
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
		return this.bukkitHandler.getUID();
	}
	
	public World getHandler()
	{
		return this.bukkitHandler;
	}
	
	public INBTConfiguration getConfig()
	{
		if (this.configFile == null)
		{
			this.configFile = Configurations.newNBTConfig(GBPWorld.getConfigFolder(), this.bukkitHandler.getUID().toString());
			this.configFile.save();
		}
		return this.configFile;
	}
	
	public INBTConfiguration getConfigSection(String path)
	{
		return (INBTConfiguration) this.getConfig().getSubSection(path);
	}
	
	public Map<String, Location> getWarps()
	{
		if (this.autoRS)
		{
			this.safeReload();
		}
		return DataUtils.getLocationMap(this.getConfigSection("Warps"));
	}
	
	public void setWarps(Map<String, Location> warps)
	{
		DataUtils.setLocationMap(this.getConfigSection("Warps"), warps);
		if (this.autoRS)
		{
			this.save();
		}
	}
	
	public Location getSpawn()
	{
		if (this.autoRS)
		{
			this.safeReload();
		}
		return DataUtils.getLocation("Spawn", this.getConfig());
	}
	
	public void setSpawn(Location loc)
	{
		DataUtils.setLocation("Spawn", loc, this.getConfig());
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
		return GamingBlockPlug.getInstance().getConfigFolder("gbp_worlddata");
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (!super.equals(obj))
		{
			return false;
		}
		
		return ((GBPWorld) obj).getHandler().equals(this.getHandler());
	}
	
	@Override
	public int hashCode()
	{
		return super.hashCode() ^ this.bukkitHandler.hashCode();
	}
}
