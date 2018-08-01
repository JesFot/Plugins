package fr.gbp.command;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.gbp.GamingBlockPlug;
import fr.gbp.perms.GPermissions;

public class GMaskCommand implements CommandExecutor, TabCompleter
{
	private GamingBlockPlug gbp;
	
	public GMaskCommand(GamingBlockPlug p_gbp)
	{
		this.gbp = p_gbp;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage("[GamingBlockPlug] Be a player is important for this command.");
			return true;
		}
		if(!GPermissions.testPermission(sender, "GamingBlockPlug.mask.use", null, false))
		{
			return true;
		}
		Player player = (Player)sender;
		boolean un = false;
		this.gbp.getConfig().reloadCustomConfig();
		if(this.gbp.getConfig().getCustomConfig().contains("hidenplayers."+player.getName().toLowerCase()))
		{
			un = this.gbp.getConfig().getCustomConfig().getBoolean("hidenplayers."+player.getName().toLowerCase(), false);
		}
		if(!un)
		{
			this.gbp.getConfig().getCustomConfig().set("hidenplayers."+player.getName().toLowerCase(), true);
			for(Player p : player.getServer().getOnlinePlayers())
			{
				if(GPermissions.testPermissionSilent(p, "GamingBlockPlug.mask.seeAll", false) && !player.isOp())
				{
					continue;
				}
				if(GPermissions.testPermissionSilent(p, "GamingBlockPlug.mask.seeAdmin", false) && player.isOp())
				{
					continue;
				}
				p.hidePlayer(player);
			}
			this.gbp.broad(ChatColor.YELLOW + player.getName() + " left the game");
		}
		else
		{
			this.gbp.getConfig().getCustomConfig().set("hidenplayers."+player.getName().toLowerCase(), false);
			for(Player p : player.getServer().getOnlinePlayers())
			{
				p.showPlayer(player);
			}
			this.gbp.broad(ChatColor.YELLOW + player.getName() + " joined the game");
		}
		this.gbp.getConfig().saveCustomConfig();
		return true;
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
	{
		return Collections.emptyList();
	}
}