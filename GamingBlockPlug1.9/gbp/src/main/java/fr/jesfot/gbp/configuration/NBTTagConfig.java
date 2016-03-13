package fr.jesfot.gbp.configuration;

import java.io.File;
import java.util.UUID;

public class NBTTagConfig extends NBTSubConfig
{
	private NBTTagConfig parent = null;
	
	public NBTTagConfig()
	{
		super();
	}
	
	public NBTTagConfig(File p_saveFolder)
	{
		super(p_saveFolder);
	}
	
	public NBTTagConfig(File p_saveFolder, String p_fileName)
	{
		super(p_saveFolder, p_fileName);
	}
	
	public NBTTagConfig(File p_saveFolder, UUID fileUUID)
	{
		super(p_saveFolder, fileUUID);
	}
	
	public NBTTagConfig(NBTConfig configFile)
	{
		super(configFile);
	}
	
	public NBTTagConfig(String subTagName)
	{
		super(subTagName.split(".")[subTagName.split(".").length - 1]);
		if(subTagName.contains("."))
		{
			String tmp = subTagName.substring(0, subTagName.length() - subTagName.split(".")[subTagName.split(".").length - 1].length());
			this.parent = new NBTTagConfig(tmp);
		}
	}
	
	public NBTTagConfig(File p_saveFolder, String p_fileName, String subTagName)
	{
		super(p_saveFolder, p_fileName, subTagName.split(".")[subTagName.split(".").length - 1]);
		if(subTagName.contains("."))
		{
			String tmp = subTagName.substring(0, subTagName.length() - subTagName.split(".")[subTagName.split(".").length - 1].length());
			this.parent = new NBTTagConfig(p_saveFolder, p_fileName, tmp);
		}
	}
	
	public NBTTagConfig(File p_saveFolder, UUID fileUUID, String subTagName)
	{
		super(p_saveFolder, fileUUID, subTagName.split(".")[subTagName.split(".").length - 1]);
		if(subTagName.contains("."))
		{
			String tmp = subTagName.substring(0, subTagName.length() - subTagName.split(".")[subTagName.split(".").length - 1].length());
			this.parent = new NBTTagConfig(p_saveFolder, fileUUID, tmp);
		}
	}
	
	public NBTTagConfig(NBTConfig configFile, String subTagName)
	{
		super(configFile, subTagName.split(".")[subTagName.split(".").length - 1]);
		if(subTagName.contains("."))
		{
			String tmp = subTagName.substring(0, subTagName.length() - subTagName.split(".")[subTagName.split(".").length - 1].length());
			this.parent = new NBTTagConfig(configFile, tmp);
		}
	}
	
	public NBTTagConfig getParent()
	{
		return this.parent;
	}
}