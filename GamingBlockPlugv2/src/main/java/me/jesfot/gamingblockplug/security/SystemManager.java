package me.jesfot.gamingblockplug.security;

import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;

public final class SystemManager
{
	private final GamingBlockPlug plugin;
	
	private LoginSystem loginSystem = null;
	private WallSystem wallSystem = null;
	private VariablesSystem variablesSystem = null;
	
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
	
	public WallSystem getWallSystem()
	{
		if (this.wallSystem == null)
		{
			this.wallSystem = new WallSystem();
		}
		return this.wallSystem;
	}
	
	public VariablesSystem getVariablesSystem()
	{
		if (this.variablesSystem == null)
		{
			this.variablesSystem = new VariablesSystem(this.plugin);
		}
		return this.variablesSystem;
	}
}
