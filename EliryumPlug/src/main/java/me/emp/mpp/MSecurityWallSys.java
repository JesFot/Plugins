package me.emp.mpp;

import java.util.ArrayList;
import java.util.List;

import me.emp.EliryumPlug;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class MSecurityWallSys
{
	private EliryumPlug emp;
	private List<Block> blocks;
	private double howManyBlocks;
	private Location coords;
	
	public MSecurityWallSys(EliryumPlug empl)
	{
		this.emp = empl;
		this.blocks = new ArrayList<Block>();
		this.howManyBlocks = this.blocks.size();
	}

	public List<Block> getBlocks()
	{
		return this.blocks;
	}
	
	public Location getBaseLocation()
	{
		return this.coords;
	}
	
	public int getY()
	{
		return this.coords.getBlockY();
	}
	
	public void setBaseLocation(Location base)
	{
		this.coords = base;
	}
	
	public void setY(int y)
	{
		this.coords.setY(y);
		if(!(this.coords.getWorld() instanceof World))
		{
			this.coords.setWorld(this.emp.getServer().getWorlds().get(0));;
		}
	}
	
	public void updateBlocks()
	{
		this.blocks.clear();
		for(int i = 0; i < this.emp.getServer().getWorlds().get(0).getMaxHeight(); i++)
		{
			Block tmp = this.coords.getWorld().getBlockAt(coords.getBlockX(), i, coords.getBlockZ());
			if(tmp.getType().equals(Material.AIR))
			{
				continue;
			}
			this.addBlock(tmp);
		}
	}
	
	public void setBlocks(List<Block> blocks)
	{
		this.blocks = blocks;
	}
	
	public void addBlock(Block block)
	{
		this.blocks.add(block);
		this.howManyBlocks = this.blocks.size();
	}
	
	public boolean removeBlock(Block block)
	{
		for(Block bc : this.blocks)
		{
			if(bc.equals(block))
			{
				this.blocks.remove(bc);
				this.howManyBlocks = this.blocks.size();
				return true;
			}
		}
		return false;
	}
	
	public double getHowManyBlocks()
	{
		return this.howManyBlocks;
	}
	
	public boolean isWall()
	{
		this.updateBlocks();
		for(int i = 0; i < this.blocks.size(); i++)
		{
			Block tmp = this.blocks.get(i);
			if(tmp.getType().equals(Material.WALL_SIGN) || tmp.getType().equals(Material.SIGN_POST))
			{
				Sign sign = (Sign)tmp.getState();
				if(sign.getLine(1).equalsIgnoreCase("[Security_wall]"))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public String cannotPass(final String playerName)
	{
		return "[Security_wall] Sory "+playerName+" but you can't pass here.";
	}
}