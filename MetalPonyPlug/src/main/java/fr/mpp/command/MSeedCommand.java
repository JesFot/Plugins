package fr.mpp.command;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

import fr.mpp.perm.MPermissions;

public class MSeedCommand implements CommandExecutor, TabCompleter
{

	public MSeedCommand()
	{
		// Code ...
    }

	@Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        return ImmutableList.of();
    }

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!MPermissions.testPermission(sender, "bukkit.command.seed", null)) return true;
        long seed;
        if (sender instanceof Player) {
            seed = ((Player) sender).getWorld().getSeed();
        } else {
            seed = Bukkit.getWorlds().get(0).getSeed();
        }
        sender.sendMessage("Seed: " + seed + "; String: " + Long.toString(seed, 36));
        return true;
	}
}