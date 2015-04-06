package fr.mpp.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mpp.MetalPonyPlug;
import fr.mpp.config.MConfig;

public class MppChestCommand implements CommandExecutor
{
	private MetalPonyPlug mpp;
	
	public MppChestCommand(MetalPonyPlug mppl)
	{
		this.mpp = mppl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player)sender;
			Location loc = player.getEyeLocation().getDirection().toBlockVector().toLocation(player.getWorld());
			MConfig conf = this.mpp.getConfig();
			conf.storeLoc("mpp.origchest.location", loc);
		}
		return false;
	}
}
