package fr.mpp.economy;

import java.util.Calendar;
import java.util.TimeZone;

import org.bukkit.scheduler.BukkitTask;

import fr.mpp.MetalPonyPlug;
import fr.mpp.utils.MSheduler.Sheduler;

public class MMensual extends Sheduler
{
	public MMensual(MetalPonyPlug mppl)
	{
		super(mppl);
	}
	
	public BukkitTask start()
	{
		return super.runTaskTimer(0L, 10000L);
	}
	
	@Override
	public void run()
	{
		this.mpp.getConfig().reloadCustomConfig();
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
		int month = cal.get(Calendar.DAY_OF_YEAR);
		if(!this.mpp.getConfig().getCustomConfig().contains("date.day_of_year"))
		{
			this.mpp.getConfig().getCustomConfig().createSection("date.day_of_year");
			this.mpp.getConfig().getCustomConfig().set("date.day_of_year", month);
			this.mpp.getConfig().saveCustomConfig();
		}
		int c = this.mpp.getConfig().getCustomConfig().getInt("date.day_of_year");
		if(c != month)
		{
			this.mpp.getConfig().getCustomConfig().set("date.day_of_year", month);
			this.mpp.getConfig().saveCustomConfig();
			this.mpp.broad("Nouveau mois !! ("+cal.get(Calendar.DAY_OF_MONTH)+")");
		}
		MetalPonyPlug.getDebug().broad("Pas de nouveau mois...", 25);
	}
}
