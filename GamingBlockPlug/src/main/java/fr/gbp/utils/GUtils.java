package fr.gbp.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

import net.minecraft.server.v1_8_R3.EntityMinecartCommandBlock;
import net.minecraft.server.v1_8_R3.ICommandListener;
import net.minecraft.server.v1_8_R3.RemoteControlCommandListener;

public class GUtils
{
	public static int toInt(String integer, int p_default)
	{
		int j = p_default;
		
		try
		{
			j = Integer.parseInt(integer);
		}
		catch(Throwable t)
		{
			;
		}
		
		return j;
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
		if(sender instanceof RemoteConsoleCommandSender)
		{
			return RemoteControlCommandListener.getInstance();
		}
		if(sender instanceof ConsoleCommandSender)
		{
			return ((CraftServer)sender.getServer()).getServer();
		}
		return null;
	}
	
	public static BlockFace getDir(Block original, Material mat)
	{
		BlockFace[] values = BlockFace.values();
		for(BlockFace face : values)
		{
			if(original.getRelative(face).getType().equals(mat))
			{
				return face;
			}
		}
		return null;
	}
}