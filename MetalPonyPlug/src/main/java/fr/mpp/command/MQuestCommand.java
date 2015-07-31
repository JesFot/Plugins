package fr.mpp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.mpp.MetalPonyPlug;
import fr.mpp.quests.MQuest;

public class MQuestCommand implements CommandExecutor
{
	private MetalPonyPlug mpp;
	@SuppressWarnings("unused")
	private MQuest quest;
	private String usageMessage = ChatColor.DARK_RED + "Usage: /<cmd> <help|list|create|take|info|edit> ...";
	
	private void sendUsageMessage(CommandSender sender, String label)
	{
		sender.sendMessage(ChatColor.DARK_RED + "======================= Usage: ============================");
		sender.sendMessage(ChatColor.DARK_GREEN + "/"+label+" help - Show this help.");
		sender.sendMessage(ChatColor.DARK_GREEN + "/"+label+" list - Show all quests.");
		sender.sendMessage(ChatColor.DARK_GREEN + "/"+label+" create <QuestName> ...- Create a quest (Not Available)");
		sender.sendMessage(ChatColor.DARK_GREEN + "/"+label+" take <QuestName|QuestID> - take a quest.");
		sender.sendMessage(ChatColor.DARK_GREEN + "/"+label+" info <QuestName|QuestID> - Show infos about a quest.");
		sender.sendMessage(ChatColor.DARK_GREEN + "/"+label+" edit <QuestName|QuestID> ...- Edit infos about a quest.");
		sender.sendMessage(ChatColor.DARK_RED + "===========================================================");
	}
	
	public MQuestCommand(MetalPonyPlug mppl)
	{
		this.mpp = mppl;
		this.quest = this.mpp.getQuest();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!cmd.getName().equalsIgnoreCase("quest"))
		{
			return false;
		}
		this.usageMessage = this.usageMessage.replace("/<cmd>", "/"+label);
		if(args.length == 0)
		{
			sender.sendMessage(this.usageMessage);
			return true;
		}
		if(args.length >= 1)
		{
			if(args[0].equalsIgnoreCase("help"))
			{
				this.sendUsageMessage(sender, label);
			}
			else if(args[0].equalsIgnoreCase("list"))
			{
				//
			}
			else if(args.length == 1)
			{
				sender.sendMessage(this.usageMessage);
				return true;
			}
		}
		if(args.length >= 2)
		{
			if(args[0].equalsIgnoreCase("take"))
			{
				//
			}
			else if(args[0].equalsIgnoreCase("info"))
			{
				//
			}
			else if(args.length == 2)
			{
				sender.sendMessage(this.usageMessage);
				return true;
			}
		}
		if(args.length >= 5)
		{
			//
		}
		sender.sendMessage(this.usageMessage);
		this.usageMessage = this.usageMessage.replace("/"+label, "/<cmd>");
		return true;
	}
}