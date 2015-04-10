package fr.mpp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mpp.MetalPonyPlug;
import fr.mpp.listener.MppChestComListener;
import fr.mpp.perm.MPermissions;

public class MppChestCommand implements CommandExecutor
{
	private MetalPonyPlug mpp;
	private MPermissions mPerm;
	
	public MppChestCommand(MetalPonyPlug mppl)
	{
		this.mpp = mppl;
		this.mPerm = mppl.getPerm();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player)sender;
			if (mPerm.getPerm(player, "mpp.low.chest"))
			{
				player.sendMessage(ChatColor.RED + "You are not allowed to perform this command");
				return true;
			}
			player.sendMessage("Do a left-click on the chest you want to assign.");
			mpp.getServer().getPluginManager().registerEvents(new MppChestComListener(player, this.mpp), mpp.getPlugin());
		}
		return true;
	}
}
