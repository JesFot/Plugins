package fr.mpp.utils;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemInventory
{	
	public static ItemStack createItem(Material material, String name, String lore)
	{
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		if (name != "")
		{
			meta.setDisplayName(name);
		}
		if (lore != "")
		{
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add(lore);
			meta.setLore(Lore);
		}
		item.setItemMeta(meta);
		return item;
	}
	
	public static void addItemtoInventory(ItemStack item, Inventory inv, int slot)
	{
		inv.setItem(slot, item);
	}
	
	public static void createIteminInv(Material material, Inventory inv, int slot, String name, String lore)
	{
		addItemtoInventory(createItem(material, name, lore), inv, slot);
	}
	
	public static Inventory createInventory(String name, int lines)
	{
		int size = lines*9;
		Inventory inv = Bukkit.createInventory(null, size, name);
		return inv;
	}
	
	public static Inventory createItemandInv(Material material, int slot, String name, String lore, String invName, int lines)
	{
		Inventory inv = createInventory(invName, lines);
		createIteminInv(material, inv, slot, name, lore);
		return inv;
	}
	
	public static void openPlayerInv(Player player, Inventory inv)
	{
		player.openInventory(inv);
	}
}