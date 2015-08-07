package fr.mpp.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.mpp.MetalPonyPlug;
import fr.mpp.utils.ItemInventory;

public class MConfig
{
	private final MetalPonyPlug mpp;
	private FileConfiguration confFile;
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;
	private Map<String, File> configFiles = new HashMap<String, File>();
	private Map<String, FileConfiguration> configs = new HashMap<String, FileConfiguration>();
	private static final int x = 0, y = 0, z = -1;
	private MVarConfig varConfig;

	public MConfig(FileConfiguration file, MetalPonyPlug p_mpp)
	{
		this.confFile = file;
		this.mpp = p_mpp;
		this.varConfig = new MVarConfig(p_mpp);
	}
	
	public MVarConfig getVarConfig()
	{
		return this.varConfig;
	}

	/**
	 * This will reload the 'save' file.
	 */
	public void reloadCustomConfig()
	{
		if (this.customConfigFile == null)
		{
			this.customConfigFile = new File(this.mpp.getPlugin().getDataFolder(), "mppConfig.yml");
		}
		this.customConfig = YamlConfiguration.loadConfiguration(this.customConfigFile);

		Reader defConfigStream = null;
		try
		{
			defConfigStream = new InputStreamReader(this.mpp.getPlugin().getResource("mppConfig.yml"), "UTF8");
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
	}
	/**
	 * 
	 * @return the save configuration file.
	 */
	public FileConfiguration getCustomConfig()
	{
	    if (this.customConfig == null)
	    {
	        this.reloadCustomConfig();
	    }
	    return this.customConfig;
	}
	public void saveCustomConfig()
	{
		if (this.customConfig == null || this.customConfigFile == null)
		{
			return;
		}
		try
		{
			this.getCustomConfig().save(this.customConfigFile);
		}
		catch (IOException ex)
		{
			this.mpp.getLogger().log(Level.SEVERE, "Could not save config to " + this.customConfigFile, ex);
		}
	}
	public void saveDefaultConfig()
	{
		if (this.customConfigFile == null)
		{
			this.customConfigFile = new File(this.mpp.getPlugin().getDataFolder(), "mppConfig.yml");
		}
		if (!this.customConfigFile.exists())
		{
			this.mpp.getPlugin().saveResource("mppConfig.yml", false);
		}
	}
	
	/**
	 * Get the main configuration file (config.yml)
	 * 
	 * @return the main configuration file
	 */
	public FileConfiguration getConf()
	{
		return this.confFile;
	}
	
	/**
	 * This will register in the save data config a location
	 * 
	 * @param name - The name of the location
	 * @param loc - The location to save
	 */
	public void storeLoc(String name, Location loc)
	{
		name = name.toLowerCase();
		String i = " , ";
		String locator = loc.getWorld().getName() +i+ loc.getX() +i+ loc.getY() +i+ loc.getZ() +i+ loc.getPitch() +i+ loc.getYaw();
		this.getCustomConfig().set(name, locator);
		if (!this.getCustomConfig().contains(name))
		{
			this.getCustomConfig().createSection(name);
		}
		this.getCustomConfig().set(name, locator);
		this.saveCustomConfig();
	}
	
	/**
	 * This will return you a stored location, registered before by {@code} storeLoc(name, loc) {@code}
	 * 
	 * @param name - The name of the location
	 * @return The location requested if exists
	 */
	public Location getLoc(String name)
	{
		name = name.toLowerCase();
		this.reloadCustomConfig();
		String key = this.getCustomConfig().getString(name);
		if (key == null || key == "")
		{
			this.mpp.getLogger().log(Level.WARNING, "[MConfig:116] Key is null");
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
			this.saveCustomConfig();
			return loc;
		}
		else
		{
			this.saveCustomConfig();
			return null;
		}
	}
	
	/**
	 * This will register in the save data config an inventory
	 * 
	 * @param name - The name of the inventory
	 * @param inv - The inventory to store
	 */
	public void storeInventory(String name, Inventory inv)
	{
		name = name.toLowerCase();
		String invpath = name + ".inv";
		String containsPath = name + ".con";
		
		name = inv.getName();
		int lines = inv.getSize()/9;
		
		String minv = name +" , "+ lines;
		this.customConfig.set(invpath, minv);

		int nb = 1;
		for(ItemStack i : inv.getContents())
		{
			if(i == null)
			{
				this.customConfig.set(containsPath+".i"+nb, null);
				nb++;
				continue;
			}
			int sl = inv.first(i);
			int a = i.getAmount();
			short d = i.getDurability();
			String m = i.getType().toString();
			int s = i.getMaxStackSize(); // not util
			//i.getItemMeta(); //lore + display name
			String dn = i.getItemMeta().getDisplayName();
			String lo = "";
			if(i.getItemMeta().getLore() != null)
			{
				for(String str : i.getItemMeta().getLore())
				{
					lo += str + " &? ";
				}
				lo = lo.substring(0, lo.length()-4);
			}
			String all = sl +" , "+ m +" , "+ a +" , "+ d +" , "+ s + " , "+ dn +" , "+lo;
			this.customConfig.set(containsPath+".i"+nb, all);
			if(!this.customConfig.contains(containsPath+".i"+nb))
			{
				this.customConfig.createSection(containsPath+".i"+nb);
			}
			this.customConfig.set(containsPath+".i"+nb, all);
			nb++;
		}
		if(!this.customConfig.contains(invpath))
		{
			this.customConfig.createSection(invpath);
		}
		this.customConfig.set(invpath, minv);
		this.saveCustomConfig();
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
	public Inventory getInventory(String name)
	{
		boolean invOnly = false;
		name = name.toLowerCase();
		this.reloadCustomConfig();
		String keyinv = this.getCustomConfig().getString(name+".inv");
		if (keyinv == null || keyinv == "")
		{
			this.mpp.getLogger().log(Level.WARNING, "[MConfig:243] KeyInv is null");
			return null;
		}
		String keycon= this.getCustomConfig().getString(name+".con");
		if (keycon == null || keycon == "")
		{
			this.mpp.getLogger().log(Level.INFO, "[MConfig:249] KeyContents is null");
			invOnly = true;
		}
		String invInfos[] = keyinv.split(" , ");
		int slots = Integer.parseInt(invInfos[1])*9;
		Inventory inv = ItemInventory.createInventory(invInfos[0], Integer.parseInt(invInfos[1]));
		if(invOnly)
		{
			return inv;
		}
		for(int i=0; i<slots; i++)
		{
			if(!this.customConfig.contains(name+".con.i"+(i+1)))
			{
				continue;
			}
			String data = this.customConfig.getString(name+".con.i"+(i+1));
			String[] datas = data.split(" , ");
			String[] lore = datas[datas.length-1].split(" &? ");
			String Lore = "";
			for (String str : lore)
			{
				Lore += str + "\n";
			}
			if(Lore.equalsIgnoreCase("\n") || Lore.equalsIgnoreCase("null\n") || Lore.equalsIgnoreCase("null\nnull") || Lore.equals(datas[5]+"\n"))
			{
				Lore = null;
			}
			if(datas[5].equalsIgnoreCase("null"))
			{
				datas[5] = (new ItemStack(Material.valueOf(datas[1]))).getItemMeta().getDisplayName();
			}
			ItemStack itemi = ItemInventory.createItem(Material.valueOf(datas[1]), datas[5], Lore);
			itemi.setAmount(Integer.parseInt(datas[2]));
			itemi.setDurability(Short.parseShort(datas[3]));
			if(i != Integer.parseInt(datas[0]))
			{
				//
			}
			ItemInventory.addItemtoInventory(itemi, inv, i);
		}
		return inv;
	}
	
	public boolean getMppActive()
	{
		return confFile.getBoolean("mpp_active");
	}

	public static int getMaxX()
	{
		return 400;//400
	}
	public static int getMaxY()
	{
		return 75;//75
	}
	public static int getMaxZ()
	{
		return -40;//-40
	}
	public static int getMinX()
	{
		return 396;//396
	}
	public static int getMinY()
	{
		return 74;//74
	}
	public static int getMinZ()
	{
		return -40;//-40
	}
	public static int getMaxBX()
	{
		return getMaxX()-x;
	}
	public static int getMaxBY()
	{
		return getMaxY()-y;
	}
	public static int getMaxBZ()
	{
		return getMaxZ()-z;
	}
	public static int getMinBX()
	{
		return getMinX()-x;
	}
	public static int getMinBY()
	{
		return getMinY()-y;
	}
	public static int getMinBZ()
	{
		return getMinZ()-z;
	}
	
	/**
	 * This will reload the 'save' file.
	 */
	public void reloadAConfig(String name)
	{
		if (!this.configFiles.containsKey(name))
		{
			this.configFiles.put(name, new File(this.mpp.getPlugin().getDataFolder(), name));
		}
		this.configs.put(name, YamlConfiguration.loadConfiguration(this.configFiles.get(name)));

		Reader defConfigStream = null;
		try
		{
			defConfigStream = new InputStreamReader(this.mpp.getPlugin().getResource(name), "UTF8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		if (defConfigStream != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			this.configs.get(name).setDefaults(defConfig);
		}
	}
	/**
	 * 
	 * @return the save configuration file.
	 */
	public FileConfiguration getAConfig(String name)
	{
	    if (!this.configs.containsKey(name))
	    {
	        this.reloadAConfig(name);
	    }
	    return this.configs.get(name);
	}
	public void saveAConfig(String name)
	{
		if (!this.configs.containsKey(name) || !this.configFiles.containsKey(name))
		{
			return;
		}
		try
		{
			this.getAConfig(name).save(this.configFiles.get(name));
		}
		catch (IOException ex)
		{
			this.mpp.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFiles.get(name), ex);
		}
	}
	public void saveADefaultConfig(String name)
	{
		if (!this.configFiles.containsKey(name))
		{
			this.configFiles.put(name, new File(this.mpp.getPlugin().getDataFolder(), name));
		}
		if (!this.configFiles.get(name).exists())
		{
			this.mpp.getPlugin().saveResource(name, false);
		}
	}
}