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
    	this.mpp.getConfig().reloadCustomConfig();
    	
    	String[] args = {""};
    	boolean end = false;
    	if (!in.contains(" "))
    	{
    		args[0] = in;
    	}
    	else
    	{
            args = in.split(" ");
    	}
        //test.chat(in + "; " + args[0] + " , " + args[1] + "; " + test.getName());
        context.setSessionData("data", in);
        if(args[0].equalsIgnoreCase("help"))
        {
        	test.sendRawMessage("Usage : present, vote <player> or propose <player>");
        }
        if(args[0].equalsIgnoreCase("vote"))
        {
        	if (args.length == 2)
        	{
        		Player pl = MPlayer.getPlayerByName(args[1]);
        		if (this.mpp.getConfig().getCustomConfig().getStringList("mpp.vote.present").contains(args[1].toLowerCase()))
        		{
        			this.mpp.getConfig().getCustomConfig().set("mpp.vote.onair."+pl.getName().toLowerCase(),this.mpp.getConfig().getCustomConfig().getInt("mpp.vote.onair."+pl.getName().toLowerCase())+1);
                	test.sendRawMessage("A voté !");
        		}
        		else
        		{
        			test.sendRawMessage("Vous ne pouvez pas voter pour cette personne.");
        		}
        	}
        }
        else if (args[0].equalsIgnoreCase("present"))
        {
        	List<String> tmp = this.mpp.getConfig().getCustomConfig().getStringList("mpp.vote.present");
        	if (!tmp.contains(test.getName().toLowerCase()))
        	{
            	tmp.add(test.getName().toLowerCase());
        		this.mpp.getConfig().getCustomConfig().set("mpp.vote.present", tmp);
        		context.setSessionData("data", test.getDisplayName()+" se presente.");
        		test.chat(test.getDisplayName() + " se présente.");
            	test.sendRawMessage("Vous vous présentez !");
        	}
        	else
        	{
        		test.sendRawMessage("Vous vous etes déjà presenté.");
        	}
        }
        else if (args[0].equalsIgnoreCase("propose"))
        {
        	if (args.length == 2)
        	{
        		Player pla = MPlayer.getPlayerByName(args[1]);
        		pla.sendRawMessage("Quelqu'un veut que vous vous presetiez (type present)");
        	}
        }
        else if (args[0].equalsIgnoreCase("exit"))
        {
        	end = true;
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