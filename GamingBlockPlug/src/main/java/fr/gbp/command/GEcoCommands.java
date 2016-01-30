package fr.gbp.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.gbp.GamingBlockPlug;
import fr.gbp.command.helpers.EcoHelper;
import fr.gbp.perms.GPermissions;
import fr.gbp.utils.UPlayer;

public class GEcoCommands implements CommandExecutor, TabCompleter
{
	/*
	 * Commands :
	 *  -/addme : add to eco system
	 *  -/account
	 *  -/toolb : give deadBush
	 *  -/bank
	 *  -/pay
	 */
	private GamingBlockPlug gbp;
	private EcoHelper ecoh;
	
	public GEcoCommands(GamingBlockPlug plugin)
	{
		this.gbp = plugin;
		this.ecoh = new EcoHelper(this.gbp);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		boolean isPlayer = (sender instanceof Player);
		if(cmd.getName().equalsIgnoreCase("toolb"))
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
		else if(cmd.getName().equalsIgnoreCase("economy"))
		{
			return ecoh.economy(sender, cmd, label, args);
		}
		
		
		sender.sendMessage("what ?");
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
	{
		List<String> result = new ArrayList<String>();
		List<String> allP = new ArrayList<String>();
		for(Player tmp : UPlayer.getOnlinePlayers())
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
				if(GPermissions.testPermissionSilent(sender, "GamingBlockPlug.economy.reset", false))
				{
					result.add("reset");
				}
				if(GPermissions.testPermissionSilent(sender, "GamingBlockPlug.economy.see", true))
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
				else if(GPermissions.testPermissionSilent(sender, "GamingBlockPlug.economy.reset", false) 
						&& args[0].equalsIgnoreCase("reset"))
				{
					result.addAll(allP);
				}
				else if(GPermissions.testPermissionSilent(sender, "GamingBlockPlug.economy.see", true) 
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