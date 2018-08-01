package fr.jesfot.gbp.discord.commands;

import fr.jesfot.gbp.discord.Bot;
import fr.jesfot.gbp.discord.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class SetJoinLeaveMsgCommand extends BaseCommand
{
	private Bot bot;
	
	public SetJoinLeaveMsgCommand(Bot jb)
	{
		super("setjlmsg", "Change join/leave message", "Reset the join or the leave server message on main channel",
				"<cmd> join|leave <msg...>");
		this.registerCommand(jb.getCommandHandler());
		this.setMinimalPermission(Permissions.MANAGE_CHANNELS);
		this.bot = jb;
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas)
	{
		if(Utils.hasPermissionSomewhere(sender, channel, Permissions.MANAGE_CHANNELS))
		{
			if(this.getArguments().size() >= 2)
			{
				String servID = datas.getGuild().getID();
				String act = this.getArguments().get(0);
				if(act.equalsIgnoreCase("join"))
				{
					String value = this.compileFrom(2);
					Utils.sendSafeMessages(channel, sender.mention() + " Setted join message to '" + value + "'.");
					this.bot.setProperty("channel.message.join." + servID, value);
					Utils.deleteSafeMessages(datas);
				}
				else if(act.equalsIgnoreCase("leave"))
				{
					String value = this.compileFrom(2);
					Utils.sendSafeMessages(channel, sender.mention() + " Setted leave message to '" + value + "'.");
					this.bot.setProperty("channel.message.leave." + servID, value);
					Utils.deleteSafeMessages(datas);
				}
			}
		}
		return true;
	}
}