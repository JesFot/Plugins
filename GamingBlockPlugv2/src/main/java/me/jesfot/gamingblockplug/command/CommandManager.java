package me.jesfot.gamingblockplug.command;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;

import me.jesfot.gamingblockplug.permission.StaticPerms;

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
			try
			{
				plugin.getLogger().info("Starting registering " + cmd.getName() + " command...");
				CommandManager.registerServerCommand(plugin, cmd);
			}
			catch(NullPointerException e)
			{
				e.printStackTrace();
			}
		}
		plugin.getLogger().info(() -> "Succesfuly loaded " + commands.size() + " commands.");
		commandsLoaded = true;
	}
	
	protected static void registerServerCommand(JavaPlugin plugin, CommandBase command)
	{
		Validate.notNull(command, "That command cannot be null");
		plugin.getCommand(command.getName()).setExecutor(command);
		plugin.getCommand(command.getName()).setTabCompleter(command);
		/*if (command.getMinimalPermission() != null)
		{
			plugin.getCommand(command.getName()).setPermission(command.getMinimalPermission().getName());
		}*/
	}
	
	protected static void unregisterServerCommand(JavaPlugin plugin, CommandBase command)
	{
		Validate.notNull(command, "That command cannot be null");
		plugin.getLogger().info("Unregistering " + command.getName() + " command...");
		plugin.getCommand(command.getName()).setPermission(StaticPerms.IMPOSSIBLE.getName());
	}
	
	public static void onPluginStopped(JavaPlugin plugin)
	{
		commandsLoaded = false;
		for(CommandBase cmd : commands)
		{
			try
			{
				unregisterServerCommand(plugin, cmd);
			}
			catch(NullPointerException e)
			{
				e.printStackTrace();
			}
		}
		plugin.getLogger().info(() -> String.format("Succesfuly unregistred %d commands.", commands.size()));
		commands.clear();
	}
	
	public static boolean areCommandsLoaded()
	{
		return commandsLoaded;
	}
	
	private CommandManager() { /* Nothing here */ }
}