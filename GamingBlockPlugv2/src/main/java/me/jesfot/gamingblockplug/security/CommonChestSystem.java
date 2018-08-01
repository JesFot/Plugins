package me.jesfot.gamingblockplug.security;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;
import me.jesfot.gamingblockplug.utils.ItemInventory;
import me.unei.configuration.api.IConfiguration;

public class CommonChestSystem
{
	private final GamingBlockPlug plugin;
	
	private Inventory sharedInventory;
	
	public CommonChestSystem(GamingBlockPlug plugin)
	{
		this.plugin = plugin;
	}
	
	public void init()
	{
		this.sharedInventory = ItemInventory.createInventory("Shared", 6);
	}
	
	public Inventory getSharedInventory()
	{
		if (this.sharedInventory == null)
		{
			this.init();
		}
		return this.sharedInventory;
	}
	
	public void saveToChest(Chest chest)
	{
		chest.getInventory().clear();
		chest.getInventory().setContents(this.getSharedInventory().getContents());
	}
	
	public void saveToConfig(IConfiguration config)
	{
		Inventory inv = this.getSharedInventory();
		config.setInteger("Size", inv.getSize());
		config.setInteger("MaxStackSize", inv.getMaxStackSize());
		config.setString("Name", inv.getName());
		for (int i = 0; i < inv.getSize(); ++i)
		{
			IConfiguration stack = config.getSubSection("SLOT_" + i);
			stack.setByte("Slot", (byte) i);
			stack.setInteger("Count", inv.getItem(i).getAmount());
			stack.setShort("Damage", inv.getItem(i).getDurability());
			stack.setString("id", inv.getItem(i).getType().name());
		}
	}
	
	public void loadFromConfig(IConfiguration config)
	{
		int size = config.getInteger("Size");
		String name = config.getString("Name");
		this.sharedInventory = this.plugin.getServer().createInventory(null, size, name);
		this.sharedInventory.setMaxStackSize(config.getInteger("MawStackSize"));
		for (int i = 0; i < this.sharedInventory.getSize(); ++i)
		{
			IConfiguration stack = config.getSubSection("SLOT_" + i);
			Material mat = Material.getMaterial(stack.getString("id"));
			short damage = stack.getShort("Damage");
			int count = stack.getInteger("Count");
			ItemStack is = new ItemStack(mat, count, damage);
			this.sharedInventory.setItem(stack.getByte("Slot"), is);
		}
	}
	
	public void loadFromChest(Chest chest)
	{
		this.getSharedInventory().clear();
		this.getSharedInventory().setContents(chest.getInventory().getContents());
	}
}
