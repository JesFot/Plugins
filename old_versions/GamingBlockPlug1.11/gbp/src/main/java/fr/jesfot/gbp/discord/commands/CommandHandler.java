package fr.jesfot.gbp.discord.commands;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.jesfot.gbp.RefString;
import sx.blah.discord.handle.obj.IMessage;

public class CommandHandler
{
	private Map<String, BaseCommand> commands;
	
	public CommandHandler()
	{
		this.commands = new HashMap<String, BaseCommand>();
	}
	
	public void addCommand(BaseCommand cmd)
	{
		this.commands.put(cmd.getName(), cmd);
	}
	
	public void removeCommand(String cmdName)
	{
		this.commands.remove(cmdName);
	}
	
	public boolean exists(String cmdName)
	{
		return this.commands.containsKey(cmdName);
	}
	
	public BaseCommand getCommand(String cmdName)
	{
		return this.commands.get(cmdName);
	}
	
	public boolean execute(IMessage command)
	{
		BaseCommand cmd = this.getCommand(this.getCommandName(command.getContent()));
		return cmd.onCommand(command.getAuthor(), command.getContent(), command.getChannel(), command);
	}
	
	public boolean isCommand(String msg)
	{
		if(msg.startsWith(RefString.BOT_COMMANDS) && msg.length() >= RefString.BOT_COMMANDS.length() + 1)
		{
			if(this.exists(this.getCommandName(msg)))
			{
				return true;
			}
		}
		return false;
	}
	
	public String getCommandName(String msg)
	{
		String tmp;
		tmp = msg.substring(RefString.BOT_COMMANDS.length());
		return tmp.split(" ")[0];
	}

	public Collection<BaseCommand> getList()
	{
		return this.commands.values();
	}

	public Set<String> getNames()
	{
		return this.commands.keySet();
	}
}