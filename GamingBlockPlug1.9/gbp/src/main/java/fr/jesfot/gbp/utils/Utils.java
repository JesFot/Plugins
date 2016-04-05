package fr.jesfot.gbp.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.craftbukkit.v1_9_R1.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

import net.minecraft.server.v1_9_R1.EntityMinecartCommandBlock;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.ICommandListener;
import net.minecraft.server.v1_9_R1.PlayerSelector;

public class Utils
{
	public static double toDouble(String value, double p_default)
	{
		double result = p_default;
		
		try
		{
			result = Double.valueOf(value);
		}
		catch(NumberFormatException ex)
		{
			result = p_default;
		}
		
		return result;
	}
	
	public static int toInt(String value, int p_default)
	{
		int result = p_default;
		
		try
		{
			result = Integer.valueOf(value);
		}
		catch(NumberFormatException ex)
		{
			result = p_default;
		}
		
		return result;
	}
	
	public static Player getProximityPlayer(Location start)
	{
		if(start.getWorld().getPlayers().size() <= 0)
		{
			return null;
		}
		if(start.getWorld().getPlayers().size() == 1)
		{
			return start.getWorld().getPlayers().get(0);
		}
		List<Player> locations = new ArrayList<Player>();
		for(Player p : start.getWorld().getPlayers())
		{
			locations.add(p);
		}
		Location myLocation = start;
		Player closest = locations.get(0);
		double closestDist = closest.getLocation().distance(myLocation);
		for (Player loc : locations)
		{
			if (loc.getLocation().distance(myLocation) < closestDist)
			{
				closestDist = loc.getLocation().distance(myLocation);
				closest = loc;
			}
		}
		return closest;
	}
	public static Player getRandomPlayer(World w)
	{
		if(w.getPlayers().size() <= 0)
		{
			return null;
		}
		if(w.getPlayers().size() == 1)
		{
			return w.getPlayers().get(0);
		}
		int r = new Random().nextInt(w.getPlayers().size());
		return w.getPlayers().get(r);
	}
	
	public static ICommandListener getListener(CommandSender sender)
	{
		if(sender instanceof Player)
		{
			return ((CraftPlayer)sender).getHandle();
		}
		if(sender instanceof BlockCommandSender)
		{
			return ((CraftBlockCommandSender)sender).getTileEntity();
		}
		if(sender instanceof CommandMinecart)
		{
			return ((EntityMinecartCommandBlock)((CraftMinecartCommand)sender).getHandle()).getCommandBlock();
		}
		/*if(sender instanceof RemoteConsoleCommandSender)
		{
			return CraftRemoteConsoleCommandSender
		}*/
		if(sender instanceof ConsoleCommandSender)
		{
			return ((CraftServer)sender.getServer()).getServer();
		}
		return null;
	}
	
	public static List<Player> getPlayers(CommandSender mySender, String myArgument)
	{
		if(!PlayerSelector.isPattern(myArgument))
		{
			return null;
		}
		final ICommandListener mcListener = Utils.getListener(mySender);
		List<Player> pls = new ArrayList<Player>();
		if(!myArgument.contains("["))
		{
			if(myArgument.equalsIgnoreCase("@p"))
			{
				Location loc = new Location((World)mcListener.getWorld().getWorld(), mcListener.getChunkCoordinates().getX(), mcListener.getChunkCoordinates().getY(),
						mcListener.getChunkCoordinates().getZ());
				pls.add(Utils.getProximityPlayer(loc));
			}
			else if(myArgument.equalsIgnoreCase("@a"))
			{
				pls.addAll(mySender.getServer().getOnlinePlayers());
			}
			else if(myArgument.equalsIgnoreCase("@r"))
			{
				pls.add(Utils.getRandomPlayer((World)mcListener.getWorld().getWorld()));
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