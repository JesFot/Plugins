package fr.jesfot.gbp.subsytems;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.utils.Utils;
import net.minecraft.server.v1_9_R2.EntityPlayer;
import net.minecraft.server.v1_9_R2.EnumItemSlot;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_9_R2.PacketPlayOutHeldItemSlot;
import net.minecraft.server.v1_9_R2.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_9_R2.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_9_R2.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_9_R2.PacketPlayOutPosition;
import net.minecraft.server.v1_9_R2.PacketPlayOutRespawn;
import net.minecraft.server.v1_9_R2.PlayerConnection;
import net.minecraft.server.v1_9_R2.WorldSettings.EnumGamemode;

public class SkinRestoreSys
{
	private GamingBlockPlug_1_9 gbp;
	protected static final String uuidurl = "https://api.mojang.com/users/profiles/minecraft/";
	public static String ALT_UUID_URL = "http://mcapi.ca/uuid/player/";
	protected static final String skinUrl = "https://sessionserver.mojang.com/session/minecraft/profile/";
	
	public SkinRestoreSys(GamingBlockPlug_1_9 plugin)
	{
		this.gbp = plugin;
	}
	
	public void MAJSkin(Player player)
	{
		try
		{
			Property props = getSkinProperty(getUUID(player.getName()));
			this.applySkin(player, props);
			this.updateSkin(player);
		}
		catch (Exception e)
		{
			this.gbp.getLogger().warning("Error '" + e.getMessage() + "' is not to report :");
			e.printStackTrace();
		}
	}
	
	public void applySkin(Player pl, Property props)
	{
		this.gbp.getLogger().info("Apllying Minecraft's skin for " + pl.getName() + "...");
		PropertyMap propmap = ((CraftPlayer)pl).getHandle().getProfile().getProperties();
		propmap.clear();
		propmap.put("textures", props);
	}
	
	@SuppressWarnings({"deprecation"})
	public void updateSkin(Player pl)
	{
		this.gbp.getLogger().info("Starting updating " + pl.getName() + "'s skin...");
		try
		{
			if(!pl.isOnline())
			{
				return;
			}
			CraftPlayer cplayer = (CraftPlayer)pl;
			EntityPlayer eplayer = cplayer.getHandle();
			int entityID = eplayer.getId();
			
			PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, eplayer);
			PacketPlayOutEntityDestroy removeEntity = new PacketPlayOutEntityDestroy(entityID);
			PacketPlayOutNamedEntitySpawn addNamed = new PacketPlayOutNamedEntitySpawn(eplayer);
			PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, eplayer);
			
			PacketPlayOutRespawn respawn = new PacketPlayOutRespawn(eplayer.getWorld().worldProvider
					.getDimensionManager().getDimensionID(),
					eplayer.getWorld().getDifficulty(), eplayer.getWorld().worldData.getType(),
					EnumGamemode.getById(pl.getGameMode().getValue()));
			
			PacketPlayOutPosition pos = new PacketPlayOutPosition(pl.getLocation().getX(), pl.getLocation().getY(),
					pl.getLocation().getZ(), pl.getLocation().getYaw(), pl.getLocation().getPitch(),
					new HashSet<PacketPlayOutPosition.EnumPlayerTeleportFlags>(), 0);
			
			PacketPlayOutEntityEquipment itemHand = new PacketPlayOutEntityEquipment(entityID, EnumItemSlot.MAINHAND,
					CraftItemStack.asNMSCopy(pl.getInventory().getItemInMainHand()));

			PacketPlayOutEntityEquipment itemOffHand = new PacketPlayOutEntityEquipment(entityID, EnumItemSlot.OFFHAND,
					CraftItemStack.asNMSCopy(pl.getInventory().getItemInOffHand()));

			PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(entityID, EnumItemSlot.HEAD,
					CraftItemStack.asNMSCopy(pl.getInventory().getHelmet()));

