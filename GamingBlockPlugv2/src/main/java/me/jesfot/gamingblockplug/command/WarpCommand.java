package me.jesfot.gamingblockplug.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import me.jesfot.gamingblockplug.data.GBPWorld;
import me.jesfot.gamingblockplug.permission.PermissionHelper;
import me.jesfot.gamingblockplug.permission.StaticPerms;
import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;

/**
 * @author JësFot
 * @since 1.13-1.0.0
 * @version 1.0
 */
public class WarpCommand extends CommandBase
{
	private final GamingBlockPlug plugin;
	
	public WarpCommand(GamingBlockPlug plugin)
	{
		super("warp");
		
		this.plugin = plugin;
		
		super.setMinimalPermission(StaticPerms.CMD_WARP);
		super.setRawUsageMessage("/<command> [<name> [<player>]]");
	}

	@Override
	protected boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage("Only for players");
			return true;
		}
		if (args.length == 0)
		{
			if (!PermissionHelper.testPermission(sender, StaticPerms.CMD_WARP_LIST, true, null))
			{
				return true;
			}
			Player player = (Player) sender;
			GBPWorld current = this.plugin.getWorldManager().getWorld(player.getWorld());
			Set<String> wps = current.getWarps().keySet();
			Iterator<String> it = wps.iterator();
			sender.sendMessage(ChatColor.GOLD + "Warps : ");
			if (wps.size() < 10)
			{
				while (it.hasNext())
				{
					sender.sendMessage(ChatColor.AQUA + " - " + it.next());
				}
			}
			else
			{
				StringBuilder sb = new StringBuilder(it.next());
				while (it.hasNext())
				{
					sb.append(',').append(' ').append(it.next());
				}
				sender.sendMessage(ChatColor.AQUA + sb.toString());
			}
		}
		else if (args.length == 1)
		{
			if (!PermissionHelper.testPermission(sender, StaticPerms.CMD_WARP_USE_GOTO, false, null))
			{
				return true;
			}
			Player player = (Player) sender;
			GBPWorld current = this.plugin.getWorldManager().getWorld(player.getWorld());
			Location warp = current.getWarps().get(args[0]);
			if (warp != null)
			{
				player.teleport(warp, TeleportCause.COMMAND);
			}
			else
			{
				player.sendMessage("Warp '" + args[0] + "' does not exists.");
			}
		}
		else if (args.length >= 2)
		{
			if (!PermissionHelper.testPermission(sender, StaticPerms.CMD_WARP_USE_OTHER, false, null))
			{
				return true;
			}
			Player targetCB = this.plugin.getPlayerExact(args[1]);
			if (targetCB == null)
			{
				sender.sendMessage("Target not found.");
				return true;
			}
			Player player = (Player) sender;
			if (!targetCB.getWorld().getUID().equals(player.getWorld().getUID()))
			{
				sender.sendMessage("Target is not in your world");
				return true;
			}
			GBPWorld current = this.plugin.getWorldManager().getWorld(targetCB.getWorld());
			Location warp = current.getWarps().get(args[0]);
			if (warp != null)
			{
				targetCB.teleport(warp, TeleportCause.COMMAND);
				targetCB.sendMessage(player.getDisplayName() + " used /warp on you (dest: '" + args[0] + "')");
			}
			else
			{
				player.sendMessage("Warp '" + args[0] + "' does not exists.");
			}
		}
		else
		{
			return false;
		}
		return true;
	}
	
	@Override
	protected List<String> executeTabComplete(CommandSender sender, Command command, String label, String[] args)
	{
		if (args.length == 1 && (sender instanceof Player))
		{
			if (PermissionHelper.testPermissionSilent(sender, StaticPerms.CMD_WARP_LIST, true))
			{
				Player player = (Player) sender;
				GBPWorld current = this.plugin.getWorldManager().getWorld(player.getWorld());
				Set<String> wps = current.getWarps().keySet();
				return CommandBase.sortStart(args[0], new ArrayList<>(wps));
			}
		}
		else if (args.length == 2)
		{
			return null;
		}
		return Collections.emptyList();
	}
}
