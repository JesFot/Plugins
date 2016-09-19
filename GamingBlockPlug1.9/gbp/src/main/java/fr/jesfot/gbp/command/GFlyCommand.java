package fr.jesfot.gbp.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.permission.PermissionsHelper;

public class GFlyCommand extends CommandBase
{
	private GamingBlockPlug_1_9 gbp;
	
	public GFlyCommand(GamingBlockPlug_1_9 plugin)
	{
		super("fly");
		this.gbp = plugin;
		this.setRawUsageMessage("/<com> [on|off]");
		plugin.getPermissionManager().addPermission("GamingBlockPlug.fly", PermissionDefault.OP,
				"Permission to use the /fly command", Permissions.globalGBP);
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		Player player;
		if(!(sender instanceof Player))
		{
			sender.sendMessage(this.color(this.gbp.getLang().get("console.onlypls")));
			return true;
		}
		player = (Player)sender;
		if(!PermissionsHelper.testPermission(sender, "GamingBlockPlug.fly", false, null))
		{
			return true;
		}
		if(args.length == 0)
		{
			// Switch
			if(player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR))
			{
				sender.sendMessage("You should not use this command while in Creative or Spectator...");
				return true;
			}
			player.setAllowFlight(!player.getAllowFlight());
			if(player.getAllowFlight())
			{
				Command.broadcastCommandMessage(sender, "Set flying to true", true);
			}
			else
			{
				Command.broadcastCommandMessage(sender, "Set flying to false", true);
			}
		}
		else if(args.length == 1)
		{
			if(args[0].equalsIgnoreCase("on"))
			{
				if(player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR))
				{
					sender.sendMessage("You should not use this command while in Creative or Spectator...");
					return true;
				}
				player.setAllowFlight(true);
				Command.broadcastCommandMessage(sender, "Set flying to true", true);
			}
			else if(args[0].equalsIgnoreCase("off"))
			{
				if(player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR))
				{
					sender.sendMessage("You should not use this command while in Creative or Spectator...");
					return true;
				}
				player.setAllowFlight(false);
				Command.broadcastCommandMessage(sender, "Set flying to false", true);
			}
			else
			{
				this.sendUsage(sender, label);
			}
		}
		else
		{
			this.sendUsage(sender, label);
		}
		return true;
	}
	
	@Override
	public List<String> executeTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		if(args.length != 1)
		{
			return super.executeTabComplete(sender, command, alias, args);
		}
		List<String> result = new ArrayList<String>();
		result.add("on");
		result.add("off");
		return this.sortStart(args[0], result);
	}
}