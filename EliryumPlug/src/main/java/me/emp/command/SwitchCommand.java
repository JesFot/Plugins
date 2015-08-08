package me.emp.command;

import java.util.ArrayList;
import java.util.List;

import me.emp.EliryumPlug;
import me.emp.perms.EPermissions;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

public class SwitchCommand implements CommandExecutor, TabCompleter
{
	private String usageMessage = ChatColor.RED + "/switch <plugin> <on|off>";
	private EliryumPlug emp;
	
	public SwitchCommand(EliryumPlug mpp)
	{
		this.emp = mpp;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!EPermissions.testPermission(sender, "Eliryum.switch", null))
		{
			return true;
		}
		if(args.length != 2)
		{
			sender.sendMessage(this.usageMessage);
			return true;
		}
		Plugin[] plugins = this.emp.getServer().getPluginManager().getPlugins();
		String pluginName = args[0];
		String act = args[1];
		boolean toOn = (act.equalsIgnoreCase("on"));
		if(pluginName.equalsIgnoreCase(this.emp.getPlugin().getDescription().getName()))
		{
			sender.sendMessage(ChatColor.RED + "You can't unload this plugin because you wont be able to re-activate it.");
			return true;
		}
		if(toOn)
		{
			for (Plugin pl : plugins)
			{
				if (pl.getName().equalsIgnoreCase(pluginName))
				{
					if (!pl.isEnabled())
					{
						this.emp.getServer().getPluginManager().enablePlugin(pl);
						this.emp.broad("Plugin "+pluginName+" Enabled.");
						this.emp.getLogger().info("Plugin "+pluginName+" has been Enabled.");
						return true;
					}
				}
			}
		}
		else
		{
			for (Plugin pl : plugins)
			{
				if (pl.getName().equalsIgnoreCase(pluginName))
				{
					if (pl.isEnabled())
					{
						this.emp.getServer().getPluginManager().disablePlugin(pl);
						this.emp.broad("Plugin "+pluginName+" Disabled.");
						this.emp.getLogger().info("Plugin "+pluginName+" has been disabled.");
						return true;
					}
				}
			}
		}
		sender.sendMessage("Please provide a valid plugin name.");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		if(!EPermissions.testPermission(sender, "Eliryum.switch", null))
		{
			return null;
		}
		if(args.length == 1)
		{
			List<String> plugs = new ArrayList<String>();
			for(Plugin pl : this.emp.getServer().getPluginManager().getPlugins())
			{
				if(pl.getName().startsWith(args[0]) && !pl.getName().equalsIgnoreCase(this.emp.getPlugin().getDescription().getName()))
				{
					plugs.add(pl.getName());
				}
			}
			return plugs;
		}
		else if(args.length == 2)
		{
			List<String> plugs = new ArrayList<String>();
			plugs.add("on");
			plugs.add("off");
			return plugs;
		}
		return new ArrayList<String>();
	}

}
