package fr.jesfot.gbp.teams;

import org.bukkit.OfflinePlayer;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.configuration.NBTSubConfig;

public class GTeam
{
	private GamingBlockPlug_1_9 gbp;
	
	private boolean exists;
	
	private final String teamID;
	private String displayName;
	private String chatColor;
	private int maxHomes;
	private boolean canUseTpa;
	private boolean canUseWorld;
	private boolean canOpenShopsInGroup;
	
	public GTeam(GamingBlockPlug_1_9 plugin, final String id)
	{
		this.gbp = plugin;
		
		this.exists = false;
		
		this.teamID = id;
		this.displayName = "default";
		this.chatColor = "&0";
		this.maxHomes = 2;
		this.canUseTpa = false;
		this.canUseWorld = false;
		this.canOpenShopsInGroup = false;
		this.update();
	}
	
	private void update()
	{
		NBTSubConfig thisConfig = new NBTSubConfig(this.gbp.getConfigFolder("teams"), "TeamsData");
		this.exists = thisConfig.readNBTFromFile().getCopy().hasKey(this.getId());
	}
	
	protected void reloadFromConfig()
	{
		if(!this.exists)
		{
			return;
		}
		NBTSubConfig thisConfig = new NBTSubConfig(this.gbp.getConfigFolder("teams"), "TeamsData", this.getId());
		thisConfig.readNBTFromFile();
		this.displayName = thisConfig.getCopy().getString("DisplayName");
		this.chatColor = thisConfig.getCopy().getString("ChatColor");
		this.maxHomes = thisConfig.getCopy().getInteger("ValuableHomes");
		this.canUseTpa = thisConfig.getCopy().getBoolean("CanUseTPA");
		this.canUseWorld = thisConfig.getCopy().getBoolean("CanUseWorld");
		this.canOpenShopsInGroup = thisConfig.getCopy().getBoolean("MultiShopsOwners");
	}
	
	protected void saveToConfig()
	{
		if(!this.exists)
		{
			return;
		}
		NBTSubConfig thisConfig = new NBTSubConfig(this.gbp.getConfigFolder("teams"), "TeamsData", this.getId());
		thisConfig.readNBTFromFile();
		thisConfig.setString("DisplayName", this.displayName);
		thisConfig.setString("ChatColor", this.chatColor);
		thisConfig.setInteger("ValuableHomes", this.maxHomes);
		thisConfig.setBoolean("CanUseTPA", this.canUseTpa);
		thisConfig.setBoolean("CanUseWorld", this.canUseWorld);
		thisConfig.setBoolean("MultiShopsOwners", this.canOpenShopsInGroup);
		thisConfig.writeNBTToFile();
	}
	
	public void delete()
	{
		for(OfflinePlayer pl : this.gbp.getServer().getOfflinePlayers())
		{
			NBTSubConfig plCfg = new NBTSubConfig(this.gbp.getConfigFolder("playerdatas"), pl.getUniqueId());
			if(plCfg.readNBTFromFile().getCopy().getString("Team").equalsIgnoreCase(this.getId()))
			{
				plCfg.setString("Team", "default").writeNBTToFile();
			}
		}
		NBTSubConfig thisConfig = new NBTSubConfig(this.gbp.getConfigFolder("teams"), "TeamsData");
		thisConfig.removeTag(this.getId());
		this.exists = false;
	}
	
	public void create()
	{
		this.exists = true;
		this.saveToConfig();
	}
	
	public String getId()
	{
		return this.teamID;
	}
	
	public String getDisplayName()
	{
		this.reloadFromConfig();
		return this.displayName;
	}
	
	public String getChatColor()
	{
		this.reloadFromConfig();
		return this.chatColor;
	}
	
	public int getMaxHomes()
	{
		this.reloadFromConfig();
		return this.maxHomes;
	}
	
	public boolean canUseTpa()
	{
		this.reloadFromConfig();
		return this.canUseTpa;
	}
	
	public boolean canUseWorld()
	{
		this.reloadFromConfig();
		return this.canUseWorld;
	}
	
	public boolean canOpenGroupChest()
	{
		this.reloadFromConfig();
		return this.canOpenShopsInGroup;
	}
	
	public void setDisplayName(final String name)
	{
		this.displayName = name;
		this.saveToConfig();
	}
	
	public void setChatColor(final String color)
	{
		this.chatColor = color;
		this.saveToConfig();
	}
	
	public void setMaxHomes(final int max)
	{
		this.maxHomes = max;
		this.saveToConfig();
	}
	
	public void setCanUseTpa(final boolean canUse)
	{
		this.canUseTpa = canUse;
		this.saveToConfig();
	}
	
	public void setCanUseWorld(final boolean canUse)
	{
		this.canUseWorld = canUse;
		this.saveToConfig();
	}
	
	public void setCanOpenShops(final boolean canOpen)
	{
		this.canOpenShopsInGroup = canOpen;
		this.saveToConfig();
	}
}