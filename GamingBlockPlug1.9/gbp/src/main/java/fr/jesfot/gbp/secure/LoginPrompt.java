package fr.jesfot.gbp.secure;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class LoginPrompt extends StringPrompt
{
	private Player player;
	private SecurityLoginSys sls;
	
	public LoginPrompt(SecurityLoginSys sec, Player sender)
	{
		this.sls = sec;
		this.player = sender;
	}
	
	public String getPromptText(ConversationContext context)
	{
		return "Password: ";
	}
	
	public Prompt acceptInput(ConversationContext context, String input)
	{
		context.setSessionData("pass", input);
		Integer tryes = (Integer)context.getSessionData("tries");
		if(this.sls.login(this.player, input))
		{
			this.player.sendRawMessage("Logged in !");
			this.sls.endLogin(player);
			return Prompt.END_OF_CONVERSATION;
		}
		else
		{
			tryes = Integer.valueOf(tryes.intValue() + 1);
			context.setSessionData("tries", tryes);
			if(tryes >= 5)
			{
				this.player.kickPlayer("Invalid password 5 times...");
				this.sls.endLogin(player);
				return Prompt.END_OF_CONVERSATION;
			}
			else
			{
				this.player.sendRawMessage("You have " + (5 - tryes) + " tries left...");
				return this;
			}
		}
	}
}