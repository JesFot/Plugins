package fr.gbp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.gbp.GamingBlockPlug;

public class GLangCommand implements CommandExecutor
{
	private GamingBlockPlug gbp;
	
	public GLangCommand(GamingBlockPlug p_gbp)
	{
		this.gbp = p_gbp;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender.isOp())
		{
			if(args.length == 1)
			{
				int id = Integer.parseInt(args[0]);
				this.gbp.getConfig().reloadCustomConfig();
				this.gbp.getConfig().getCustomConfig().set("lang", id);
				this.gbp.getConfig().saveCustomConfig();
				this.gbp.getConfig().reloadCustomConfig();
				return true;
			}
			return false;
		}
		return false;
	}
}