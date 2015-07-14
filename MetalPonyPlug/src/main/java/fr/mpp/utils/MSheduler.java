package fr.mpp.utils;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import fr.mpp.MetalPonyPlug;

public class MSheduler
{
	private MetalPonyPlug mpp;
	
	public MSheduler(MetalPonyPlug mppl)
	{
		this.mpp = mppl;
	}
	
	public BukkitTask runTask(BukkitRunnable clazz)
	{
		return clazz.runTask(this.mpp.getPlugin());
	}
	
	public BukkitTask runTaskAsync(BukkitRunnable clazz)
	{
		return clazz.runTaskAsynchronously(this.mpp.getPlugin());
	}
	
	public BukkitTask runTaskLater(BukkitRunnable clazz, long time)
	{
		return clazz.runTaskLater(this.mpp.getPlugin(), time);
	}
	
	public BukkitTask runTaskLaterAsync(BukkitRunnable clazz, long time)
	{
		return clazz.runTaskLaterAsynchronously(this.mpp.getPlugin(), time);
	}
	
	public static abstract class Sheduler extends BukkitRunnable
	{
		protected MetalPonyPlug mpp;
		
		public Sheduler(MetalPonyPlug mppl)
		{
			this.mpp = mppl;
		}
		
		public BukkitTask runTask()
		{
			return super.runTask(mpp.getPlugin());
		}
		
		public BukkitTask runTaskAsync()
		{
			return super.runTaskAsynchronously(mpp.getPlugin());
		}
		
		public BukkitTask runTaskLater(long time)
		{
			return super.runTaskLater(mpp.getPlugin(), time);
		}
		
		public BukkitTask runTaskLaterAsync(long time)
		{
			return super.runTaskLaterAsynchronously(mpp.getPlugin(), time);
		}
		
		public BukkitTask runTaskTimer(long delay, long period)
		{
			return super.runTaskTimer(mpp.getPlugin(), delay, period);
		}
		
		public BukkitTask runTaskTimerAsync(long delay, long period)
		{
			return super.runTaskTimerAsynchronously(mpp.getPlugin(), delay, period);
		}
		
		public void stop()
		{
			this.cancel();
		}
	}
}