package fr.mpp.utils;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import fr.mpp.command.MelectionsCommand;

public class ElecPrompt extends ValidatingPrompt
{
	/*private MelectionsCommand parent;
	//private Datas dat;*/
	
	public ElecPrompt(MelectionsCommand melec)
	{
		//this.parent = melec;
		//this.dat = this.parent.getDat();
	}
 
    @Override
    public String getPromptText(ConversationContext context)
    {
    	String tmp2 = "[" + ((Player)context.getForWhom()).getName() + "] Vote Session: " + context.getSessionData("data");
    	return tmp2;
    }
 
    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, String in)
    {
    	/*Player test = (Player)context.getForWhom();
    	
    	String[] args = {""};
    	if (!in.contains(" "))
    	{
    		args[0] = in;
    	}
    	else
    	{
            args = in.split(" ");
    	}
        context.setSessionData("data", in);
        if (args[0].equalsIgnoreCase("help"))
        {
        	test.sendRawMessage("Usage : present, vote <player> or propose <player>");
        }
        else if (args[0].equalsIgnoreCase("startmsg"))
        {
        	//context.setSessionData("data", this.dat.msgsC.get("msgStart"));
        	test.sendRawMessage(this.dat.msgsC.get("msgStart"));
        }
        else if (args[0].equalsIgnoreCase("vote"))
        {
        	if (args.length == 2)
        	{
        		Player pl = MPlayer.getPlayerByName(args[1]);
        		if (this.dat.vote(test, pl, context))
        		{
        			Player plW = this.dat.getWiner();
        			plW.sendRawMessage("Vous devenez " + this.parent.statu + " !!");
        			ClassesUtils cu = new ClassesUtils(this.parent.getMpp().getConfig());
        			Classes before = cu.getRank(plW, RankLevel.STATUT);
        			if (before == Classes.Default)
        			{
        				cu.setRank(Classes.Maire, plW, RankLevel.STATUT);
        			}
        			if (cu.getRankAffich(plW) == RankLevel.STATUT)
        			{
        				plW.setCustomName(Classes.Maire.getClasse().getDisplayName() + plW.getCustomName());
        				plW.setPlayerListName(ChatColor.DARK_BLUE + plW.getPlayerListName());
        			}
                	return END_OF_CONVERSATION;
        		}
        	}
        }
        else if (args[0].equalsIgnoreCase("present"))
        {
        	this.dat.present(test, context);
        }
        else if (args[0].equalsIgnoreCase("propose"))
        {
        	if (args.length == 2)
        	{
        		Player pla = MPlayer.getPlayerByName(args[1]);
        		this.dat.propose(test, pla, context);
        	}
        }
        else if (args[0].equalsIgnoreCase("exit"))
        {
        	return END_OF_CONVERSATION;
        }*/
		return this;
    }
 
    @Override
    protected boolean isInputValid(ConversationContext context, String in)
    {
        return false;
    }
 
}