package fr.jesfot.gbp.configuration;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import net.minecraft.server.v1_9_R2.NBTBase;
import net.minecraft.server.v1_9_R2.NBTTagCompound;

public class NBTSubConfig
{
	private String tagName = null;
	private NBTConfig file;
	
	public NBTSubConfig()
	{
		this.file = new NBTConfig();
	}
	
	public NBTSubConfig(File p_saveFolder)
	{
		this.file = new NBTConfig(p_saveFolder);
	}
	
	public NBTSubConfig(File p_saveFolder, String p_fileName)
	{
		this.file = new NBTConfig(p_saveFolder, p_fileName);
	}
	
	public NBTSubConfig(File p_saveFolder, UUID fileUUID)
	{
		this.file = new NBTConfig(p_saveFolder, fileUUID);
	}
	
	public NBTSubConfig(NBTConfig configFile)
	{
		this.file = configFile;
	}
	
	public NBTSubConfig(String subTagName)
	{
		this.tagName = subTagName;
		this.file = new NBTConfig();
	}
	
	public NBTSubConfig(File p_saveFolder, String p_fileName, String subTagName)
	{
		this.tagName = subTagName;
		this.file = new NBTConfig(p_saveFolder, p_fileName);
	}
	
	public NBTSubConfig(File p_saveFolder, UUID fileUUID, String subTagName)
	{
		this.tagName = subTagName;
		this.file = new NBTConfig(p_saveFolder, fileUUID);
	}
	
	public NBTSubConfig(NBTConfig configFile, String subTagName)
	{
		this.tagName = subTagName;
		this.file = configFile;
	}
	
	public NBTTagCompound getCopy()
	{
		if(this.file.getCopy() == null)
		{
			return null;
		}
		if(this.tagName == null)
		{
			return this.file.getCopy();
		}
		return (NBTTagCompound)this.file.getCopy().getCompound(this.tagName).clone();
	}
	
	public NBTSubConfig setCopy(NBTTagCompound compound)
	{
		if(this.tagName == null)
		{
			this.file.setCopy(compound);
			return this;
		}
		NBTTagCompound tmp = this.file.getCopy();
		tmp.set(this.tagName, compound);
		this.file.setCopy(tmp);
		return this;
	}
	
	public NBTSubConfig writeNBTToFile()
	{
		this.file.writeNBTToFile();
		return this;
	}
	
	public NBTSubConfig readNBTFromFile()
	{
		this.file.readNBTFromFile();
		return this;
	}
	
	public NBTSubConfig setString(String key, String value)
	{
		NBTTagCompound tmp = this.getCopy();
		tmp.setString(key, value);
		this.setCopy(tmp);
		return this;
	}
	
	public NBTSubConfig setDouble(String key, double value)
	{
		NBTTagCompound tmp = this.getCopy();
		tmp.setDouble(key, value);
		this.setCopy(tmp);
		return this;
	}
	
	public NBTSubConfig setBoolean(String key, boolean value)
	{
		NBTTagCompound tmp = this.getCopy();
		tmp.setBoolean(key, value);
		this.setCopy(tmp);
		return this;
	}
	
	public NBTSubConfig setByte(String key, byte value)
	{
		NBTTagCompound tmp = this.getCopy();
		tmp.setByte(key, value);
		this.setCopy(tmp);
		return this;
	}
	
	public NBTSubConfig setByteArray(String key, byte[] value)
	{
		NBTTagCompound tmp = this.getCopy();
		tmp.setByteArray(key, value);
		this.setCopy(tmp);
		return this;
	}
	
	public NBTSubConfig setFloat(String key, float value)
	{
		NBTTagCompound tmp = this.getCopy();
		tmp.setFloat(key, value);
		this.setCopy(tmp);
		return this;
	}
	
	public NBTSubConfig setIntArray(String key, int[] value)
	{
		NBTTagCompound tmp = this.getCopy();
		tmp.setIntArray(key, value);
		this.setCopy(tmp);
		return this;
	}
	
	public NBTSubConfig setInteger(String key, int value)
	{
		NBTTagCompound tmp = this.getCopy();
		tmp.setInt(key, value);
		this.setCopy(tmp);
		return this;
	}

	public NBTSubConfig setLong(String key, long value)
	{
		NBTTagCompound tmp = this.getCopy();
		tmp.setLong(key, value);
		this.setCopy(tmp);
		return this;
	}
	
	public NBTSubConfig setTag(String key, NBTBase value)
	{
		NBTTagCompound tmp = this.getCopy();
		tmp.set(key, value);
		this.setCopy(tmp);
		return this;
	}
	
	public NBTSubConfig setLocation(String key, Location location)
	{
		if(location == null)
		{
			return this;
		}
		NBTTagCompound tmp = this.getCopy();
		NBTTagCompound loc = new NBTTagCompound();
		loc.setDouble("CoordX", location.getX());
		loc.setDouble("CoordY", location.getY());
		loc.setDouble("CoordZ", location.getZ());
		loc.setFloat("Pitch", location.getPitch());
		loc.setFloat("Yaw", location.getYaw());
		loc.setString("World", location.getWorld().getName());
		tmp.set(key, loc);
		this.setCopy(tmp);
		return this;
	}
	
	public Location getLocation(String key)
	{
		NBTTagCompound tmp = this.getCopy();
		NBTTagCompound loc = tmp.getCompound(key);
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

	public NBTSubConfig removeTag(String key)
	{
		NBTTagCompound tmp = this.getCopy();
		tmp.remove(key);
		this.setCopy(tmp);
		return this;
	}
	
	public void setUnaccessible()
	{
		this.file.setUnaccessible();
	}
	
	public boolean existFile()
	{
		return this.file.existFile();
	}
}