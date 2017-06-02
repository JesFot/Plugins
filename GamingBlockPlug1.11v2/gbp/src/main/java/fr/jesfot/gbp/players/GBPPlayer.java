package fr.jesfot.gbp.players;

import java.io.File;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import fr.jesfot.gbp.GamingBlockPlug_1_11;
import fr.jesfot.gbp.configuration.NBTSubConfig;

public class GBPPlayer
{
	private UUID playerUid;
	private String playerName;
	private String displayName;
	private File configFolder;
	private NBTSubConfig configFile;
	
	private GBPPlayer.LogState logState;
	
	public GBPPlayer(UUID uid, String name, String disp)
	{
		this.playerUid = uid;
		this.playerName = name;
		this.displayName = disp;
		this.configFolder = null;
		this.configFile = null;
		this.logState = LogState.Unknown;
	}
	
	public GBPPlayer(UUID uid, String name)
	{
		this(uid, name, name);
	}
	
	public GBPPlayer(Player original)
	{
		this(original.getUniqueId(), original.getName(), original.getDisplayName());
	}
	
	public void setConfigFolder(File folder)
	{
		this.configFolder = folder;
		if(folder != null && !folder.exists())
		{
			GamingBlockPlug_1_11.getTheLogger().log(Level.FINEST, "Creating player configuration folder...");
			if(folder.mkdirs())
			{
				GamingBlockPlug_1_11.getTheLogger().log(Level.FINEST, "Successfuly created the player configuration folder.");
			}
			else
			{
				GamingBlockPlug_1_11.getTheLogger().log(Level.WARNING, "Enable to create the player configuration directory tree.");
			}
		}
	}
	
	public void setConfigFolder(String folderpath)
	{
		this.setConfigFolder(new File(folderpath));
	}
	
	public void update(Server server)
	{
		if(this.playerUid == null)
		{
			return;
		}
		Player orig = server.getPlayer(this.getUniqueId());
		if(orig != null)
		{
			orig.setDisplayName(this.displayName);
		}
	}
	
	public void load()
	{
		if(this.configFolder == null || this.playerUid == null)
		{
			return;
		}
		if(this.configFile == null)
		{
			this.configFile = new NBTSubConfig(this.configFolder, this.playerUid);
			this.configFile.getFile().initFolders();
		}
		this.configFile.readNBTFromFile();
	}
	
	public void save()
	{
		if(this.configFile == null)
		{
			this.load();
		}
		if(this.configFile == null)
		{
			return;
		}
		this.configFile.writeNBTToFile();
	}
	
	public void setLogState(GBPPlayer.LogState state)
	{
		this.logState = state;
	}
	
	public void setDisplayName(final String newName)
	{
		this.displayName = newName;
	}
	
	public GBPPlayer.LogState getLogState()
	{
		return this.logState;
	}
	
	public UUID getUniqueId()
	{
		return this.playerUid;
	}
	
	public String getName()
	{
		return this.playerName;
	}
	
	public String getDisplayName()
	{
		return this.displayName;
	}
	
	public File getConfigFolder()
	{
		return this.configFolder;
	}
	
	public enum LogState
	{
		OffLine,
		Identifying,
		Joining,
		Logingin,
		Playing,
		Unknown
	}
}
