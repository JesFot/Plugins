package fr.gbp.command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.entity.Player;

import fr.gbp.GamingBlockPlug;
import fr.gbp.command.helpers.TpaPrompt;
import fr.gbp.utils.UPlayer;

public class GTpaCommand implements CommandExecutor
{
	private String usageMessage;
	private GamingBlockPlug gbp;
	
	public GTpaCommand(GamingBlockPlug p_gbp)
	{
		this.gbp = p_gbp;
		this.usageMessage = "/<gtpa> target";
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		this.usageMessage = "/"+label+" target";
		if(!cmd.getName().equalsIgnoreCase("gtpa"))
		{
			return false;
		}
		if(args.length < 1 || args.length > 1)
		{
			sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
			return false;
		}
		
		Player player;
		
		if(args.length == 1)
		{
			if(sender instanceof Player)
			{
				player = (Player)sender;
			}
			else
			{
				sender.sendMessage("Please be a player !");
				return true;
			}
		}
		else
		{
			player = null;
		}
		
		if(player == null)
		{
			sender.sendMessage("Whut ?");
			return true;
		}
		
		Player target = UPlayer.getPlayerByName(args[args.length - 1]);
		if(target == null)
		{
			sender.sendMessage("Cannot find player " + args[args.length - 1] + ". No tp.");
			return true;
		}
		target.sendMessage(player.getDisplayName() + " wants to teleport to you, accepte ? [yes/no]");
		ConversationFactory factory = new ConversationFactory(this.gbp.getPlugin());
		final Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("data", "first");
		map.put("player", player);
		map.put("target", target);
		Conversation conv = factory.withFirstPrompt(new TpaPrompt()).withPrefix(new ConversationPrefix(){
			public String getPrefix(ConversationContext context)
			{
				return ChatColor.GREEN + "GTPA" + ChatColor.WHITE + " ";
			}
		}).withInitialSessionData(map).withLocalEcho(false).withTimeout(60).buildConversation(target);
		conv.addConversationAbandonedListener(new ConversationAbandonedListener()
		{
			public void conversationAbandoned(ConversationAbandonedEvent event)
			{
				if(!event.gracefulExit())
				{
					if(event.getContext().getSessionData("data") != "no" || event.getContext().getSessionData("data") != "yes")
					{
						((Player)event.getContext().getSessionData("player")).sendMessage("Refused by timeout.");
					}
				}
			}
		});
		conv.begin();
		Command.broadcastCommandMessage(sender, "Teleported (or not) " + player.getDisplayName() + " to " + target.getDisplayName());
		return true;
	}
}