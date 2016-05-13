package fr.jesfot.gbp.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public class EntityPortalEnterEvent extends org.bukkit.event.entity.EntityPortalEnterEvent
{
	private PortalType type;
	
	public EntityPortalEnterEvent(Entity entity, Location position)
	{
		super(entity, position);
		Block bl = position.getBlock();
		Material m = bl.getType();
		if(m.equals(Material.ENDER_PORTAL))
		{
			this.type = PortalType.END;
		}
		else if(m.equals(Material.PORTAL))
		{
			this.type = PortalType.NETHER;
		}
		else
		{
			this.type = PortalType.UNKWNON;
		}
	}
	
	public PortalType getPortalType()
	{
		return this.type;
	}
	
	public static enum PortalType
	{
		NETHER,
		END,
		UNKWNON;
	}
}