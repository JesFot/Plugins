package fr.jesfot.gbp.stats;

import java.util.Arrays;
import java.util.Calendar;

import org.bukkit.OfflinePlayer;

import fr.jesfot.gbp.GamingBlockPlug_1_11;
import fr.jesfot.gbp.configuration.Configurations;
import fr.jesfot.gbp.configuration.NBTSubConfig;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import net.minecraft.server.v1_11_R1.NBTTagList;

public class PlayerStatistics
{
	private GamingBlockPlug_1_11 gbp;
	private NBTSubConfig config;
	
	public PlayerStatistics(GamingBlockPlug_1_11 plugin)
	{
		this.gbp = plugin;
		this.config = null;
	}
	
	public PlayerStatistics player(OfflinePlayer player)
	{
		if(this.config != null)
		{
			if(this.config.getName().equalsIgnoreCase(player.getUniqueId().toString()))
			{
				return this;
			}
			this.rmPlayer();
		}
		this.config = new NBTSubConfig(this.gbp.getConfigFolder(Configurations.PLAYERS_DATS), player.getUniqueId().toString(), "Stats");
		this.config.readNBTFromFile().writeNBTToFile();
		return this;
	}
	
	public PlayerStatistics login()
	{
		Calendar cal = date(this.config.getCopy().getCompound("LastDate"));
		NBTTagList list = this.config.getCopy().getList("ConnectionsPerDay", this.config.getCopy().getTypeId());
		NBTTagCompound elem = (date(cal, Calendar.getInstance()) ? (NBTTagCompound)list.remove(list.size() - 1) : new NBTTagCompound());
		elem.setString("Day", Calendar.getInstance().getTime().toString());
		elem.setInt("Connections", elem.getInt("Connections") + 1);
		list.add(elem);
		this.config.setTag("ConnectionsPerDay", list);
		this.config.setTag("LastDate", date(Calendar.getInstance()));
		this.config.setLong("ConnectTime", Calendar.getInstance().getTimeInMillis());
		return this;
	}
	
	public PlayerStatistics logout()
	{
		long connect = this.config.getCopy().getLong("ConnectTime");
		long now = Calendar.getInstance().getTimeInMillis();
		long sum = now - connect;
		int[] times = this.config.getCopy().getIntArray("TimePerConnectionsInMillis");
		times = Arrays.copyOf(times, times.length + 1);
		times[times.length - 1] = (int)sum;
		this.config.setIntArray("TimePerConnectionsInMillis", times);
		return this;
	}
	
	public PlayerStatistics rmPlayer()
	{
		this.config.writeNBTToFile().setUnaccessible();
		return this;
	}
	
	private NBTTagCompound date(Calendar time)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInt("Year", time.get(Calendar.YEAR));
		nbt.setInt("Month", time.get(Calendar.MONTH));
		nbt.setInt("Day", time.get(Calendar.DAY_OF_MONTH));
		nbt.setInt("Hour", time.get(Calendar.HOUR_OF_DAY));
		nbt.setInt("Minutes", time.get(Calendar.MINUTE));
		nbt.setInt("Seconds", time.get(Calendar.SECOND));
		return nbt;
	}
	
	private Calendar date(NBTTagCompound nbt)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(nbt.getInt("Year"), nbt.getInt("Month"), nbt.getInt("Day"), nbt.getInt("Hour"), nbt.getInt("Minutes"), nbt.getInt("Seconds"));
		return cal;
	}
	
	private boolean date(Calendar c1, Calendar c2)
	{
		if(c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR))
		{
			return false;
		}
		if(c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH))
		{
			return false;
		}
		if(c1.get(Calendar.DAY_OF_MONTH) != c2.get(Calendar.DAY_OF_MONTH))
		{
			return false;
		}
		return true;
	}
}