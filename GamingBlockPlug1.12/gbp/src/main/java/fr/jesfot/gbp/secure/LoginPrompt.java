package fr.jesfot.gbp.secure;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import fr.jesfot.gbp.GamingBlockPlug_1_12;
import fr.jesfot.gbp.stats.PlayerStatistics;

public class LoginPrompt extends StringPrompt
{
	private Player player;
	private SecurityLoginSys sls;
	private PlayerStatistics stat;
	
	public LoginPrompt(SecurityLoginSys sec, Player sender, PlayerStatistics stats)
	{
		this.sls = sec;
		this.player = sender;
		this.stat = stats;
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
			this.stat.player(this.player);
			//this.stat.login();
			this.player.sendRawMessage("Logged in !");
			this.sls.endLogin(player);
			GamingBlockPlug_1_12.getMe().getLogger().info(player.getName() + " succesfuly logged in with his password.");
			GamingBlockPlug_1_12.getMyLogger().info(player.getName() + " succesfuly logged in with his password.");
			return Prompt.END_OF_CONVERSATION;
		}
		else
		{
			tryes = Integer.valueOf(tryes.intValue() + 1);
			context.setSessionData("tries", tryes);
			GamingBlockPlug_1_12.getMyLogger().info(player.getName() + " used wrong password ('" + input + "') for the "
					+ tryes.intValue() + " time(s).");
			if(tryes >= 5)
			{
				context.setSessionData("kick", Boolean.TRUE);
				this.player.kickPlayer("Invalid password 5 times...");
				this.sls.endLogin(player);
				GamingBlockPlug_1_12.getMe().getLogger().info(player.getName() + " was kick for 5 invalid passwords.");
				GamingBlockPlug_1_12.getMyLogger().info(player.getName() + " kicked for wrong passwords.");
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