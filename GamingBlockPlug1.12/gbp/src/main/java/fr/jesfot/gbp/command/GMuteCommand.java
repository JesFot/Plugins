package fr.jesfot.gbp.command;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import fr.jesfot.gbp.GamingBlockPlug_1_11;
import fr.jesfot.gbp.configuration.Configurations;
import fr.jesfot.gbp.configuration.NBTConfig;
import fr.jesfot.gbp.configuration.NBTSubConfig;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.permission.PermissionsHelper;

public class GMuteCommand extends CommandBase
{
	private GamingBlockPlug_1_11 gbp;
	
	public GMuteCommand(GamingBlockPlug_1_11 plugin)
	{
		super("mute");
		this.gbp = plugin;
		this.setRawUsageMessage("<com> <player> [true|false]");
		plugin.getPermissionManager().addPermission(Permissions.GBP_PERMS + ".mute", PermissionDefault.OP,
				"Permission to use the /mute command", Permissions.globalGBP);
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(!PermissionsHelper.testPermission(sender, Permissions.GBP_PERMS + ".mute", false, null))
		{
			return true;
		}
		if(args.length != 1 && args.length != 2)
		{
			this.sendUsage(sender, label);
			return true;
		}
		boolean on = false;
		boolean force = false;
		if(args.length == 2)
		{
			on = Boolean.parseBoolean(args[1]);
			force = true;
		}
		String targetStr = args[0];
		Player target = this.gbp.getPlayerExact(targetStr);
		if(target == null)
		{
			sender.sendMessage(this.gbp.getLang().getColored("player.notfound"));
			return true;
		}
		NBTConfig playerCfg = new NBTConfig(this.gbp.getConfigFolder(Configurations.PLAYERS_DATS), target.getUniqueId());
		NBTSubConfig playerConfig = new NBTSubConfig(playerCfg);
		playerCfg.readNBTFromFile();
		if(force)
		{
			playerConfig.setBoolean("Muted", on);
		}
		else
		{
			on = !playerCfg.getCopy().getBoolean("Muted");
			playerConfig.setBoolean("Muted", on);
		}
		playerConfig.writeNBTToFile();
		if(on)
		{
			target.sendMessage(this.gbp.getLang().getColored("mute.mute", "You are now unmuted !"));
		}
		else
		{
			target.sendMessage(this.gbp.getLang().getColored("mute.unmute", "You are now muted !"));
		}
		sender.sendMessage(this.gbp.getLang().getColored("mute.set." + Boolean.toString(on), "Setted muted status to true or false for <player>")
				.replaceAll("<player>", target.getName()));
		Command.broadcastCommandMessage(sender, "Defined muted to " + Boolean.toString(on) + " for " + target.getName(), false);
		return true;
	}
	
	@Override
	public List<String> executeTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		if(args.length == 1)
		{
			return this.getPlayers(args[0]);
		}
		else if(args.length == 2)
		{
			return this.sortStart(args[1], Arrays.asList("true", "false"));
		}
		return super.executeTabComplete(sender, command, alias, args);
	}
}