package fr.gbp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.gbp.GamingBlockPlug;
import fr.gbp.perms.GPermissions;
import fr.gbp.utils.UPlayer;

public class GGivePermsCommand implements CommandExecutor
{
	private GamingBlockPlug gbp;
	
	public GGivePermsCommand(GamingBlockPlug p_gbp)
	{
		this.gbp = p_gbp;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args)
	{
		if(!cmd.getName().equalsIgnoreCase("givePerm"))
		{
			return false;
		}
		
		if(!GPermissions.testPermission(sender, "GamingBlockPlug.giveperms", null))
		{
			return true;
		}
		
		if(args.length < 1 || args.length > 2)
		{
			sender.sendMessage(ChatColor.RED + "Usage: /givePerm [player] <[-]permission>");
			return true;
		}
		
		if(args.length == 1)
		{
			String perm = args[0];
			boolean minus = perm.startsWith("-");
			if(minus)
			{
				perm.substring(1);
			}
			if(sender instanceof Player)
			{
				Player player = (Player)sender;
				this.gbp.getPermissions().setPerm(player, this.gbp.getPerms().getPerm(perm), !minus);
			}
			else
			{
				sender.sendMessage("Please provide a player or be a player.");
			}
		}
		else
		{
			String perm = args[1];
			boolean minus = perm.startsWith("-");
			if(minus)
			{
				perm.substring(1);
			}
			Player player = UPlayer.getPlayerByName(args[0]);
			if(player == null)
			{
				sender.sendMessage("Please provide an existing player.");
				return true;
			}
			if(this.gbp.getPerms().getPerm(perm) == null)
			{
				sender.sendMessage("Please verify this permission.");
				return true;
			}
			this.gbp.getPermissions().setPerm(player, this.gbp.getPerms().getPerm(perm), !minus);
		}
		
		return true;
	}
}