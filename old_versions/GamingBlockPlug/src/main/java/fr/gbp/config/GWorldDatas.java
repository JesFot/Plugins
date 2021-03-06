package fr.gbp.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;

import fr.gbp.GamingBlockPlug;
import fr.gbp.utils.InventoryStringSerializer;

public class GWorldDatas
{
	private final GamingBlockPlug gbp;
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;
	private File worldsFolder = null;
	
	public GWorldDatas(GamingBlockPlug p_gbp)
	{
		this.gbp = p_gbp;
		this.worldsFolder = new File(this.gbp.getPlugin().getDataFolder(), "worlds");
	}

	/**
	 * This will reload the 'save' file.
	 */
	public GWorldDatas reloadWorldConfig(String lang)
	{
		if (this.customConfigFile == null || !lang.contains(this.customConfigFile.getName()))
		{
			this.customConfigFile = new File(this.worldsFolder, lang);
		}
		this.customConfig = YamlConfiguration.loadConfiguration(this.customConfigFile);

		Reader defConfigStream = null;
		try
		{
			defConfigStream = new InputStreamReader(this.gbp.getPlugin().getResource("worlds/void_world_datas.prelink.yml"), "UTF8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		if (defConfigStream != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			this.customConfig.setDefaults(defConfig);
		}
		return this;
	}
	/**
	 * 
	 * @return the save configuration file.
	 */
	public FileConfiguration getWorldConfig(String lang)
	{
	    if (this.customConfig == null || !lang.contains(this.customConfigFile.getName()))
	    {
	        this.reloadWorldConfig(lang);
	    }
	    return this.customConfig;
	}
	public GWorldDatas saveWorldConfig(String lang)
	{
		if (this.customConfig == null || this.customConfigFile == null || !lang.contains(this.customConfigFile.getName()))
		{
			return this;
		}
		try
		{
			this.getWorldConfig(lang).save(this.customConfigFile);
		}
		catch (IOException ex)
		{
			this.gbp.getLogger().log(Level.SEVERE, "Could not save config to " + this.customConfigFile, ex);
		}
		return this;
	}
	public GWorldDatas saveDefaultWorldConfig(String lang)
	{
		if (this.customConfigFile == null || !lang.contains(this.customConfigFile.getName()))
		{
			this.customConfigFile = new File(this.worldsFolder, lang);
		}
		if (!this.customConfigFile.exists())
		{
			this.gbp.getPlugin().saveResource("worlds/void_world_datas.prelink.yml", false);
		}
		return this;
	}
	
	/**
	 * This will register in the save data config a location
	 * 
	 * @param name - The name of the location
	 * @param loc - The location to save
	 */
	public void storeLoc(String name, Location loc, String world)
	{
		name = name.toLowerCase();
		String i = " , ";
		String locator;
		if(loc == null)
		{
			locator = "";
		}
		else
		{
			 locator = loc.getWorld().getName() +i+ loc.getX() +i+ loc.getY() +i+ loc.getZ() +i+ loc.getPitch() +i+ loc.getYaw();
		}
		this.getWorldConfig(world).set(name, locator);
		if (!this.getWorldConfig(world).contains(name))
		{
			this.getWorldConfig(world).createSection(name);
		}
		this.getWorldConfig(world).set(name, locator);
		this.saveWorldConfig(world);
	}
	
	/**
	 * This will return you a stored location, registered before by {@code} storeLoc(name, loc) {@code}
	 * 
	 * @param name - The name of the location
	 * @return The location requested if exists
	 */
	public Location getLoc(String name, String world)
	{
		name = name.toLowerCase();
		this.reloadWorldConfig(world);
		String key = this.getWorldConfig(world).getString(name);
		if (key == null || key == "")
		{
			this.gbp.getLogger().log(Level.WARNING, "[MWorldDatas:135] Key is null");
			return null;
		}
		String[] split = key.split(" , ");
		if (split.length == 6)
		{
			Location loc = new Location(Bukkit.getWorld(split[0]),
					Double.parseDouble(split[1]),
					Double.parseDouble(split[2]),
					Double.parseDouble(split[3]),
					Float.parseFloat(split[4]),
					Float.parseFloat(split[5]));
			this.saveWorldConfig(world);
			return loc;
		}
		else
		{
			this.saveWorldConfig(world);
			return null;
		}
	}
	
	/**
	 * This will register in the save data config an inventory
	 * 
	 * @param name - The name of the inventory
	 * @param inv - The inventory to store
	 */
	public void storeInventory(String name, Inventory inv, String world)
	{
		name = name.toLowerCase();
		String invpath = name + ".inv";
		String containsPath = name + ".con";
		
		name = inv.getName();
		
		this.getWorldConfig(world).set(invpath, name);
		
		if(!this.customConfig.contains(invpath))
		{
			this.customConfig.createSection(invpath);
		}
		this.customConfig.set(invpath, name);
		if(!this.customConfig.contains(containsPath))
		{
			this.customConfig.createSection(containsPath);
		}
		this.customConfig.set(containsPath, InventoryStringSerializer.inventoryToString(inv));
		this.saveWorldConfig(world);
	}
	//example:
	//Inventory inv = ItemInventory.createItemandInv(Material.APPLE, 0, "still", "lorem ipsum\nme re", "invName", 3);
	//ItemInventory.createIteminInv(Material.BONE, inv, 3, "alive", "dolor sit amet\nvoila");
	//inv.getItem(3).setAmount(5);
	//this.config.storeInventory("test.inv.un", inv);
	
	/**
	 * This will return you a stored inventory, registered before by {@code} storeInventory(name, inv) {@code}
	 * 
	 * @param name - The name of the inventory
	 * @return The inventory requested if exists
	 */
	public Inventory getInventory(String name, String world)
	{
		name = name.toLowerCase();
		this.reloadWorldConfig(world);
		String keyinv = this.getWorldConfig(world).getString(name+".inv");
		if (keyinv == null || keyinv == "")
		{
			this.gbp.getLogger().log(Level.WARNING, "[GWorldDatas:236] KeyInv is null");
			return null;
		}
		String keycon= this.getWorldConfig(world).getString(name+".con");
		if (keycon == null || keycon == "")
		{
			this.gbp.getLogger().log(Level.INFO, "[GWorldDatas:242] KeyContents is null");
			return null;
		}
		Inventory inv = InventoryStringSerializer.stringToInventory(keycon, keyinv);
		return inv;
	}
	
	public String get(String path, String world)
	{
		return this.get(path, "", world);
	}
	
	public String get(String path, String value, String world)
	{
		if(path == null || path == "" || path.contains(" "))
		{
			return null;
		}
		String path2 = path.replaceAll("-", "_");
		this.reloadWorldConfig(world);
		if(!this.getWorldConfig(world).contains(path2))
		{
			this.getWorldConfig(world).createSection(path2);
			this.getWorldConfig(world).set(path2, value);
		}
		return this.getWorldConfig(world).getString(path2, value);
	}
}