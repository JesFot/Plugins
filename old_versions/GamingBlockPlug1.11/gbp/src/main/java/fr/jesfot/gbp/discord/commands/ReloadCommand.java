package fr.jesfot.gbp.discord.commands;

import fr.jesfot.gbp.discord.Bot;
import fr.jesfot.gbp.discord.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class ReloadCommand extends BaseCommand
{
	private Bot bot;
	
	public static IChannel __channel = null;
	
	public ReloadCommand(Bot jb)
	{
		super("/reload", "Reload the bot", "Switch the bot off and then re-restart it", "<cmd>");
		this.registerCommand(jb.getCommandHandler());
		this.setMinimalPermission(Permissions.MANAGE_SERVER);
		this.bot = jb;
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas)
	{
		if(Utils.hasPermissionSomewhere(sender, channel, Permissions.MANAGE_SERVER))
		{
			Utils.deleteSafeMessages(datas);
			Utils.safeLogout(this.bot.getClient());
			try
			{
				this.bot.getClient().login();
				__channel = channel;
			}
			catch(Exception e)
			{
				try
				{
					Utils.sendSafeMessages(this.bot.getClient().getOrCreatePMChannel(sender), "I think you made me crash");
				}
				catch(Exception e1)
				{
					e1.printStackTrace();
				}
			}
		}
		return true;
	}
}