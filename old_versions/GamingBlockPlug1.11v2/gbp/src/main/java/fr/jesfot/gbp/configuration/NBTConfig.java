package fr.jesfot.gbp.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.LogManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.material.MaterialData;

import fr.jesfot.gbp.GamingBlockPlug_1_11;
import net.minecraft.server.v1_11_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import net.minecraft.server.v1_11_R1.NBTTagList;
import net.minecraft.server.v1_11_R1.NBTTagString;

public class NBTConfig
{
	public static final String NBT_FILE_EXT = new String(".dat");
	public static final String TMP_FILE_EXT = new String(".tmp");
	
	private NBTTagCompound mainCompound;
	
	private File folder;
	private String fileName = "ERROR_IN_THIS_FILE";
	private boolean canAccess;
	private File datFile;
	
	public NBTConfig()
	{
		this.mainCompound = new NBTTagCompound();
		this.canAccess = false;
		this.datFile = null;
	}
	
	public NBTConfig(File saveFolder, String p_fileName)
	{
		this();
		this.fileName = p_fileName;
		this.folder = saveFolder;
	}
	
	public NBTConfig(File saveFolder, UUID p_fileName)
	{
		this(saveFolder, p_fileName.toString());
	}
	
	public NBTConfig(File saveFolder)
	{
		this();
		this.folder = saveFolder;
	}
	
	public void initFolders()
	{
		if(this.folder == null)
		{
			return;
		}
		if(!this.folder.exists())
		{
			GamingBlockPlug_1_11.getTheLogger().log(Level.FINEST, "Creating NBT configuration folder... (" + this.folder.getPath() + ")");
			if(this.folder.mkdirs())
			{
				GamingBlockPlug_1_11.getTheLogger().log(Level.FINEST, "Successfuly created the NBT configuration folder. (" + this.folder.getPath() + ")");
			}
			else
			{
				GamingBlockPlug_1_11.getTheLogger().log(Level.WARNING, "Enable to create the NBT configuration directory tree. (" + this.folder.getPath() + ")");
			}
		}
		this.datFile = new File(this.folder, this.fileName + NBT_FILE_EXT);
		this.canAccess = true;
	}
	
	public NBTConfig writeNBTToFile()
	{
		if(this.isUnaccessible())
		{
			return this;
		}
		File file1 = new File(this.folder, this.fileName + NBT_FILE_EXT + TMP_FILE_EXT);
		try
		{
			GamingBlockPlug_1_11.getTheLogger().finest("Writing nbt to file " + this.datFile.getPath() + "");
			NBTCompressedStreamTools.a((NBTTagCompound)this.mainCompound.clone(), new FileOutputStream(file1));
			if(this.datFile.exists())
			{
				this.datFile.delete();
			}
			file1.renameTo(this.datFile);
			this.cleanTmp();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return this;
		}
		return this;
	}
	
	public NBTConfig readNBTFromFile()
	{
		if(this.isUnaccessible())
		{
			return this;
		}
		if(!this.datFile.exists())
		{
			return this.writeNBTToFile();
		}
		NBTTagCompound tmp;
		try
		{
			GamingBlockPlug_1_11.getTheLogger().finest("Reading nbt from file " + this.datFile.getPath() + "");
			tmp = NBTCompressedStreamTools.a((InputStream)(new FileInputStream(this.datFile)));
		}
		catch(IOException e)
		{
			LogManager.getLogManager().getLogger("[ERROR [NBTConfig.java]]").severe("Error while reading " + this.datFile.getName() + ".");
			e.printStackTrace();
			return this;
		}
		if(tmp != null)
		{
			this.mainCompound = (NBTTagCompound)tmp.clone();
		}
		return this;
	}
	
	public NBTTagCompound getAsIs()
	{
		if(this.isUnaccessible())
		{
			return null;
		}
		if(this.mainCompound == null)
		{
			this.setCopy(new NBTTagCompound());
		}
		return this.mainCompound;
	}
	
	public NBTTagCompound getCopy()
	{
		if(this.isUnaccessible())
		{
			return null;
		}
		if(this.mainCompound == null)
		{
			return new NBTTagCompound();
		}
		return (NBTTagCompound)this.mainCompound.clone();
	}
	
	public NBTConfig setCopy(NBTTagCompound compound)
	{
		if(this.isUnaccessible())
		{
			return this;
		}
		this.mainCompound = (NBTTagCompound)compound.clone();
		return this;
	}
	
	public void setUnaccessible()
	{
		this.canAccess = false;
		this.mainCompound = null;
	}
	
	public boolean isUnaccessible()
	{
		return !this.canAccess;
	}
	
