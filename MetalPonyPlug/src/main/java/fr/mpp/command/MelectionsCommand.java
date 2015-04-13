package fr.mpp.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
	
	public MetalPonyPlug getMpp()
	{
		return this.mpp;
	}
	
	public class Datas
	{
		private String statut;
		public List<Player> players;
		private int playersN;
		public Map<String, String> msgsC = new HashMap<String, String>();
		private Map<Player, Player> votes = new HashMap<Player, Player>();
		private int votesN;
		private Map<Player, Boolean> presented = new HashMap<Player, Boolean>();
		private Map<Player, Integer> proposed = new HashMap<Player, Integer>();
		public Map<String, Player> playersName = new HashMap<String, Player>();
		private MelectionsCommand parent;
		
		public Datas()
		{
			this.parent = tis;
			this.statut = statu;
			this.players = Arrays.asList(Bukkit.getOnlinePlayers());
			this.playersN = this.players.size();
			this.votesN = 0;
			for (Player pl : this.players)
			{
				this.presented.put(pl, Boolean.FALSE);
				this.votes.put(pl, null);
				this.proposed.put(pl, 0);
				this.playersName.put(pl.getName(), pl);
			}
			this.msgsC.put("msgStart", "Chat pour élire un " + this.statut + " ! tapez 'help' pour savoir quoi faire.");
		}
		public Datas(MelectionsCommand par)
		{
			this.parent = par;
			this.statut = par.statu;
			this.players = Arrays.asList(Bukkit.getOnlinePlayers());
			this.playersN = this.players.size();
			this.votesN = 0;
			for (Player pl : this.players)
			{
				this.presented.put(pl, Boolean.FALSE);
				this.proposed.put(pl, 0);
				this.playersName.put(pl.getName(), pl);
			}
			this.msgsC.put("msgStart", "Chat pour élire un " + this.statut + " ! tapez 'help' pour savoir quoi faire.");
		}
		
		public MelectionsCommand getParent()
		{
			return this.parent;
		}
		public Player getWiner()
		{
			Map<Player, Integer> count = new HashMap<Player, Integer>();
			for (Player pl : this.votes.values())
			{
				count.put(pl, count.getOrDefault(pl, 0)+1);
			}
			int maxi = (Collections.max(count.values()));
			for (Entry<Player, Integer> entry : count.entrySet())
			{
				if (entry.getValue() == maxi)
				{
					return entry.getKey();
				}
			}
			return null;
		}
		public void propose(Player source, Player cible, ConversationContext context)
		{
			this.proposed.replace(cible, this.proposed.get(cible)+1);
			cible.sendRawMessage("Quelqu'un veut que vous vous presentiez (tapez 'present' dans l'interface election)");
		}
		public void present(Player player, ConversationContext context)
		{
			if (!this.presented.get(player))
        	{
				this.presented.replace(player, Boolean.TRUE);
        		this.parent.mpp.broad(player.getDisplayName() + " se présente.");
        		//player.sendRawMessage("Vous vous présentez !");
        		context.setSessionData("data", "Vous vous présentez !");
        	}
        	else
        	{
        		context.setSessionData("data", "Vous vous etes déjà presenté.");
        	}
		}
		public boolean vote(Player source, Player cible, ConversationContext context)
		{
			if (this.presented.get(cible))
    		{
    			this.votes.replace(source, cible);
    			context.setSessionData("data", "A voté !");
    		}
    		else
    		{
    			context.setSessionData("data", "Vous ne pouvez pas voter pour cette personne.");
    		}
			if (this.votesN >= this.playersN)
			{
				this.parent.mpp.broad("Le vote est terminé, tout le monde a voté.");
				return true;
			}
			return false;
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
		if (!cmd.getName().equalsIgnoreCase("election"))
		{
			return false;
		}
		if (sender instanceof Player){}
		else
		{
			sender.sendMessage("You must be a player...");
			return true;
		}
		if (args.length == 1 || args.length == 2){}
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
			else
			{
				if (args.length == 1){}
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
		hMap.put("times", 0);
		factory = factory.withFirstPrompt(new ElecPrompt(this)).withPrefix(new ConversationPrefix(){
			@Override
			public String getPrefix(ConversationContext context)
			{
				return ChatColor.GREEN + "Elections" + ChatColor.RESET + " ";
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
		return true;
	}
}