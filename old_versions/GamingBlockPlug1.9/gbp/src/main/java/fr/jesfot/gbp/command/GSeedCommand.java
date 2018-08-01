package fr.jesfot.gbp.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GSeedCommand extends CommandBase
{
	public GSeedCommand()
	{
		super("seed");
		this.setRawUsageMessage("/<com>");
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
	{
		long seed;
		if(sender instanceof Player)
		{
			seed = ((Player)sender).getWorld().getSeed();
		}
		else
		{
			seed = Bukkit.getWorlds().get(0).getSeed();
		}
		sender.sendMessage("Seed: " + seed + "; String: " + Long.toString(seed, 36));
		return true;
	}
}