package me.jesfot.gamingblockplug.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.unei.configuration.api.IConfiguration;
import me.unei.configuration.api.IFlatConfiguration;
import me.unei.configuration.api.exceptions.FileFormatException;
import me.unei.configuration.api.format.INBTCompound;
import me.unei.configuration.api.format.INBTList;
import me.unei.configuration.formats.nbtlib.TagCompound;
import me.unei.configuration.formats.nbtlib.TagList;

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
	
	public static INBTCompound toNBT(Inventory inv, INBTCompound nbt, String specialName)
	{
		boolean player = (inv instanceof PlayerInventory) && false;
		//PlayerInventory pinv = player ? (PlayerInventory) inv : null;
		if (inv == null)
		{
			nbt.remove(specialName);
			return nbt;
		}
		String name = inv.getName();
		INBTList list = new TagList();
		if (player)
		{
			//((CraftInventoryPlayer) pinv).getInventory().a(list);
		}
		else
		{
			for (int i = 0; i < (player ? inv.getSize() - 5 : inv.getSize()); i++)
			{
				if (inv.getItem(i) != null)
				{
					INBTCompound compound = new TagCompound();
					compound.setByte("Slot", (byte) i);
					compound.setInt("Count", inv.getItem(i).getAmount());
					compound.setShort("Damage", inv.getItem(i).getDurability());
					compound.setString("id", inv.getItem(i).getType().name());
					list.add(compound);
				}
			}
		}
		INBTCompound cp = new TagCompound();
		cp.set("Contents", list);
		cp.setInt("Size", (player ? inv.getSize() - 5 : inv.getSize()));
		cp.setInt("MaxStackSize", inv.getMaxStackSize());
		cp.setString("Name", name);
		cp.setBoolean("Type", player);
		nbt.set(specialName, cp);
		return nbt;
	}
	
	public static INBTCompound toNBT(Inventory inv, INBTCompound nbt)
	{
		return DataUtils.toNBT(inv, nbt, inv.getName());
	}
	
	public static Inventory fromNBT(INBTCompound nbt, String name)
	{
		if (!nbt.hasKey(name))
		{
			return null;
		}
		INBTCompound cp = ((INBTCompound) nbt.clone()).getCompound(name);
		String invName = cp.getString("Name");
		if (invName == "")
		{
			return null;
		}
		boolean player = cp.getBoolean("Type");
		int size = cp.getInt("Size"), maxSize = cp.getInt("MaxStackSize");
		INBTList list = cp.getList("Contents", cp.getTypeId());
		Inventory inv = null;
		if (player)
		{
			//inv = InventorySerializer.getNewInv();
			//inv.setMaxStackSize(maxSize);
			//((CraftInventoryPlayer) inv).getInventory().b(list);
		}
		else
		{
			inv = ItemInventory.createInventory(invName, size / 9);
			inv.setMaxStackSize(maxSize);
			for (int i = 0; i < list.size(); i++)
			{
				INBTCompound compound = (INBTCompound) list.get(i);
				int j = compound.getByte("Slot") & 0xFF;
				int c = compound.getInt("Count");
				short d = compound.getShort("Damage");
				Material ma = Material.getMaterial(compound.getString("id"));
				ItemStack itemstack = new ItemStack(ma, c, d);
				
				if (itemstack != null)
				{
					if ((j >= 0) && (j < inv.getSize()))
					{
						inv.setItem(j, itemstack);
					}
				}
			}
		}
		return inv;
	}
}
