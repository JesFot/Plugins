package me.jesfot.gamingblockplug.permission;

import java.util.Optional;
import java.util.function.Consumer;

import org.bukkit.OfflinePlayer;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;

@SuppressWarnings("squid:S2259")
public final class PermGroup
{
	private static LuckPermsApi getLPAPI()
	{
		return LuckPerms.getApiSafe().orElse(null);
	}
	
	public static boolean isLPAPIOk()
	{
		return LuckPerms.getApiSafe().isPresent();
	}
	
	public static void registerGroup(String name)
	{
		if (PermGroup.isLPAPIOk())
		{
			try
			{
				PermGroup.getLPAPI().getGroupManager().createAndLoadGroup(name).join();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void registerGroupAsync(String name)
	{
		if (PermGroup.isLPAPIOk())
		{
			try
			{
				PermGroup.getLPAPI().getGroupManager().createAndLoadGroup(name);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void joinGroup(String groupName, OfflinePlayer player)
	{
		if (PermGroup.isLPAPIOk())
		{
			try
			{
				User u = PermGroup.getLPAPI().getUser(player.getUniqueId());
				u.clearParents();
				u.setPermission(PermGroup.getLPAPI().getNodeFactory().makeGroupNode(groupName).build());
				u.setPrimaryGroup(groupName);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void deleteGroup(String name)
	{
		if (PermGroup.isLPAPIOk())
		{
			try
			{
				Optional<Group> t = PermGroup.getLPAPI().getGroupManager().loadGroup(name).get();
				if (t.isPresent())
				{
					PermGroup.getLPAPI().getGroupManager().deleteGroup(t.get());
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void deleteGroupAsync(String name)
	{
		if (PermGroup.isLPAPIOk())
		{
			try
			{
				PermGroup.getLPAPI().getGroupManager().loadGroup(name).thenAcceptAsync(new Consumer<Optional<Group>>() {
					@Override
					public void accept(Optional<Group> t) {
						if (t.isPresent())
						{
							PermGroup.getLPAPI().getGroupManager().deleteGroup(t.get());
						}
					}
				});
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
