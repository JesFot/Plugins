package me.emp.command;

import java.util.HashMap;
import java.util.Map;

import me.emp.utils.MPlayer;
import me.emp.utils.TestPrompt;
import me.emp.EliryumPlug;

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

public class MtpaCommand implements CommandExecutor
{
	private String usageMessage;
	private EliryumPlug emp;

	public MtpaCommand(EliryumPlug emp)
	{
		this.emp = emp;
		this.usageMessage = "/mtpa [player] <target>";
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		this.usageMessage = "/"+label+" [player] <target>";
		if (!cmd.getName().equalsIgnoreCase("mtpa"))
		{
			return false;
		}
		if (args.length < 1 || args.length > 2)
		{
			sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
			return false;
		}
		
		Player player;
		
		if (args.length == 1)
		{
			if (sender instanceof Player)
			{
				player = (Player)sender;
			}
			else
			{
				sender.sendMessage("Please provide a player!");
				return true;
			}
		}
		else
		{
			player = MPlayer.getPlayerByName(args[0]);
		}
		
		if (player == null)
		{
			sender.sendMessage("Player not found: " + args[0]);
			return true;
		}
		
		if (args.length < 3)
		{
			Player target = MPlayer.getPlayerByName(args[args.length - 1]);
			if (target == null)
			{
				sender.sendMessage("Can't find player " + args[args.length - 1] + ". No tp.");
				return true;
			}
			target.sendMessage(player.getDisplayName() + " wants to teleport to you, accepte ? [y/n]");
			ConversationFactory factory = new ConversationFactory(this.emp.getPlugin());
			final Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("data", "first");
			map.put("player", player);
			map.put("target", target);
			Conversation conv = factory.withFirstPrompt(new TestPrompt()).withPrefix(new ConversationPrefix(){
				@Override
				public String getPrefix(ConversationContext context)
				{
					return ChatColor.GREEN + "MTPA" + ChatColor.WHITE + " ";
				}
			}).withInitialSessionData(map).withLocalEcho(false).buildConversation(target);
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
			conv.begin();
			Command.broadcastCommandMessage(sender, "Teleported (or not) " + player.getDisplayName() + " to " + target.getDisplayName());
		}
		return true;
	}
}