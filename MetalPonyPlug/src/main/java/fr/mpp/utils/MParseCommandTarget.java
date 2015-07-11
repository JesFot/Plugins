package fr.mpp.utils;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class MParseCommandTarget
{
	//
	protected String targetMsg;
	protected String at;
	protected String arguments;
	protected char type;
	
	protected Location location = null;
	
	protected Map<String, String> args;
	
	public MParseCommandTarget(final String message)
	{
		this.targetMsg = message;
		this.at = message.substring(0, 3);
		this.type = message.charAt(1);
		this.arguments = message.substring(2);
	}
	
	public MParseCommandTarget(final String message, final Location loc)
	{
		this(message);
		this.location = loc;
	}
	
	public void parseArgs()
	{
		String arga = this.targetMsg.split("[")[0].split("]")[0];
		String arg[] = arga.split(",");
		for(String a : arg)
		{
			a = a.replaceAll("", "");
			this.args.put(a.split("=")[0], a.split("=")[1]);
		}
	}
	
	public boolean targetIsPlayer()
	{
		return !(this.type=='e');
	}
	
	public boolean multipleTargets()
	{
		return (this.type=='a' || this.type=='e');
	}
	
	public void setLocation(Location loc)
	{
		this.location = loc;
	}
	
	public void setWorld(World w)
	{
		if(this.location==null)
		{
			this.location = new Location(w, 0, 0, 0);
		}
		else
		{
			this.location.setWorld(w);
		}
	}
	
	public Player getTarget()
	{
		if(this.location == null)
		{
			return null;
		}
		if(this.type=='p')
		{
			return MPlayer.getProximityPlayer(this.location);
		}
		else if(this.type=='r')
		{
			return MPlayer.getRandomPlayer(this.location.getWorld());
		}
		return null;
	}
}