package fr.mpp.command;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
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
			if (args[0].equalsIgnoreCase("setting") && args[1].equalsIgnoreCase("set"))
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
			else if(args[0].equalsIgnoreCase("world") && args[1].equalsIgnoreCase("set"))
			{
				String w = args[2];
				String act = args[3];
				if(act.equalsIgnoreCase("load"))
				{
					this.mpp.getServer().createWorld(new WorldCreator(w));
				}
				else if(act.equalsIgnoreCase("unload"))
				{
					this.mpp.getServer().unloadWorld(w, true);
				}
			}
			else if(args[0].equalsIgnoreCase("world") && args[1].equalsIgnoreCase("tp"))
			{
				String w = args[2];
				World world = this.mpp.getServer().getWorld(w);
				if(sender instanceof Player)
				{
					Player p = (Player)sender;
					p.getLocation()/*.setWorld(world)*/;
					Location spawn = world.getSpawnLocation();
					p.teleport(spawn);
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
