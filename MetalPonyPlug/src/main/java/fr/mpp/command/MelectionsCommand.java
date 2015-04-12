package fr.mpp.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

import fr.mpp.MetalPonyPlug;
import fr.mpp.utils.ElecPrompt;

public class MelectionsCommand implements CommandExecutor
{
	private String usageMessage;
	private MetalPonyPlug mpp;
	public String statu;
	private MelectionsCommand tis;
	
	public MelectionsCommand(MetalPonyPlug mppl)
	{
		this.mpp = mppl;
		this.tis = this;
		this.usageMessage = "Usage : /election <start> or /election <set> <type>";
		this.statu = "maire";
	}
	
	public class Datas
	{
		public List<Player> players;
		public Map<String, String> msgsC = new HashMap<String, String>();
		public Map<Player, Player> votes = new HashMap<Player, Player>();
		public Map<Player, Boolean> presented = new HashMap<Player, Boolean>();
		public Map<Player, Integer> proposed = new HashMap<Player, Integer>();
		public Map<String, Player> playersName = new HashMap<String, Player>();
		private MelectionsCommand parent;
		
		public Datas()
		{
			this.parent = tis;
			this.players = Arrays.asList(Bukkit.getOnlinePlayers());
			for (Player pl : this.players)
			{
				this.presented.put(pl, Boolean.FALSE);
				this.votes.put(pl, null);
				this.proposed.put(pl, 0);
				this.playersName.put(pl.getName(), pl);
			}
			this.msgsC.put("msgCommun", "Chat pour élire un " + this.parent.statu + " ! tapez 'help' pour savoir quoi faire.");
		}
		public Datas(MelectionsCommand par)
		{
			this.parent = par;
			this.players = Arrays.asList(Bukkit.getOnlinePlayers());
			for (Player pl : this.players)
			{
				this.presented.put(pl, Boolean.FALSE);
				this.proposed.put(pl, 0);
				this.playersName.put(pl.getName(), pl);
			}
			this.msgsC.put("msgCommun", "Chat pour élire un " + this.parent.statu + " ! tapez 'help' pour savoir quoi faire.");
		}
		
		public MelectionsCommand getParent()
		{
			return this.parent;
		}
	}
	Datas dat = new Datas(this);
	
	public Datas getDat()
	{
		return this.dat;
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
		if (args.length == 1)
		{
			//
		}
		else
		{
			sender.sendMessage(ChatColor.RED + this.usageMessage);
			return true;
		}
		if (args[0].equalsIgnoreCase("start") || args[0].equalsIgnoreCase("set"))
		{
			if (args[0].equalsIgnoreCase("set"))
			{
				if (args.length == 2)
				{
					String type = args[1];
					this.statu = type;
				}
				else
				{
					sender.sendMessage(ChatColor.RED + this.usageMessage);
					return true;
				}
			}
		}
		else
		{
			sender.sendMessage(ChatColor.RED + this.usageMessage);
			return true;
		}
		
		ConversationFactory factory = new ConversationFactory(this.mpp.getPlugin());
		final Map<Object, Object> hMap = new HashMap<Object, Object>();
		hMap.put("data", "Chat pour élire un " + this.statu + " ! tapez 'help' pour savoir quoi faire.");
		hMap.put("player", (Player)sender);
		factory = factory.withFirstPrompt(new ElecPrompt(this)).withPrefix(new ConversationPrefix(){
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
		
		for (Player pl : Bukkit.getOnlinePlayers())
		{
			Conversation convi = factory.buildConversation(pl);
			convi.addConversationAbandonedListener(new ConvAL());
			convi.begin();
		}
		
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