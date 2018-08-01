package me.jesfot.gamingblockplug.security;

import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;

public final class SystemManager
{
	private final GamingBlockPlug plugin;
	
	private LoginSystem loginSystem = null;
	private SkinRestorerSystem skinRestorer = null;
	private WallSystem wallSystem = null;
	
	public SystemManager(GamingBlockPlug plugin)
	{
		this.plugin = plugin;
	}
	
	public LoginSystem getLoginSystem()
	{
		if (this.loginSystem == null)
		{
			this.loginSystem = new LoginSystem(this.plugin);
		}
		return this.loginSystem;
	}
	
	public SkinRestorerSystem getSkinRestorer()
	{
		if (this.skinRestorer == null)
		{
			this.skinRestorer = new SkinRestorerSystem();
		}
		return this.skinRestorer;
	}
	
	public WallSystem getWallSystem()
	{
		if (this.wallSystem == null)
		{
			this.wallSystem = new WallSystem();
		}
		return this.wallSystem;
	}
}