			PacketPlayOutEntityEquipment chestplate = new PacketPlayOutEntityEquipment(entityID, EnumItemSlot.CHEST,
					CraftItemStack.asNMSCopy(pl.getInventory().getChestplate()));

			PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(entityID, EnumItemSlot.LEGS,
					CraftItemStack.asNMSCopy(pl.getInventory().getLeggings()));

			PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(entityID, EnumItemSlot.FEET,
					CraftItemStack.asNMSCopy(pl.getInventory().getBoots()));
			
			PacketPlayOutHeldItemSlot slot = new PacketPlayOutHeldItemSlot(pl.getInventory().getHeldItemSlot());
			
			for(Player inWorld : pl.getWorld().getPlayers())
			{
				final CraftPlayer craftOnline = (CraftPlayer)inWorld;
				PlayerConnection con = craftOnline.getHandle().playerConnection;
				this.gbp.getLogger().info("Sending packets for " + inWorld.getName() + "...");
				if(inWorld.equals(pl))
				{
					con.sendPacket(removeInfo);
					con.sendPacket(addInfo);
					con.sendPacket(respawn);
					con.sendPacket(pos);
					con.sendPacket(slot);
					craftOnline.updateScaledHealth();
					craftOnline.getHandle().triggerHealthUpdate();
					craftOnline.updateInventory();
					Bukkit.getScheduler().runTask(this.gbp.getPlugin(), new Runnable(){
						public void run()
						{
							craftOnline.getHandle().updateAbilities();
						}
					});
					continue;
				}
				con.sendPacket(removeEntity);
				con.sendPacket(removeInfo);
				if(inWorld.canSee(pl))
				{
					con.sendPacket(addInfo);
					con.sendPacket(addNamed);
					con.sendPacket(itemHand);
					con.sendPacket(itemOffHand);
					con.sendPacket(helmet);
					con.sendPacket(chestplate);
					con.sendPacket(leggings);
					con.sendPacket(boots);
				}
			}
		}
		catch(Exception e)
		{
			// ignore
		}
	}
	
	public static String getUUID(String name) throws Exception
	{
		GamingBlockPlug_1_9.getMe().getLogger().info("Getting " + name + "'s uuid from Minecraft's server...");
		String output = Utils.readUrl(uuidurl + name);

		String idbeg = "\"id\":\"";
		String idend = "\"}";

		if (output.isEmpty() || output.contains("TooManyRequestsException"))
		{
			output = Utils.readUrl(ALT_UUID_URL + name).replace(" ", "");

			idbeg = "\"uuid\":\"";
			idend = "\",\"id\":";

			String response = Utils.getStringBetween(output, idbeg, idend);

			if(response.startsWith("[{\"uuid\":null"))
			{
				throw new Exception("No premium account !");
			}
			
			GamingBlockPlug_1_9.getMe().getLogger().info("Resolved uuid '" + output.substring(7, 39)
				+ "' for " + name);
			return response;
		}
		GamingBlockPlug_1_9.getMe().getLogger().info("Resolved uuid '" + output.substring(7, 39)
		+ "' for " + name);
		return output.substring(7, 39);
	}
	
	public static Property getSkinProperty(String uuid) throws Exception
	{
		String output = Utils.readUrl(skinUrl + uuid + "?unsigned=false");
		
		String sigbeg = "[{\"signature\":\"";
		String mid = "\",\"name\":\"textures\",\"value\":\"";
		String valend = "\"}]";
		
		String value;
		
		if(output == null || output.isEmpty() || output.contains("TooManyRequestsException"))
		{
			throw new Exception("Failed to connect mojang's servers");
		}
		
		value = Utils.getStringBetween(output, mid, valend).replace("\\/", "/");
		String signature = Utils.getStringBetween(output, sigbeg, mid).replace("\\/", "/");
		GamingBlockPlug_1_9.getMe().getLogger().info("Resolved skin for '" + uuid + "'");
		return new Property("textures", value, signature);
	}
}