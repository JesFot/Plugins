package fr.mpp.utils;

import java.util.List;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import fr.mpp.MetalPonyPlug;

public class ElecPrompt extends ValidatingPrompt
{
	private MetalPonyPlug mpp;
	
	public ElecPrompt(MetalPonyPlug mppl)
	{
		this.mpp = mppl;
	}
 
    @Override
    public String getPromptText(ConversationContext context)
    {
        //Bukkit.getLogger().info("hit get prompt text for TestPrompt");
        return "[" + ((Player)context.getSessionData("player")).getDisplayName() + "] Vote Session: " + context.getSessionData("data");
    }
 
    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, String in)
    {
    	Player test = (Player)context.getForWhom();
    	in = test.getName() + " says " + in;
    	test.sendMessage("Blub");
    	test.chat("Test ! chat()");
    	this.mpp.getConfig().reloadCustomConfig();
    	
    	boolean end = false;
    	Player player;
    	player = (Player)context.getSessionData("player");
    	player.sendMessage("Blop !");
        context.setSessionData("data", in + "; Crotte");
        String[] args = in.split(" ");
        context.setSessionData("data", in + "; " + args[0] + "; " + args.toString());
        if(args[0].equalsIgnoreCase("vote"))
        {
        	if (args.length == 2)
        	{
        		Player pl = MPlayer.getPlayerByName(args[1]);
        		if (this.mpp.getConfig().getCustomConfig().getStringList("mpp.vote.present").contains(args[1]))
        		{
        			this.mpp.getConfig().getCustomConfig().set("mpp.vote.onair."+pl.getName().toLowerCase(),this.mpp.getConfig().getCustomConfig().getInt("mpp.vote.onair."+pl.getName().toLowerCase())+1);
                	player.sendMessage("A voté !");
        		}
        		else
        		{
        			player.sendMessage("Vous ne pouvez pas voter pour cette personne.");
        		}
        	}
        }
        else if (args[0].equalsIgnoreCase("present") || in.contains("present"))
        {
        	context.setSessionData("data", in + "; In present");
        	List<String> tmp = this.mpp.getConfig().getCustomConfig().getStringList("mpp.vote.present");
        	if (!tmp.contains(player.getName().toLowerCase()))
        	{
            	tmp.add(player.getName().toLowerCase());
        		this.mpp.getConfig().getCustomConfig().set("mpp.vote.present", tmp);
        		context.setSessionData("data", player.getDisplayName()+" se presente .");
            	player.sendMessage("Vous vous présentez !");
        	}
        	else
        	{
        		context.setSessionData("data", player.getDisplayName()+" tente de se re-presenter.");
        		player.sendMessage("Vous vous etes déjà presenté.");
        	}
        }
        else if (args[0].equalsIgnoreCase("propose"))
        {
        	context.setSessionData("data", in + args.length);
        	if (args.length == 2)
        	{
        		Player pla = MPlayer.getPlayerByName(args[1]);
        		pla.sendMessage("Quelqu'un veut que vous vous presetiez (/elecmaire present)");
        		pla.chat("Test ! chat()");
        	}
        }
        
        this.mpp.getConfig().saveCustomConfig();
        
        if (end)
        {
        	return END_OF_CONVERSATION;
        }
		return this;
    }
 
    @Override
    protected boolean isInputValid(ConversationContext context, String in)
    {
        //Bukkit.getLogger().info("hit input valid for TestPrompt");
        return true;
    }
 
}