package fr.mpp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mpp.MetalPonyPlug;
import fr.mpp.mpp.ClassesUtils;
import fr.mpp.mpp.RankLevel;
import fr.mpp.utils.MPlayer;

public class TestmppCommand implements CommandExecutor
{
	private MetalPonyPlug mpp;
	
	public TestmppCommand(MetalPonyPlug mpp)
	{
		this.mpp = mpp;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (label.equalsIgnoreCase("testmpp"))
		{
			if(args.length == 0)
			{
				//
			}
			else if (args[0].equalsIgnoreCase("setting") && args[1].equalsIgnoreCase("set"))
			{
				String truc = args[2];
				if (truc.equalsIgnoreCase("affich"))
				{
					String afiich = args[3];
					String pl = args[4];
					Player player = MPlayer.getPlayerByName(pl);
					ClassesUtils cu = new ClassesUtils(this.mpp.getConfig());
					cu.setRankAffich(player, RankLevel.getRankByName(afiich));
				}
				else if (truc.equalsIgnoreCase("affichi"))
				{
					int afiich = Integer.parseInt(args[3]);
					String pl = args[4];
					Player player = MPlayer.getPlayerByName(pl);
					ClassesUtils cu = new ClassesUtils(this.mpp.getConfig());
					cu.setRankAffich(player, RankLevel.getRankByID(afiich));
				}
			}
			
			String msg;
			if (args.length >= 1)
			{
				msg = "Et votre texte : " + args[0];
			}
			else
			{
				msg = "";
			}
			sender.sendMessage("Cette commande et donc le plugin fonctionnent." + msg);
			return true;
		}
		return false;
	}

}
