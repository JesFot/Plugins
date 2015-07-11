package fr.mpp.command;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.BlockCommandSender;
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
				Player pl = null;
				Player pls[] = {};
				if(args.length == 4 && args[3].startsWith("@"))
				{
					Location start = null;
					if(sender instanceof BlockCommandSender)
					{
						BlockCommandSender commandB = (BlockCommandSender)sender;
						start = commandB.getBlock().getLocation();
					}
					else if(sender instanceof Player)
					{
						Player myPlayer = (Player)sender;
						start = myPlayer.getLocation();
					}
					else
					{
						if(args[3].startsWith("@p"))
						{
							sender.sendMessage("Nope.");
							return true;
						}
						else
						{
							start = new Location(this.mpp.getServer().getWorlds().get(0), 0, 0, 0);
						}
					}
					pls = MPlayer.getPlayerByRep(args[3], start);
					if(pls.length == 1)
					{
						this.mpp.broad("[DEBUG] pls.length == 1");
						pl = pls[0];
					}
				}
				else
				{
					pl = (args.length==4 ? MPlayer.getPlayerByName(args[3]) : ((sender instanceof Player) ? (Player)sender : null));
				}
				World world = this.mpp.getServer().getWorld(w);
				Location spawn = world.getSpawnLocation();
				if(pl != null)
				{
					this.mpp.broad("[DEBUG] pl != null");
					Location old = (world.getPlayers().contains(pl) ? world.getPlayers().get(world.getPlayers().indexOf(pl)).getLocation() : spawn);
					pl.teleport(old);
				}
				else if(pls.length >= 2)
				{
					this.mpp.broad("[DEBUG] pls.length >= 2");
					for(Player p : pls)
					{
						Location old = (world.getPlayers().contains(p) ? world.getPlayers().get(world.getPlayers().indexOf(p)).getLocation() : spawn);
						p.teleport(old);
					}
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
