package fr.mpp.quests;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;

import fr.mpp.utils.MPlayer;

public class KillMobs extends Quest implements Listener
{
	private int mobsNumber;
	private EntityType mob;
	private boolean notify = false;
	
	public KillMobs(Player p_player)
	{
		super(p_player.getName());
		super.setQuestType(QuestType.KILLMOB);
	}
	
	@SuppressWarnings("deprecation")
	public void setMob(EntityType p_mob)
	{
		if(p_mob.getTypeId()>=50 && p_mob.getTypeId()<= 68 && p_mob != EntityType.ENDER_DRAGON && p_mob != EntityType.WITHER)
		{
			this.mob = p_mob;
		}
	}
	
	public EntityType getMob()
	{
		return this.mob;
	}
	
	public void setHowManyMobs(int p_nb)
	{
		this.mobsNumber = p_nb;
	}
	
	public int getHowManyMobs(int p_nb)
	{
		return this.mobsNumber;
	}
	
	public void setName(String p_name)
	{
		super.setName("[KM]"+p_name);
	}
	
	@EventHandler
	public void onEntityKill(EntityDeathEvent event)
	{
		if(super.getName()!=null && (event.getEntity().getCustomName() == null || !event.getEntity().getCustomName().equals(super.getName().replaceAll("_", " "))))
		{
			return;
		}
		if(event.getEntity().getLastDamageCause().getCause().equals(DamageCause.ENTITY_ATTACK))
		{
			EntityDamageByEntityEvent damage = (EntityDamageByEntityEvent)event.getEntity().getLastDamageCause();
			if(damage.getDamager() instanceof Player && ((Player)damage.getDamager()).equals(MPlayer.getPlayerByName(super.getPlayerName())) &&
					damage.getEntity().getType().equals(this.mob))
			{
				this.mobsNumber--;
				if(this.mobsNumber == 0)
				{
					HandlerList.unregisterAll(this);
				}
				else if(this.notify)
				{
					Player player = MPlayer.getPlayerByName(super.getPlayerName());
					player.sendMessage("");
				}
			}
		}
	}
}