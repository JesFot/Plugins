package fr.jesfot.gbp.command;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.bukkit.plugin.java.JavaPlugin;

import fr.jesfot.gbp.permission.Permissions;

public class CommandManager
{
	private static final Collection<CommandBase> commands = new HashSet<CommandBase>();
	
	private static boolean commandsLoaded = false;
	
	public static void registerCommand(CommandBase cmd)
	{
		if(commandsLoaded)
		{
			throw new IllegalStateException("Tried to register command after Server starting");
		}
		commands.add(cmd);
	}
	
	public static void registerCommands(CommandBase...cmds)
	{
		if(commandsLoaded)
		{
			throw new IllegalStateException("Tried to register command after Server starting");
		}
		Collections.addAll(commands, cmds);
	}
	
	public static void loadCommands(JavaPlugin plugin)
	{
		if(commandsLoaded)
		{
			throw new IllegalStateException("Tried to register command after Server starting");
		}
		for(CommandBase cmd : commands)
		{
			CommandManager.registerServerCommand(plugin, cmd);
		}
		commandsLoaded = true;
	}
	
	protected static void registerServerCommand(JavaPlugin plugin, CommandBase command)
	{
		plugin.getCommand(command.getName()).setExecutor(command);
		plugin.getCommand(command.getName()).setTabCompleter(command);
	}
	
	protected static void unregisterServerCommand(JavaPlugin plugin, CommandBase command)
	{
		plugin.getCommand(command.getName()).setPermission(Permissions.impossiblePermission.getName());
	}
	
	public static void onPluginStopped(JavaPlugin plugin)
	{
		commandsLoaded = false;
		for(CommandBase cmd : commands)
		{
			unregisterServerCommand(plugin, cmd);
		}
		commands.clear();
	}
	
	public static boolean areCommandsLoaded()
	{
		return commandsLoaded;
	}
	
	private CommandManager(){};
}