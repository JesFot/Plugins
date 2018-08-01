package fr.jesfot.gbp.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.utils.Utils;

public class GPluginListener implements Listener
{
	private GamingBlockPlug_1_9 gbp;
	
	public GPluginListener(GamingBlockPlug_1_9 plugin)
	{
		this.gbp = plugin;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginEnable(final PluginEnableEvent event)
	{
		// Code ...
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginDisable(final PluginDisableEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onServerCommand(final ServerCommandEvent event)
	{
		// Code ...
		event.setCommand(event.getCommand().replace("<bidule>", "Truc"));
	}
	
	@EventHandler
	public void onRconCommand(final RemoteServerCommandEvent event)
	{
		String msg = "";
		if(event.getCommand().contains("${") && event.getCommand().contains("}"))
		{
			for(String arg : event.getCommand().split("\\$"))
			{
				if(arg.startsWith("{") && arg.contains("}"))
				{
					String var = arg.substring(1, arg.indexOf("}"));
					arg = this.gbp.getVars().getToString(var)+((arg.endsWith("}") && arg.indexOf("}")==arg.length()-1)
							? "" : arg.substring(arg.indexOf("}") + 1));
				}
				msg += arg + "";
			}
			event.setCommand(ChatColor.translateAlternateColorCodes('&', msg));
		}
	}
	
	@EventHandler
	public void onCommandBlockActivated(final BlockRedstoneEvent event)
	{
		if(event.getBlock().getType().equals(Material.COMMAND))
		{
			if(event.getNewCurrent() != 0 && event.getOldCurrent() == 0)
			{
				Block bloc = event.getBlock();
				final CommandBlock cmd = (CommandBlock)bloc.getState();
				final String command = cmd.getCommand();
				BlockCommandSender sender = Utils.getBlockCommandSender(cmd);
				String msg = "";
				if(command.contains("${") && command.contains("}"))
				{
					for(String arg : command.split("\\$"))
					{
						if(arg.startsWith("{") && arg.contains("}"))
						{
							String var = arg.substring(1, arg.indexOf("}"));
							arg = this.gbp.getVars().getToString(var)+((arg.endsWith("}") && arg.indexOf("}")==arg.length()-1)
									? "" : arg.substring(arg.indexOf("}") + 1));
						}
						msg += arg + "";
					}
				}
				else
				{
					return;
				}
				if(!Bukkit.dispatchCommand(sender, ChatColor.translateAlternateColorCodes('&', msg)))
				{
					this.gbp.broadAdmins("Error while executing '" + msg + "' command (" + sender.getName() + "; "
							+ sender.getBlock().getX() + ", " + sender.getBlock().getY() + ", " + sender.getBlock().getZ()
							+ "; " + sender.getBlock().getWorld().getName() + ")");
				}
				event.setNewCurrent(0);
			}
		}
	}
	
	@EventHandler
	public void onPlayerCommand(final PlayerCommandPreprocessEvent event)
	{
		String msg = "";
		if(event.getMessage().contains("${") && event.getMessage().contains("}") && event.getPlayer().isOp())
		{
			for(String arg : event.getMessage().split("\\$"))
			{
				if(arg.startsWith("{") && arg.contains("}"))
				{
					String var = arg.substring(1, arg.indexOf("}"));
					arg = this.gbp.getVars().getToString(var)+((arg.endsWith("}") && arg.indexOf("}")==arg.length()-1)
							? "" : arg.substring(arg.indexOf("}") + 1));
				}
				msg += arg + "";
			}
			event.setMessage(ChatColor.translateAlternateColorCodes('&', msg));
		}
	}
}