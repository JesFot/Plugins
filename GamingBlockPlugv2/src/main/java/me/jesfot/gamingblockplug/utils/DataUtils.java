package me.jesfot.gamingblockplug.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import me.unei.configuration.api.IConfiguration;
import me.unei.configuration.api.IFlatConfiguration;
import me.unei.configuration.api.exceptions.FileFormatException;
import me.unei.configuration.api.format.INBTCompound;
import me.unei.configuration.formats.nbtlib.TagCompound;

public class DataUtils
{
	public static boolean safeReload(IFlatConfiguration config)
	{
		try
		{
			config.reload();
			return true;
		}
		catch (FileFormatException ignored)
		{
			return false;
		}
	}
	
	public static INBTCompound setLocation(String key, Location location, INBTCompound store)
	{
		if (location == null)
		{
			return store;
		}
		INBTCompound loc = new TagCompound();
		loc.setDouble("CoordX", location.getX());
		loc.setDouble("CoordY", location.getY());
		loc.setDouble("CoordZ", location.getZ());
		loc.setFloat("Pitch", location.getPitch());
		loc.setFloat("Yaw", location.getYaw());
		loc.setString("World", location.getWorld().getName());
		store.set(key, loc);
		return store;
	}
	
	public static Location getLocation(String key, INBTCompound store)
	{
		if (!store.hasKey(key))
		{
			return null;
		}
		INBTCompound loc = store.getCompound(key);
		double x, y, z;
		float pitch, yaw;
		x = loc.getDouble("CoordX");
		y = loc.getDouble("CoordY");
		z = loc.getDouble("CoordZ");
		pitch = loc.getFloat("Pitch");
		yaw = loc.getFloat("Yaw");
		String world = loc.getString("World");
		World w = Bukkit.getWorld(world);
		if (w == null)
		{
			return null;
		}
		return new Location(w, x, y, z, yaw, pitch);
	}
	
	public static IConfiguration setLocation(String key, Location location, IConfiguration store)
	{
		if (location == null)
		{
			return store;
		}
		IConfiguration loc = store.getSubSection(key);
		loc.setDouble("CoordX", location.getX());
		loc.setDouble("CoordY", location.getY());
		loc.setDouble("CoordZ", location.getZ());
		loc.setFloat("Pitch", location.getPitch());
		loc.setFloat("Yaw", location.getYaw());
		loc.setString("World", location.getWorld().getName());
		return store;
	}
	
	public static Location getLocation(String key, IConfiguration store)
	{
		if (!store.contains(key))
		{
			return null;
		}
		IConfiguration loc = store.getSubSection(key);
		double x, y, z;
		float pitch, yaw;
		x = loc.getDouble("CoordX");
		y = loc.getDouble("CoordY");
		z = loc.getDouble("CoordZ");
		pitch = loc.getFloat("Pitch");
		yaw = loc.getFloat("Yaw");
		String world = loc.getString("World");
		World w = Bukkit.getWorld(world);
		if (w == null)
		{
			return null;
		}
		return new Location(w, x, y, z, yaw, pitch);
	}
	
	public static Map<String, Location> getLocationMap(IConfiguration config)
	{
		Map<String, Location> result = new HashMap<>();
		for (String key : config.getKeys())
		{
			result.put(key, DataUtils.getLocation(key, config));
		}
		return result;
	}
	
	public static void setLocationMap(IConfiguration config, Map<String, Location> warps)
	{
		for (Entry<String, Location> entry : warps.entrySet())
		{
			DataUtils.setLocation(entry.getKey(), entry.getValue(), config);
		}
	}
}
