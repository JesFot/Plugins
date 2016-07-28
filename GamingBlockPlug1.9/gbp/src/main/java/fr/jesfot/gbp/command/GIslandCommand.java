package fr.jesfot.gbp.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.permission.Permissions;
import fr.jesfot.gbp.permission.PermissionsHelper;
import fr.jesfot.gbp.zoning.island.IslandPrompt;

public class GIslandCommand extends CommandBase
{
	private GamingBlockPlug_1_9 gbp;
	private String usageMessage = ChatColor.RED +
			"/<com> [start]";
	
	public GIslandCommand(GamingBlockPlug_1_9 plugin)
	{
		super("island");
		this.gbp = plugin;
		Permission island = plugin.getPermissionManager().addPermission("GamingBlockPlug.island",
				PermissionDefault.OP, "Island's permission", Permissions.globalGBP);
		plugin.getPermissionManager().addPermission("GamingBlockPlug.island.see", PermissionDefault.TRUE,
				"Allows you to see the actual center of the island", island);
		plugin.getPermissionManager().addPermission("GamingBlockPlug.island.control", PermissionDefault.OP,
				"Allows you to control the island", island);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 0)
		{
			if(PermissionsHelper.testPermission(sender, "GamingBlockPlug.island.see", true, null))
			{
				sender.sendMessage(ChatColor.GREEN + "Actual location : " + ChatColor.AQUA + this.gbp.getIsland().getLoc()
						+ ChatColor.GREEN + ".");
			}
			return true;
		}
		if(args[0].equalsIgnoreCase("start"))
		{
			if(PermissionsHelper.testPermission(sender, "GamingBlockPlug.island.control", false, null))
			{
				boolean isP = false;
				if(sender instanceof Player)
				{
					isP = true;
				}
				ConversationFactory factory = new ConversationFactory(this.gbp.getPlugin());
				final Map<Object, Object> data = new HashMap<Object, Object>();
				data.put("nextAct", "");
				data.put("text", "Starting new session...");
				Conversation conv = factory.withFirstPrompt(new IslandPrompt(this.gbp)).withPrefix(new ConversationPrefix()
				{
					public String getPrefix(ConversationContext context)
					{
						return ChatColor.GREEN + "Island Control" + ChatColor.RESET + " ";
					}
				}).withInitialSessionData(data).withLocalEcho(false).withTimeout(60)
						.buildConversation(isP?(Player)sender:null);
				conv.begin();
				Command.broadcastCommandMessage(sender, "Start controling island", false);
			}
			return true;
		}
		sender.sendMessage(this.usageMessage.replaceAll("<com>", label));
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args)
	{
		List<String> result = new ArrayList<String>();
		
		if(args.length == 0)
		{
			return null;
		}
		if(args.length == 1)
		{
			if("start".toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
			{
				if(PermissionsHelper.testPermissionSilent(sender, "GamingBlockPlug.island.control", false))
				{
					result.add("start");
				}
			}
			result.add("");
		}
		
		return result;
	}
}