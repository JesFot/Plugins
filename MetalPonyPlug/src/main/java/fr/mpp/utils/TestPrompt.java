package fr.mpp.utils;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class TestPrompt extends ValidatingPrompt
{
 
    @Override
    public String getPromptText(ConversationContext context)
    {
        return "Reponse: " + context.getSessionData("data");
    }
 
    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, String in)
    {
    	Player player, target;
    	player = (Player)context.getSessionData("player");
    	target = (Player)context.getSessionData("target");
        context.setSessionData("data", in);
        if(in.equalsIgnoreCase("y") || in.equalsIgnoreCase("yes"))
        {
        	player.sendMessage("Accepted !");
			player.teleport(target, TeleportCause.COMMAND);
            return END_OF_CONVERSATION;
        }
        else if (in.equalsIgnoreCase("n") || in.equalsIgnoreCase("non"))
        {
        	player.sendMessage("Refused !");
            return END_OF_CONVERSATION;
        }
        else
        {
        	context.getForWhom().sendRawMessage("You must give a response ! ([y/n])");
        }
		return this;
    }
 
    @Override
    protected boolean isInputValid(ConversationContext context, String in)
    {
        return true;
    }
 
}