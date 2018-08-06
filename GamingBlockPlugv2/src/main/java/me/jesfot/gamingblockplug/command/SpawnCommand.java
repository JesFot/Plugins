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
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import me.jesfot.gamingblockplug.data.GBPWorld;
import me.jesfot.gamingblockplug.permission.PermissionHelper;
import me.jesfot.gamingblockplug.permission.StaticPerms;
import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;
import me.jesfot.gamingblockplug.utils.NumberUtils;

/**
 * @author JësFot
 * @since 1.13-1.0.0
 * @version 1.1
 */
public class SpawnCommand extends CommandBase
{
	private final GamingBlockPlug plugin;
	
	public SpawnCommand(GamingBlockPlug plugin)
	{
		super("spawn");
		
		this.plugin = plugin;
		
		super.setMinimalPermission(StaticPerms.CMD_SPAWN);
		super.setRawUsageMessage("/<command> [set [[<world>] <x> <y> <z> [<pitch> <yaw>]]] | /<command> remove [<world>]");
	}

	@Override
	protected boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (args.length == 0 && (sender instanceof Player))
		{
			if (!PermissionHelper.testPermission(sender, StaticPerms.CMD_SPAWN_USE, true, null))
			{
				return true;
			}
			Player player = (Player) sender;
			GBPWorld current = this.plugin.getWorldManager().getWorld(player.getWorld());
			Location spawn = current.getSpawn();
			if (spawn == null)
			{
				spawn = current.getHandler().getSpawnLocation();
			}
			player.teleport(spawn, TeleportCause.COMMAND);
		}
		else if (args[0].equalsIgnoreCase("remove"))
		{
			if (!PermissionHelper.testPermission(sender, StaticPerms.CMD_SPAWN_SET, false, null))
			{
				return true;
			}
			String world = (args.length == 2) ? args[1] : null;
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
			this.plugin.getWorldManager().getWorld(w).removeSpawn();
			Command.broadcastCommandMessage(sender, "Resetted spawn position for world '" + w.getName() + "'", true);
		}
		else if (args[0].equalsIgnoreCase("set"))
		{
			if (!PermissionHelper.testPermission(sender, StaticPerms.CMD_SPAWN_SET, false, null))
			{
				return true;
			}
			boolean allOk = true;
			switch (args.length)
			{
				case 1:
					break;
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
			if (sender instanceof Player)
			{
				x = ((Player) sender).getLocation().getX();
				y = ((Player) sender).getLocation().getY();
				z = ((Player) sender).getLocation().getZ();
				pitch = ((Player) sender).getLocation().getPitch();
				yaw = ((Player) sender).getLocation().getYaw();
			}
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
			this.plugin.getWorldManager().getWorld(w).setSpawn(loc);
			Command.broadcastCommandMessage(sender, "Defined spawn position for world '" + w.getName() + "'", true);
		}
		else
		{
			super.sendUsage(sender, label);
		}
		return true;
	}
	
	@Override
	protected List<String> executeTabComplete(CommandSender sender, Command command, String label, String[] args)
	{
		if (!PermissionHelper.testPermissionSilent(sender, StaticPerms.CMD_SPAWN_SET, true))
		{
			return Collections.emptyList();
		}
		if (args.length == 1)
		{
			return Arrays.asList("set");
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
		if (args.length == 3)
		{
			if (sender instanceof Player && NumberUtils.isNumber(args[1]))
			{
				return Arrays.asList(Integer.toString(((Player) sender).getLocation().getBlockY()));
			}
		}
		if (args.length == 4)
		{
			if (sender instanceof Player && NumberUtils.isNumber(args[1]))
			{
				return Arrays.asList(Integer.toString(((Player) sender).getLocation().getBlockZ()));
			}
		}
		return Collections.emptyList();
	}
}
