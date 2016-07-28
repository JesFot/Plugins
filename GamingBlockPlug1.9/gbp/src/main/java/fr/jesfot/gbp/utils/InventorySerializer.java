package fr.jesfot.gbp.utils;

import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_9_R2.NBTTagCompound;
import net.minecraft.server.v1_9_R2.NBTTagList;

public class InventorySerializer
{
	public static NBTTagCompound toNBT(Inventory inv, NBTTagCompound nbt, String specialName)
	{
		if(inv == null)
		{
			nbt.remove(specialName);
			return nbt;
		}
		String name = inv.getName();
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < inv.getSize(); i++)
		{
			if(inv.getItem(i) != null)
			{
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("Slot", (byte)i);
				InventorySerializer.getItemStack(inv.getItem(i)).save(compound);
				list.add(compound);
			}
		}
		NBTTagCompound cp = new NBTTagCompound();
		cp.set("Contents", list);
		cp.setInt("Size", inv.getSize());
		cp.setInt("MaxStackSize", inv.getMaxStackSize());
		cp.setString("Name", name);
		nbt.set(specialName, cp);
		return nbt;
	}
	
	public static NBTTagCompound toNBT(Inventory inv, NBTTagCompound nbt)
	{
		if(inv == null)
		{
			return nbt;
		}
		String name = inv.getName();
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < inv.getSize(); i++)
		{
			if(inv.getItem(i) != null)
			{
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("Slot", (byte)i);
				InventorySerializer.getItemStack(inv.getItem(i)).save(compound);
				list.add(compound);
			}
		}
		NBTTagCompound cp = new NBTTagCompound();
		cp.set("Contents", list);
		cp.setInt("Size", inv.getSize());
		cp.setInt("MaxStackSize", inv.getMaxStackSize());
		cp.setString("Name", name);
		nbt.set(name, cp);
		return nbt;
	}
	
	public static Inventory fromNBT(NBTTagCompound nbt, String name)
	{
		if(!nbt.hasKey(name))
		{
			return null;
		}
		NBTTagCompound cp = ((NBTTagCompound)nbt.clone()).getCompound(name);
		String invName = cp.getString("Name");
		if(invName == "")
		{
			return null;
		}
		int size = cp.getInt("Size"), maxSize = cp.getInt("MaxStackSize");
		NBTTagList list = cp.getList("Contents", cp.getTypeId());
		Inventory inv = ItemInventory.createInventory(invName, size / 9);
		inv.setMaxStackSize(maxSize);
		for(int i = 0; i < list.size(); i++)
		{
			NBTTagCompound compound = list.get(i);
			int j = compound.getByte("Slot") & 0xFF;
			ItemStack itemstack = CraftItemStack.asBukkitCopy(net.minecraft.server.v1_9_R2.ItemStack.createStack(compound));
			
			if(itemstack != null)
			{
				if((j >= 0) && (j < inv.getSize()))
				{
					inv.setItem(j, itemstack);
				}
			}
		}
		return inv;
	}
	
	private static net.minecraft.server.v1_9_R2.ItemStack getItemStack(ItemStack item)
	{
		return CraftItemStack.asNMSCopy(item);
	}
}