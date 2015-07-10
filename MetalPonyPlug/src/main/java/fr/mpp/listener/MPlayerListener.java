package fr.mpp.listener;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import fr.mpp.MetalPonyPlug;
import fr.mpp.config.MConfig;
import fr.mpp.mpp.Classes;
import fr.mpp.mpp.ClassesUtils;
import fr.mpp.mpp.ComunSys;
import fr.mpp.mpp.MHalfBedSys;
import fr.mpp.mpp.RankLevel;
import fr.mpp.utils.ItemInventory;
import fr.mpp.utils.Locate;

public class MPlayerListener implements Listener
{
	private final MetalPonyPlug mpp;
	private MConfig confS;
	private final MHalfBedSys mhbs;
	private ComunSys cs;
	
	public MPlayerListener(MetalPonyPlug mppl)
	{
		this.mpp = mppl;
		this.confS = this.mpp.getConfig();
		this.mhbs = new MHalfBedSys(mppl);
		this.cs = new ComunSys();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLogin(PlayerLoginEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		String pN = event.getPlayer().getName();
		this.confS.reloadCustomConfig();
		if (pN.equalsIgnoreCase("JesFot") || pN.equalsIgnoreCase("lydia_drew"))
		{
			//this.mpp.getPerm().addAttachment(event.getPlayer());
			//this.mpp.getPerm().setPerm(event.getPlayer(), "all", true);
			event.getPlayer().addAttachment(mpp.getPlugin(), "all", true);
			if (pN.equalsIgnoreCase("JesFot"))
			{
				this.confS.getCustomConfig().set("mpp.rank."+RankLevel.STATUT.getName()+".jesfot", Classes.Prince.getAppel());
				event.getPlayer().sendMessage("Vous etes Prince.");
				event.getPlayer().setCustomName(Classes.Prince.getClasse().getDisplayName() + pN);
				event.setJoinMessage(ChatColor.YELLOW + "The Prince joined the game.");
				//this.mpp.mainCity.setPrince(new Prince(event.getPlayer()));
			}
			else
			{
				this.confS.getCustomConfig().set("mpp.rank."+RankLevel.STATUT.getName()+".lydia_drew", Classes.Princess.getAppel());
				event.getPlayer().sendMessage("Vous etes Princesse.");
				event.getPlayer().setCustomName(Classes.Princess.getClasse().getDisplayName() + pN);
				event.setJoinMessage(ChatColor.YELLOW + "The Princesse joined the game.");
				//this.mpp.mainCity.setPrincesse(new Prince(event.getPlayer()));
			}
		}
		if (this.confS.getCustomConfig().getBoolean("mppbase.registered."+pN.toLowerCase()) != false)
		{
			this.confS.getCustomConfig().set("mppbase.logtimes."+pN.toLowerCase(), (this.confS.getCustomConfig().getInt("mppbase.logtimes."+pN.toLowerCase())+1));
			if (this.confS.getCustomConfig().getInt("mppbase.logtimes."+pN.toLowerCase()) >= 50)
			{
				this.confS.getCustomConfig().set("mpp.rank."+RankLevel.HAB.getName()+"."+pN.toLowerCase(), Classes.Regular.getAppel());
				event.getPlayer().setCustomName(Classes.Regular.getClasse().getDisplayName() + pN);
				event.getPlayer().setDisplayName(Classes.Regular.getClasse().getDisplayName() + pN);
			}
			String c = this.confS.getCustomConfig().getString("mpp.rank."+RankLevel.MAIN.getName()+"."+pN.toLowerCase());
			Classes cl = ClassesUtils.getClasseByAppelName(c);
			event.getPlayer().setCustomName(cl.getClasse().getDisplayName() + pN);
			event.getPlayer().setDisplayName(cl.getClasse().getDisplayName() + pN);
		}
		else
		{
			this.confS.getCustomConfig().set("mpp.rank."+RankLevel.HAB.getName()+"."+pN.toLowerCase(), Classes.Noobie.getAppel());
			this.confS.getCustomConfig().set("mpp.rank."+RankLevel.MAIN.getName()+"."+pN.toLowerCase(), Classes.Noobie.getAppel());
			this.confS.getCustomConfig().set("mppbase.registered."+pN.toLowerCase(), true);
			this.confS.getCustomConfig().set("mppbase.logtimes."+pN.toLowerCase(), 1);
			event.getPlayer().setCustomName(Classes.Noobie.getClasse().getDisplayName() + pN);
			event.getPlayer().setDisplayName(Classes.Noobie.getClasse().getDisplayName() + pN);
		}
		this.confS.saveCustomConfig();
		this.mpp.getEconomy().addPlayer(event.getPlayer());
		
		//this.mpp.mainCity.addCityzen(event.getPlayer());
		
		this.confS.reloadCustomConfig();
		if(this.confS.getCustomConfig().contains("logmsg") && this.confS.getCustomConfig().getString("logmsg") != null)
		{
			String logmsg = this.confS.getCustomConfig().getString("logmsg");
			String[] logmsgs = logmsg.split(" n ");
			//event.getPlayer().sendMessage(ChatColor.GOLD + this.confS.getCustomConfig().getString("logmsg"));
			for(String str : logmsgs)
			{
				event.getPlayer().sendMessage(ChatColor.GOLD + str);
			}
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(final PlayerRespawnEvent event)
	{
		// Code ...
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onPlayerEnchant(final EnchantItemEvent event)
	{
		// Code ...
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		Action rightClickAir = Action.RIGHT_CLICK_AIR;
		Action rightClickBlock = Action.RIGHT_CLICK_BLOCK;
		Action leftClickAir = Action.LEFT_CLICK_AIR;
		Action leftClickBlock = Action.LEFT_CLICK_BLOCK;
		if (action == rightClickAir || action == rightClickBlock)
		{
			if (action == rightClickAir)
			{
				// Code ...
				if (event.getItem().getType().equals(Material.ARROW))
				{
					Player player = event.getPlayer();
					this.cs.setCInv(this.confS.getInventory("mpp.origchest.inv"));
					player.openInventory(this.cs.getCInv());
				}
			}
			else
			{
				if (event.getClickedBlock().getType().equals(Material.CHEST))
				{
					this.confS.reloadCustomConfig();
					Block block = event.getClickedBlock();
					Location bLoc = block.getLocation();
					Location oLoc = this.confS.getLoc("mpp.origchest.location");
					if (Locate.compare2Loc(bLoc, oLoc))
					{
						event.setCancelled(true);
						event.getPlayer().openInventory(this.cs.getCInv());
					}
				}
				else if(ItemInventory.isEqual(event.getClickedBlock().getType(), new Material[]{Material.CROPS, Material.CARROT, Material.POTATO, Material.NETHER_WARTS}))
				{
					if(!event.hasItem())
					{
						//
					}
					else if(ItemInventory.isHoe(event.getItem().getType()))
					{
						Block block = event.getClickedBlock();
						Location loc = block.getLocation();
						boolean cr = block.getType()!=Material.NETHER_WARTS?CropState.getByData(block.getData()).equals(CropState.RIPE):
							CropState.getByData(block.getData()).equals(CropState.SMALL);
						
						if(cr)
						{
							Material m = block.getType();
							block.breakNaturally();
							loc.getWorld().getBlockAt(loc).setType(m);
						}
					}
				}
			}
		}
		else if (action == leftClickAir || action == leftClickBlock)
		{
			if (action == leftClickAir)
			{
				// Code ...
			}
			else
			{
				// Code ...
			}
		}
	}
	
	@EventHandler
	public void onPlayerClickEntity(final PlayerInteractEntityEvent event)
	{
		ClassesUtils cu = new ClassesUtils(this.confS);
		Entity entity = event.getRightClicked();
		if (entity instanceof ItemFrame)
		{
			ItemFrame frame = (ItemFrame)entity;
			ItemStack stack = frame.getItem();
			Material mat = stack.getType();
			Location loc = frame.getLocation();
			if (ClassesUtils.isInZone(loc))
			{
				String name = stack.getItemMeta().getDisplayName();
				Classes cl = ClassesUtils.getClasseByName(name);
				if (mat == cl.getClasse().getItem())
				{
					cu.addRank(cl, event.getPlayer(), RankLevel.MAIN);
					event.getPlayer().sendMessage("You become " + cl.getAppel());
					mpp.getLogger().log(Level.INFO, event.getPlayer().getName() + " clicked at the right place and became " + cl.getAppel());
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerChat(final AsyncPlayerChatEvent event)
	{
		// Code ...
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayergoBed(final PlayerBedEnterEvent event)
	{
		Player player = event.getPlayer();
		this.mhbs.updatePlayers();
		this.mhbs.addPlayerInBed(player);
		player.getServer().broadcastMessage(this.mhbs.howManyInBedText());
		if (this.mhbs.hasHalfInBed())
		{
			this.mpp.broad("HalfInBed !!");
			this.mhbs.passNight(this.mhbs.getPlayersInBed());
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLeaveBed(final PlayerBedLeaveEvent event)
	{
		Player player = event.getPlayer();
		this.mhbs.updatePlayers();
		this.mhbs.removePlayerInBed(player);
		this.mhbs.endPassN();
	}
	
	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		long time = System.currentTimeMillis()-event.getPlayer().getLastPlayed();
		event.setQuitMessage(event.getQuitMessage()+" T="+time);
	}

	@EventHandler
	public void onPlayerKick(final PlayerKickEvent event)
	{
		// Code ..
	}

	@EventHandler
	public void onPlayerChangeWorld(final PlayerChangedWorldEvent event)
	{
		// Code ...
	}

	@EventHandler
	public void onPlayerGMChange(final PlayerGameModeChangeEvent event)
	{
		ClassesUtils cu = new ClassesUtils(this.confS);
		Player player = event.getPlayer();
		if (cu.getRank(player, RankLevel.STATUT).getClasse().hasPrivilege())
		{
			return;
		}
	}

	@EventHandler
	public void onPlayerTeleport(final PlayerTeleportEvent event)
	{
		// Code ...
	}
}
