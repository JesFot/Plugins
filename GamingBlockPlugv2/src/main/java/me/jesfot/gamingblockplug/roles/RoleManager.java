package me.jesfot.gamingblockplug.roles;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.OfflinePlayer;

import me.jesfot.gamingblockplug.data.GBPPlayer;
import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;
import me.jesfot.gamingblockplug.utils.DataUtils;
import me.unei.configuration.api.Configurations;
import me.unei.configuration.api.INBTConfiguration;

/**
 * Role management class.
 * 
 * @author JësFot
 * @since 1.13-1.0.0
 */
public class RoleManager
{
	public static final String DEFAULT_NAME = "default";

	private final GamingBlockPlug plugin;
	private Map<String, Role> roles;
	
	private INBTConfiguration mainConfig = null;
	
	public RoleManager(GamingBlockPlug plugin)
	{
		this.plugin = plugin;
		this.roles = new HashMap<>();
	}
	
	public void reloadRoles()
	{
		this.roles.clear();
		if (this.mainConfig == null)
		{
			this.mainConfig = Configurations.newNBTConfig(this.plugin.getConfigFolder("groups"), "RolesData");
		}
		DataUtils.safeReload(this.mainConfig);
		for (String key : this.mainConfig.getKeys())
		{
			Role role = new Role(this.plugin, key);
			role.setConfig(this.mainConfig);
			role.reloadFromConfig();
			this.roles.put(key, role);
		}
	}
	
	public Set<String> getRoleList()
	{
		return this.roles.keySet();
	}
	
	public void saveAll()
	{
		for (Role role : this.roles.values())
		{
			role.saveToConfig();
		}
	}
	
	public boolean exists(String id)
	{
		return this.roles.containsKey(id);
	}
	
	public void create(Role role)
	{
		if (this.mainConfig == null)
		{
			this.mainConfig = Configurations.newNBTConfig(this.plugin.getConfigFolder("groups"), "RolesData");
		}
		role.setConfig(this.mainConfig);
		role.create();
		this.roles.put(role.getId(), role);
	}
	
	public Role getOrCreate(String id)
	{
		Role role;
		if (!this.exists(id))
		{
			role = new Role(this.plugin, id);
			this.create(role);
		}
		else
		{
			role = this.roles.get(id);
		}
		return role;
	}
	
	public Role get(String id)
	{
		if (!this.exists(id))
		{
			return this.getOrCreate(RoleManager.DEFAULT_NAME);
		}
		return this.roles.get(id);
	}
	
	public void delete(String id)
	{
		if (!this.exists(id))
		{
			return;
		}
		Role role = this.get(id);
		role.delete();
		this.roles.remove(id);
	}
	
	public boolean setRole(final OfflinePlayer player, String id)
	{
		if (!this.exists(id))
		{
			return false;
		}
		GBPPlayer playerCfg = this.plugin.getPlayerManager().getPlayer(player);
		playerCfg.setRoleId(id);
		return true;
	}
	
	public boolean resetRole(final OfflinePlayer player)
	{
		GBPPlayer playerCfg = this.plugin.getPlayerManager().getPlayer(player);
		playerCfg.setRoleId(RoleManager.DEFAULT_NAME);
		return true;
	}
	
	public Role getRoleOf(final OfflinePlayer player)
	{
		GBPPlayer playerCfg = this.plugin.getPlayerManager().getPlayer(player);
		String name = playerCfg.getRoleId();
		return this.get(name);
	}
}
