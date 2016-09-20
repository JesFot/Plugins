package fr.jesfot.gbp.secure;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import fr.jesfot.gbp.GamingBlockPlug_1_9;

public class RegisterPrompt extends StringPrompt
{
	private Player player;
	private SecurityLoginSys sls;
	private String lastPassword = null;
	
	public RegisterPrompt(SecurityLoginSys sec, Player sender)
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
		if(this.lastPassword == null)
		{
			this.lastPassword = input;
			this.player.sendRawMessage("Retype password please :");
			return this;
		}
		else
		{
			if(this.lastPassword.equals(input))
			{
				this.sls.register(this.player, input);
				this.player.sendRawMessage("Succesfuly registred !");
				this.sls.endLogin(player);
				GamingBlockPlug_1_9.getMe().getLogger().info(player.getName() + " succesfuly registred new password.");
				GamingBlockPlug_1_9.getMyLogger().info(player.getName() + " succesfuly registred with his password.");
				return Prompt.END_OF_CONVERSATION;
			}
			tryes = Integer.valueOf(tryes.intValue() + 1);
			context.setSessionData("tries", tryes);
			GamingBlockPlug_1_9.getMyLogger().info(player.getName() + " used not corresponding password "
					+ "('" + input + "' != '" + this.lastPassword + "') for the "
					+ tryes.intValue() + " time(s).");
			if(tryes >= 5)
			{
				context.setSessionData("kick", Boolean.TRUE);
				this.player.kickPlayer("Invalid second password 5 times...");
				this.sls.endLogin(player);
				GamingBlockPlug_1_9.getMe().getLogger().info(player.getName() + " wrote 5 times two differents passwords.");
				GamingBlockPlug_1_9.getMyLogger().info(player.getName() + " kicked for wrong passwords.");
				return Prompt.END_OF_CONVERSATION;
			}
			else
			{
				this.player.sendRawMessage("You have " + (5 - tryes) + " tries left...");
				this.lastPassword = null;
				return this;
			}
		}
	}
}