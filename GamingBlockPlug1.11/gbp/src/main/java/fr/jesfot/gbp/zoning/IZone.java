package fr.jesfot.gbp.zoning;

import org.bukkit.Location;

public interface IZone
{
	public abstract boolean isInZone(final Location location);
}