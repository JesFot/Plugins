package fr.jesfot.gbp.discord;

import java.util.concurrent.atomic.AtomicBoolean;

import fr.jesfot.gbp.GamingBlockPlug_1_11;
import fr.jesfot.gbp.RefString;
import fr.jesfot.gbp.discord.commands.CommandHandler;
import fr.jesfot.gbp.discord.commands.VersionCommand;
import fr.jesfot.gbp.discord.utils.Utils;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RateLimitException;

public class Bot implements Runnable
{
	private IDiscordClient client;
	
	private CommandHandler commandHandler;
	
	private GamingBlockPlug_1_11 gbp;
	private String discord_token;
	
	private AtomicBoolean started;
	
	public Bot(GamingBlockPlug_1_11 plugin)
	{
		this.started = new AtomicBoolean(false);
		this.gbp = plugin;
	}

	public void run()
	{
		this.started.set(true);
		this.gbp.getConfigs().getConfig("bot_discord").reloadConfig();
		this.discord_token = this.gbp.getConfigs().getConfig("bot_discord").getConfig().getString("token", "%%%NULL");
		this.gbp.getConfigs().getConfig("bot_discord").saveConfig();
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
	
	public void stop()
	{
		this.started.set(false);
		try
		{
			this.client.logout();
		}
		catch(DiscordException ex)
		{
			ex.printStackTrace();
		}
		catch(RateLimitException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public boolean isRunning()
	{
		return this.started.get();
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