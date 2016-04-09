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
	
	private final String[] othersCommad;
	
	protected CommandBase(final String cmdBaseName)
	{
		this.name = cmdBaseName;
		this.othersCommad = new String[]{};
	}
	
	protected CommandBase(final String cmdBaseName, String...othersCmds)
	{
		this.name = cmdBaseName;
		this.othersCommad = othersCmds;
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
	
	public final boolean hasOtherNames()
	{
		return this.othersCommad.length>0;
	}
	
	public final String[] getOthersNames()
	{
		return this.othersCommad;
	}
	
	public final String getOtherName(final int index)
	{
		return this.othersCommad[index];
	}
}