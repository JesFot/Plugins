package fr.jesfot.gbp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.jesfot.gbp.GamingBlockPlug_1_9;

public class GShopCommand extends CommandBase
{
	private GamingBlockPlug_1_9 gbp;
	
	public GShopCommand(GamingBlockPlug_1_9 plugin)
	{
		super("shop");
		this.gbp = plugin;
		this.setRawUsageMessage("/<com> list");
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 1)
		{
			if(args[0].equalsIgnoreCase("list"))
			{
				sender.sendMessage(color(this.gbp.getLang().get("shop.list", "There are &6<amount>&r "
						+ "shops registered on the server.").replaceAll("<amount>",
								"" + this.gbp.getShops().getNumberOfShops())));
				return true;
			}
		}
		this.sendUsage(sender, label);
		return true;
	}
}