package fr.jesfot.gbp.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import fr.jesfot.gbp.GamingBlockPlug_1_11;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.permission.PermissionsHelper;
import fr.jesfot.gbp.utils.Utils;

public class GTpcCommand extends CommandBase
{
	static final int MAX_COORD = 30000000;
	static final int MIN_COORD_MINUS_ONE = -30000001;
	static final int MIN_COORD = -30000000;
	
	private GamingBlockPlug_1_11 gbp;
	
	public GTpcCommand(GamingBlockPlug_1_11 plugin)
	{
		super("gtpc");
		this.gbp = plugin;
		this.setRawUsageMessage("/<com> [player] <x> <y> <z>");
		Permission tpc = plugin.getPermissionManager().addPermission("GamingBlockPlug.tpc", PermissionDefault.OP,
				"Gtpc's permissions", Permissions.globalGBP);
		plugin.getPermissionManager().addPermission("GamingBlockPlug.tpc.use", PermissionDefault.TRUE,
				"Allows you to teleport your self to given coordinates", tpc);
		plugin.getPermissionManager().addPermission("GamingBlockPlug.tpc.target", PermissionDefault.OP,
				"Allows you to teleport someone to given coordinates", tpc);
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(!PermissionsHelper.testPermission(sender, "GamingBlockPlug.tpc.use", true, null))
		{
			System.out.println("No perm tpc_use");
			return true;
		}
		
		if(args.length < 3 || args.length > 4)
		{
			System.out.println("length < 3 | > 4");
			this.sendUsage(sender, label);
			return true;
		}
		
		Player player;
		
		if(args.length == 3)
		{
			if(sender instanceof Player)
			{
				player = (Player)sender;
			}
			else
			{
    			System.out.println("No player");
				sender.sendMessage(this.color(this.gbp.getLang().get("console.missplayer")));
				return true;
			}
		}
		else if(PermissionsHelper.testPermission(sender, "GamingBlockPlug.tpc.target", false, "I'm sorry, "
				+ "but you do not have permission to target a player in this command. "
        		+ "Please contact the server administrators if you believe that this is an error."))
		{
			player = this.gbp.getPlayerExact(args[0]);
		}
		else
		{
			System.out.println("No perm tpc_target");
			if(sender instanceof Player)
			{
				player = (Player)sender;
			}
			else
			{
				player = null;
			}
		}
		
		if(player == null)
		{
			System.out.println("player == null");
			sender.sendMessage(this.color(this.gbp.getLang().get("player.notfound").replaceAll("<player>", args[0])));
			return true;
		}
		
		if(player.getWorld() != null)
		{
			Location playerLocation = player.getLocation();
			double x = this.getCoordinate(playerLocation.getX(), args[args.length - 3]);
			double y = this.getCoordinate(playerLocation.getY(), args[args.length - 2], 0, 0);
			double z = this.getCoordinate(playerLocation.getZ(), args[args.length - 1]);
			
			if(x == MIN_COORD_MINUS_ONE || y == MIN_COORD_MINUS_ONE || z == MIN_COORD_MINUS_ONE)
			{
    			System.out.println("No loc valid");
				sender.sendMessage("Please provide a valid location!");
				return true;
			}
			
			playerLocation.setX(x);
			playerLocation.setY(y);
			playerLocation.setZ(z);

			System.out.println("Teleport");
			
			player.teleport(playerLocation, TeleportCause.COMMAND);
			Command.broadcastCommandMessage(sender, String.format("Teleported %s to %.2f, %.2f, %.2f",
					player.getDisplayName(), x, y, z));
		}
		return true;
	}
	
	private double getCoordinate(double current, String input)
	{
		return this.getCoordinate(current, input, MIN_COORD, MAX_COORD);
	}
	
	private double getCoordinate(double current, String input, int min, int max)
	{
		boolean relative = input.startsWith("~");
		double result = relative ? current : 0;
		
		if(!relative || input.length() > 1)
		{
			boolean exact = input.contains(".");
			if(relative)
			{
				input = input.substring(1);
			}
			
			double testResult = Utils.toDouble(input, MIN_COORD_MINUS_ONE);
			if(testResult == MIN_COORD_MINUS_ONE)
			{
				return MIN_COORD_MINUS_ONE;
			}
			result += testResult;
			
			if(!exact && !relative)
			{
				result += 0.5f;
			}
		}
		if(min != 0 || max != 0)
		{
			if(result < min)
			{
				result = MIN_COORD_MINUS_ONE;
			}
			if(result > max)
			{
				result = MIN_COORD_MINUS_ONE;
			}
		}
		
		return result;
	}
	
	private Location getBlockSight(Player player)
	{
		List<Block> blcks = player.getLineOfSight((Set<Material>)null, 5);
		return blcks.get(blcks.size() - 1).getLocation();
	}
	
	@Override
	public List<String> executeTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		if(!PermissionsHelper.testPermissionSilent(sender, "GamingBlockPlug.tpc.use", true))
		{
			return Collections.emptyList();
		}
		
		if(args.length > 4)
		{
			return Collections.emptyList();
		}
		
		if(args.length == 1)
		{
			List<String> result = this.getPlayers(args[0]);
			if(sender instanceof Player)
			{
				int x = this.getBlockSight((Player)sender).getBlockX();
				String X = Integer.toString(x);
				if(X.startsWith(args[0]))
				{
					result.add(X);
				}
			}
			return result;
		}
		if(sender instanceof Player)
		{
			List<String> result = new ArrayList<String>();
			Player pl = (Player)sender;
			String x = Integer.toString(this.getBlockSight(pl).getBlockX());
			String y = Integer.toString(this.getBlockSight(pl).getBlockY());
			String z = Integer.toString(this.getBlockSight(pl).getBlockZ());
			if(args.length == 2) // X || Y
			{
				if(x.startsWith(args[1]))
				{
					result.add(x);
				}
				if(y.startsWith(args[1]))
				{
					result.add(y);
				}
			}
			if(args.length == 3) // Y || Z
			{
				if(z.startsWith(args[2]))
				{
					result.add(z);
				}
				if(y.startsWith(args[2]))
				{
					result.add(y);
				}
			}
			if(args.length == 4) // Z
			{
				if(z.startsWith(args[3]))
				{
					result.add(z);
				}
			}
		}
		else
		{
			List<String> result = new ArrayList<String>();
			result.add("0");
			return result;
		}
		return super.executeTabComplete(sender, command, alias, args);
	}
}