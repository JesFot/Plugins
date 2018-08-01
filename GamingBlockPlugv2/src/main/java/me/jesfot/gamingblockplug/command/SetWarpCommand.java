package me.jesfot.gamingblockplug.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jesfot.gamingblockplug.permission.PermissionHelper;
import me.jesfot.gamingblockplug.permission.StaticPerms;
import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;
import me.jesfot.gamingblockplug.utils.NumberUtils;

public class SetWarpCommand extends CommandBase
{
	private final GamingBlockPlug plugin;
	
	public SetWarpCommand(GamingBlockPlug plugin)
	{
		super("setwarp");
		
		this.plugin = plugin;
		
		super.setMinimalPermission(StaticPerms.CMD_WARP);
		super.setRawUsageMessage("/<command> <name> [[<world>] <x> <y> <z> [<pitch> <yaw>]]");
	}

	@Override
	protected boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (!PermissionHelper.testPermission(sender, StaticPerms.CMD_WARP_SET, false, null))
		{
			return true;
		}
		boolean allOk = true;
		switch (args.length)
		{
			case 6:
				allOk &= NumberUtils.isNumber(args[5]);
				allOk &= NumberUtils.isNumber(args[4]);
			case 4:
				allOk &= NumberUtils.isNumber(args[3]);
				allOk &= NumberUtils.isNumber(args[2]);
				allOk &= NumberUtils.isNumber(args[1]);
				break;
			case 7:
				allOk &= NumberUtils.isNumber(args[6]);
				allOk &= NumberUtils.isNumber(args[5]);
			case 5:
				allOk &= NumberUtils.isNumber(args[4]);
				allOk &= NumberUtils.isNumber(args[3]);
				allOk &= NumberUtils.isNumber(args[2]);
				break;
				
			default:
				return false;
		}
		if (!allOk)
		{
			return false;
		}
		String world = null;
		double x = 0, y = 0, z = 0;
		float pitch = 0, yaw = 0;
		switch (args.length)
		{
			case 1:
				break;
			case 6:
				pitch = NumberUtils.toFloat(args[4], 0);
				yaw = NumberUtils.toFloat(args[5], 0);
			case 4:
				x = NumberUtils.toDouble(args[1], 0);
				y = NumberUtils.toDouble(args[2], 0);
				z = NumberUtils.toDouble(args[3], 0);
				break;
			case 7:
				pitch = NumberUtils.toFloat(args[5], 0);
				yaw = NumberUtils.toFloat(args[6], 0);
			case 5:
				world = args[1];
				x = NumberUtils.toDouble(args[2], 0);
				y = NumberUtils.toDouble(args[3], 0);
				z = NumberUtils.toDouble(args[4], 0);
				break;
		}
		World w = (world == null) ? null : Bukkit.getWorld(world);
		if (w == null && (sender instanceof Player))
		{
			w = ((Player) sender).getWorld();
		}
		else if (w == null)
		{
			sender.sendMessage("Precise world if you are not playing !");
			return true;
		}
		Location loc = new Location(w, x, y, z, yaw, pitch);
		this.plugin.getWorldManager().getWorld(w).addWarp(args[0], loc);
		Command.broadcastCommandMessage(sender, "New warp '" + args[0] + "' registered in world '" + w.getName() + "'", true);
		return true;
	}
	
	@Override
	protected List<String> executeTabComplete(CommandSender sender, Command command, String label, String[] args)
	{
		if (!PermissionHelper.testPermissionSilent(sender, StaticPerms.CMD_WARP_SET, true))
		{
			return Collections.emptyList();
		}
		if (args.length == 2)
		{
			if (sender instanceof Player)
			{
				return Arrays.asList(Integer.toString(((Player) sender).getLocation().getBlockX()));
			}
			else
			{
				List<String> result = new ArrayList<>();
				Bukkit.getWorlds().forEach(new Consumer<World>() {
					@Override
					public void accept(World t)
					{
						result.add(t.getName());
					}
				});
				return super.sortStart(args[1], result);
			}
		}
		else if (args.length == 3)
		{
			if (sender instanceof Player && NumberUtils.isNumber(args[1]))
			{
				return Arrays.asList(Integer.toString(((Player) sender).getLocation().getBlockY()));
			}
		}
		else if (args.length == 4)
		{
			if (sender instanceof Player && NumberUtils.isNumber(args[1]))
			{
				return Arrays.asList(Integer.toString(((Player) sender).getLocation().getBlockZ()));
			}
		}
		return Collections.emptyList();
	}
}
