package me.jesfot.gamingblockplug.security.prompts;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import me.jesfot.gamingblockplug.data.GBPPlayer;
import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;
import me.jesfot.gamingblockplug.security.LoginSystem;

public class RegisterPrompt extends StringPrompt
{
	private String lastPassword = null;
	
	private final LoginSystem masterSystem;
	private final GBPPlayer player;
	
	public RegisterPrompt(LoginSystem system, GBPPlayer player)
	{
		this.masterSystem = system;
		this.player = player;
	}
	
	@Override
	public String getPromptText(ConversationContext context)
	{
		return "Password: ";
	}

	@Override
	public Prompt acceptInput(ConversationContext context, String input)
	{
		context.setSessionData("pass", input);
		Integer tries = (Integer) context.getSessionData("tries");
		if (this.lastPassword == null)
		{
			this.lastPassword = input;
			context.getForWhom().sendRawMessage("Retype password please :");
			return this;
		}
		if (this.lastPassword.equals(input))
		{
			this.masterSystem.register(this.player, input);
			context.getForWhom().sendRawMessage("Succesfuly registred !");
			this.masterSystem.endLogin(this.player);
			this.player.setIsLogged(true);
			GamingBlockPlug.getInstance().getLogger().info(this.player.getHandler().getName() + " succesfuly registred new password.");
			return Prompt.END_OF_CONVERSATION;
		}
		tries = Integer.valueOf(tries.intValue() + 1);
		context.setSessionData("tries", tries);
		if (tries >= 5)
		{
			context.setSessionData("kick", Boolean.TRUE);
			((Player) context.getForWhom()).kickPlayer("Invalid second password 5 times...");
			this.masterSystem.endLogin(this.player);
			return Prompt.END_OF_CONVERSATION;
		}
		context.getForWhom().sendRawMessage("You have " + (5 - tries) + " tries left...");
		this.lastPassword = null;
		return this;
	}
}
