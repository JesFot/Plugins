package fr.gbp.island;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
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
	
	public String getPromptText(ConversationContext context)
	{
		return "[[YOU]Commander]" + context.getSessionData("text");
	}
	
	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input)
	{
		String toDo = (String)context.getSessionData("nextAct");
		if(toDo != "")
		{
			this.help(toDo, context.getForWhom());
			this.up(toDo, context.getForWhom());
			this.down(toDo, context.getForWhom());
			context.setSessionData("nextAct", "");
		}
		this.help(input, context.getForWhom());
		this.up(input, context.getForWhom());
		this.down(input, context.getForWhom());
		context.setSessionData("text", input);
		if(input.equalsIgnoreCase("exit"))
		{
			return END_OF_CONVERSATION;
		}
		return this;
	}
	
	@Override
	protected boolean isInputValid(ConversationContext context, String input)
	{
		return true;
	}
	
	private void help(String input, Conversable target)
	{
		if(input.equalsIgnoreCase("help"))
		{
			target.sendRawMessage(ChatColor.GREEN + "List of available commands :");
			target.sendRawMessage(ChatColor.AQUA + "  - help : Show this message");
			target.sendRawMessage(ChatColor.AQUA + "  - exit : Exit this prompt.");
			target.sendRawMessage(ChatColor.AQUA + "  - up [blocks] : Move the island 'blocks' blocks up.");
			target.sendRawMessage(ChatColor.AQUA + "  - down [blocks] : Move the island 'blocks' blocks down.");
		}
	}
	
	private void up(String input, Conversable target)
	{
		if(input.startsWith("up"))
		{
			String[] splited = input.split(" ");
			if(splited.length <= 1)
			{
				this.gbp.getIsland().moveUp();
			}
			else
			{
				try
				{
					int b = Integer.parseInt(splited[1]);
					this.gbp.getIsland().moveUp(b);
				}
				catch(NumberFormatException ex)
				{
					target.sendRawMessage("Exception during parsing command ('blocks' must be an integer).");
					target.sendRawMessage("Usage: up [blocks]");
					ex.printStackTrace();
				}
			}
		}
	}
	
	private void down(String input, Conversable target)
	{
		if(input.startsWith("down"))
		{
			String[] splited = input.split(" ");
			if(splited.length <= 1)
			{
				this.gbp.getIsland().moveDown();
			}
			else
			{
				try
				{
					int b = Integer.parseInt(splited[1]);
					this.gbp.getIsland().moveDown(b);
				}
				catch(NumberFormatException ex)
				{
					target.sendRawMessage("Exception during parsing command ('blocks' must be an integer).");
					target.sendRawMessage("Usage: down [blocks]");
					ex.printStackTrace();
				}
			}
		}
	}
}