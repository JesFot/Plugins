package fr.mpp.config;

import org.bukkit.configuration.file.FileConfiguration;

import fr.mpp.MetalPonyPlug;

public class MConfig
{
	@SuppressWarnings("unused")
	private final MetalPonyPlug mpp;
	private FileConfiguration confFile;
	private static final int x = 0, y = 0, z = 0;

	public MConfig(FileConfiguration file, MetalPonyPlug p_mpp)
	{
		this.confFile = file;
		this.mpp = p_mpp;
	}
	
	public boolean getMppActive()
	{
		return confFile.getBoolean("mpp_active");
	}

	// int x = 395, y = 79, z = -27; //391
	public static int getMaxX()
	{
		return 395;
	}
	public static int getMaxY()
	{
		return 79;
	}
	public static int getMaxZ()
	{
		return -27;
	}
	public static int getMinX()
	{
		return 391;
	}
	public static int getMinY()
	{
		return 78;
	}
	public static int getMinZ()
	{
		return -27;
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
}