package fr.gbp.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import fr.gbp.GamingBlockPlug;
import fr.gbp.perms.GPermissions;

public class GReloadCommand implements CommandExecutor
{
	private GamingBlockPlug gbp;
	
	public GReloadCommand(GamingBlockPlug p_gbp)
	{
		this.gbp = p_gbp;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!cmd.getName().equalsIgnoreCase("reload"))
		{
			return false;
		}
		if(!GPermissions.testPermission(sender, "bukkit.command.reload", null, true))
		{
			return false;
		}
		int time = 5;
		if(args.length >= 1)
		{
			time = Integer.parseInt(args[0]);
		}
		this.gbp.broad(ChatColor.DARK_RED + "RELOAD will start in "+time+" seconds.");
		(new BukkitRunnable()
		{
			protected int count = 0;
			public BukkitRunnable setVal(int p_timer)
			{
				this.count = p_timer;
				return this;
			}
			
			@Override
			public void run()
			{
				Bukkit.broadcastMessage("Reload in "+count+" seconds.");
				count--;
				if(count == 0)
				{
					Bukkit.broadcastMessage("Reload start, please donnot chat.");
					Bukkit.reload();
					Bukkit.broadcastMessage(ChatColor.GREEN + "Reload complete.");
					this.cancel();
				}
			}
		}).setVal(time).runTaskTimer(this.gbp.getPlugin(), 0, 20);
		return true;
	}
}