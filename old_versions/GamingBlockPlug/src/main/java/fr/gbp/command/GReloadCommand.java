package fr.gbp.command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
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
import org.bukkit.scheduler.BukkitRunnable;

import fr.gbp.GamingBlockPlug;
import fr.gbp.island.IslandPrompt;
import fr.gbp.perms.GPermissions;

public class GReloadCommand implements CommandExecutor
{
	private GamingBlockPlug gbp;
	
	public GReloadCommand(GamingBlockPlug p_gbp)
	{
		this.gbp = p_gbp;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!cmd.getName().equalsIgnoreCase("greload"))
		{
			return false;
		}
		if(args.length >= 1)
		{
			if(args[0].equalsIgnoreCase("island"))
			{
				if(sender.isOp() && sender instanceof Player)
				{
					Player pl = (Player)sender;
					ConversationFactory factory = new ConversationFactory(this.gbp.getPlugin());
					final Map<Object, Object> map = new HashMap<Object, Object>();
					map.put("text", "help");
					map.put("nextAct", "help");
					Conversation conv = factory.withFirstPrompt(new IslandPrompt(this.gbp)).withPrefix(new ConversationPrefix(){
						public String getPrefix(ConversationContext context)
						{
							return ChatColor.GREEN + "[Island Controler]" + ChatColor.WHITE + " ";
						}
					}).withInitialSessionData(map).withLocalEcho(false).buildConversation(pl);
					conv.addConversationAbandonedListener(new ConversationAbandonedListener()
					{
						public void conversationAbandoned(ConversationAbandonedEvent event)
						{
							if(!event.gracefulExit())
							{
								//
							}
						}
					});
					conv.begin();
				}
				return true;
			}
		}
		if(!GPermissions.testPermission(sender, "bukkit.command.reload", null, true))
		{
			return false;
		}
		int time = 5;
		if(args.length >= 1)
		{
			time = Integer.parseInt(args[0]);
		}
		this.gbp.broad(ChatColor.DARK_RED + "RELOAD will start in "+time+" seconds.");
		(new BukkitRunnable()
		{
			protected int count = 0;
			public BukkitRunnable setVal(int p_timer)
			{
				this.count = p_timer;
				return this;
			}
			
			public void run()
			{
				Bukkit.broadcastMessage("Reload in "+count+" seconds.");
				count--;
				if(count == 0)
				{
					Bukkit.broadcastMessage("Reload start, please donnot chat.");
					Bukkit.reload();
					Bukkit.broadcastMessage(ChatColor.GREEN + "Reload complete.");
					this.cancel();
				}
			}
		}).setVal(time).runTaskTimer(this.gbp.getPlugin(), 0, 20);
		return true;
	}
}