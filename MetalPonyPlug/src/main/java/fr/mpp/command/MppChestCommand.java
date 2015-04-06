package fr.mpp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mpp.MetalPonyPlug;
import fr.mpp.listener.MppChestComListener;

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
			player.sendMessage("Do a left-click on the chest you want to assign.");
			mpp.getServer().getPluginManager().registerEvents(new MppChestComListener(player, this.mpp), mpp.getPlugin());
		}
		return true;
	}
}
