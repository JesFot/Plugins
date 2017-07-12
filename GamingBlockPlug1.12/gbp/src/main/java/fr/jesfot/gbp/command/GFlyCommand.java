package fr.jesfot.gbp.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import fr.jesfot.gbp.GamingBlockPlug_1_12;
import fr.jesfot.gbp.configuration.NBTConfig;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.permission.PermissionsHelper;
import fr.jesfot.gbp.utils.Utils;

public class GFlyCommand extends CommandBase
{
	private GamingBlockPlug_1_12 gbp;
	
	public GFlyCommand(GamingBlockPlug_1_12 plugin)
	{
		super("fly");
		this.gbp = plugin;
		this.setRawUsageMessage("/<com> [on|off] | /<com> <player> <on|off>");
		plugin.getPermissionManager().addPermission("GamingBlockPlug.fly", PermissionDefault.TRUE,
				"Permission to use the /fly command", Permissions.globalGBP);
		plugin.getPermissionManager().addPermission("GamingBlockPlug.flyforce", PermissionDefault.OP,
				"Permission to use the /fly <player> command", Permissions.globalGBP);
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
		if(player.getGameMode().equals(GameMode.ADVENTURE))
		{
			sender.sendMessage("Cannot use /" + label + " in adventure mode !");
			return true;
		}
		if(!PermissionsHelper.testPermission(sender, "GamingBlockPlug.fly", false, null))
		{
			GamingBlockPlug_1_12.getMyLogger().info(player.getName() + " tried to use /"
					+ command.getName() + " " + Utils.compile(args, 0, " "));
			return true;
		}
		NBTConfig playerCfg = new NBTConfig(this.gbp.getConfigFolder("playerdatas"), player.getUniqueId());
		String teamName = playerCfg.readNBTFromFile().getCopy().getString("Team");
		if(!this.gbp.getTeams().getIfExists(teamName).canUseFly())
		{
			sender.sendMessage("You are not allowed to use that command.");
			return true;
		}
		GamingBlockPlug_1_12.getMyLogger().info(player.getName() + " used /"
				+ command.getName() + " " + Utils.compile(args, 0, " "));
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
		else if(args.length == 2 && PermissionsHelper.testPermission(sender, "GamingBlockPlug.flyforce", false, null))
		{
			Player target = this.gbp.getPlayerExact(args[0]);
			if(target == null)
			{
				sender.sendMessage(this.color(this.gbp.getLang().get("player.notfound").replaceAll("<player>", args[0])));
				return true;
			}
			if(args[1].equalsIgnoreCase("on"))
			{
				if(target.getGameMode().equals(GameMode.CREATIVE) || target.getGameMode().equals(GameMode.SPECTATOR))
				{
					sender.sendMessage("You should not use this command while target is in Creative or Spectator...");
					return true;
				}
				target.setAllowFlight(true);
				Command.broadcastCommandMessage(sender, "Set flying to true for " + args[0], true);
				target.sendMessage(this.color("&cYou were allowed to fly"));
			}
			else if(args[1].equalsIgnoreCase("off"))
			{
				if(target.getGameMode().equals(GameMode.CREATIVE) || target.getGameMode().equals(GameMode.SPECTATOR))
				{
					sender.sendMessage("You should not use this command while target is in Creative or Spectator...");
					return true;
				}
				target.setAllowFlight(false);
				Command.broadcastCommandMessage(sender, "Set flying to false for " + args[0], true);
				target.sendMessage(this.color("&cYou are not allowed to fly anymore"));
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
		if(args.length >= 3)
		{
			return super.executeTabComplete(sender, command, alias, args);
		}
		List<String> result = new ArrayList<String>();
		if(args.length == 1 || PermissionsHelper.testPermissionSilent(sender, "GamingBlockPlug.flyforce", false))
		{
			result.add("on");
			result.add("off");
		}
		if(args.length == 1 && PermissionsHelper.testPermissionSilent(sender, "GamingBlockPlug.flyforce", false))
		{
			result.addAll(this.getPlayers(args[0]));
		}
		return this.sortStart(args[args.length - 1], result);
	}
}