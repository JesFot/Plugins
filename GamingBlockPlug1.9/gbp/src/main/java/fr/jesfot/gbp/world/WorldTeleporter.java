package fr.jesfot.gbp.world;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.configuration.NBTConfig;
import fr.jesfot.gbp.configuration.NBTSubConfig;
import fr.jesfot.gbp.configuration.NBTTagConfig;
import fr.jesfot.gbp.utils.InventorySerializer;
import fr.jesfot.gbp.utils.Utils;

public class WorldTeleporter
{
	public static void tpToWorld(GamingBlockPlug_1_9 gbp, Player[] players, String worldName)
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
				pl.sendMessage("This dimension (\"" + worldName + "\") cannot be loaded.");
			}
			WorldLoader.unloadWorld(gbp, worldName);
			return;
		}
		Location worldSpawn = world.getSpawnLocation();
		String groupName = worldNext.readNBTFromFile().getCopy().getString("Group");
		if(groupName == "")
		{
			groupName = "_undifined_";
		}
		for(Player player : players)
		{
			World playerWorld = player.getWorld();
			NBTSubConfig current = new NBTSubConfig(gbp.getConfigFolder("worldsdatas"), playerWorld.getName());
			String groupActual = current.readNBTFromFile().getCopy().getString("Group");
			if(groupActual == groupName && groupName != "_undifined_")
			{
				keepInventory = true;
				if(world.getGameRuleValue("keepLastLocation") == "true")
				{
					useLastLocation = true;
				}
				if(world.getGameRuleValue("changeBedSpawn") == "false")
				{
					changeBedSpawn = false;
				}
			}
			NBTConfig playerConfig = new NBTConfig(gbp.getConfigFolder("playerdatas"), player.getUniqueId());
			NBTTagConfig playerWorldConfig = new NBTTagConfig(playerConfig, "WorldsStore."+worldName);
			NBTTagConfig playerAWorldConfig = new NBTTagConfig(playerConfig, "WorldsStore."+playerWorld.getName());
			Location PlayerBed = playerWorldConfig.readNBTFromFile().getLocation("BedSpawn").clone();
			Location lastLocation = playerWorldConfig.getLocation("LastPos").clone();
			playerAWorldConfig.readNBTFromFile().setLocation("BedSpawn", player.getBedSpawnLocation().clone());
			playerAWorldConfig.setLocation("LastPos", player.getLocation().clone()).writeNBTToFile();
			//
			Inventory ender = InventorySerializer.fromNBT(playerWorldConfig, "EnderChest");
			Inventory normal = InventorySerializer.fromNBT(playerWorldConfig, "Normal");
			playerAWorldConfig = InventorySerializer.toNBT(player.getEnderChest(), playerAWorldConfig, "EnderChest");
			playerAWorldConfig = InventorySerializer.toNBT(player.getInventory(), playerAWorldConfig, "Normal");
			//
			int lvls = playerWorldConfig.getCopy().getInt("Lvl");
			playerAWorldConfig.setInteger("Lvl", player.getLevel());
			int food = playerWorldConfig.getCopy().getInt("FoodLvl");
			playerAWorldConfig.setInteger("FoodLvl", player.getFoodLevel());
			double health = playerWorldConfig.getCopy().getDouble("Health");
			playerAWorldConfig.setDouble("Health", player.getHealth()).writeNBTToFile();
			//
			if(!keepInventory)
			{
				player.getEnderChest().clear();
				player.getInventory().clear();
				player.getEnderChest().setContents(ender.getContents());
				player.getInventory().setContents(normal.getContents());
				player.setLevel(lvls);
				player.setFoodLevel(food);
				player.setHealth(health);
			}
			if(changeBedSpawn)
			{
				player.setBedSpawnLocation(PlayerBed, true);
			}
			if(useLastLocation)
			{
				player.teleport(lastLocation, TeleportCause.PLUGIN);
			}
			else
			{
				player.teleport(worldSpawn, TeleportCause.PLUGIN);
			}
			gbp.getEconomy().getPEconomy(player).getStoredInventory();
			if(playerWorld.getPlayers().isEmpty())
			{
				if(playerWorld.getGameRuleValue("autoUnLoad") == "true")
				{
					WorldLoader.unloadWorld(gbp, playerWorld.getName());
				}
			}
		}
	}
	
	public static void tpToWorld(GamingBlockPlug_1_9 gbp, CommandSender sender, String argument, String worldName)
	{
		Player[] pls = {};
		if(argument.startsWith("@"))
		{
			pls = Utils.getPlayers(sender, argument).toArray(new Player[]{});
			if(pls == null)
			{
				sender.sendMessage(ChatColor.RED + gbp.getLang().get("player.notfound"));
			}
		}
		else
		{
			Player pl = (argument!="" ? gbp.getPlayerExact(argument) : ((sender instanceof Player) ? (Player)sender : null));
			pls = new Player[]{pl};
		}
		WorldTeleporter.tpToWorld(gbp, pls, worldName);
	}
}