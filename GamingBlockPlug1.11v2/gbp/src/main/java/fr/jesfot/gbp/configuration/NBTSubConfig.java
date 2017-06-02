package fr.jesfot.gbp.configuration;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import net.minecraft.server.v1_11_R1.NBTBase;
import net.minecraft.server.v1_11_R1.NBTTagCompound;

public class NBTSubConfig
{
	private String tagName = null;
	private NBTConfig file;
	private NBTSubConfig parent = null;
	
	public NBTSubConfig()
	{
		this(new NBTConfig());
	}
	
	public NBTSubConfig(File p_saveFolder)
	{
		this(new NBTConfig(p_saveFolder));
	}
	
	public NBTSubConfig(File p_saveFolder, String p_fileName)
	{
		this(new NBTConfig(p_saveFolder, p_fileName));
	}
	
	public NBTSubConfig(File p_saveFolder, UUID fileUUID)
	{
		this(new NBTConfig(p_saveFolder, fileUUID));
	}
	
	public NBTSubConfig(NBTConfig configFile)
	{
		this.file = configFile;
	}
	
	public NBTSubConfig(String subTagName)
	{
		this(new NBTConfig(), subTagName);
	}
	
	public NBTSubConfig(File p_saveFolder, String p_fileName, String subTagName)
	{
		this(new NBTConfig(p_saveFolder, p_fileName), subTagName);
	}
	
	public NBTSubConfig(File p_saveFolder, UUID fileUUID, String subTagName)
	{
		this(new NBTConfig(p_saveFolder, fileUUID), subTagName);
	}
	
	public NBTSubConfig(NBTConfig configFile, String subTagName)
	{
		this.tagName = subTagName;
		this.file = configFile;
	}
	
	public NBTSubConfig(NBTSubConfig p_parent, NBTConfig root, String subTagName)
	{
		this.parent = p_parent;
		this.file = root;
		this.tagName = subTagName;
	}
	
	public NBTSubConfig(NBTSubConfig p_parent, String subTagName)
	{
		this.parent = p_parent;
		this.file = p_parent.getFile();
		this.tagName = subTagName;
	}
	
	public NBTConfig getFile()
	{
		if(this.file != null)
		{
			return this.file;
		}
		if(this.parent != null)
		{
			this.file = this.parent.getFile();
		}
		return this.file;
	}
	
	public NBTSubConfig getChild(String subTag)
	{
		NBTSubConfig child = new NBTSubConfig(this, this.getFile(), subTag);
		return child;
	}
	
	public NBTSubConfig putChild(NBTSubConfig children)
	{
		NBTTagCompound tmp = this.getCopy();
		tmp.set(children.getTagName(), children.getCopy());
		this.setCopy(tmp);
		return this;
	}
	
	public String getName()
	{
		return this.file.getName();
	}
	
	public String getTagName()
	{
		return this.tagName;
	}
	
	public NBTTagCompound getCopy()
	{
		if(this.tagName == null)
		{
			if(this.parent != null)
			{
				return this.parent.getCopy();
			}
			return this.file.getCopy();
		}
		NBTTagCompound papa;
		if(this.parent != null)
		{
			papa = this.parent.getCopy();
		}
		else
		{
			papa = this.file.getCopy();
		}
		if(papa == null)
		{
			return null;
		}
		return (NBTTagCompound)papa.getCompound(this.tagName).clone();
	}
	
	public NBTSubConfig setCopy(NBTTagCompound compound)
	{
		if(this.tagName == null)
		{
			if(this.parent != null)
			{
				this.parent.setCopy(compound);
				return this;
			}
			this.file.setCopy(compound);
			return this;
		}
		NBTTagCompound papa;
		if(this.parent != null)
		{
			papa = this.parent.getCopy();
		}
		else
		{
			papa = this.file.getCopy();
		}
		if(papa == null)
		{
			return this;
		}
		papa.set(this.tagName, compound);
		if(this.parent != null)
		{
			this.parent.setCopy(papa);
			return this;
		}
		this.file.setCopy(papa);
		return this;
	}
	
	public NBTSubConfig writeNBTToFile()
	{
		if(this.parent != null)
		{
			this.parent.writeNBTToFile();
		}
		else
		{
			this.file.writeNBTToFile();
		}
		return this;
	}
	
	public NBTSubConfig readNBTFromFile()
	{
		if(this.parent != null)
		{
			this.parent.readNBTFromFile();
		}
		else
		{
			this.file.readNBTFromFile();
		}
		return this;
	}
	
	public String getString(String key)
	{
		NBTTagCompound tmp = this.getCopy();
		return tmp.getString(key);
	}
	
	public double getDouble(String key)
	{
		NBTTagCompound tmp = this.getCopy();
		return tmp.getDouble(key);
	}
	
	public boolean getBoolean(String key)
	{
		NBTTagCompound tmp = this.getCopy();
		return tmp.getBoolean(key);
	}
	
	public byte getByte(String key)
	{
		NBTTagCompound tmp = this.getCopy();
		return tmp.getByte(key);
	}
	
	public byte[] getByteArray(String key)
	{
		NBTTagCompound tmp = this.getCopy();
		return tmp.getByteArray(key);
	}
	
	public float getFloat(String key)
	{
		NBTTagCompound tmp = this.getCopy();
		return tmp.getFloat(key);
	}
	
	public int[] getIntArray(String key)
	{
		NBTTagCompound tmp = this.getCopy();
		return tmp.getIntArray(key);
	}
	
	public int getInteger(String key)
	{
		NBTTagCompound tmp = this.getCopy();
		return tmp.getInt(key);
	}

	public long getLong(String key)
	{
		NBTTagCompound tmp = this.getCopy();
		return tmp.getLong(key);
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
		if(this.parent != null)
		{
			this.parent.setUnaccessible();
		}
		else
		{
			this.file.setUnaccessible();
		}
	}
	
	public boolean existFile()
	{
		if(this.parent != null)
		{
			return this.parent.existFile();
		}
		else
		{
			return this.file.existFile();
		}
	}
}