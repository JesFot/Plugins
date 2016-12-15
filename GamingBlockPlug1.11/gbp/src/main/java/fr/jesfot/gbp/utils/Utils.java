package fr.jesfot.gbp.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_11_R1.CraftServer;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import net.minecraft.server.v1_11_R1.CommandException;
import net.minecraft.server.v1_11_R1.EntityMinecartCommandBlock;
import net.minecraft.server.v1_11_R1.EntityPlayer;
import net.minecraft.server.v1_11_R1.ICommandListener;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import net.minecraft.server.v1_11_R1.PlayerSelector;
import net.minecraft.server.v1_11_R1.TileEntity;
import net.minecraft.server.v1_11_R1.TileEntityCommand;

public class Utils
{
	public static boolean isNumber(String value)
	{
		try
		{
			Double.parseDouble(value);
		}
		catch(NumberFormatException e)
		{
			return false;
		}
		return true;
	}
	
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
	
	public static float toFloat(String value, float p_default)
	{
		float result = p_default;
		
		try
		{
			result = Float.valueOf(value);
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
		try
		{
			for(EntityPlayer o : PlayerSelector.getPlayers(mcListener, myArgument, EntityPlayer.class))
			{
				pls.add(((EntityPlayer)o).getBukkitEntity());
			}
		}
		catch(CommandException e)
		{
			e.printStackTrace();
		}
		return pls;
	}
	
	public static NBTTagCompound setLocation(String key, Location location, NBTTagCompound store)
	{
		if(location == null)
		{
			return store;
		}
		NBTTagCompound loc = new NBTTagCompound();
		loc.setDouble("CoordX", location.getX());
		loc.setDouble("CoordY", location.getY());
		loc.setDouble("CoordZ", location.getZ());
		loc.setFloat("Pitch", location.getPitch());
		loc.setFloat("Yaw", location.getYaw());
		loc.setString("World", location.getWorld().getName());
		store.set(key, loc);
		return store;
	}
	
	public static Location getLocation(String key, NBTTagCompound store)
	{
		NBTTagCompound loc = store.getCompound(key);
		double x, y, z;
		float pitch, yaw;
		x = loc.getDouble("CoordX");
		y = loc.getDouble("CoordY");
		z = loc.getDouble("CoordZ");
		pitch = loc.getFloat("Pitch");
		yaw = loc.getFloat("Yaw");
		String world = loc.getString("World");
		World w = Bukkit.getWorld(world);
		if(w == null)
		{
			return null;
		}
		return new Location(w, x, y, z, yaw, pitch);
	}
	
	public static <E> Set<E> translate(List<E> arg)
	{
		Set<E> result = new HashSet<E>();
		
		for(E e : arg)
		{
			result.add(e);
		}
		
		return result;
	}
	
	public static String color(String msg)
	{
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	public static String compile(String[] list, int firstIndex, String separator)
	{
		if(firstIndex < 0 || firstIndex >= list.length)
		{
			return "";
		}
		String result = list[firstIndex];
		for(int i = firstIndex+1; i < list.length; i++)
		{
			result += " " + list[i];
		}
		return result;
	}
	
	public static int getAmountOf(Inventory inv, ItemStack item)
	{
		MaterialData datas = item.getData();
		ItemStack[] items = inv.getContents();
		int has = 0;
		ItemStack[] arrayOfItemStack1;
		int j = (arrayOfItemStack1 = items).length;
		for(int i = 0; i < j; i++)
		{
			ItemStack is = arrayOfItemStack1[i];
			if(is != null && is.getAmount() > 0 && is.getData().equals(datas))
			{
				has += is.getAmount();
			}
		}
		return has;
	}

	public static String getStringBetween(final String base, final String begin, final String end)
	{
		try
		{
			Pattern patbeg = Pattern.compile(Pattern.quote(begin));
			Pattern patend = Pattern.compile(Pattern.quote(end));

			int resbeg = 0;
			int resend = base.length() - 1;

			Matcher matbeg = patbeg.matcher(base);

			while (matbeg.find())
				resbeg = matbeg.end();

			Matcher matend = patend.matcher(base);

			while (matend.find())
				resend = matend.start();

			return base.substring(resbeg, resend);
		}
		catch(Exception e)
		{
			return base;
		}
	}

	
	public static String readUrl(String url)
	{
		try
		{
			HttpURLConnection con = (HttpURLConnection)(new URL(url)).openConnection();
			
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "GamingBlock");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			
			String line;
			StringBuilder output = new StringBuilder();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			while((line = in.readLine()) != null)
			{
				output.append(line);
			}
			
			in.close();
			
			return output.toString();
		}
		catch(Exception e)
		{
			return "";
		}
	}
	
	public static BlockCommandSender getBlockCommandSender(CommandBlock block)
	{
		BlockCommandSender sender = null;
		Location loc = block.getLocation();
		CraftWorld cworld = (CraftWorld)loc.getWorld();
		TileEntity tile = cworld.getTileEntityAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		TileEntityCommand tileCmd = (TileEntityCommand)tile;
		sender = new CraftBlockCommandSender(tileCmd.getCommandBlock());
		return sender;
	}

	public static <A> List<A> convert(Set<A> set)
	{
		List<A> result = new ArrayList<A>();
		for(A el : set)
		{
			result.add(el);
		}
		return result;
	}
}