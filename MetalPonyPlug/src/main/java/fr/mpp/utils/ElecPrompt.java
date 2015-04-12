package fr.mpp.utils;

import java.util.Map;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import fr.mpp.command.MelectionsCommand;
import fr.mpp.command.MelectionsCommand.Datas;

public class ElecPrompt extends ValidatingPrompt
{
	private MelectionsCommand parent;
	private Datas dat;
	
	public ElecPrompt(MelectionsCommand melec)
	{
		this.parent = melec;
		this.dat = this.parent.getDat();
	}
 
    @Override
    public String getPromptText(ConversationContext context)
    {
        return "[" + ((Player)context.getSessionData("player")).getName() + "] Vote Session: " + context.getSessionData("data");
    }
 
    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, String in)
    {
    	Player test = (Player)context.getForWhom();
    	
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
        context.setSessionData("data", in + "\n" + this.dat.msgsC.get("msgCommun"));
        if(args[0].equalsIgnoreCase("help"))
        {
        	test.sendRawMessage("Usage : present, vote <player> or propose <player>");
        }
        if(args[0].equalsIgnoreCase("vote"))
        {
        	if (args.length == 2)
        	{
        		Player pl = MPlayer.getPlayerByName(args[1]);
        		if (this.dat.presented.get(pl))
        		{
        			this.dat.votes.replace(test, pl);
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
        	Map<Player, Boolean> tmp = this.dat.presented;
        	if (!tmp.get(test))
        	{
            	tmp.replace(test, true);
        		context.setSessionData("data", test.getName()+" se presente.");
        		this.dat.msgsC.replace("msgCommun", test.getDisplayName() + " se présente.");
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
        		this.dat.proposed.replace(pla, (this.dat.proposed.get(pla)+1).toString());
        		pla.sendRawMessage("Quelqu'un veut que vous vous presetiez (tapez 'present' dans l'interface election)");
        	}
        }
        else if (args[0].equalsIgnoreCase("exit"))
        {
        	end = true;
        }
        
        if (end)
        {
        	return END_OF_CONVERSATION;
        }
		return this;
    }
 
    @Override
    protected boolean isInputValid(ConversationContext context, String in)
    {
        return true;
    }
 
}