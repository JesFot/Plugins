package fr.mpp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mpp.MText.MLang;
import fr.mpp.MetalPonyPlug;

public class MLangCommand implements CommandExecutor
{
	private String usageMessage = ChatColor.RED + "Usage: /<command> <Lang>";
	private MetalPonyPlug mpp;
	
	public MLangCommand(MetalPonyPlug mppl)
	{
		this.mpp = mppl;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!cmd.getName().equalsIgnoreCase("language"))
		{
			return false;
		}
		this.usageMessage = this.usageMessage.replace("<command>", label);
		if(args.length != 1)
		{
			sender.sendMessage(this.usageMessage);
			return true;
		}
		String arg = args[0];
		if(arg.length() == 1)
		{
			int id = Integer.parseInt(arg);
			if(sender instanceof Player)
			{
				this.mpp.getConfig().reloadCustomConfig();
				if(!this.mpp.getConfig().getCustomConfig().contains("mppbase.lang."+((Player)sender).getName().toLowerCase()))
				{
					this.mpp.getConfig().getCustomConfig().createSection("mppbase.lang."+((Player)sender).getName().toLowerCase());
				}
				this.mpp.getConfig().getCustomConfig().set("mppbase.lang."+((Player)sender).getName().toLowerCase(), MLang.getByID(id).getID());
				this.mpp.getConfig().saveCustomConfig();
			}
			else
			{
				this.mpp.getLang().setLang(MLang.getByID(id));
			}
		}
		else
		{
			if(sender instanceof Player)
			{
				this.mpp.getConfig().reloadCustomConfig();
				if(!this.mpp.getConfig().getCustomConfig().contains("mppbase.lang."+((Player)sender).getName().toLowerCase()))
				{
					this.mpp.getConfig().getCustomConfig().createSection("mppbase.lang."+((Player)sender).getName().toLowerCase());
				}
				this.mpp.getConfig().getCustomConfig().set("mppbase.lang."+((Player)sender).getName().toLowerCase(), MLang.getByName(arg).getID());
				this.mpp.getConfig().saveCustomConfig();
			}
			else
			{
				this.mpp.getLang().setLang(MLang.getByName(arg));
			}
		}
		return true;
	}
}
