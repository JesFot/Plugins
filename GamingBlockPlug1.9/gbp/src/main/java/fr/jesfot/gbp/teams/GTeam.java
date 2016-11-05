package fr.jesfot.gbp.teams;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.OfflinePlayer;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.configuration.NBTSubConfig;
import fr.jesfot.gbp.utils.Utils;

public class GTeam
{
	private GamingBlockPlug_1_9 gbp;
	
	private boolean exists;
	
	private final String teamID;
	private String displayName;
	private String chatColor;
	private int maxHomes;
	//CanUseSpectate
	private boolean canUseTpa;
	private boolean canUseWorld;
	private boolean canUseFly;
	private boolean canUseSpectate;
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
		this.canUseFly = false;
		this.canUseSpectate = false;
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
		NBTSubConfig thisConfig = new NBTSubConfig(this.gbp.getConfigFolder("teams"), "TeamsData", this.getId());
		thisConfig.readNBTFromFile();
		this.displayName = thisConfig.getCopy().getString("DisplayName");
		this.chatColor = thisConfig.getCopy().getString("ChatColor");
		this.maxHomes = thisConfig.getCopy().getInt("ValuableHomes");
		this.canUseTpa = thisConfig.getCopy().getBoolean("CanUseTPA");
		this.canUseFly = thisConfig.getCopy().getBoolean("CanUseFly");
		this.canUseSpectate = thisConfig.getCopy().getBoolean("CanUseSpectate");
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
		thisConfig.setBoolean("CanUseFly", this.canUseFly);
		thisConfig.setBoolean("CanUseSpectate", this.canUseSpectate);
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
	
	public Set<String> getPossibilities(String key)
	{
		Set<String> result = new HashSet<String>();
		if(key.equalsIgnoreCase("color") || key.equalsIgnoreCase("chatcolor"))
		{
			result.add("§a&a§r");
			result.add("§b&b§r");
			result.add("§c&c§r");
			result.add("§d&d§r");
			result.add("§e&e§r");
			result.add("§f&f§r");
			result.add("§0&0§r");
			result.add("§1&1§r");
			result.add("§2&2§r");
			result.add("§3&3§r");
			result.add("§4&4§r");
			result.add("§5&5§r");
			result.add("§6&6§r");
			result.add("§7&7§r");
			result.add("§8&8§r");
			result.add("§9&9§r");
			//
			result.add("§k&k§r");
			result.add("§l&l§r");
			result.add("§m&m§r");
			result.add("§n&n§r");
			result.add("§o&o§r");
			result.add("§r&r§r");
		}
		if(key.equalsIgnoreCase("maxhomes") || key.equalsIgnoreCase("homes") || key.equalsIgnoreCase("maxhome"))
		{
			result.add("1");
			result.add("2");
			result.add("4");
			result.add("5");
			result.add("10");
			result.add("20");
			result.add("50");
			result.add("100");
		}
		if(key.equalsIgnoreCase("canusefly") || key.equalsIgnoreCase("fly"))
		{
			result.add("true");
			result.add("false");
		}
		if(key.equalsIgnoreCase("canusetpa") || key.equalsIgnoreCase("tpa"))
		{
			result.add("true");
			result.add("false");
		}
		if(key.equalsIgnoreCase("canusespectate") || key.equalsIgnoreCase("spectate"))
		{
			result.add("true");
			result.add("false");
		}
		if(key.equalsIgnoreCase("canuseworld") || key.equalsIgnoreCase("world"))
		{
			result.add("true");
			result.add("false");
		}
		if(key.equalsIgnoreCase("shop") || key.equalsIgnoreCase("canopenshops") || key.equalsIgnoreCase("canopen"))
		{
			result.add("true");
			result.add("false");
		}
		return result;
	}
	
	public String getOrSet(String key, String value)
	{
		if(key.equalsIgnoreCase("color") || key.equalsIgnoreCase("chatcolor"))
		{
			if(value != null)
			{
				this.setChatColor(value);
			}
			return this.getChatColor();
		}
		if(key.equalsIgnoreCase("displayname") || key.equalsIgnoreCase("name"))
		{
			if(value != null)
			{
				this.setDisplayName(value);
			}
			return this.getDisplayName();
		}
		if(key.equalsIgnoreCase("maxhomes") || key.equalsIgnoreCase("homes") || key.equalsIgnoreCase("maxhome"))
		{
			if(value != null)
			{
				this.setMaxHomes(Utils.toInt(value, this.getMaxHomes()));
			}
			return Integer.toString(this.getMaxHomes());
		}
		if(key.equalsIgnoreCase("canusetpa") || key.equalsIgnoreCase("tpa"))
		{
			if(value != null)
			{
				this.setCanUseTpa(Boolean.valueOf(value).booleanValue());
			}
			return Boolean.toString(this.canUseTpa());
		}
		if(key.equalsIgnoreCase("canusefly") || key.equalsIgnoreCase("fly"))
		{
			if(value != null)
			{
				this.setCanUseFly(Boolean.valueOf(value).booleanValue());
			}
			return Boolean.toString(this.canUseFly());
		}
		if(key.equalsIgnoreCase("canusespectate") || key.equalsIgnoreCase("spectate"))
		{
			if(value != null)
			{
				this.setCanUseSpectate(Boolean.valueOf(value).booleanValue());
			}
			return Boolean.toString(this.canUseSpectate());
		}
		if(key.equalsIgnoreCase("canuseworld") || key.equalsIgnoreCase("world"))
		{
			if(value != null)
			{
				this.setCanUseWorld(Boolean.parseBoolean(value));
			}
			return Boolean.toString(this.canUseWorld());
		}
		if(key.equalsIgnoreCase("shop") || key.equalsIgnoreCase("canopenshops") || key.equalsIgnoreCase("canopen"))
		{
			if(value != null)
			{
				this.setCanOpenShops(Boolean.parseBoolean(value));
			}
			return Boolean.toString(this.canOpenGroupChest());
		}
		return null;
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
	
	public boolean canUseFly()
	{
		this.reloadFromConfig();
		return this.canUseFly;
	}
	
	public boolean canUseSpectate()
	{
		this.reloadFromConfig();
		return this.canUseSpectate;
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
	
	public void setCanUseFly(final boolean canUse)
	{
		this.canUseFly = canUse;
		this.saveToConfig();
	}
	
	public void setCanUseSpectate(final boolean canUse)
	{
		this.canUseSpectate = canUse;
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