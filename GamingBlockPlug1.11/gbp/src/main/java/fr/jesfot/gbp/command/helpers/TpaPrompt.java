package fr.jesfot.gbp.command.helpers;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class TpaPrompt extends ValidatingPrompt
{
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