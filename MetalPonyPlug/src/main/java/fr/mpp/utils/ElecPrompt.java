package fr.mpp.utils;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import fr.mpp.command.MelectionsCommand;
import fr.mpp.command.MelectionsCommand.Datas;
import fr.mpp.mpp.Classes;
import fr.mpp.mpp.ClassesUtils;
import fr.mpp.mpp.RankLevel;

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
    	String tmp2 = "[" + ((Player)context.getSessionData("player")).getName() + "] Vote Session: " + context.getSessionData("data");
    	return tmp2;
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
        context.setSessionData("data", in);
        if (args[0].equalsIgnoreCase("help"))
        {
        	test.sendRawMessage("Usage : present, vote <player> or propose <player>");
        }
        else if (args[0].equalsIgnoreCase("startmsg"))
        {
        	context.setSessionData("data", this.dat.msgsC.get("msgStart"));
        }
        else if (args[0].equalsIgnoreCase("vote"))
        {
        	if (args.length == 2)
        	{
        		Player pl = MPlayer.getPlayerByName(args[1]);
        		end = this.dat.vote(test, pl, context);
        		if (end)
        		{
        			Player plW = this.dat.getWiner();
        			plW.sendMessage("Vous devenez " + this.parent.statu + " !!");
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