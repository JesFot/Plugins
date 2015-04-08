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
        //Bukkit.getLogger().info("hit get prompt text for TestPrompt");
        return "Reponse: " + context.getSessionData("data");
    }
 
    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, String in)
    {
    	Player player, target;
    	player = (Player)context.getSessionData("player");
    	target = (Player)context.getSessionData("target");
        //Bukkit.getLogger().info("hit accept validated input for TestPrompt");
        context.setSessionData("data", in);
        if(in.equalsIgnoreCase("y"))
        {
        	player.sendMessage("Accepted !");
        	//Bukkit.getLogger().info("Accepted");
			player.teleport(target, TeleportCause.COMMAND);
            return END_OF_CONVERSATION;
        }
        else if (in.equalsIgnoreCase("n"))
        {
        	player.sendMessage("Refused !");
        	//Bukkit.getLogger().info("Refused");
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