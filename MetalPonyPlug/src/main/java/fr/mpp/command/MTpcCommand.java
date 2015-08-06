package fr.mpp.command;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import fr.mpp.utils.MPlayer;

public class MTpcCommand implements CommandExecutor
{
    static final int MAX_COORD = 30000000;
    static final int MIN_COORD_MINUS_ONE = -30000001;
    static final int MIN_COORD = -30000000;
	private String usageMessage;
	
	public MTpcCommand()
	{
        this.usageMessage = "/mtpc [player] <x> <y> <z>";
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
        if(args.length < 3 || args.length > 4)
        {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
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
                sender.sendMessage("Please provide a player!");
                return true;
            }
        }
        else
        {
            player = MPlayer.getPlayerByName(args[0]);
        }
        if(player == null)
        {
            sender.sendMessage("Player not found: " + args[0]);
            return true;
        }
        
        if(player.getWorld() != null)
        {
            Location playerLocation = player.getLocation();
            double x = getCoordinate(sender, playerLocation.getX(), args[args.length - 3]);
            double y = getCoordinate(sender, playerLocation.getY(), args[args.length - 2], 0, 0);
            double z = getCoordinate(sender, playerLocation.getZ(), args[args.length - 1]);

            if(x == MIN_COORD_MINUS_ONE || y == MIN_COORD_MINUS_ONE || z == MIN_COORD_MINUS_ONE)
            {
                sender.sendMessage("Please provide a valid location!");
                return true;
            }

            playerLocation.setX(x);
            playerLocation.setY(y);
            playerLocation.setZ(z);

            player.teleport(playerLocation, TeleportCause.COMMAND);
            Command.broadcastCommandMessage(sender, String.format("Teleported %s to %.2f, %.2f, %.2f", player.getDisplayName(), x, y, z));
        }
		return true;
	}
	
	private double getCoordinate(CommandSender sender, double current, String input)
	{
        return getCoordinate(sender, current, input, MIN_COORD, MAX_COORD);
    }

    private double getCoordinate(CommandSender sender, double current, String input, int min, int max)
    {
        boolean relative = input.startsWith("~");
        double result = relative ? current : 0;

        if(!relative || input.length() > 1)
        {
            boolean exact = input.contains(".");
            if (relative) input = input.substring(1);

            double testResult = getDouble(sender, input);
            if(testResult == MIN_COORD_MINUS_ONE)
            {
                return MIN_COORD_MINUS_ONE;
            }
            result += testResult;

            if (!exact && !relative) result += 0.5f;
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
    
    private static double getDouble(CommandSender sender, String input)
    {
        try
        {
            return Double.parseDouble(input);
        }
        catch(NumberFormatException ex)
        {
            return MIN_COORD_MINUS_ONE;
        }
    }
}