package fr.jesfot.gbp.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

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
	
	public final List<String> getPlayers(final String startOfName)
	{
		String name = startOfName.toLowerCase();
		List<String> pls = new ArrayList<String>();
		for(Player player : Bukkit.getOnlinePlayers())
		{
			String pl = player.getName().toLowerCase();
			if(pl.startsWith(name))
			{
				pls.add(player.getName());
			}
		}
		return pls;
	}
}