package fr.mpp;

import org.bukkit.plugin.java.JavaPlugin;

public class MPP
{
	private static MetalPonyPlug mpp;
	
	public static void setMetalPonyPlug(MetalPonyPlug mppl)
	{
		mpp = mppl;
	}
	
	public static MetalPonyPlug getmpp()
	{
		return mpp;
	}
	
	public static JavaPlugin getPlugin()
	{
		return mpp.getPlugin();
	}
}