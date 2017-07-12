package fr.jesfot.gbp.subsytems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import fr.jesfot.gbp.GamingBlockPlug_1_12;
import fr.jesfot.gbp.configuration.NBTSubConfig;

public class SecurityWallSys
{
	private GamingBlockPlug_1_12 gbp;
	private List<Block> blocks;
	private int howManyBlocks;
	private Location coords;
	private NBTSubConfig storage;
	
	public SecurityWallSys(GamingBlockPlug_1_12 plugin)
	{
		this.gbp = plugin;
		this.blocks = new ArrayList<Block>();
		this.howManyBlocks = this.blocks.size();
		this.storage = plugin.getNBT("Security_Wall");
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
		return this.getBaseLocation().getBlockY();
	}
	
	public void setBaseLocation(final Location base)
	{
		this.coords = base;
	}
	
	public void setY(final int y)
	{
		this.coords.setY(y);
		if(!(this.coords.getWorld() instanceof World))
		{
			this.coords.setWorld(this.gbp.getServer().getWorlds().get(0));
		}
	}
	
	public void addBlock(Block block)
	{
		this.blocks.add(block);
		this.howManyBlocks = this.blocks.size();
	}
	
	public void updateBlocks()
	{
		this.blocks.clear();
		for(int i = 0; i < this.gbp.getServer().getWorlds().get(0).getMaxHeight(); i++)
		{
			Block tmp = this.coords.getWorld().getBlockAt(this.coords.getBlockX(), i, this.coords.getBlockZ());
			if(tmp.getType().equals(Material.AIR))
			{
				continue;
			}
			this.addBlock(tmp);
		}
	}
	
	public void setBlocks(Collection<Block> coll)
	{
		this.blocks = new ArrayList<Block>(coll);
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
	
	public void addToStorage(final World w, final int x, final int z)
	{
		int[] intsX = this.storage.readNBTFromFile().getCopy().getIntArray("World_X_" + w.getUID().toString());
		intsX = Arrays.copyOf(intsX, intsX.length + 1);
		intsX[intsX.length - 1] = x;
		this.storage.setIntArray("World_X_" + w.getUID().toString(), intsX).writeNBTToFile();
		int[] intsZ = this.storage.readNBTFromFile().getCopy().getIntArray("World_Z_" + w.getUID().toString());
		intsZ = Arrays.copyOf(intsZ, intsZ.length + 1);
		intsZ[intsZ.length - 1] = z;
		this.storage.setIntArray("World_Z_" + w.getUID().toString(), intsZ).writeNBTToFile();
	}
	
	public void removeFromStorage(final World w, final int x, final int z)
	{
		int[] intsX = this.storage.readNBTFromFile().getCopy().getIntArray("World_X_" + w.getUID().toString());
		int[] newIntsX = new int[intsX.length - 1];
		int subX = 0;
		for(int iX = 0; iX < intsX.length; iX++)
		{
			if(intsX[iX] != x)
			{
				newIntsX[iX - subX] = intsX[iX];
			}
			else
			{
				subX += 1;
			}
		}
		this.storage.setIntArray("World_X_" + w.getUID().toString(), newIntsX).writeNBTToFile();
		int[] intsZ = this.storage.readNBTFromFile().getCopy().getIntArray("World_Z_" + w.getUID().toString());
		int[] newIntsZ = new int[intsZ.length - 1];
		int subZ = 0;
		for(int iZ = 0; iZ < intsZ.length; iZ++)
		{
			if(intsZ[iZ] != z)
			{
				newIntsZ[iZ - subZ] = intsZ[iZ];
			}
			else
			{
				subZ += 1;
			}
		}
		this.storage.setIntArray("World_Z_" + w.getUID().toString(), newIntsZ).writeNBTToFile();
	}
	
	public boolean isWall()
	{
		this.updateBlocks();
		String uid = this.coords.getWorld().getUID().toString();
		if(this.storage.readNBTFromFile().getCopy().hasKey("World_X_" + uid))
		{
			if(this.storage.getCopy().getIntArray("World_X_" + uid).length != 0)
			{
				int[] ints = this.storage.getCopy().getIntArray("World_X_" + uid);
				for(int i : ints)
				{
					if(i == this.getBaseLocation().getBlockX())
					{
						if(this.storage.readNBTFromFile().getCopy().hasKey("World_Z_" + uid))
						{
							if(this.storage.getCopy().getIntArray("World_Z_" + uid).length != 0)
							{
								int[] ints_ = this.storage.getCopy().getIntArray("World_Z_" + uid);
								for(int i_ : ints_)
								{
									if(i_ == this.getBaseLocation().getBlockZ())
									{
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		for(int i = 0; i < this.getHowManyBlocks(); i++)
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
	
	public String message(final String playerName)
	{
		return this.gbp.getLang().get("securitywall.disallow", "[Security_Wall] Sorry"
				+ " <player> but you can't pass here.").replaceAll("<player>", playerName);
	}

	public String allowMessage(final String playerName)
	{
		return this.gbp.getLang().get("securitywall.breakallow", "[Security_Wall] Info, "
				+ "<player>: This block was part of a wall.").replaceAll("<player>", playerName);
	}
}