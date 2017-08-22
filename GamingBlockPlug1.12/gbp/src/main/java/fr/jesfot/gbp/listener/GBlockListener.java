package fr.jesfot.gbp.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.jesfot.gbp.GamingBlockPlug_1_12;
import fr.jesfot.gbp.economy.Money;
import fr.jesfot.gbp.permission.PermissionsHelper;
import fr.jesfot.gbp.shop.ShopObject;
import fr.jesfot.gbp.subsytems.SecurityWallSys;
import fr.jesfot.gbp.utils.Utils;

public class GBlockListener implements Listener
{
	private GamingBlockPlug_1_12 gbp;
	private final SecurityWallSys sws;
	
	public GBlockListener(GamingBlockPlug_1_12 plugin)
	{
		this.sws = new SecurityWallSys(plugin);
		this.gbp = plugin;
	}
	
	@EventHandler
	public void onBlockPlace(final BlockPlaceEvent event)
	{
		if (event.getBlock().getState() instanceof Sign)
		{
			Sign s = (Sign) event.getBlock().getState();
			if (s.getLine(1).equalsIgnoreCase("[Security_wall]"))
			{
				Player p = event.getPlayer();
				if (!PermissionsHelper.testPermissionSilent(p, "GamingBlockPlug.secureWall.place", false))
				{
					event.setCancelled(true);
					p.sendMessage(this.gbp.getLang().get("securitywall.placedisallow",
							"[Security_Wall] " + "You donnot have the right to make a wall."));
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onSignUpdate(final SignChangeEvent event)
	{
		if (event.getBlock().getState() instanceof Sign)
		{
			if (event.getLine(1).equalsIgnoreCase("[Security_wall]"))
			{
				Player p = event.getPlayer();
				if (!PermissionsHelper.testPermissionSilent(p, "GamingBlockPlug.secureWall.place", false))
				{
					event.setCancelled(true);
					p.sendMessage(this.gbp.getLang().get("securitywall.placedisallow",
							"[Security_Wall] " + "You donnot have the right to make a wall."));
				}
				else
				{
					this.gbp.getLogger().info(p.getName() + " placed a new Security wall at "
							+ event.getBlock().getLocation().toString());
				}
				return;
			}
			if (event.getLine(0).equalsIgnoreCase("[shop]"))
			{
				org.bukkit.material.Sign sign = (org.bukkit.material.Sign) event.getBlock().getState().getData();
				double price;
				int amount;
				Block relBlock = event.getBlock().getRelative(sign.getFacing().getOppositeFace());
				
				if (relBlock.getType().equals(Material.CHEST))
				{
					Sign signBlock = (Sign) event.getBlock().getState();
					if (!PermissionsHelper.testPermission(event.getPlayer(), "GamingBlockPlug.shops.create", false,
							"&cYou are not allowed to create shops."))
					{
						event.setCancelled(true);
						return;
					}
					if (event.getPlayer().getInventory().getItemInOffHand().getAmount() < 1)
					{
						event.getPlayer().sendMessage(
								ChatColor.RED + "You must hold the item you want to sell in your off hand.");
						return;
					}
					if (Utils.isNumber(event.getLine(1)))
					{
						amount = Utils.toInt(event.getLine(1), 0);
						if (amount < 1)
						{
							event.getPlayer().sendMessage(ChatColor.RED + "The amount (l 2) needs to be positive.");
							amount = 0;
						}
					}
					else
					{
						event.getPlayer().sendMessage(ChatColor.RED + "The amount (l 2) needs to be a number.");
						return;
					}
					if (Utils.isNumber(event.getLine(2)))
					{
						price = Utils.toDouble(event.getLine(2), 0);
						if (price < 0)
						{
							event.getPlayer().sendMessage(ChatColor.RED + "The price (l 3) needs to be positive.");
							price = 0;
						}
					}
					else
					{
						event.getPlayer().sendMessage(ChatColor.RED + "The price (l 3) needs to be a number.");
						return;
					}
					String iName;
					ItemStack is = event.getPlayer().getInventory().getItemInOffHand();
					ItemMeta im = is.getItemMeta();
					String n = (im != null ? im.getDisplayName() : null);
					if (!event.getLine(3).isEmpty())
					{
						iName = event.getLine(3);
					}
					else
					{
						if (im != null && im.hasDisplayName())
						{
							iName = n;
						}
						else
						{
							iName = is.getType().name().replace('_', ' ').toLowerCase();
						}
					}
					relBlock.getRelative(sign.getFacing()).setType(Material.WALL_SIGN);
					Sign newSign = (Sign) relBlock.getRelative(sign.getFacing()).getState();
					
					org.bukkit.material.Sign matSign = new org.bukkit.material.Sign(Material.WALL_SIGN);
					matSign.setFacingDirection(sign.getFacing());
					
					newSign.setData(matSign);
					newSign.setLine(0, ChatColor.BOLD + "[shop]");
					newSign.setLine(1, "Selling: " + ChatColor.BOLD + amount);
					newSign.setLine(2, "" + ChatColor.RED + price + Money.getSym());
					newSign.setLine(3, iName);
					newSign.update();
					signBlock.update();
					
					this.gbp.getLogger().info(
							event.getPlayer().getName() + " placed a new shop at " + relBlock.getLocation().toString());
					
					ShopObject shop = new ShopObject(event.getPlayer(), price, amount, 0, relBlock.getLocation(),
							newSign.getLocation(), is, iName);
					this.gbp.getShops().addShop(shop);
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(final BlockBreakEvent event)
	{
		this.sws.setBaseLocation(event.getBlock().getLocation());
		this.sws.updateBlocks();
		if (this.sws.isWall() && !PermissionsHelper.testPermissionSilent(event.getPlayer(),
				"GamingBlockPlug.secureWall.break", false))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(this.sws.message(event.getPlayer().getName()));
		}
		else if (this.sws.isWall())
		{
			event.getPlayer().sendMessage(this.sws.allowMessage(event.getPlayer().getName()));
		}
		// Last:
		if (event.getBlock().getType().equals(Material.WALL_SIGN))
		{
			org.bukkit.material.Sign sign = (org.bukkit.material.Sign) event.getBlock().getState().getData();
			ShopObject shop = this.gbp.getShops()
					.getShop(event.getBlock().getRelative(sign.getAttachedFace()).getLocation());
			if (shop == null)
			{
				return;
			}
			if (shop.isOwner(event.getPlayer()))
			{
				if (!PermissionsHelper.testPermission(event.getPlayer(), "GamingBlockPlug.shops.destroy", false,
						"&cYou are not allowed to destroy your shops."))
				{
					event.setCancelled(true);
					return;
				}
				this.gbp.getShops().removeShop(shop);
				return;
			}
			if (PermissionsHelper.testPermissionSilent(event.getPlayer(), "GamingBlockPlug.shops.op", false))
			{
				this.gbp.getShops().removeShop(shop);
				return;
			}
			event.setCancelled(true);
		}
		else if (event.getBlock().getType().equals(Material.CHEST))
		{
			Chest chest = (Chest) event.getBlock().getState();
			InventoryHolder ih = chest.getInventory().getHolder();
			if (ih instanceof DoubleChest)
			{
				DoubleChest dchest = (DoubleChest) ih;
				Chest chleft = (Chest) dchest.getLeftSide();
				Chest chright = (Chest) dchest.getRightSide();
				ShopObject shopLeft = this.gbp.getShops().getShop(chleft.getLocation());
				ShopObject shopRight = this.gbp.getShops().getShop(chright.getLocation());
				if (shopLeft != null)
				{
					event.setCancelled(true);
					event.getPlayer().sendMessage(ChatColor.RED + "Sign before !");
				}
				if (shopRight != null)
				{
					event.setCancelled(true);
					event.getPlayer().sendMessage(ChatColor.RED + "Sign before !");
				}
			}
			else
			{
				ShopObject shop = this.gbp.getShops().getShop(event.getBlock().getLocation());
				if (shop != null)
				{
					event.setCancelled(true);
					event.getPlayer().sendMessage(ChatColor.RED + "Sign before !");
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBurn(final BlockBurnEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onBlockDispense(final BlockDispenseEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onBlockGrow(final BlockGrowEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onPistonExtend(final BlockPistonExtendEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onPistonRetract(final BlockPistonRetractEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onRedstoneChange(final BlockRedstoneEvent event)
	{
		// Code ...
	}
}