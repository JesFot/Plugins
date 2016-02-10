package fr.gbp.island;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

import fr.gbp.GamingBlockPlug;

public class IslandPrompt extends ValidatingPrompt
{
	private GamingBlockPlug gbp;
	
	public IslandPrompt(GamingBlockPlug plugin)
	{
		this.gbp = plugin;
	}
	
	@Override
	public String getPromptText(ConversationContext context)
	{
		return "[[YOU]Commander]" + context.getSessionData("data");
	}
	
	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input)
	{
		context.setSessionData("LastInput", input);
		if(input.equalsIgnoreCase("exit"))
		{
			return END_OF_CONVERSATION;
		}
		else if(input.equalsIgnoreCase("help"))
		{
			context.getForWhom().sendRawMessage(ChatColor.GREEN + "List of available commands :");
			context.getForWhom().sendRawMessage(ChatColor.AQUA + "  - help : Show this message");
			context.getForWhom().sendRawMessage(ChatColor.AQUA + "  - exit : Exit this prompt.");
		}
		else if(input.equalsIgnoreCase("up"))
		{
			this.gbp.getIsland().moveUp();
		}
		else if(input.startsWith("up "))
		{
			int b = Integer.parseInt(input.split(" ")[1]);
			this.gbp.getIsland().moveUp(b);
		}
		else
		{
			context.getForWhom().sendRawMessage("Error while parsing command \""+input+"\", use 'help' for a list of commands.");
		}
		return this;
	}
	
	@Override
	protected boolean isInputValid(ConversationContext context, String input)
	{
		return true;
	}
}