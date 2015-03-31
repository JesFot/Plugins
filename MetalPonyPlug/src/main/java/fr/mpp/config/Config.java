package fr.mpp.config;

public class Config
{
	private final MetalPonyPlug mpp;
	private FileConfiguration confFile;

	public Config(FileConfiguration file, MetalPonyPlug p_mpp)
	{
		this.confFile = file;
		this.mpp = p_mpp;
	}
}