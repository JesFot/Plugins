package fr.jesfot.gbp.players;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import fr.jesfot.gbp.GamingBlockPlug_1_11;
import fr.jesfot.gbp.configuration.NBTSubConfig;
import fr.jesfot.gbp.permissions.PermissionGroup;

import net.minecraft.server.v1_11_R1.NBTTagCompound;
import net.minecraft.server.v1_11_R1.NBTTagList;

public class GBPPlayer
{
	private UUID playerUid;
	private String playerName;
	private String displayName;
	private File configFolder;
	private NBTSubConfig configFile;
	
	private PermissionGroup permGroup;
	private Map<String, Boolean> permissionsOverrides;
	
	private GBPPlayer.LogState logState;
	
	public GBPPlayer(UUID uid, String name, String disp)
	{
		this.playerUid = uid;
		this.playerName = name;
		this.displayName = disp;
		this.configFolder = null;
		this.configFile = null;
		this.permGroup = null;
		this.permissionsOverrides = new HashMap<String, Boolean>();
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
	
	public void setPermissionGroup(PermissionGroup group)
	{
		if(!this.permGroup.equals(group))
		{
			this.permGroup = group;
			group.addPlayer(this);
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
		NBTTagList permList = this.configFile.getCopy().getList("PermissionsOverrides", 10);
		for (int i = 0; i < permList.size(); i++)
		{
			NBTTagCompound comp = permList.get(i);
			this.permissionsOverrides.put(comp.getString("PermissionName"), comp.getBoolean("Value"));
			comp = null;
		}
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
		NBTTagList permList = new NBTTagList();
		NBTTagCompound comp = null;
		for (Entry<String, Boolean> en : this.permissionsOverrides.entrySet())
		{
			comp = new NBTTagCompound();
			comp.setString("PermissionName", en.getKey());
			comp.setBoolean("Value", Boolean.valueOf(en.getValue()));
			permList.add(comp);
			comp = null;
		}
		this.configFile.setTag("PermissionsOverrides", permList);
		this.configFile.writeNBTToFile();
	}
	
	public boolean hasPermission(String permission)
	{
		if(this.permissionsOverrides.containsKey(permission))
		{
			return this.permissionsOverrides.get(permission).booleanValue();
		}
		return this.permGroup.hasPermission(permission, GamingBlockPlug_1_11.getMe().getServer().getPlayer(this.playerUid));
	}
	
	public boolean hasPermission(Permission permission)
	{
		if(this.permissionsOverrides.containsKey(permission.getName()))
		{
			return this.permissionsOverrides.get(permission.getName()).booleanValue();
		}
		return this.permGroup.hasPermission(permission, GamingBlockPlug_1_11.getMe().getServer().getPlayer(this.playerUid));
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
