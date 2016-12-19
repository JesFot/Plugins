package fr.jesfot.gbp.discord.commands;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import fr.jesfot.gbp.RefString;
import fr.jesfot.gbp.discord.utils.Utils;

public class VersionCommand extends BaseCommand
{
	public VersionCommand()
	{
		super("version", "Show the bot version", "Show the bot version", "<cmd>");
	}

	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas)
	{
		String msg = "";
		msg += sender.mention(true);
		msg += "\n" + RefString.BOT_NAME + " ";
		msg += "version " + RefString.BOT_VERSION + " ";
		msg += "by " + RefString.BOT_AUTHOR;
		Utils.sendSafeMessages(channel, msg);
		Utils.deleteSafeMessages(datas);
		return true;
	}
}
