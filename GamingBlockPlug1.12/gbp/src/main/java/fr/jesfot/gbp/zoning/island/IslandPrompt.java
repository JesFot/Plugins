package fr.jesfot.gbp.zoning.island;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

import fr.jesfot.gbp.GamingBlockPlug_1_12;

public class IslandPrompt extends ValidatingPrompt
{
	private GamingBlockPlug_1_12 gbp;
	
	public IslandPrompt(GamingBlockPlug_1_12 plugin)
	{
		this.gbp = plugin;
	}
	
	public String getPromptText(ConversationContext context)
	{
		return ChatColor.GOLD + "[[YOU]Commander] Executed command : " + ChatColor.BLUE + context.getSessionData("text");
	}
	
	protected Prompt acceptValidatedInput(ConversationContext context, String input)
	{
		String toDo = (String)context.getSessionData("nextAct");
		if(toDo != null && !toDo.isEmpty())
		{
			this.help(toDo, context.getForWhom());
			this.up(input, context.getForWhom());
			this.down(toDo, context.getForWhom());
			this.north(toDo, context.getForWhom());
			context.setSessionData("nextAct", "");
		}
		this.help(input, context.getForWhom());
		this.up(input, context.getForWhom());
		this.down(input, context.getForWhom());
		this.north(input, context.getForWhom());
		context.setSessionData("text", input);
		if(input.equalsIgnoreCase("exit"))
		{
			context.getForWhom().sendRawMessage(ChatColor.GREEN + "Exited the island controller, bye bye.");
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
			target.sendRawMessage(ChatColor.AQUA + "  - north [blocks] : Move the island 'blocks' blocks to the north.");
		}
	}
	
	private void up(final String input, Conversable target)
	{
		if(input.startsWith("up"))
		{
			String[] splited = input.split(" ");
			if(splited.length <= 1)
			{
				this.gbp.getIsland().moveUp(1);
			}
			else
			{
				try
				{
					int b = Integer.parseInt(splited[1]);
					this.gbp.getIsland().moveUp(b);
					target.sendRawMessage("Successfully goes up");
				}
				catch(NumberFormatException ex)
				{
					target.sendRawMessage("Exception during parsing argument 'blocks' "
							+ "("+splited[1]+" is not an integer).");
					target.sendRawMessage("Usage: up [blocks]");
					ex.printStackTrace();
				}
			}
		}
	}
	
	private void down(final String input, Conversable target)
	{
		if(input.startsWith("down"))
		{
			String[] splited = input.split(" ");
			if(splited.length <= 1)
			{
				this.gbp.getIsland().moveDown(1);
			}
			else
			{
				try
				{
					int b = Integer.parseInt(splited[1]);
					this.gbp.getIsland().moveDown(b);
					target.sendRawMessage("Successfully goes down");
				}
				catch(NumberFormatException ex)
				{
					target.sendRawMessage("Exception during parsing argument 'blocks' "
							+ "("+splited[1]+" is not an integer).");
					target.sendRawMessage("Usage: down [blocks]");
					ex.printStackTrace();
				}
			}
		}
	}
	
	private void north(final String input, Conversable target)
	{
		if(input.startsWith("north"))
		{
			/*String[] splited = input.split(" ");
			if(splited.length <= 1)
			{
				this.gbp.getIsland().moveNorth(1);
			}
			else
			{
				try
				{
					int b = Integer.parseInt(splited[1]);
					this.gbp.getIsland().moveNorth(b);
					target.sendRawMessage("Successfully goes north");
				}
				catch(NumberFormatException ex)
				{
					target.sendRawMessage("Exception during parsing argument 'blocks' "
							+ "("+splited[1]+" is not an integer).");
					target.sendRawMessage("Usage: north [blocks]");
					ex.printStackTrace();
				}
			}*/
		}
	}
}