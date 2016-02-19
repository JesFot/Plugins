package fr.gbp.utils;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryStringSerializer
{
	//@SuppressWarnings("deprecation")
	public static String inventoryToString(Inventory p_inventory)
	{
		String serialization = p_inventory.getSize() + ";";
		for(int i = 0; i < p_inventory.getSize(); i++)
		{
			ItemStack iS = p_inventory.getItem(i);
			if(iS != null)
			{
				String serializedItemStack = new String();
				String iSType = String.valueOf(iS.getType().toString());
				serializedItemStack += "t@" + iSType;
				
				if(iS.getDurability() != 0)
				{
					String iSDurab = String.valueOf(iS.getDurability());
					serializedItemStack += ":d@" + iSDurab;
				}
				if(iS.getAmount() != 1)
				{
					String iSAmount = String.valueOf(iS.getAmount());
					serializedItemStack += ":a@" + iSAmount;
				}
				Map<Enchantment, Integer> iSEnch = iS.getEnchantments();
				if(iSEnch.size() > 0)
				{
					for(Entry<Enchantment, Integer> ench : iSEnch.entrySet())
					{
						serializedItemStack += ":e@" + ench.getKey().getName() + "@" + ench.getValue();
					}
				}
				serialization += i + "&" + serializedItemStack + ";";
			}
		}
		return serialization;
	}
	
	//@SuppressWarnings("deprecation")
	public static Inventory stringToInventory(String p_invString, String name)
	{
		String[] serializedBlocks = p_invString.split(";");
		String invInfo = serializedBlocks[0];
		Inventory deserializedInventory = ItemInventory.createInventory(name, Integer.valueOf(invInfo)/9);
		
		for(int i = 1; i < serializedBlocks.length; i++)
		{
			String[] serializedBlock = serializedBlocks[i].split("&");
			int stackPos = Integer.valueOf(serializedBlock[0]);
			
			if(stackPos >= deserializedInventory.getSize())
			{
				continue;
			}
			ItemStack iS = null;
			boolean createdIs = false;
			
			String[] serializedIS = serializedBlock[1].split(":");
			for(String itemInfo : serializedIS)
			{
				String[] itemAttribute = itemInfo.split("@");
				if(itemAttribute[0].equals("t"))
				{
					iS = new ItemStack(Material.getMaterial(itemAttribute[1]));
					createdIs = true;
				}
				else if(itemAttribute[0].equals("d") && createdIs)
				{
					iS.setDurability(Short.valueOf(itemAttribute[1]));
				}
				else if(itemAttribute[0].equals("a") && createdIs)
				{
					iS.setAmount(Integer.valueOf(itemAttribute[1]));
				}
				else if(itemAttribute[0].equals("e") && createdIs)
				{
					iS.addUnsafeEnchantment(Enchantment.getByName(itemAttribute[1]), Integer.valueOf(itemAttribute[2]));
					//iS.addEnchantment(Enchantment.getById(Integer.valueOf(itemAttribute[1])), Integer.valueOf(itemAttribute[2]));
				}
			}
			deserializedInventory.setItem(stackPos, iS);
		}
		
		return deserializedInventory;
	}
}