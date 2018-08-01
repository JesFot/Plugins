package me.jesfot.gamingblockplug.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.entity.Player;

import me.unei.configuration.reflection.NMSReflection;

public final class PlayerReflection
{
	private static Class<?> craftPlayerClass, nmsPlayerClass;
	
	static
	{
		craftPlayerClass = NMSReflection.getOBCClass("entity.CraftPlayer");
		nmsPlayerClass = NMSReflection.getNMSClass("Player", true);
	}
	
	public static int getPing(Player player)
	{
		try
		{
			Object craftPlayer = craftPlayerClass.cast(player);

			Method getHandle = craftPlayerClass.getMethod("getHandle");
			Object nmsPlayer = getHandle.invoke(craftPlayer);

			Field ping = nmsPlayerClass.getDeclaredField("ping");

			return ping.getInt(nmsPlayer);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}
}
