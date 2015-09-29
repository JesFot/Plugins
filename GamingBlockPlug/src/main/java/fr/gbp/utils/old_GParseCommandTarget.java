/*package fr.gbp.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.ICommandListener;
import net.minecraft.server.v1_8_R3.PlayerSelector;

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

class DatasArgs
{
	public double minRange;
	public double range;
	public double x;
	public double y;
	public double z;
	
	public double gamemode;
	public double maxPlayers;
	public int maxLevel;
	public int minLevel;
	
	public Map<String, Double> scoreMin;
	public Map<String, Double> scoreMax;
	
	public List<String> team;
	public List<String> name;
	public List<EntityType> type;
	
	public void assign(String key, String value)
	{
		if(key.length() == 4)
		{
			if(key=="team")
			{
				team.add(value);
			}
			else if(key=="name")
			{
				name.add(value);
			}
			else if(key=="type")
			{
				type.add(EntityType.valueOf(value));
			}
		}
	}
}

public class old_GParseCommandTarget
{
	protected String targetMsg;
	protected String at;
	protected String arguments;
	// New
	protected String fullMessage;
	protected CommandSender sender;
	protected TargetType type;
	protected SenderType sType;
	
	protected Player player;
	protected ConsoleCommandSender console;
	protected BlockCommandSender comamndBlock;
	
	protected Map<String, String> argumentList;
	
	// End New
	
	protected Location location = null;
	
	protected Map<String, String> args;
	
	public old_GParseCommandTarget(final String message)
	{
		this.targetMsg = message;
		this.at = message.substring(0, 2);
		this.type = TargetType.getType(message.charAt(1));
		this.arguments = message.substring(2);
	}
	
	public static List<Player> getPlayers(CommandSender mySender, String myArgument)
	{
		if(!PlayerSelector.isPattern(myArgument))
		{
			return null;
		}
		final ICommandListener mcListener = GUtils.getListener(mySender);
		List<Player> pls = new ArrayList<Player>();
		if(!myArgument.contains("["))
		{
			myArgument += "[0,0,0]";
		}
		for(EntityPlayer o : PlayerSelector.getPlayers(mcListener, myArgument, EntityPlayer.class))
		{
			pls.add(((EntityPlayer)o).getBukkitEntity());
			continue;
		}
		return pls;
	}
	
	public old_GParseCommandTarget(final String arg, CommandSender cmdSender)
	{
		if(cmdSender instanceof Player)
		{
			this.sType = SenderType.PLAYER;
			this.player = (Player)cmdSender;
		}
		else if(cmdSender instanceof ConsoleCommandSender)
		{
			this.sType = SenderType.CONSOLE;
			this.console = (ConsoleCommandSender)cmdSender;
		}
		else if(cmdSender instanceof BlockCommandSender)
		{
			this.sType = SenderType.COMMANDBLOCK;
			this.comamndBlock = (BlockCommandSender)cmdSender;
		}
		this.sender = cmdSender;
		this.fullMessage = arg;
		String atX = arg.split("[")[0];
		this.type = TargetType.getType(atX.charAt(1));
		String rest = arg.split("[")[1].split("]")[0];
		List<String> args = Arrays.asList(rest.split(" , "));
		List<String> args2 = new ArrayList<String>();
		for(String a : args)
		{
			if(a.contains(", "))
			{
				String as[] = a.split(", ");
				for(String b : as)
				{
					args2.add(b);
				}
			}
			else
			{
				args2.add(a);
			}
		}
		List<String> args3 = new ArrayList<String>();
		for(String a : args2)
		{
			if(a.contains(","))
			{
				String as[] = a.split(",");
				for(String b : as)
				{
					args3.add(b);
				}
			}
			else
			{
				args3.add(a);
			}
		}
		for(String a : args3)
		{
			String map[] = a.split("=", 2);
			this.argumentList.put(map[0], map[1]);
		}
	}
	
	public old_GParseCommandTarget(final String message, final Location loc)
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
			if(this.argumentList.containsKey("name"))
			{
				if(this.argumentList.containsKey("r"))
				{
					Player prox = UPlayer.getPlayerByName(this.argumentList.get("name"));
					if(prox.getLocation().distanceSquared(location) > Integer.parseInt(this.argumentList.get("r")))
					{
						return null;
					}
				}
				return UPlayer.getPlayerByName(this.argumentList.get("name"));
			}
			if(this.argumentList.containsKey("r"))
			{
				Player prox = UPlayer.getProximityPlayer(this.location);
				if(prox.getLocation().distanceSquared(location) > Integer.parseInt(this.argumentList.get("r")))
				{
					return null;
				}
			}
			return UPlayer.getProximityPlayer(this.location);
		}
		else if(this.type==TargetType.RANDOM)
		{
			return UPlayer.getRandomPlayer(this.location.getWorld());
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
}*/