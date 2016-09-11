package fr.jesfot.gbp.shop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Sign;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.configuration.NBTConfig;
import fr.jesfot.gbp.configuration.NBTSubConfig;
import fr.jesfot.gbp.permission.Permissions;
import net.minecraft.server.v1_9_R2.NBTTagCompound;

public class Shops
{
	private GamingBlockPlug_1_9 gbp;
	private HashMap<Location, ShopObject> allShops = new HashMap<Location, ShopObject>();
	
	public Shops(GamingBlockPlug_1_9 plugin)
	{
		this.gbp = plugin;
		Permission shop = plugin.getPermissionManager().addPermission("GamingBlockPlug.shops", PermissionDefault.OP, "Shops' permission", Permissions.globalGBP);
		plugin.getPermissionManager().addPermission("GamingBlockPlug.shops.use", PermissionDefault.TRUE, "Allows you to use shops", shop);
		plugin.getPermissionManager().addPermission("GamingBlockPlug.shops.create", PermissionDefault.TRUE, "Allows you to create shops", shop);
		plugin.getPermissionManager().addPermission("GamingBlockPlug.shops.destroy", PermissionDefault.TRUE, "Allows you to destroy your shops", shop);
		plugin.getPermissionManager().addPermission("GamingBlockPlug.shops.op", PermissionDefault.OP, "Allows you to do everythings with shops", shop);
	}
	
	public ShopObject getShop(Location loc)
	{
		return this.allShops.get(loc);
	}
	
	public void addShop(ShopObject shop)
	{
		this.allShops.put(shop.getLocation(), shop);
	}
	
	public boolean removeShop(ShopObject shop)
	{
		if(this.allShops.containsKey(shop.getLocation()))
		{
			this.allShops.remove(shop.getLocation());
			shop.delete();
			return true;
		}
		return false;
	}
	
	public void subDeleteAll()
	{
		for(Entry<Location, ShopObject> e : this.allShops.entrySet())
		{
			e.getValue().delete();
		}
	}
	
	private ArrayList<ShopObject> getOrderedShopList()
	{
		ArrayList<ShopObject> list = new ArrayList<ShopObject>(this.allShops.values());
		Collections.sort(list, new Comparator<ShopObject>()
		{
			public int compare(ShopObject shop1, ShopObject shop2)
			{
				return shop1.getOwner().getName().compareToIgnoreCase(shop2.getOwner().getName());
			}
		});
		return list;
	}
	
	public int getNumberOfShops()
	{
		return this.allShops.size();
	}
	
	public void saveShops()
	{
		NBTSubConfig shopsConfigFile = new NBTSubConfig(this.gbp.getConfigFolder(null), "Shops");
		
		ArrayList<ShopObject> shopList = this.getOrderedShopList();
		
		int shopNumber = 1;
		for(int i = 0; i < shopList.size(); i++)
		{
			ShopObject s = shopList.get(i);
			
			NBTTagCompound ownerConfig = shopsConfigFile.getCopy().getCompound(s.getOwner().getUniqueId().toString());
			ownerConfig.setString("Name", s.getOwner().getName());
			NBTTagCompound shopConfig = ownerConfig.getCompound("_" + shopNumber);
			NBTConfig.setLocation(shopConfig, "Sign_Location", s.getSignLocation());
			shopConfig.setDouble("Price", s.getPrice());
			shopConfig.setInt("Amount", s.getAmount());
			shopConfig.setInt("Times_Used", s.getTimesUsed());
			NBTTagCompound itemConfig = shopConfig.getCompound("Item");
			ItemStack is = s.getItem();
			ItemMeta im = is.getItemMeta();
			if(im.getDisplayName() != null)
			{
				itemConfig.setString("Name", im.getDisplayName());
			}
			else
			{
				itemConfig.setString("Name", "");
			}
			itemConfig.setShort("Durability", is.getDurability());
			NBTConfig.setMaterialData(itemConfig, "Data", is.getData());
			NBTConfig.setEnchantments(itemConfig, "enchs", is.getEnchantments());
			NBTConfig.setLore(itemConfig, "Lore", im.getLore());
			shopConfig.set("Item", itemConfig);
			ownerConfig.set("_" + shopNumber, shopConfig);
			shopsConfigFile.setTag(s.getOwner().getUniqueId().toString(), ownerConfig);
			shopsConfigFile.writeNBTToFile();
			
			//s.delete();
			
			shopNumber++;
			if((i < shopList.size() - 1) && (!s.getOwner().equals(shopList.get(i + 1).getOwner())))
			{
				shopNumber = 1;
			}
		}
	}
	
	public void loadShops()
	{
		NBTConfig shopsFile = new NBTConfig(this.gbp.getConfigFolder(null), "Shops");
		NBTConfig shopsBackupFile = new NBTConfig(this.gbp.getConfigFolder(null), "ShopsBackup");
		if(!shopsBackupFile.existFile())
		{
			shopsBackupFile.setCopy(shopsFile.readNBTFromFile().getCopy()).writeNBTToFile();
		}
		this.loadShopsFromConfig(new NBTSubConfig(shopsFile));
	}
	
	private void loadShopsFromConfig(NBTSubConfig shopsConfigFile)
	{
		shopsConfigFile.readNBTFromFile();
		
		Set<String> ShopsOwnersUUID = shopsConfigFile.getCopy().c();
		
		for(Iterator<String> it1 = ShopsOwnersUUID.iterator(); it1.hasNext();)
		{
			UUID uid = UUID.fromString(it1.next());
			OfflinePlayer shopOwner = this.gbp.getServer().getOfflinePlayer(uid);
			
			Set<String> shopsId = shopsConfigFile.getCopy().getCompound(uid.toString()).c();
			for(Iterator<String> it2 = shopsId.iterator(); it2.hasNext();)
			{
				String shopID = it2.next();
				if(shopID.equals("Name"))
				{
					continue;
				}
				NBTTagCompound shopConfig = shopsConfigFile.getCopy().getCompound(uid.toString()).getCompound(shopID);
				Location signLocation = NBTConfig.getLocation(shopConfig, "Sign_Location");
				Block block = signLocation.getBlock();
				
				if(block.getType().equals(Material.WALL_SIGN))
				{
					Sign sign = (Sign)block.getState().getData();
					Location loc = block.getRelative(sign.getAttachedFace()).getLocation();
					double price = shopConfig.getDouble("Price");
					int amount = shopConfig.getInt("Amount");
					int timesUsed = shopConfig.getInt("Times_Used");
					
					NBTTagCompound itemConfig = shopConfig.getCompound("Item");
					MaterialData data = NBTConfig.getMaterialData(itemConfig, "Data");
					ItemStack is = new ItemStack(data.getItemType());
					is.setData(data);
					short durab = itemConfig.getShort("Durability");
					is.setDurability(durab);
					ItemMeta im = is.getItemMeta();
					String name = itemConfig.getString("Name");
					if(!name.isEmpty())
					{
						im.setDisplayName(name);
					}
					List<String> lore = NBTConfig.getLore(itemConfig, "Lore");
					if(lore.size() > 1)
					{
						im.setLore(lore);
					}
					is.setItemMeta(im);
					is.addUnsafeEnchantments(NBTConfig.getEnchantments(itemConfig, "enchs"));
					
					ShopObject shop = new ShopObject(shopOwner, price, amount, timesUsed, loc, signLocation, is);
					shop.updateSign();
					this.addShop(shop);
				}
			}
		}
	}
}