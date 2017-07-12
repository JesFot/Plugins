package fr.jesfot.gbp.command;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import fr.jesfot.gbp.GamingBlockPlug_1_12;
import fr.jesfot.gbp.configuration.NBTConfig;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.permission.PermissionsHelper;

public class SpectateCommand extends CommandBase
{
	private GamingBlockPlug_1_12 gbp;
	
	public SpectateCommand(GamingBlockPlug_1_12 plugin)
	{
		super("spectate");
		plugin.getPermissionManager().addPermission("GamingBlockPlug.spectate", PermissionDefault.TRUE,
				"Allow to use the /spectate command ( /gm 3 )", Permissions.globalGBP);
		this.gbp = plugin;
		this.setRawUsageMessage("/<com>");
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(!command.getName().equalsIgnoreCase("spectate"))
		{
			return false;
		}
		if(!PermissionsHelper.testPermission(sender, "GamingBlockPlug.spectate", false, null))
		{
			return true;
		}
		if(!(sender instanceof Player))
		{
			sender.sendMessage(this.color(this.gbp.getLang().get("console.onlypls")));
			return false;
		}
		Player player = (Player)sender;
		
		NBTConfig playerCfg = new NBTConfig(this.gbp.getConfigFolder("playerdatas"), player.getUniqueId());
		String teamName = playerCfg.readNBTFromFile().getCopy().getString("Team");
		if(!this.gbp.getTeams().getIfExists(teamName).canUseSpectate())
		{
			sender.sendMessage("You are not allowed to use that command.");
			return true;
		}
		if(player.getGameMode().equals(GameMode.ADVENTURE))
		{
			sender.sendMessage("Cannot use /" + label + " in adventure mode !");
			return true;
		}
		if(args.length != 0)
		{
			this.sendUsage(sender, label);
			return true;
		}
		if(!player.getGameMode().equals(GameMode.SPECTATOR))
		{
			player.setGameMode(GameMode.SPECTATOR);
			Command.broadcastCommandMessage(sender, "Switched in spectator mode", true);
		}
		else
		{
			player.setGameMode(GameMode.SURVIVAL);
			Command.broadcastCommandMessage(sender, "Switched in normal mode (spec)", true);
		}
		return true;
	}
}