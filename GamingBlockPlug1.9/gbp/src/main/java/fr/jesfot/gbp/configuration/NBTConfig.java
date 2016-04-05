package fr.jesfot.gbp.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.logging.LogManager;

import net.minecraft.server.v1_9_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_9_R1.NBTTagCompound;

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
		if(!this.canAccess)
		{
			return this;
		}
		File file1 = new File(this.saveFolder, this.fileName + ".dat.tmp");
		File file2 = new File(this.saveFolder, this.fileName + ".dat");
		try
		{
			NBTCompressedStreamTools.a((NBTTagCompound)this.mainCompound.clone(), new FileOutputStream(file1));
			if(file2.exists())
			{
				file2.delete();
			}
			file1.renameTo(file2);
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
		if(!this.canAccess)
		{
			return this;
		}
		File file = new File(this.saveFolder, this.fileName + ".dat");
		if(!file.exists())
		{
			return this.writeNBTToFile();
		}
		NBTTagCompound tmp;
		try
		{
			tmp = NBTCompressedStreamTools.a((InputStream)(new FileInputStream(file)));
		}
		catch(IOException e)
		{
			LogManager.getLogManager().getLogger("[ERROR [NBTConfig.java]]").severe("Error while reading " + file.getName() + ".");
			e.printStackTrace();
			return this;
		}
		if(tmp != null)
		{
			this.mainCompound = (NBTTagCompound)tmp.clone();
		}
		return this;
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
		File file = new File(this.saveFolder, this.fileName + ".dat.tmp");
		if(file.exists())
		{
			file.delete();
		}
		return this;
	}
	
	public NBTConfig delete()
	{
		File file = new File(this.saveFolder, this.fileName + ".dat");
		if(file.exists())
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
}