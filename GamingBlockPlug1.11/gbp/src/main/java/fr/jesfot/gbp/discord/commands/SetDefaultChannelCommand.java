package fr.jesfot.gbp.discord.commands;

import fr.jesfot.gbp.discord.Bot;
import fr.jesfot.gbp.discord.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class SetDefaultChannelCommand extends BaseCommand
{
	private Bot bot;
	
	public SetDefaultChannelCommand(Bot jb)
	{
		super("setchannel", "Sets the default text channel", "Define in wich channel the bot should display "
				+ "join/leave messages", "<cmd> [channelName]");
		this.registerCommand(jb.getCommandHandler());
		this.setMinimalPermission(Permissions.MANAGE_CHANNELS);
		this.bot = jb;
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas)
	{
		if(Utils.hasPermissionSomewhere(sender, channel, Permissions.MANAGE_CHANNELS))
		{
			if(this.getArguments().size() == 1)
			{
				Utils.sendSafeMessages(channel, sender.mention(true) + " Setted the channel " + this.getArguments().get(0) + " as default one.");
				this.bot.setProperty("channel.default." + datas.getGuild().getID(), datas.getGuild().getChannelsByName(this.getArguments().get(0)).get(0).getID());
			}
			else
			{
				Utils.sendSafeMessages(channel, sender.mention(true) + " Setted this channel as default one.");
				this.bot.setProperty("channel.default." + datas.getGuild().getID(), channel.getID());
			}
			Utils.deleteSafeMessages(datas);
		}
		return true;
	}
}