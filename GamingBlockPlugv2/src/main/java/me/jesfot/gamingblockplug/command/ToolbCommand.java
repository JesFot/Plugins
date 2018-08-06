package me.jesfot.gamingblockplug.command;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.jesfot.gamingblockplug.permission.StaticPerms;

/**
 * @author JësFot
 * @since 1.13-1.1.0
 * @version 1.0
 */
public class ToolbCommand extends CommandBase
{
	public ToolbCommand()
	{
		super("toolb");
		
		super.setMinimalPermission(StaticPerms.CMD_TOOLB);
		super.setRawUsageMessage("/<command>");
	}

	@Override
	protected boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			if (player.getInventory().contains(Material.DEAD_BUSH))
			{
				player.sendMessage("You already have this item in your inventory");
				return true;
			}
			Map<Integer, ItemStack> nulled = player.getInventory().addItem(new ItemStack(Material.DEAD_BUSH));
			if (!nulled.isEmpty())
			{
				player.getWorld().dropItem(player.getEyeLocation(), nulled.remove(Integer.valueOf(0)));
			}
		}
		else
		{
			sender.sendMessage("Only players can use this command, use /give instead.");
		}
		return true;
	}
	
	@Override
	protected List<String> executeTabComplete(CommandSender sender, Command command, String label, String[] args)
	{
		return Collections.emptyList();
	}
}
