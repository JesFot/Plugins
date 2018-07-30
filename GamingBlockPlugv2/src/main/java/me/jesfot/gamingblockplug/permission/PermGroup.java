package me.jesfot.gamingblockplug.permission;

import java.util.Optional;
import java.util.function.Consumer;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.LuckPermsApi;

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
					};
				});
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
