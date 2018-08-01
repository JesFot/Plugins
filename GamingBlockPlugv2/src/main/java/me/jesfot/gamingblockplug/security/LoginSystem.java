package me.jesfot.gamingblockplug.security;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

import me.jesfot.gamingblockplug.data.GBPPlayer;
import me.jesfot.gamingblockplug.permission.PermissionHelper;
import me.jesfot.gamingblockplug.permission.StaticPerms;
import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;
import me.jesfot.gamingblockplug.security.prompts.LoginPrompt;
import me.jesfot.gamingblockplug.security.prompts.RegisterPrompt;
import me.unei.configuration.api.IConfiguration;

public class LoginSystem
{
	private final GamingBlockPlug plugin;
	
	private HashSet<UUID> currentlyLogin = new HashSet<>();
	
	public LoginSystem(GamingBlockPlug plugin)
	{
		this.plugin = plugin;
	}
	
	public boolean hasAccount(GBPPlayer player)
	{
		IConfiguration cfg = player.getConfig();
		if (!cfg.contains("Password"))
		{
			return false;
		}
		if (cfg.contains("Registered") && cfg.getBoolean("Registered"))
		{
			return true;
		}
		return false;
	}
	
	public void register(GBPPlayer player, String password)
	{
		if (!this.hasAccount(player))
		{
			player.setPassword(password, true);
		}
	}
	
	public void unregister(GBPPlayer player)
	{
		if (this.hasAccount(player))
		{
			player.setPassword(null, false);
		}
	}
	
	public boolean testPass(GBPPlayer player, String password)
	{
		if (password != null && this.hasAccount(player))
		{
			return password.equals(player.getPassword());
		}
		return false;
	}
	
	public void setLogin(GBPPlayer player)
	{
		this.currentlyLogin.add(player.getUniqueId());
	}
	
	public boolean isLogin(UUID player)
	{
		return this.currentlyLogin.contains(player);
	}
	
	public boolean isLogin(PlayerEvent event)
	{
		return this.isLogin(event.getPlayer().getUniqueId());
	}
	
	public boolean isLogin(GBPPlayer player)
	{
		return this.isLogin(player.getUniqueId());
	}
	
	public void endLogin(GBPPlayer player)
	{
		this.currentlyLogin.remove(player.getUniqueId());
	}
	
	public void launchLogin(final GBPPlayer player)
	{
		ConversationFactory factory = new ConversationFactory(this.plugin.getPlugin());
		final HashMap<Object, Object> data = new HashMap<>();
		data.put("tries", Integer.valueOf(0));
		data.put("kick", Boolean.FALSE);
		
		factory.withInitialSessionData(data).withLocalEcho(false).withPrefix(new ConversationPrefix() {
			@Override
			public String getPrefix(ConversationContext context)
			{
				return ChatColor.GREEN + "Login" + ChatColor.RESET + " ";
			}
		}).addConversationAbandonedListener(new ConversationAbandonedListener() {
			@Override
			public void conversationAbandoned(ConversationAbandonedEvent abandonedEvent)
			{
				if(!abandonedEvent.gracefulExit() && !((Boolean) abandonedEvent.getContext().getSessionData("kick")).booleanValue())
				{
					LoginSystem.this.plugin.getLogger().info(player.getHandler().getName()
							+ " didn't login or register until 5 minutes.");
					((Player) player.getHandler()).kickPlayer("You must login until 5 minutes");
				}
			}
		});
		Conversation conv;
		this.plugin.getLogger().info("Starting Login system for " + player.getHandler().getName() + "...");
		this.setLogin(player);
		if (this.hasAccount(player))
		{
			((Player) player.getHandler()).sendMessage(ChatColor.RED + "Please enter your password :");
			conv = factory.withFirstPrompt(new LoginPrompt(this, player)).buildConversation((Player) player.getHandler());
			this.plugin.getLogger().info(player.getHandler().getName() + " has already an account, asking for password...");
		}
		else
		{
			((Player) player.getHandler()).sendMessage(ChatColor.RED + "Register with a new password :");
			conv = factory.withFirstPrompt(new RegisterPrompt(this, player)).buildConversation((Player) player.getHandler());
			this.plugin.getLogger().info(player.getHandler().getName() + " didn't has already an account, asking for new password...");
		}
		conv.begin();
	}
	
	public boolean autoLogin(GBPPlayer player)
	{
		if (PermissionHelper.testPermissionSilent((Player) player.getHandler(), StaticPerms.LOGIN_BYPASS, false))
		{
			player.setIsLogged(true);
			return false;
		}
		if (!this.hasAccount(player) && !PermissionHelper.testPermissionSilent((Player) player.getHandler(), StaticPerms.LOGIN_REGISTER, true))
		{
			((Player) player.getHandler()).kickPlayer("You cannot access this server without an account");
			return false;
		}
		this.launchLogin(player);
		return true;
	}
}
