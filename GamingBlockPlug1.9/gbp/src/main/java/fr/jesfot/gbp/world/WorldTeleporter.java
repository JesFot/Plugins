package fr.jesfot.gbp.world;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.configuration.NBTConfig;
import fr.jesfot.gbp.configuration.NBTSubConfig;
import fr.jesfot.gbp.utils.InventorySerializer;
import fr.jesfot.gbp.utils.Utils;
import net.minecraft.server.v1_9_R2.NBTTagCompound;

public class WorldTeleporter
{
	public static boolean tpToWorld(GamingBlockPlug_1_9 gbp, Player[] players, String worldName)
	{
		boolean keepInventory = false;
		boolean useLastLocation = false;
		boolean changeBedSpawn = true;
		NBTSubConfig worldNext = new NBTSubConfig(gbp.getConfigFolder("worldsdatas"), worldName);
		World world = gbp.getServer().getWorld(worldName);
		if(world == null)
		{
			for(Player pl : players)
			{
				pl.sendMessage("This dimension (\"" + worldName + "\") is not loaded.");
			}
			WorldLoader.unloadWorld(gbp, worldName);
			return false;
		}
		Location worldSpawn = world.getSpawnLocation();
		String groupName = worldNext.readNBTFromFile().getCopy().getString("Group");
		if(groupName == "")
		{
			groupName = "_undifined_";
		}
		for(Player player : players)
		{
			if(player == null)
			{
				continue;
			}
			World playerWorld = player.getWorld();
			if(WorldComparator.isEqualWorld(playerWorld, world, gbp))
			{
				break;
			}
			NBTSubConfig current = new NBTSubConfig(gbp.getConfigFolder("worldsdatas"), playerWorld.getName());
			String groupActual = current.readNBTFromFile().getCopy().getString("Group");
			if(groupActual.contentEquals(groupName) && groupName != "_undifined_")
			{
				//player.sendMessage("[DEBUG] Group name="+groupActual);
				//player.sendMessage("[DEBUG] keep Loc="+WorldComparator.getKeepLocation(gbp, worldName));
				//player.sendMessage("[DEBUG] Change Bed="+WorldComparator.getChangeBedSpawn(gbp, worldName));
				if(WorldComparator.getKeepInventory(gbp, worldName))
				{
					keepInventory = true;
				}
				if(WorldComparator.getKeepLocation(gbp, worldName))
				{
					useLastLocation = true;
				}
				if(!WorldComparator.getChangeBedSpawn(gbp, worldName))
				{
					changeBedSpawn = false;
				}
			}
			NBTConfig playerConfig = new NBTConfig(gbp.getConfigFolder("playerdatas"), player.getUniqueId());
			NBTSubConfig playerWorldsStoreConfig = new NBTSubConfig(playerConfig, ("WorldsStore")).readNBTFromFile();
			NBTTagCompound playerAWorldConfig = playerWorldsStoreConfig.getCopy().getCompound(playerWorld.getName());
			NBTTagCompound playerWorldConfig = playerWorldsStoreConfig.getCopy().getCompound(worldName);
			Location PlayerBed = Utils.getLocation("BedSpawn", playerWorldConfig);
			Location lastLocation = Utils.getLocation("LastPos", playerWorldConfig);
			Utils.setLocation("BedSpawn", player.getBedSpawnLocation(), playerAWorldConfig);
			Utils.setLocation("LastPos", player.getLocation().clone(), playerAWorldConfig);
			//
			Inventory ender = InventorySerializer.fromNBT(playerWorldConfig, "EnderChest");
			Inventory normal = InventorySerializer.fromNBT(playerWorldConfig, "Normal");
			playerAWorldConfig = InventorySerializer.toNBT(player.getEnderChest(), playerAWorldConfig, "EnderChest");
			playerAWorldConfig = InventorySerializer.toNBT(player.getInventory(), playerAWorldConfig, "Normal");
			//
			int lvls = playerWorldConfig.getInt("Lvl");
			playerAWorldConfig.setInt("Lvl", player.getLevel());
			int food = playerWorldConfig.getInt("FoodLvl");
			playerAWorldConfig.setInt("FoodLvl", player.getFoodLevel());
			double tmp = 0;
			double health = (tmp=playerWorldConfig.getDouble("Health"))!=0?tmp:20;
			playerAWorldConfig.setDouble("Health", player.getHealth());
			//
			playerWorldsStoreConfig.setTag(playerWorld.getName(), playerAWorldConfig).writeNBTToFile();
			//
			if(!keepInventory)
			{
				player.getEnderChest().clear();
				player.getInventory().clear();
				if(ender!=null)
					player.getEnderChest().setContents(ender.getContents());
				if(normal!=null)
					player.getInventory().setContents(normal.getContents());
				player.setLevel(lvls);
				player.setFoodLevel(food==0?player.getFoodLevel():food);
				player.setHealth(health);
				gbp.getEconomy().getPEconomy(player).storeInventory();
				gbp.getEconomy().getPEconomy(player).getStoredInventory();
			}
			if(changeBedSpawn && PlayerBed != null)
			{
				player.setBedSpawnLocation(PlayerBed, true);
			}
			if(useLastLocation && lastLocation != null)
			{
				player.teleport(lastLocation, TeleportCause.PLUGIN);
			}
			else
			{
				player.teleport(worldSpawn, TeleportCause.PLUGIN);
			}
			if(WorldComparator.getDefaultGamemode(gbp, worldName) != "NaN")
			{
				player.setGameMode(GameMode.valueOf(WorldComparator.getDefaultGamemode(gbp, worldName)));
			}
			gbp.getEconomy().getPEconomy(player).getStoredInventory();
			if(playerWorld.getPlayers().isEmpty())
			{
				if(WorldComparator.getAutoUnload(gbp, playerWorld.getName()))
				{
					//player.sendMessage("[DEBUG] Auto-unload.");
					gbp.getLogger().info("[Worlds' Manager] Auto-unloading world " + playerWorld.getName() + ".");
					WorldLoader.unloadWorld(gbp, playerWorld.getName());
				}
			}
		}
		return true;
	}
	
	public static boolean tpToWorld(GamingBlockPlug_1_9 gbp, CommandSender sender, String argument, String worldName)
	{
		Player[] pls = {};
		if(argument.startsWith("@"))
		{
			pls = Utils.getPlayers(sender, argument).toArray(new Player[]{});
			if(pls == null)
			{
				sender.sendMessage(ChatColor.RED + gbp.getLang().get("player.notfound"));
				return false;
			}
		}
		else
		{
			Player pl = (argument!="" ? gbp.getPlayerExact(argument) : ((sender instanceof Player) ? (Player)sender : null));
			if(pl == null)
			{
				sender.sendMessage("Be a player or give a player as argument please.");
				return false;
			}
			pls = new Player[]{pl};
		}
		return WorldTeleporter.tpToWorld(gbp, pls, worldName);
	}
}