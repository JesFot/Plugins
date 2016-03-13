package fr.jesfot.gbp.command;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public abstract class CommandBase implements CommandExecutor, TabCompleter
{
	private final String name;
	
	protected CommandBase(final String cmdBaseName)
	{
		this.name = cmdBaseName;
	}
	
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return Collections.emptyList();
	}

	public abstract boolean onCommand(CommandSender sender, Command command, String label, String[] args);
	
	public final String getName()
	{
		return this.name;
	}
}