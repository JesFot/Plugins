package me.jesfot.gamingblockplug.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.World;

public class WorldManager
{
	private Map<UUID, GBPWorld> worlds = new HashMap<>();
	
	private boolean autoRS = false;
	
	public WorldManager() { /* Nothing here */ }
	
	public void setAutoReloadSave(final boolean value)
	{
		this.setAutoReloadSave(value, false);
	}
	
	public void setAutoReloadSave(final boolean value, final boolean applyToAll)
	{
		boolean old = this.autoRS;
		this.autoRS = value;
		if (old != value && applyToAll)
		{
			this.worlds.values().forEach(new Consumer<GBPWorld>() {
				@Override
				public void accept(GBPWorld t)
				{
					t.setAutoReloadSave(value);
				}
			});
		}
	}
	
	public void registerWorld(GBPWorld world)
	{
		this.worlds.put(world.getUniqueId(), world);
	}
	
	public GBPWorld registerBukkitWorld(World world)
	{
		GBPWorld wd = new GBPWorld(world, this.autoRS);
		this.registerWorld(wd);
		return wd;
	}
	
	public GBPWorld getWorld(UUID uid)
	{
		return this.worlds.get(uid);
	}
	
	public GBPWorld getWorld(World world)
	{
		GBPWorld wd = this.worlds.get(world.getUID());
		if (wd == null)
		{
			return this.registerBukkitWorld(world);
		}
		return wd;
	}
	
	public void removeWorld(GBPWorld world)
	{
		this.worlds.remove(world.getUniqueId());
	}
	
	public void removeWorld(UUID uid)
	{
		this.worlds.remove(uid);
	}
	
	public void removeWorld(World world)
	{
		this.worlds.remove(world.getUID());
	}
	
	public int getWorldNb()
	{
		return this.worlds.size();
	}
}
