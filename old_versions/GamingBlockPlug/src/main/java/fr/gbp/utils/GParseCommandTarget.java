package fr.gbp.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.ICommandListener;
import net.minecraft.server.v1_8_R3.PlayerSelector;


public class GParseCommandTarget
{
	
	public static List<Player> getPlayers(CommandSender mySender, String myArgument)
	{
		if(!PlayerSelector.isPattern(myArgument))
		{
			return null;
		}
		final ICommandListener mcListener = GUtils.getListener(mySender);
		List<Player> pls = new ArrayList<Player>();
		if(!myArgument.contains("["))
		{
			if(myArgument.equalsIgnoreCase("@p"))
			{
				Location loc = new Location((World)mcListener.getWorld().getWorld(), mcListener.getChunkCoordinates().getX(), mcListener.getChunkCoordinates().getY(),
						mcListener.getChunkCoordinates().getZ());
				pls.add(UPlayer.getProximityPlayer(loc));
			}
			else if(myArgument.equalsIgnoreCase("@a"))
			{
				pls.addAll(UPlayer.getOnlinePlayers());
			}
			else if(myArgument.equalsIgnoreCase("@r"))
			{
				pls.add(UPlayer.getRandomPlayer((World)mcListener.getWorld().getWorld()));
			}
			return pls;
		}
		for(EntityPlayer o : PlayerSelector.getPlayers(mcListener, myArgument, EntityPlayer.class))
		{
			pls.add(((EntityPlayer)o).getBukkitEntity());
			continue;
		}
		return pls;
	}
}