package me.jesfot.gamingblockplug.security;

import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;
import me.jesfot.gamingblockplug.utils.DataUtils;

public final class SystemManager
{
	private final GamingBlockPlug plugin;
	
	private LoginSystem loginSystem = null;
	private SkinRestorerSystem skinRestorer = null;
	private WallSystem wallSystem = null;
	private HalfInBedSystem passNightSystem = null;
	private CommonChestSystem sharedChestSystem = null;
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
	
	public HalfInBedSystem getHalfInBedSystem()
	{
		if (this.passNightSystem == null)
		{
			this.passNightSystem = new HalfInBedSystem(this.plugin);
		}
		return this.passNightSystem;
	}
	
	public CommonChestSystem getSharedChestSystem()
	{
		if (this.sharedChestSystem == null)
		{
			this.sharedChestSystem = new CommonChestSystem(this.plugin);
			DataUtils.safeReload(this.plugin.getNBT(null));
			if (this.plugin.getNBT(null).contains("CommonChest"))
			{
				this.sharedChestSystem.loadFromConfig(this.plugin.getNBT("CommonChest"));
			}
		}
		return this.sharedChestSystem;
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
