package fr.jesfot.gbp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.permission.PermissionsHelper;
import fr.jesfot.gbp.subsytems.SecurityWallSys;
import fr.jesfot.gbp.utils.Utils;

public class GSecurityWallCommand extends CommandBase
{
	private GamingBlockPlug_1_9 gbp;
	private SecurityWallSys sws;
	
	public GSecurityWallCommand(GamingBlockPlug_1_9 plugin)
	{
		super("addwall");
		this.gbp = plugin;
		this.sws = new SecurityWallSys(plugin);
		plugin.getPermissionManager().addPermission("GamingBlockPlug.secureWall.modify", PermissionDefault.OP,
				"Allows you to set/remove security walls", Permissions.SecureWallPerm);
		this.gbp.getPermissionManager().addPermission("GamingBlockPlug.secureWall.place", PermissionDefault.OP,
				"Allows you to place security wall signs", Permissions.SecureWallPerm);
		this.gbp.getPermissionManager().addPermission("GamingBlockPlug.secureWall.break", PermissionDefault.FALSE,
				"Allows you to break security walls' blocks", Permissions.SecureWallPerm);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(!PermissionsHelper.testPermission(sender, "GamingBlockPlug.secureWall.modify", false, null))
		{
			return true;
		}
		if(!(sender instanceof Player))
		{
			sender.sendMessage(this.gbp.getLang().get("console.onlypls", "Player !"));
		}
		Player player = (Player)sender;
		if(args.length == 0)
		{
			sender.sendMessage("Usage: /addwall add|remove [locX locZ]");
		}
		else
		{
			if(args.length == 1)
			{
				if(args[0].equalsIgnoreCase("add"))
				{
					this.sws.addToStorage(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockZ());
				}
				else if(args[0].equalsIgnoreCase("remove"))
				{
					this.sws.removeFromStorage(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockZ());
				}
				else
				{
					sender.sendMessage("Usage: /addwall add|remove [locX locZ]");
				}
			}
			else if(args.length >= 3)
			{
				if(args[0].equalsIgnoreCase("add"))
				{
					int x = Utils.toInt(args[1], player.getLocation().getBlockX());
					int z = Utils.toInt(args[2], player.getLocation().getBlockZ());
					this.sws.addToStorage(player.getWorld(), x, z);
				}
				else if(args[0].equalsIgnoreCase("remove"))
				{
					int x = Utils.toInt(args[1], player.getLocation().getBlockX());
					int z = Utils.toInt(args[2], player.getLocation().getBlockZ());
					this.sws.removeFromStorage(player.getWorld(), x, z);
				}
				else
				{
					sender.sendMessage("Usage: /addwall add|remove [locX locZ]");
				}
			}
			else
			{
				sender.sendMessage("Usage: /addwall add|remove [locX locZ]");
			}
		}
		return true;
	}
}