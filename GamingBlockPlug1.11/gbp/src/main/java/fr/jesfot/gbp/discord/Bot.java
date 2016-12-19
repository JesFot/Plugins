package fr.jesfot.gbp.discord;

import fr.jesfot.gbp.GamingBlockPlug_1_11;
import fr.jesfot.gbp.RefString;
import fr.jesfot.gbp.discord.commands.CommandHandler;
import fr.jesfot.gbp.discord.commands.VersionCommand;
import fr.jesfot.gbp.discord.utils.Utils;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

public class Bot implements Runnable
{
	private IDiscordClient client;
	
	private CommandHandler commandHandler;
	
	private GamingBlockPlug_1_11 gbp;
	private String discord_token;
	
	public Bot(GamingBlockPlug_1_11 plugin)
	{
		this.gbp = plugin;
	}

	public void run()
	{
		this.gbp.getConfigs().getConfig("bot_discord").reloadConfig();
		this.discord_token = this.gbp.getConfigs().getConfig("bot_discord").getConfig().getString("token", "%%%NULL");
		if(this.discord_token.startsWith("%%%"))
		{
			return;
		}
		try
		{
			this.client = Utils.getClient(this.discord_token, RefString.BOT_LOGIN_ON_START);
		}
		catch(DiscordException ex)
		{
			ex.printStackTrace();
		}
		
		this.commandHandler = new CommandHandler();
		
		this.commandHandler.addCommand(new VersionCommand());
	}
	
	public IDiscordClient getClient()
	{
		return this.client;
	}
	
	public CommandHandler getCommandHandler()
	{
		return this.commandHandler;
	}
	
	public void setProperty(String key, String value)
	{
		this.gbp.getConfigs().getConfig("bot_discord").reloadConfig();
		this.gbp.getConfigs().getConfig("bot_discord").getConfig().set(key, value);
		this.gbp.getConfigs().getConfig("bot_discord").saveConfig();
	}
}