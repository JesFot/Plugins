package me.jesfot.gamingblockplug.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;

import me.jesfot.gamingblockplug.permission.PermissionHelper;
import me.jesfot.gamingblockplug.permission.StaticPerms;
import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;

public class GPluginListener implements Listener
{
	private final GamingBlockPlug plugin;
	
	public GPluginListener(GamingBlockPlug plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onCommand(final ServerCommandEvent event)
	{
		StringBuilder msg = new StringBuilder();
		if(event.getCommand().contains("${") && event.getCommand().contains("}"))
		{
			this.plugin.getSystemManager().getVariablesSystem().loadFromFile();
			String[] splited = event.getCommand().split("\\$");
			for(String arg : splited)
			{
				if(arg.startsWith("{") && arg.contains("}"))
				{
					String var = arg.substring(1, arg.indexOf("}"));
					arg = this.plugin.getSystemManager().getVariablesSystem().getToString(var)
							+ ((arg.endsWith("}") && arg.indexOf("}") == arg.length() - 1)
									? "" : arg.substring(arg.indexOf("}") + 1));
				}
				msg.append(arg);
			}
			event.setCommand(msg.toString());
		}
	}
	
	@EventHandler
	public void onRconCommand(final RemoteServerCommandEvent event)
	{
		StringBuilder msg = new StringBuilder();
		if(event.getCommand().contains("${") && event.getCommand().contains("}"))
		{
			this.plugin.getSystemManager().getVariablesSystem().loadFromFile();
			String[] splited = event.getCommand().split("\\$");
			for(String arg : splited)
			{
				if(arg.startsWith("{") && arg.contains("}"))
				{
					String var = arg.substring(1, arg.indexOf("}"));
					arg = this.plugin.getSystemManager().getVariablesSystem().getToString(var)
							+ ((arg.endsWith("}") && arg.indexOf("}") == arg.length() - 1)
									? "" : arg.substring(arg.indexOf("}") + 1));
				}
				msg.append(arg);
			}
			event.setCommand(msg.toString());
		}
	}
	
	@EventHandler
	public void onPlayerCommand(final PlayerCommandPreprocessEvent event)
	{
		if (PermissionHelper.testPermissionSilent(event.getPlayer(), StaticPerms.VARS_CMD, false))
		{
			StringBuilder msg = new StringBuilder();
			if(event.getMessage().contains("${") && event.getMessage().contains("}"))
			{
				this.plugin.getSystemManager().getVariablesSystem().loadFromFile();
				String[] splited = event.getMessage().split("\\$");
				for(String arg : splited)
				{
					if(arg.startsWith("{") && arg.contains("}"))
					{
						String var = arg.substring(1, arg.indexOf("}"));
						arg = this.plugin.getSystemManager().getVariablesSystem().getToString(var)
								+ ((arg.endsWith("}") && arg.indexOf("}") == arg.length() - 1)
										? "" : arg.substring(arg.indexOf("}") + 1));
					}
					msg.append(arg);
				}
				event.setMessage(msg.toString());
			}
		}
	}
}
