package fr.mpp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.mpp.MetalPonyPlug;

public class MppOffCommand implements CommandExecutor
{
	private MetalPonyPlug mpp;
	
	public MppOffCommand(MetalPonyPlug mpp)
	{
		this.mpp = mpp;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		this.mpp.getPlugin();
		return true;
	}

}
