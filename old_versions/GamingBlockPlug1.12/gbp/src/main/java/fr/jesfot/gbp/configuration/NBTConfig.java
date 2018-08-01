package fr.jesfot.gbp.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.LogManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.material.MaterialData;

import net.minecraft.server.v1_12_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NBTTagString;

public class NBTConfig
{
	private File saveFolder;
	private NBTTagCompound mainCompound;
	private String fileName = "ERROR_IN_THIS_FILE";
	private UUID uuidName = null;
	private boolean canAccess = true;
	
	public NBTConfig()
	{
		this.mainCompound = new NBTTagCompound();
		this.uuidName = UUID.randomUUID();
		this.fileName = this.uuidName.toString();
		this.saveFolder = new File("eliryum_savefolder");
		this.saveFolder.mkdirs();
	}
	
	public NBTConfig(File p_saveFolder)
	{
		this.mainCompound = new NBTTagCompound();
		this.uuidName = UUID.randomUUID();
		this.fileName = this.uuidName.toString();
		this.saveFolder = p_saveFolder;
		this.saveFolder.mkdirs();
	}
	
	public NBTConfig(File p_saveFolder, String p_fileName)
	{
		this.mainCompound = new NBTTagCompound();
		this.fileName = p_fileName;
		this.saveFolder = p_saveFolder;
		this.saveFolder.mkdirs();
	}
	
	public NBTConfig(File p_saveFolder, UUID fileUUID)
	{
		this.mainCompound = new NBTTagCompound();
		this.fileName = fileUUID.toString();
		this.uuidName = fileUUID;
		this.saveFolder = p_saveFolder;
		this.saveFolder.mkdirs();
	}
	
	public NBTConfig writeNBTToFile()
	{
		if (this.isUnaccessible())
		{
			return this;
		}
		File file1 = new File(this.saveFolder, this.fileName + ".dat.tmp");
		File file2 = new File(this.saveFolder, this.fileName + ".dat");
		try
		{
			NBTCompressedStreamTools.a((NBTTagCompound) this.mainCompound.clone(), new FileOutputStream(file1));
			if (file2.exists())
			{
				file2.delete();
			}
			file1.renameTo(file2);
			this.cleanTmp();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return this;
		}
		return this;
	}
	
	public NBTConfig readNBTFromFile()
	{
		if (this.isUnaccessible())
		{
			return this;
		}
		File file = new File(this.saveFolder, this.fileName + ".dat");
		if (!file.exists())
		{
			return this.writeNBTToFile();
		}
		NBTTagCompound tmp;
		try
		{
			tmp = NBTCompressedStreamTools.a((new FileInputStream(file)));
		}
		catch (IOException e)
		{
			LogManager.getLogManager().getLogger("[ERROR [NBTConfig.java]]")
					.severe("Error while reading " + file.getName() + ".");
			e.printStackTrace();
			return this;
		}
		if (tmp != null)
		{
			this.mainCompound = (NBTTagCompound) tmp.clone();
		}
		return this;
	}
	
	public NBTTagCompound getCopy()
	{
		if (this.isUnaccessible())
		{
			return null;
		}
		if (this.mainCompound == null)
		{
			return new NBTTagCompound();
		}
		return (NBTTagCompound) this.mainCompound.clone();
	}
	
	public NBTConfig setCopy(NBTTagCompound compound)
	{
		if (this.isUnaccessible())
		{
			return this;
		}
		this.mainCompound = (NBTTagCompound) compound.clone();
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
		File file = new File(this.saveFolder, this.fileName + ".dat.tmp");
		if (file.exists())
		{
			file.delete();
		}
		return this;
	}
	
	public NBTConfig delete()
	{
		File file = new File(this.saveFolder, this.fileName + ".dat");
		if (file.exists())
		{
			file.delete();
		}
		return this;
	}
	
	public boolean existFile()
	{
		File file = new File(this.saveFolder, this.fileName + ".dat");
		return file.exists();
	}
	
	public long size()
	{
		File file = new File(this.saveFolder, this.fileName + ".dat");
		return file.length();
	}
	
	public String getName()
	{
		return this.fileName;
	}
	
	public static NBTTagCompound setLocation(NBTTagCompound compound, String key, Location location)
	{
		if (location == null)
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
		if (w == null)
		{
			return null;
		}
		return new Location(w, x, y, z, yaw, pitch);
	}
	
	@SuppressWarnings("deprecation")
	public static NBTTagCompound setMaterialData(NBTTagCompound compound, String key, MaterialData data)
	{
		if (data == null)
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
		if (material == null)
		{
			return null;
		}
		return new MaterialData(material, data);
	}
	
	@SuppressWarnings("deprecation")
	public static NBTTagCompound setEnchantments(NBTTagCompound compound, String key, Map<Enchantment, Integer> enchs)
	{
		if (enchs == null || enchs.isEmpty())
		{
			return compound;
		}
		NBTTagList list = new NBTTagList();
		for (Entry<Enchantment, Integer> entry : enchs.entrySet())
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
		for (int i = 0; i < list.size(); i++)
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
		if (strings == null || strings.isEmpty())
		{
			return compound;
		}
		NBTTagList list = new NBTTagList();
		for (String str : strings)
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
		for (int i = 0; i < list.size(); i++)
		{
			NBTTagString str = (NBTTagString) list.i(i);
			result.add(str.c_());
		}
		return result;
	}
}