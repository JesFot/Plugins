package fr.mpp.command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.entity.Player;

import fr.mpp.MetalPonyPlug;
import fr.mpp.utils.ElecPrompt;
import fr.mpp.utils.MPlayer;

public class MelecMaireCommand implements CommandExecutor
{
	private String usageMessage;
	private MetalPonyPlug mpp;
	
	public MelecMaireCommand(MetalPonyPlug mppl)
	{
		this.mpp = mppl;
		this.usageMessage = "Usage : /elecmaire <vote|present> [player] or /elecmaire <start>";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args)
	{
		/*if (!alias.equalsIgnoreCase("elecmaire") || !alias.equalsIgnoreCase("maire") || !alias.equalsIgnoreCase("election"))
		{
			return false;
		}*/
		
		ConversationFactory factory = new ConversationFactory(this.mpp.getPlugin());
		final Map<Object, Object> hMap = new HashMap<Object, Object>();
		hMap.put("data", "Chat pour élire un maire ! tapez 'help' pour savoir quoi faire.");
		hMap.put("player", (Player)sender);
		Conversation conv = factory.withFirstPrompt(new ElecPrompt(this.mpp)).withPrefix(new ConversationPrefix(){
			@Override
			public String getPrefix(ConversationContext context)
			{
				return ChatColor.GREEN + "Elections" + ChatColor.WHITE + " ";
			}
		}).withInitialSessionData(hMap).withLocalEcho(false).buildConversation((Conversable)sender);
		conv.addConversationAbandonedListener(new ConversationAbandonedListener()
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
		});
		
		for (Player pl : Bukkit.getOnlinePlayers())
		{
			pl.beginConversation(conv);
		}
		
		if (args.length >= 2)
		{
			if (args[0].equalsIgnoreCase("propose") || args[0].equalsIgnoreCase("prop"))
			{
				Player target = MPlayer.getPlayerByName(args[1]);
				target.sendMessage("On veut vous voir maire !");
			}
			else if (args[0].equalsIgnoreCase("vote"))
			{
				//
			}
			else
			{
				sender.sendMessage(ChatColor.RED + this.usageMessage);
				return true;
			}
		}
		else if (args.length == 1)
		{
			if (args[0].equalsIgnoreCase("present"))
			{
				if (sender instanceof Player)
				{
					Player player = (Player)sender;
					player.sendMessage("Vous vous présentez.");
				}
				else
				{
					sender.sendMessage("Present yourself when you are a player please.");
				}
			}
			else if (args[0].equalsIgnoreCase("start"))
			{
				//
			}
			else
			{
				sender.sendMessage(ChatColor.RED + this.usageMessage);
				return true;
			}
		}
		else
		{
			sender.sendMessage(ChatColor.RED + this.usageMessage);
		}
		return true;
	}
}