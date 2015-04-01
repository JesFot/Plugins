package fr.mpp.config;

import org.bukkit.configuration.file.FileConfiguration;

import fr.mpp.MetalPonyPlug;

public class MConfig
{
	@SuppressWarnings("unused")
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

	// int x = 395, y = 79, z = -27; //394
	public int getMaxX()
	{
		return 395;
	}
	public int getMaxY()
	{
		return 79;
	}
	public int getMaxZ()
	{
		return -27;
	}
	public int getMinX()
	{
		return 389;
	}
	public int getMinY()
	{
		return 78;
	}
	public int getMinZ()
	{
		return -27;
	}
}