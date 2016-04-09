package fr.jesfot.gbp.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.command.helpers.EcoHelper;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.permission.PermissionsHelper;

public class GEcoCommand extends CommandBase
{
	private GamingBlockPlug_1_9 gbp;
	private EcoHelper ecoh;
	
	public GEcoCommand(GamingBlockPlug_1_9 plugin)
	{
		super("economy", "toolb");
		this.gbp = plugin;
		Permission eco = plugin.getPermissionManager().addPermission("GamingBlockPlug.economy", PermissionDefault.OP, "Economy's permission", Permissions.globalGBP);
		plugin.getPermissionManager().addPermission("GamingBlockPlug.economy.reset", PermissionDefault.OP, "Allows you to reset an account", eco);
		plugin.getPermissionManager().addPermission("GamingBlockPlug.economy.see", PermissionDefault.OP, "Allows you to see a balance", eco);
		plugin.getPermissionManager().addPermission("GamingBlockPlug.economy.passEm", PermissionDefault.TRUE, "Allows you to change Em in money", eco);
		this.ecoh = new EcoHelper(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		boolean isPlayer = (sender instanceof Player);
		if(command.getName().equalsIgnoreCase("toolb"))
		{
			if(!isPlayer)
			{
				sender.sendMessage(this.gbp.getLang().get("console.noinv"));
				return false;
			}
			Player player = (Player)sender;
			player.getInventory().addItem(new ItemStack(Material.DEAD_BUSH));
			return true;
		}
		else if(command.getName().equalsIgnoreCase("economy"))
		{
			if(args.length == 0)
			{
				return this.ecoh.view(sender, command, label, args, isPlayer);
			}
			if(args[0].equalsIgnoreCase("pay"))
			{
				this.ecoh.pay(sender, command, label, args, isPlayer);
				return true;
			}
			if(args[0].equalsIgnoreCase("inventory"))
			{
				return this.ecoh.inventory(sender, command, label, args, isPlayer);
			}
			if(args[0].equalsIgnoreCase("reset"))
			{
				return this.ecoh.reset(sender, command, label, args);
			}
			if(args[0].equalsIgnoreCase("see"))
			{
				return this.ecoh.see(sender, command, label, args);
			}
			if(args[0].equalsIgnoreCase("takeEm") || args[0].equalsIgnoreCase("te"))
			{
				return this.ecoh.takeEm(sender, command, label, args, isPlayer);
			}
			if(args[0].equalsIgnoreCase("storeEm") || args[0].equalsIgnoreCase("se"))
			{
				return this.ecoh.storeEm(sender, command, label, args, isPlayer);
			}
			sender.sendMessage(ChatColor.DARK_RED + this.ecoh.usage(sender, command, label, args));
			return true;
		}
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
	{
		List<String> result = new ArrayList<String>();
		List<String> allP = new ArrayList<String>();
		for(Player tmp : this.gbp.getOnlinePlayers())
		{
			allP.add(tmp.getName());
		}
		if(cmd.getName().equalsIgnoreCase("toolb"))
		{
			return Collections.emptyList();
		}
		else if(cmd.getName().equalsIgnoreCase("economy"))
		{
			switch(args.length)
			{
			case 1:
				result.add("pay");
				result.add("inventory");
				result.add("TakeEm");
				result.add("StoreEm");
				if(PermissionsHelper.testPermissionSilent(sender, "GamingBlockPlug.economy.reset", false))
				{
					result.add("reset");
				}
				if(PermissionsHelper.testPermissionSilent(sender, "GamingBlockPlug.economy.see", true))
				{
					result.add("see");
				}
				break;
			case 2:
				if(args[0].equalsIgnoreCase("pay"))
				{
					result.addAll(allP);
					result.add("console");
				}
				else if(PermissionsHelper.testPermissionSilent(sender, "GamingBlockPlug.economy.reset", false) 
						&& args[0].equalsIgnoreCase("reset"))
				{
					result.addAll(allP);
				}
				else if(PermissionsHelper.testPermissionSilent(sender, "GamingBlockPlug.economy.see", true) 
						&& args[0].equalsIgnoreCase("see"))
				{
					result.addAll(allP);
					result.addAll(this.gbp.getEconomy().getList(allP));
				}
				break;
			}
			List<String> response = new ArrayList<String>();
			for(String res : result)
			{
				if(res.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
				{
					response.add(res);
				}
			}
			return response;
		}
		return null;
	}
}