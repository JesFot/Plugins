package fr.mpp.command;

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

import fr.mpp.MetalPonyPlug;
import fr.mpp.utils.ElecPrompt;

public class MelecMaireCommand implements CommandExecutor
{
	private String usageMessage;
	private MetalPonyPlug mpp;
	
	public MelecMaireCommand(MetalPonyPlug mppl)
	{
		this.mpp = mppl;
		this.usageMessage = "Usage : /elecmaire <start>";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args)
	{
		/*if (!alias.equalsIgnoreCase("elecmaire") || !alias.equalsIgnoreCase("maire") || !alias.equalsIgnoreCase("election"))
		{
			return false;
		}*/
		if (sender instanceof Player)
		{
			//
		}
		else
		{
			sender.sendMessage("You must be a player...");
			return true;
		}
		if (args[0].equalsIgnoreCase("start"))
		{
			//
		}
		else
		{
			sender.sendMessage(ChatColor.RED + this.usageMessage);
			return true;
		}
		
		ConversationFactory factory = new ConversationFactory(this.mpp.getPlugin());
		final Map<Object, Object> hMap = new HashMap<Object, Object>();
		hMap.put("data", "Chat pour élire un maire ! tapez 'help' pour savoir quoi faire.");
		hMap.put("player", (Player)sender);
		factory = factory.withFirstPrompt(new ElecPrompt(this.mpp)).withPrefix(new ConversationPrefix(){
			@Override
			public String getPrefix(ConversationContext context)
			{
				return ChatColor.GREEN + "Elections" + ChatColor.WHITE + " ";
			}
		}).withInitialSessionData(hMap).withLocalEcho(false);
		
		class ConvAL implements ConversationAbandonedListener
		{
			@Override
			public void conversationAbandoned(ConversationAbandonedEvent event)
			{
				if (event.gracefulExit())
				{
					//mpp.getLogger().info("graceful exit");
				}
				try
				{
					//mpp.getLogger().info("Canceller" + event.getCanceller().toString());
				}
				catch (NullPointerException n)
				{
					//mpp.getLogger().info("null Canceller");
				}
			}
		}
		Conversation conv = factory.buildConversation((Player)sender);
		conv.addConversationAbandonedListener(new ConvAL());
		
		/*for (Player pl : Bukkit.getOnlinePlayers())
		{
			if (pl.getName().equalsIgnoreCase(sender.getName()))
			{
				continue;
			}
			pl.beginConversation(conv);
		}*/
		conv.begin();
		
		if (args.length >= 2)
		{
			//
		}
		else if (args.length == 1)
		{
			//
		}
		else
		{
			sender.sendMessage(ChatColor.RED + this.usageMessage);
		}
		return true;
	}
}