package me.emp.command;

import me.emp.EliryumPlug;
import me.emp.perms.EPermissions;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MMaskCommand implements CommandExecutor
{
	private EliryumPlug emp;
	
	public MMaskCommand(EliryumPlug p_emp)
	{
		this.emp = p_emp;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage("[EliryumPlug] Be a player is important for this command.");
			return true;
		}
		if(!EPermissions.testPermission(sender, "Eliryum.mask.use", null))
		{
			return true;
		}
		Player player = (Player)sender;
		boolean un = false;
		emp.getConfig().reloadCustomConfig();
		if(emp.getConfig().getCustomConfig().contains("hidenplayers."+player.getName().toLowerCase()))
		{
			un = emp.getConfig().getCustomConfig().getBoolean("hidenplayers."+player.getName().toLowerCase(), false);
		}
		if(!un)
		{
			emp.getConfig().getCustomConfig().set("hidenplayers."+player.getName().toLowerCase(), true);
			for(Player p : player.getServer().getOnlinePlayers())
			{
				if(EPermissions.testPermissionSilent(p, "Eliryum.mask.seeAll") && !player.isOp())
				{
					continue;
				}
				if(EPermissions.testPermissionSilent(p, "Eliryum.mask.seeAdmin") && player.isOp())
				{
					continue;
				}
				p.hidePlayer(player);
			}
			this.emp.broad(ChatColor.GRAY+"["+ChatColor.RED+"-"+ChatColor.GRAY+"] "+ChatColor.RED+player.getName());
		}
		else
		{
			emp.getConfig().getCustomConfig().set("hidenplayers."+player.getName().toLowerCase(), false);
			for(Player p : player.getServer().getOnlinePlayers())
			{
				p.showPlayer(player);
			}
			this.emp.broad(ChatColor.GRAY+"["+ChatColor.GREEN+"+"+ChatColor.GRAY+"] "+ChatColor.GREEN+player.getName());
		}
		emp.getConfig().saveCustomConfig();
		return true;
	}
}