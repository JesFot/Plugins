package me.jesfot.gamingblockplug.roles;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import me.jesfot.gamingblockplug.data.GBPPlayer;
import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;
import me.jesfot.gamingblockplug.utils.DataUtils;
import me.unei.configuration.api.IConfiguration;
import me.unei.configuration.api.INBTConfiguration;

/**
 * Represent a single role.
 * 
 * @author JësFot
 * @since 1.13-1.0.0
 */
public class Role
{
	private final GamingBlockPlug plugin;
	
	private INBTConfiguration generalConfig = null;
	private boolean exists;
	
	private final String roleID;
	private String displayName;
	private String chatColor;
	
	public Role(GamingBlockPlug plugin, final String id)
	{
		this.exists = false;
		
		this.plugin = plugin;
		this.roleID = id;
		
		this.displayName = RoleManager.DEFAULT_NAME;
		this.chatColor = "&0";
	}
	
	public void update()
	{
		this.exists = this.generalConfig.contains(this.roleID);
	}
	
	void setConfig(INBTConfiguration config)
	{
		this.generalConfig = config;
		if (config != null)
		{
			this.update();
		}
	}
	
	protected void reloadFromConfig()
	{
		DataUtils.safeReload(this.generalConfig);
		this.update();
		if (!this.exists)
		{
			return;
		}
		IConfiguration myconfig = this.generalConfig.getSubSection(this.roleID);
		this.displayName = myconfig.getString("DisplayName");
		this.chatColor = myconfig.getString("ChatColor");
	}
	
	protected void saveToConfig()
	{
		if (!this.exists)
		{
			return;
		}
		DataUtils.safeReload(this.generalConfig);
		IConfiguration myconfig = this.generalConfig.getSubSection(this.roleID);
		myconfig.setString("DisplayName", this.displayName);
		myconfig.setString("ChatColor", this.chatColor);
		myconfig.save();
	}
	
	public void delete()
	{
		for (OfflinePlayer pl : this.plugin.getServer().getOfflinePlayers())
		{
				GBPPlayer plCfg = this.plugin.getPlayerManager().getPlayer(pl);
				if (plCfg.getRoleId().equalsIgnoreCase(this.roleID))
				{
					plCfg.setRoleId(RoleManager.DEFAULT_NAME);
				}
		}
		this.generalConfig.remove(this.roleID);
		this.generalConfig.save();
		this.exists = false;
	}
	
	public void create()
	{
		if (!this.exists)
		{
			this.exists = true;
			this.saveToConfig();
		}
	}
	
	public Set<String> getOptions(String key)
	{
		Set<String> result = new HashSet<>();
		key = key.toLowerCase();
		switch (key)
		{
			case "color":
			case "chatcolor":
				result.addAll(Arrays.asList(
						"&a",
						"&b",
						"&c",
						"&d",
						"&e",
						"&f",
						"&0",
						"&1",
						"&2",
						"&3",
						"&4",
						"&5",
						"&6",
						"&7",
						"&8",
						"&9",
						"&k",
						"&l",
						"&m",
						"&n",
						"&o",
						"&r"));
				break;
		}
		return result;
	}
	
	public String getOrSet(String key, String value)
	{
		this.reloadFromConfig();
		key = key.toLowerCase();
		switch (key)
		{
			case "color":
			case "chatcolor":
				if (value == null)
				{
					return this.getChatColor();
				}
				this.setChatColor(value);
				break;
			case "name":
			case "displayname":
				if (value == null)
				{
					return this.getDisplayName();
				}
				this.setDisplayName(value);
				break;
		}
		this.saveToConfig();
		return null;
	}
	
	public final String getId()
	{
		return this.roleID;
	}
	
	public String getDisplayName()
	{
		return this.displayName;
	}
	
	public String getChatColor()
	{
		return this.chatColor;
	}
	
	public void setChatColor(String color)
	{
		this.chatColor = color;
	}
	
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	
	public String prependFormat(String format)
	{
		StringBuilder roleing = new StringBuilder("&7[");
		
		roleing.append(this.getChatColor()).append(this.getDisplayName());
		roleing.append("&r&7]&r");
		String rolePrefix = ChatColor.translateAlternateColorCodes('&', roleing.toString());
		return rolePrefix + format;
	}

	@Override
	public int hashCode()
	{
		return this.getId().toLowerCase().hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!super.equals(obj))
		{
			return false;
		}
		Role other = (Role) obj;
		if (!other.getId().equalsIgnoreCase(this.getId()))
		{
			return false;
		}
		return true;
	}
}
