package fr.jesfot.gbp.command;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.entity.Player;

import fr.jesfot.gbp.GamingBlockPlug_1_11;
import fr.jesfot.gbp.command.helpers.TpaPrompt;
import fr.jesfot.gbp.configuration.NBTConfig;
import fr.jesfot.gbp.world.WorldComparator;

public class GTpaCommand extends CommandBase
{
	private GamingBlockPlug_1_11 gbp;
	
	public GTpaCommand(GamingBlockPlug_1_11 p_gbp)
	{
		super("gtpa");
		this.gbp = p_gbp;
		this.setRawUsageMessage("/<com> <target>");
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!cmd.getName().equalsIgnoreCase("gtpa"))
		{
			return false;
		}
		if(args.length < 1 || args.length > 1)
		{
			this.sendUsage(sender, label);
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
		if(player.getGameMode().equals(GameMode.ADVENTURE))
		{
			sender.sendMessage("Cannot use /" + label + " in adventure mode !");
			return true;
		}
		
		NBTConfig playerCfg = new NBTConfig(this.gbp.getConfigFolder("playerdatas"), player.getUniqueId());
		String teamName = playerCfg.readNBTFromFile().getCopy().getString("Team");
		if(!this.gbp.getTeams().getIfExists(teamName).canUseTpa())
		{
			sender.sendMessage("You are not allowed to use that command.");
			return true;
		}
		
		Player target = this.gbp.getPlayerExact(args[args.length - 1]);
		if(target == null)
		{
			sender.sendMessage("Cannot find player " + args[args.length - 1] + ". No tp.");
			return true;
		}
		if(WorldComparator.compareWorlds(player.getWorld(), target.getWorld(), this.gbp)<0)
		{
			player.sendMessage("Refused because target is in another world.");
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
	
	@Override
	public List<String> executeTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		if(args.length == 1)
		{
			return null;
		}
		return Collections.emptyList();
	}
}