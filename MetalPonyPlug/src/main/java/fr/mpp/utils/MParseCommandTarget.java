package fr.mpp.utils;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

enum TargetType
{
	RANDOM('r'),
	CLOSESTS('p'),
	ALL('a'),
	ENTITY('e'),
	NULL('n');
	
	public char charac;
	
	TargetType(final char c)
	{
		this.charac = c;
	}
	
	public static TargetType getType(final char at)
	{
		switch(at)
		{
		case 'r':
			return RANDOM;
		case 'p':
			return CLOSESTS;
		case 'a':
			return ALL;
		case 'e':
			return ENTITY;
		}
		return NULL;
	}
}

enum SenderType
{
	CONSOLE,
	PLAYER,
	PLUGIN,
	COMMANDBLOCK;
}

public class MParseCommandTarget
{
	protected String targetMsg;
	protected String at;
	protected String arguments;
	protected CommandSender sender;
	protected TargetType type;
	protected SenderType sType;
	
	protected Location location = null;
	
	protected Map<String, String> args;
	
	public MParseCommandTarget(final String message)
	{
		this.targetMsg = message;
		this.at = message.substring(0, 3);
		this.type = TargetType.getType(message.charAt(1));
		this.arguments = message.substring(2);
	}
	
	public MParseCommandTarget(final String arg, CommandSender cmdSender)
	{
		if(cmdSender instanceof Player)
		{
			this.sType = SenderType.PLAYER;
		}
		else if(cmdSender instanceof ConsoleCommandSender)
		{
			this.sType = SenderType.CONSOLE;
		}
		else if(cmdSender instanceof BlockCommandSender)
		{
			this.sType = SenderType.COMMANDBLOCK;
		}
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
		return !(this.type==TargetType.ENTITY);
	}
	
	public boolean multipleTargets()
	{
		return (this.type==TargetType.ALL || this.type==TargetType.ENTITY);
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
		if(this.type==TargetType.CLOSESTS)
		{
			return MPlayer.getProximityPlayer(this.location);
		}
		else if(this.type==TargetType.RANDOM)
		{
			return MPlayer.getRandomPlayer(this.location.getWorld());
		}
		else if(this.type==TargetType.ALL && this.location.getWorld().getPlayers().size()==1)
		{
			return this.location.getWorld().getPlayers().get(0);
		}
		return null;
	}
	
	public List<Player> getTargets()
	{
		if(this.type==TargetType.ALL || this.type==TargetType.ENTITY)
		{
			return this.location.getWorld().getPlayers();
		}
		return null;
	}
	
	public List<Entity> getTargetsE()
	{
		if(this.type==TargetType.ENTITY)
		{
			return this.location.getWorld().getEntities();
		}
		return null;
	}
}