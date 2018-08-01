package fr.jesfot.gbp.utils;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryPlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;

public class InventorySerializer
{
	public static NBTTagCompound toNBT(Inventory inv, NBTTagCompound nbt, String specialName)
	{
		boolean player = (inv instanceof PlayerInventory);
		PlayerInventory pinv = player ? (PlayerInventory) inv : null;
		if (inv == null)
		{
			nbt.remove(specialName);
			return nbt;
		}
		String name = inv.getName();
		NBTTagList list = new NBTTagList();
		if (player)
		{
			((CraftInventoryPlayer) pinv).getInventory().a(list);
		}
		else
		{
			for (int i = 0; i < (player ? inv.getSize() - 5 : inv.getSize()); i++)
			{
				if (inv.getItem(i) != null)
				{
					NBTTagCompound compound = new NBTTagCompound();
					compound.setByte("Slot", (byte) i);
					compound.setInt("Count", inv.getItem(i).getAmount());
					compound.setShort("Damage", inv.getItem(i).getDurability());
					compound.setString("id", inv.getItem(i).getType().name());
					list.add(compound);
				}
			}
		}
		NBTTagCompound cp = new NBTTagCompound();
		cp.set("Contents", list);
		cp.setInt("Size", (player ? inv.getSize() - 5 : inv.getSize()));
		cp.setInt("MaxStackSize", inv.getMaxStackSize());
		cp.setString("Name", name);
		cp.setBoolean("Type", player);
		nbt.set(specialName, cp);
		return nbt;
	}
	
	public static NBTTagCompound toNBT(Inventory inv, NBTTagCompound nbt)
	{
		return InventorySerializer.toNBT(inv, nbt, inv.getName());
	}
	
	public static Inventory fromNBT(NBTTagCompound nbt, String name)
	{
		if (!nbt.hasKey(name))
		{
			return null;
		}
		NBTTagCompound cp = ((NBTTagCompound) nbt.clone()).getCompound(name);
		String invName = cp.getString("Name");
		if (invName == "")
		{
			return null;
		}
		boolean player = cp.getBoolean("Type");
		int size = cp.getInt("Size"), maxSize = cp.getInt("MaxStackSize");
		NBTTagList list = cp.getList("Contents", cp.getTypeId());
		Inventory inv;
		if (player)
		{
			inv = InventorySerializer.getNewInv();
			inv.setMaxStackSize(maxSize);
			((CraftInventoryPlayer) inv).getInventory().b(list);
		}
		else
		{
			inv = ItemInventory.createInventory(invName, size / 9);
			inv.setMaxStackSize(maxSize);
			for (int i = 0; i < list.size(); i++)
			{
				NBTTagCompound compound = list.get(i);
				int j = compound.getByte("Slot") & 0xFF;
				int c = compound.getInt("Count");
				short d = compound.getShort("Damage");
				Material ma = Material.getMaterial(compound.getString("id"));
				ItemStack itemstack = new ItemStack(ma, c, d);
				
				if (itemstack != null)
				{
					if ((j >= 0) && (j < inv.getSize()))
					{
						inv.setItem(j, itemstack);
					}
				}
			}
		}
		return inv;
	}
	
	/*
	 * private static net.minecraft.server.v1_11_R1.ItemStack
	 * getItemStack(ItemStack item) { return CraftItemStack.asNMSCopy(item); }
	 */
	
	private static org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryPlayer getNewInv()
	{
		net.minecraft.server.v1_12_R1.PlayerInventory pinv = new net.minecraft.server.v1_12_R1.PlayerInventory(null);
		org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryPlayer inv = new org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryPlayer(
				pinv);
		return (inv);
	}
}