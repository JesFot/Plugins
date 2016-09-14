package fr.jesfot.gbp.secure;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;

import fr.jesfot.gbp.GamingBlockPlug_1_9;
import fr.jesfot.gbp.configuration.NBTSubConfig;

public class SecurityLoginSys
{
	private GamingBlockPlug_1_9 gbp;
	private NBTSubConfig configFilePass;
	private List<String> currentlyLogin = new ArrayList<String>();
	
	public SecurityLoginSys(GamingBlockPlug_1_9 plugin)
	{
		this.gbp = plugin;
		this.configFilePass = new NBTSubConfig(this.gbp.getConfigFolder("Login"), "Passwords");
	}
	
	public Set<String> getAccountList()
	{
		return this.configFilePass.readNBTFromFile().getCopy().c();
	}
	
	public boolean hasAccount(Player player)
	{
		for(String str : this.getAccountList())
		{
			if(player.getUniqueId().toString().equals(str))
			{
				return true;
			}
			if(player.getName().equals(str))
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean byName(Player player)
	{
		if(true)//this.hasAccount(player))
		{
			for(String str : this.getAccountList())
			{
				if(player.getName().equals(str))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public void register(Player player, final String password)
	{
		if(!this.hasAccount(player))
		{
			this.configFilePass.setString(player.getUniqueId().toString(), password).writeNBTToFile();
		}
	}
	
	public void unregister(Player player)
	{
		if(this.hasAccount(player))
		{
			this.configFilePass.removeTag(player.getUniqueId().toString());
			this.configFilePass.removeTag(player.getName()).writeNBTToFile();
		}
	}
	
	public boolean login(Player player, final String pass)
	{
		if(this.hasAccount(player))
		{
			String password;
			if(this.byName(player))
			{
				password = this.configFilePass.getCopy().getString(player.getName());
			}
			else
			{
				password = this.configFilePass.getCopy().getString(player.getUniqueId().toString());
			}
			return pass.equals(password);
		}
		return false;
	}
	
	public void addLogin(Player current)
	{
		if(!this.isLogin(current))
		{
			this.currentlyLogin.add(current.getName().toString());
		}
	}
	
	public boolean isLogin(Player player)
	{
		return this.currentlyLogin.contains(player.getName().toString());
	}
	
	@Deprecated
	public boolean isLogin(String uid)
	{
		return this.currentlyLogin.contains(uid.toString());
	}
	
	public void endLogin(Player current)
	{
		if(this.isLogin(current))
		{
			this.currentlyLogin.remove(current.getName().toString());
		}
	}
}