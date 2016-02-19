package fr.gbp.command.helpers;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class TpaPrompt extends ValidatingPrompt {

	public String getPromptText(ConversationContext context)
	{
		return "Response: " + context.getSessionData("data");
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input)
	{
		Player player, target;
		player = (Player)context.getSessionData("player");
		target = (Player)context.getSessionData("target");
		context.setSessionData("data", input);
		if(input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes"))
		{
			context.setSessionData("data", "yes");
			player.sendMessage("Accepted !");
			if(!player.getWorld().equals(target.getWorld()) && !player.getWorld().getName().equalsIgnoreCase(target.getWorld().getName()+"_nether")
					 && !player.getWorld().getName().equalsIgnoreCase(target.getWorld().getName()+"_the_end")
					 && !player.getWorld().getName().equalsIgnoreCase(target.getWorld().getName()+"_end")
					 && !target.getWorld().getName().equalsIgnoreCase(player.getWorld().getName()+"_nether")
					 && !target.getWorld().getName().equalsIgnoreCase(player.getWorld().getName()+"_the_end")
					 && !target.getWorld().getName().equalsIgnoreCase(player.getWorld().getName()+"_end"))
			{
				player.sendMessage("Refused because target is in another world.");
				target.sendRawMessage("You are not in the same world.");
				return END_OF_CONVERSATION;
			}
			player.teleport(target, TeleportCause.COMMAND);
			return END_OF_CONVERSATION;
		}
		else if(input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no"))
		{
			context.setSessionData("data", "no");
			player.sendMessage("Refused !");
			return END_OF_CONVERSATION;
		}
		else
		{
			context.getForWhom().sendRawMessage("You must give a response ! ([yes/no])");
		}
		return this;
	}

	@Override
	protected boolean isInputValid(ConversationContext context, String input)
	{
		return true;
	}

}
