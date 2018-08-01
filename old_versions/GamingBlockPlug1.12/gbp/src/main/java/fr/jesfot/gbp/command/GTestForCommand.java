package fr.jesfot.gbp.command;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.jesfot.gbp.GamingBlockPlug_1_12;
import fr.jesfot.gbp.utils.Utils;

public class GTestForCommand extends CommandBase
{
	private GamingBlockPlug_1_12 gbp;
	
	public GTestForCommand(GamingBlockPlug_1_12 plugin)
	{
		super("gtestfor");
		this.setRawUsageMessage("/<com> <targets> <teams list...>");
		this.gbp = plugin;
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(args.length < 2)
		{
			this.sendUsage(sender, label);
			return true;
		}
		if(!(sender instanceof BlockCommandSender))
		{
			sender.sendMessage("This command might be executed by a commandblock !");
			return true;
		}
		Player target = null;
		List<Player> tmp = Utils.getPlayers(sender, args[0]);
		if(tmp != null)
		{
			target = tmp.get(0);
		}
		else
		{
			target = this.gbp.getPlayerExact(args[0]);
		}
		BlockCommandSender realSender = (BlockCommandSender)sender;
		if(target == null)
		{
			realSender.getBlock().getRelative(BlockFace.DOWN, 2).setType(Material.REDSTONE_BLOCK);
			return true;
		}
		for(int i = 1; i < args.length; i++)
		{
			if(!this.gbp.getTeams().existsTeam(args[i]))
			{
				continue;
			}
			if(this.gbp.getTeams().getTeamOf(target).getId().equalsIgnoreCase(args[i]))
			{
				break;
			}
			else if(i == args.length - 1)
			{
				realSender.getBlock().getRelative(BlockFace.DOWN, 2).setType(Material.REDSTONE_BLOCK);
				return true;
			}
		}
		realSender.getBlock().getRelative(BlockFace.UP, 3).setType(Material.REDSTONE_BLOCK);
		return true;
	}
}
