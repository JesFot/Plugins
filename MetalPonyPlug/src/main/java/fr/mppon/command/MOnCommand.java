package fr.mppon.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import fr.mppon.MetalPonyPlug_on;

public class MOnCommand implements CommandExecutor
{
	private MetalPonyPlug_on mppon;
	
	public MOnCommand(MetalPonyPlug_on mpp)
	{
		this.mppon = mpp;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!cmd.getName().equalsIgnoreCase("mpp-on"))
		{
			return false;
		}
		Plugin[] plugins = this.mppon.getServer().getPluginManager().getPlugins();
		
		for (Plugin pl : plugins)
		{
			if (pl.getName().equalsIgnoreCase("metalponyplug"))
			{
				if (!pl.isEnabled())
				{
					this.mppon.getServer().getPluginManager().enablePlugin(pl);
				}
			}
		}
		return true;
	}

}
