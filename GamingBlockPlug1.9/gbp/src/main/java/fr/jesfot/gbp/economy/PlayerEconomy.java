package fr.jesfot.gbp.economy;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.configuration.NBTConfig;
import fr.jesfot.gbp.configuration.NBTSubConfig;
import fr.jesfot.gbp.configuration.NBTTagConfig;
import fr.jesfot.gbp.utils.InventorySerializer;
import fr.jesfot.gbp.utils.ItemInventory;

public class PlayerEconomy
{
	private GamingBlockPlug_1_9 gbp;
	private OfflinePlayer player;
	private double money;
	private NBTSubConfig config;
	
	private Inventory menu;
	private Inventory inv;
	
	public PlayerEconomy(GamingBlockPlug_1_9 p_gbp, OfflinePlayer source)
	{
		this.gbp = p_gbp;
		this.player = source;
		NBTConfig playerCfg = new NBTConfig(this.gbp.getConfigFolder("playerdatas"), this.player.getUniqueId());
		this.config = new NBTSubConfig(playerCfg, "Economy");
		this.menu = ItemInventory.createItemandInv(Material.EMERALD, 0, "Bank", "", "Choisissez " + this.player.getName(), 2);
		ItemInventory.createIteminInv(Material.APPLE, this.menu, 9, "Commun Chest", "");
		if(this.player.isOnline() && this.player instanceof Player)
		{
			Player pl = (Player)this.player;NBTTagConfig world = new NBTTagConfig(playerCfg, pl.getWorld().getName());
			world.readNBTFromFile();
			if(world.getCopy().hasKey("bank"))
			{
				this.inv = InventorySerializer.fromNBT(world, "bank");
			}
			else
			{
				this.inv = ItemInventory.createInventory("Your Bank", 5);
				world = InventorySerializer.toNBT(this.inv, world, "bank");
				world.writeNBTToFile();
			}
		}
		else
		{
			this.inv = ItemInventory.createInventory("NULL", 1);
		}
		this.money = Money.getBasics();
		if(this.config.readNBTFromFile().getCopy().hasKey("balance"))
		{
			this.getStoredMoney();
		}
		this.storeMoney();
	}
	
	public double getBalance()
	{
		this.getStoredMoney();
		return this.money;
	}
	
	public void setBalance(double p_money)
	{
		this.money = p_money;
		this.storeMoney();
	}
	
	public void storeMoney()
	{
		this.config.setDouble("balance", this.money);
		this.config.writeNBTToFile();
	}
	
	public void getStoredMoney()
	{
		this.config.readNBTFromFile();
		this.money = this.config.getCopy().getDouble("balance");
	}
	
	public void add(double amount)
	{
		this.setBalance(this.getBalance() + amount);
	}
	
	public void remove(double amount)
	{
		this.setBalance(this.getBalance() - amount);
	}
	
	public Inventory getInventory()
	{
		return this.inv;
	}
	
	public void storeInventory()
	{
		if(this.player.isOnline() && this.player instanceof Player)
		{
			Player pl = (Player)this.player;
			NBTTagConfig world = new NBTTagConfig(new NBTConfig(this.gbp.getConfigFolder("playerdatas"), this.player.getUniqueId()), pl.getWorld().getName());
			world.readNBTFromFile();
			world = InventorySerializer.toNBT(this.inv, world, "bank");
			world.writeNBTToFile();
		}
	}
	
	public void getStoredInventory()
	{
		if(this.player.isOnline() && this.player instanceof Player)
		{
			Player pl = (Player)this.player;
			NBTTagConfig world = new NBTTagConfig(new NBTConfig(this.gbp.getConfigFolder("playerdatas"), this.player.getUniqueId()), pl.getWorld().getName());
			world.readNBTFromFile();
			if(world.getCopy().hasKey("bank"))
			{
				this.inv = InventorySerializer.fromNBT(world, "bank");
			}
			else
			{
				this.inv = ItemInventory.createInventory("Your Bank", 5);
				world = InventorySerializer.toNBT(this.inv, world, "bank");
				world.writeNBTToFile();
			}
		}
		else
		{
			this.inv = ItemInventory.createInventory("NULL", 1);
		}
	}
	
	public Inventory getMenu()
	{
		return this.menu;
	}
	
	public void resetMoney()
	{
		double baseM = Money.getBasics();
		this.setBalance(baseM);
	}
	
	public void removeMoney()
	{
		this.config.removeTag("balance");
		this.config.writeNBTToFile();
	}
	
	public void removeBank()
	{
		this.inv = null;
		this.storeInventory();
	}
	
	public boolean hasEnough(final double amount)
	{
		double result = this.getBalance();
		if(result >= amount && amount >= 0)
		{
			return true;
		}
		return false;
	}
}