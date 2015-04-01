package fr.mpp.config;

import org.bukkit.configuration.file.FileConfiguration;

import fr.mpp.MetalPonyPlug;

public class MConfig
{
	private final MetalPonyPlug mpp;
	private FileConfiguration confFile;

	public MConfig(FileConfiguration file, MetalPonyPlug p_mpp)
	{
		this.confFile = file;
		this.mpp = p_mpp;
	}
	
	public boolean getMppActive()
	{
		return confFile.getBoolean("mpp_active");
	}
	
	public int getMaxX()
	{
		return 0;
	}
	public int getMaxY()
	{
		return 0;
	}
	public int getMaxZ()
	{
		return 0;
	}
	public int getMinX()
	{
		return 0;
	}
	public int getMinY()
	{
		return 0;
	}
	public int getMinZ()
	{
		return 0;
	}
}