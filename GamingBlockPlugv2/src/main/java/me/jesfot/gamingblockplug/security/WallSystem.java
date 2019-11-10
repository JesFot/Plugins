package me.jesfot.gamingblockplug.security;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import me.jesfot.gamingblockplug.permission.PermissionHelper;
import me.jesfot.gamingblockplug.permission.StaticPerms;

public class WallSystem
{
	public static final String WALL_TEXT = "[Security_Wall]";
	
	public Column getColumn(Location location)
	{
		if (location == null || location.getWorld() == null)
		{
			throw new IllegalArgumentException("Location and it's world must not be null");
		}
		return new Column(location.getWorld(), location.getBlockX(), location.getBlockZ());
	}
	
	public Column getColumn(Block block)
	{
		if (block == null)
		{
			throw new IllegalArgumentException("block must not be null");
		}
		return this.getColumn(block.getLocation());
	}
	
	public boolean canBreak(Player player, Block block)
	{
		if (PermissionHelper.testPermissionSilent(player, StaticPerms.WALLS_BREAK, false))
		{
			return true;
		}
		Column col = this.getColumn(block);
		return !col.isWall();
	}
	
	public boolean canPlace(Player player, Block block)
	{
		return PermissionHelper.testPermissionSilent(player, StaticPerms.WALLS_PLACE, false);
	}
	
	public class Column
	{
		private Set<Block> blocks;
		private int blocksCount;
		private int x, z;
		private World world;
		
		private Boolean isWall = null;
		
		private Column(World world, int x, int z)
		{
			this.blocks = new HashSet<>();
			this.blocksCount = 0;
			this.world = world;
			this.x = x;
			this.z = z;
		}
		
		public void addBlock(Block block)
		{
			if (block != null && !block.getType().equals(Material.AIR) && block.getX() == this.x && block.getZ() == this.z)
			{
				this.blocks.add(block);
				this.blocksCount = this.blocks.size();
			}
		}
		
		public void updateBlocks()
		{
			this.blocks.clear();
			for(int i = 0; i < this.world.getMaxHeight(); i++)
			{
				Block tmp = this.world.getBlockAt(this.x, i, this.z);
				this.addBlock(tmp);
			}
			this.isWall = null;
		}
		
		public boolean removeBlock(Block block)
		{
			for(Block bc : this.blocks)
			{
				if(bc.equals(block))
				{
					this.blocks.remove(bc);
					this.blocksCount = this.blocks.size();
					return true;
				}
			}
			return false;
		}
		
		public int getBlockCount()
		{
			return this.blocksCount;
		}
		
		public boolean isWall()
		{
			if (this.isWall != null)
			{
				return this.isWall.booleanValue();
			}
			this.updateBlocks();
			for (Block block : this.blocks)
			{
				if (block.getType().equals(Material.ACACIA_SIGN)
						|| block.getType().equals(Material.ACACIA_WALL_SIGN)
						|| block.getType().equals(Material.BIRCH_SIGN)
						|| block.getType().equals(Material.BIRCH_WALL_SIGN)
						|| block.getType().equals(Material.DARK_OAK_SIGN)
						|| block.getType().equals(Material.DARK_OAK_WALL_SIGN)
						|| block.getType().equals(Material.JUNGLE_SIGN)
						|| block.getType().equals(Material.JUNGLE_WALL_SIGN)
						|| block.getType().equals(Material.OAK_SIGN)
						|| block.getType().equals(Material.OAK_WALL_SIGN)
						|| block.getType().equals(Material.SPRUCE_SIGN)
						|| block.getType().equals(Material.SPRUCE_WALL_SIGN))
				{
					Sign signBlock = (Sign) block.getState();
					String[] lines = signBlock.getLines();
					for (String line : lines)
					{
						if (WallSystem.WALL_TEXT.equalsIgnoreCase(line))
						{
							this.isWall = Boolean.TRUE;
							return true;
						}
					}
				}
			}
			this.isWall = Boolean.FALSE;
			return false;
		}
	}
}
