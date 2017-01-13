package fr.jesfot.gbp.zoning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;

public class ChunkZone implements IZone
{
	public static Integer CHUNKS_LIMIT = Integer.valueOf(10);
	
	private List<Chunk> chunks;
	
	public ChunkZone()
	{
		this.chunks = new ArrayList<Chunk>();
	}
	
	public List<Chunk> getChunks()
	{
		return Collections.unmodifiableList(this.chunks);
	}
	
	public List<Chunk> getRawChunks()
	{
		return this.chunks;
	}
	
	public ChunkZone addChunk(Chunk ch)
	{
		if(!this.contains(ch) && this.chunks.size() < ChunkZone.CHUNKS_LIMIT.intValue())
		{
			this.getRawChunks().add(ch);
		}
		return this;
	}
	
	public boolean contains(Chunk ch)
	{
		for(Chunk c : this.getChunks())
		{
			if(c.getX() == ch.getX() && c.getZ() == ch.getZ())
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isInZone(Location location)
	{
		return this.contains(location.getChunk());
	}
}