	public NBTConfig cleanTmp()
	{
		if(this.isUnaccessible())
		{
			return this;
		}
		File file = new File(this.folder, this.fileName + NBT_FILE_EXT + TMP_FILE_EXT);
		if(file.exists())
		{
			file.delete();
		}
		return this;
	}
	
	public NBTConfig delete()
	{
		if(this.isUnaccessible())
		{
			return this;
		}
		if(this.datFile.exists())
		{
			this.datFile.delete();
		}
		return this;
	}
	
	public boolean existFile()
	{
		if(this.datFile == null)
		{
			return false;
		}
		return this.datFile.exists();
	}
	
	public long size()
	{
		if(this.datFile == null)
		{
			return 0L;
		}
		return this.datFile.length();
	}
	
	public String getName()
	{
		return this.fileName;
	}
	
	public static NBTTagCompound setLocation(NBTTagCompound compound, String key, Location location)
	{
		if(location == null)
		{
			return compound;
		}
		NBTTagCompound loc = new NBTTagCompound();
		loc.setDouble("CoordX", location.getX());
		loc.setDouble("CoordY", location.getY());
		loc.setDouble("CoordZ", location.getZ());
		loc.setFloat("Pitch", location.getPitch());
		loc.setFloat("Yaw", location.getYaw());
		loc.setString("World", location.getWorld().getName());
		compound.set(key, loc);
		return compound;
	}
	
	public static Location getLocation(NBTTagCompound compound, String key)
	{
		NBTTagCompound loc = compound.getCompound(key);
		double x, y, z;
		float pitch, yaw;
		x = loc.getDouble("CoordX");
		y = loc.getDouble("CoordY");
		z = loc.getDouble("CoordZ");
		pitch = loc.getFloat("Pitch");
		yaw = loc.getFloat("Yaw");
		String world = loc.getString("World");
		World w = Bukkit.getWorld(world);
		if(w == null)
		{
			return null;
		}
		return new Location(w, x, y, z, yaw, pitch);
	}
	
	@SuppressWarnings("deprecation")
	public static NBTTagCompound setMaterialData(NBTTagCompound compound, String key, MaterialData data)
	{
		if(data == null)
		{
			return compound;
		}
		NBTTagCompound dat = new NBTTagCompound();
		dat.setString("ItemType", data.getItemType().name());
		dat.setByte("Data", data.getData());
		compound.set(key, dat);
		return compound;
	}
	
	@SuppressWarnings("deprecation")
	public static MaterialData getMaterialData(NBTTagCompound compound, String key)
	{
		NBTTagCompound dat = compound.getCompound(key);
		String materialStr;
		Material material;
		byte data;
		materialStr = dat.getString("ItemType");
		data = dat.getByte("Data");
		material = Material.getMaterial(materialStr);
		if(material == null)
		{
			return null;
		}
		return new MaterialData(material, data);
	}
	
	@SuppressWarnings("deprecation")
	public static NBTTagCompound setEnchantments(NBTTagCompound compound, String key, Map<Enchantment, Integer> enchs)
	{
		if(enchs == null || enchs.isEmpty())
		{
			return compound;
		}
		NBTTagList list = new NBTTagList();
		for(Entry<Enchantment, Integer> entry : enchs.entrySet())
		{
			NBTTagCompound ench = new NBTTagCompound();
			ench.setInt("id", entry.getKey().getId());
			ench.setInt("lvl", entry.getValue().intValue());
			list.add(ench);
		}
		compound.set(key, list);
		return compound;
	}
	
	@SuppressWarnings("deprecation")
	public static Map<Enchantment, Integer> getEnchantments(NBTTagCompound compound, String key)
	{
		NBTTagList list = compound.getList(key, compound.getTypeId());
		Map<Enchantment, Integer> result = new HashMap<Enchantment, Integer>();
		for(int i = 0; i < list.size(); i++)
		{
			NBTTagCompound ench = list.get(i);
			int enchId = ench.getInt("id");
			int lvl = ench.getInt("lvl");
			Enchantment en = Enchantment.getById(enchId);
			result.put(en, Integer.valueOf(lvl));
		}
		return result;
	}
	
	public static NBTTagCompound setLore(NBTTagCompound compound, String key, List<String> strings)
	{
		if(strings == null || strings.isEmpty())
		{
			return compound;
		}
		NBTTagList list = new NBTTagList();
		for(String str : strings)
		{
			list.add(new NBTTagString(str));
		}
		compound.set(key, list);
		return compound;
	}
	
	public static List<String> getLore(NBTTagCompound compound, String key)
	{
		List<String> result = new ArrayList<String>();
		NBTTagList list = compound.getList(key, (new NBTTagString()).getTypeId());
		for(int i = 0; i < list.size(); i++)
		{
			NBTTagString str = (NBTTagString)list.h(i);
			result.add(str.c_());
		}
		return result;
	}
}
