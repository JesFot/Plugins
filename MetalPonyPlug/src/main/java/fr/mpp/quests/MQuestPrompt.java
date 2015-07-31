package fr.mpp.quests;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

public class MQuestPrompt
{
	public class setQuestPrompt extends ValidatingPrompt
	{
		@Override
		public String getPromptText(ConversationContext context)
		{
			return "";
		}

		@Override
		protected Prompt acceptValidatedInput(ConversationContext context, String input)
		{
			context.setSessionData("data", input);
			String mode = (String)context.getSessionData("next");
			switch(mode)
			{
			case "name":
				context.setSessionData("name", input);
				context.getForWhom().sendRawMessage("Quest's description :");
				context.setSessionData("next", "desc");
				break;
			case "desc":
				context.setSessionData("description", input);
				context.getForWhom().sendRawMessage("Quest's gain (money) :");
				context.setSessionData("next", "money");
				break;
			case "money":
				context.setSessionData("money", input);
				context.getForWhom().sendRawMessage("Quest's type (type types to see types) :");
				context.setSessionData("next", "type");
				break;
			case "type":
				if(input.equalsIgnoreCase("types"))
				{
					context.getForWhom().sendRawMessage("");
				}
				else
				{
					context.getForWhom().sendRawMessage("Quest's goal (type help to see what you have to type) :");
					context.setSessionData("next", "goal");
				}
				break;
			case "goal":
				if(input.equalsIgnoreCase("help"))
				{
					//
				}
				else
				{
					context.setSessionData("goal", input);
					context.setSessionData("next", "");
				}
			}
			return this;
		}

		@Override
		protected boolean isInputValid(ConversationContext context, String input)
		{
			return true;
		}
	}
}