package com.yoshiplex.games.mariokart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.yoshiplex.Main;
import com.yoshiplex.YPTime;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.games.ArenaGame;
import com.yoshiplex.games.mariokart.commands.CharacterCommand;
import com.yoshiplex.games.mariokart.commands.MKCommand;
import com.yoshiplex.games.mariokart.effects.MKNoneEffect;
import com.yoshiplex.games.mariokart.items.GiveItem;
import com.yoshiplex.games.mariokart.listeners.MKListener;
import com.yoshiplex.games.mariokart.projectiles.MKProjectileManager;
import com.yoshiplex.games.mariokart.shop.CharacterShop;
import com.yoshiplex.games.mariokart.shop.MarioKartCosmeticsShop;
import com.yoshiplex.games.mariokart.track.MKCheckPoint;
import com.yoshiplex.games.mariokart.track.MKItemBox;
import com.yoshiplex.games.mariokart.track.RaceManager;
import com.yoshiplex.games.mariokart.tracks.BowserCastle2;
import com.yoshiplex.games.mariokart.tracks.MKTrack;
import com.yoshiplex.games.mariokart.tracks.MarioCircuit;
import com.yoshiplex.games.mariokart.tracks.RainbowRoad;
import com.yoshiplex.teleportation.worlds.WorldMarioKart;
import com.yoshiplex.util.UnloadedLocation;
import com.yoshiplex.util.YPLibrary;

public class MKManager extends ArenaGame<MKPlayer>{
	private static MKManager manager;
	private static final UnloadedLocation joinArea = new UnloadedLocation("Mario_Circuit", 451.5, 4, -580.5, 180, 0);
	private static final UnloadedLocation waitingArea = new UnloadedLocation("Mario_Circuit", 473, 4, -553, 0, 0);
	
	
	private MKTrack track = null;
	private int tickToChange = 0;
	private MKState state = MKState.NONE;
	private RaceManager raceManager = null;
	private MKProjectileManager proManager = null;

	private boolean tourney = false;
	private List<Player> tourneyPlayers = new ArrayList<>();
	private boolean pause = false;
	
	public MKManager(Main main){
		super();
		manager = this;
		new MKListener();
		//new MKPacketListener();
		main.getCommand("mk").setExecutor(new MKCommand(this));
		main.getCommand("character").setExecutor(new CharacterCommand());
		new MarioKartCosmeticsShop(main);
		new CharacterShop(main);
	}
	public void joinPlayer(Player p){
		if(tourney && !tourneyPlayers.contains(p)){
			p.sendMessage(ChatColor.RED + "There is a tourney going on right now so you cannot join.");
			return;
		}
		boolean contains = false;
		MKPlayer mkp = MKPlayer.getPlayer(p);
		if(mkp != null){
			contains = true;
		}
		if(contains){
			p.sendMessage(ChatColor.RED + "You are already joined in the game!");
			return;
		} 
		mkp = MKPlayer.getNewMKPlayer(p);
		p.teleport(waitingArea);
		mkp.setAllowedMove(false);
		mkp.setDriving(false);
		mkp.setLap(0);
		if(state == MKState.INGAME){
			p.sendMessage(ChatColor.YELLOW + "There is a game going on right now. Please wait for the race to finish so you can join again.");
			mkp.setInGame(false);
			
		}
	}
	public RaceManager getRaceManager(){
		return raceManager;
	}
	public void quitPlayer(MKPlayer p){
		if(p.getYPPlayer() == null){
			MKPlayer.removePlayer(p);
			return;
		}
		p.onLeave();
		while(MKPlayer.getPlayer(p.getYPPlayer().toPlayer()) != null){
			MKPlayer.removePlayer(p);
		}
		Player player = p.getYPPlayer().toPlayer();
		if(player == null){
			return;
		}
		player.teleport(joinArea);
		player.sendMessage(ChatColor.YELLOW + "You quit the game!");
		player.setAllowFlight(false);
		
	}
	
	@Override
	public void toArena(){
		this.track = getRandomTrack();
		state = MKState.INGAME;
		List<Integer> used = new ArrayList<>();
		for(MKPlayer p : MKPlayer.getPlayers()){
			
			p.setItem(null);
			p.setEffect(new MKNoneEffect());
			Player player = p.getYPPlayer().toPlayer();
			int place = p.getPlace();
			p.setInGame(true);
			p.resetVariables();
			p.setFinished(false);
			p.setAllowedMove(false);
			p.setDriving(true);
			p.setLap(0);
			place = 12 - place;
			if(place == 12 || used.contains(place)){
				place = 0;
				while(used.contains(place)){
					place++;
				}
			}
			used.add(place);
			player.teleport(track.getStartingPositions().get(place));
			p.resetInventory();
			player.sendMessage(ChatColor.GREEN + "Race starting in 40 seconds. You are starting in position " + (place + 1) + ".");
			p.getYPPlayer().setMainBar(WorldMarioKart.getInstance().getBossBar());
		}
		this.raceManager = new RaceManager(track);
		this.proManager = new MKProjectileManager();
		new BukkitRunnable() {
			int time = 40;
			@Override
			public void run() {
				
				if(time == 10 || time == 15 || time == 20 || time == 30 || (time <=5 && time > 0)){
					broadcast(ChatColor.YELLOW + "Starting in " + time);
				}
				if(time == 37){
					for(MKPlayer p : MKPlayer.getPlayers()){
						YPPlayer player = p.getYPPlayer();
						player.setPack(track.getPack(), false);
					}
				}
				
				if(time == 10){
					MKSound.TENSECOND.sendAll();
				} else if(time == 3){
					MKSound.THREESECOND.sendAll();
				} 
				if(time > 0){
					for(MKPlayer p : MKPlayer.getPlayers()){
						YPLibrary.sendTitle(p.getYPPlayer().toPlayer(), ChatColor.YELLOW + ChatColor.BOLD.toString() + time, ChatColor.YELLOW + "Get ready!", 20);
					}
				} 
				if(time == 0){
					for(MKPlayer p : MKPlayer.getPlayers()){
						Player player = p.getYPPlayer().toPlayer();
						p.setAllowedMove(true);
						p.setLap(0);
						p.setFinished(false);
						YPLibrary.sendTitle(player, "", "", 1);
					}
					broadcast(ChatColor.YELLOW + "GO!!!");
				}
				if(time == -2){
					MKSound.MUSIC.sendAll();
					this.cancel();
				}
				time--;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}
	
	@Override
	public void toLobby(){
		state = MKState.INLOBBY;
		raceManager = null;
		if(proManager != null){
			proManager.removeAll();
			proManager = null;
			System.out.println("A projectile was removed!");
		}
		if(track != null){
			for(MKItemBox box : track.getItems()){
				box.remove();
			}
		}
		int size = MKPlayer.getPlayers().size();
		for(MKPlayer p : MKPlayer.getPlayers()){
			int money = 0;
			p.setItem(null);
			p.setEffect(new MKNoneEffect());
			Player player = p.getYPPlayer().toPlayer();
			p.setDriving(false);
			p.setAllowedMove(false);
			p.setFinished(true);
			int place = p.getPlace();
			player.teleport(waitingArea);
			ChatColor color = null;
			if(place >= 6){
				color = ChatColor.RED;
			} else {
				color = ChatColor.GREEN;
			}
			String th = "th";
			if(place == 1){
				th = "st";
				money = size * 2;
			} else if(place == 2){
				th = "nd";
				if(size > 2){
					money = size;
				}
			} else if(place == 3){
				th = "rd";
				if(size > 4){
					money = size / 2;
				}
			}
			player.sendMessage(color + "You came in " + ChatColor.GRAY + place + ChatColor.ITALIC + th + ChatColor.RESET + color.toString() + " place!");
			if(money != 0){
				Main.economy.depositPlayer(player, money);
				player.sendMessage(ChatColor.GREEN + "You got " + money + " coins!");
			}
			p.setPlace(0);
			p.setLap(0);
			
		}
	}
	private MKTrack next = null;
	private MKTrack getRandomTrack(){
		if(next != null){
			MKTrack next = this.next;
			this.next = null;
			next.enable();
			return next;
		}
		List<MKTrack> tracks = getTracks();
		MKTrack track = tracks.get((new Random()).nextInt(tracks.size()));
		track.enable();
		return track;
	}
	public void setNextTrack(MKTrack track){
		this.next = track;
	}
	public MKState getState(){
		return state;
	}
	public static List<MKTrack> getTracks(){
		return Arrays.asList(new MarioCircuit(), new RainbowRoad(), new BowserCastle2());
	}
	
	@Override
	public void run(){
		Iterator<MKPlayer> it = MKPlayer.getPlayers().iterator();
		while(it.hasNext()){
			MKPlayer p = it.next();
			if(p.getYPPlayer().toPlayer() == null || !p.getYPPlayer().toPlayer().isOnline()){
				it.remove();
			}
			p.run();
		}
		if(tickToChange <= YPTime.getTime() || tickToChange == 0){
			if(state == MKState.INGAME || state == MKState.NONE || MKPlayer.getPlayers().size() < 2){
				tickToChange+=(2 * 20 * 60);
				if((MKPlayer.getPlayers().size() < 2 || pause) && state != MKState.INGAME && state != MKState.NONE){
					if(!pause){
						broadcast(ChatColor.YELLOW + "There are not enough players to start a new match!");
					}
					
				} else {
					toLobby();
				}
				track = null;
			} else if(state == MKState.INLOBBY){
				toArena();
				tickToChange+=(track.getMaxMinutes() * 20 * 60);
			}
		}
		if(proManager != null && state == MKState.INGAME){
			proManager.run();
		}
		if(track != null){
			for(MKItemBox box : track.getItems()){
				box.update();
			}
			
		}
		String time = YPLibrary.getMinutesSecondsFormat(tickToChange);
		List<MKPlayer> toRemove = new ArrayList<>();
		boolean updatePlaces = true;
		
		for(MKPlayer p : MKPlayer.getPlayers()){
			if(p.getYPPlayer() == null || p.getYPPlayer().toPlayer() == null){ 
				toRemove.add(p);
				continue;
			}
			if(!p.getYPPlayer().toPlayer().isOnline()){
				toRemove.add(p);
				continue;
			}
			if(p.getItem() != null && !p.getItem().isInUse()){
				p.setItem(null);
			}
			//Player player = p.getYPPlayer().toPlayer();
		
			p.updateScoreboard(time);
			
			if(track == null || state != MKState.INGAME){
				continue;
			}
			if(p.isInGame() && track.isDeath(p.getYPPlayer().getLocation()) && !p.isFinished()){
				
				Location spawn = null;
				int passed = -1;
				for(MKCheckPoint point : track.getCheckPoints()){
					if(point.isPassedFor(p, p.getLap())){
						passed++;
					} else {
						break;
					}
				}
				if(passed > -1){
					spawn = track.getCheckPoints().get(passed).getSpawn();
				}
				if(spawn == null){
					spawn = track.getStartingPositions().get(0);
				}
				p.respawn(spawn);
				MKSound.CHAR_DEATH.send(p);
			}
			for(MKCheckPoint point : track.getCheckPoints()){ // goes first to last
				if(p.isFinished()) break;
				boolean inPoint = point.isInCheckPoint(p);
				
				if(inPoint || point.isPassedFor(p, p.getLap())){
					point.setPassedForLap(p, p.getLap());
				} else {
					break;
				}
			}
			
			for(MKItemBox box : track.getItems()){
				if(box.isInItem(p)){
					if(p.getItem() != null && p.getItem().isInUse()){
						box.removeFor(10 * 20);
						MKSound.ITEMBOXHIT.send(p, false);
						continue;
					}
					box.removeFor(15 * 20);
					p.setItem(box.getNext(p));
					p.getItem().onGive();
					MKSound.ITEMBOXHIT.send(p, false);
					final MKPlayer send = p;
					new BukkitRunnable() {
						int ran = 0;
						@Override
						public void run() {
							if(send.getItem() instanceof GiveItem){
								if(ran % 2 == 0){
									MKSound.ITEMINCOMING.send(send, true);
								}
							} else {
								MKSound.ITEMGET.send(send, true);
								this.cancel();
							}
							if(ran > 20 * 5){
								this.cancel();
							}
							ran++;
						}
					}.runTaskTimer(Main.getInstance(), 10, 4);
				}
			}
		
			if(track.isInFinishLine(p)){
				if(p.getLap() == 0 && !track.startAfterFinishLine()){
					p.setLap(1);
				} else {
					if(p.canGoToNextLap(track) && !p.isFinished()){
						p.setLap(p.getLap() + 1);
					}
				}
				if(p.getLap() >= track.getLapAmount() + 1){
					p.setLap(track.getLapAmount() + 1);
					if(!p.isFinished()){
						p.setFinished(true);
						p.setDriving(false);
						p.setAllowedMove(false);
						p.getYPPlayer().toPlayer().teleport(waitingArea);
						updatePlaces = false;
						raceManager.updatePlaces();
						p.sendMessage(ChatColor.GRAY + "You finished the race!");
					}
				}
				
			}
		}
		for(MKPlayer p : toRemove){
			quitPlayer(p);
			MKPlayer.removePlayer(p);
		}
		if(YPTime.getTime() % 20 == 0 && raceManager != null && state == MKState.INGAME && updatePlaces){
			raceManager.updatePlaces();
		}
	}
	
	public boolean isOnRoad(MKPlayer p){
		if(track == null){
			return true;
		}
		Location l = p.getYPPlayer().toPlayer().getLocation();
		Material m = l.getBlock().getRelative(BlockFace.DOWN).getType();
		if(m == Material.AIR){
			return true;
		}
		return (track.useOffRoad() && !track.offRoad().contains(m)) || (!track.useOffRoad() && track.onRoad().contains(m));
	}
	
	public MKTrack getTrack(){
		return track;
	}
	
	public static MKManager getManager(){
		return manager;
	}
	public MKProjectileManager getProManager() {
		return proManager;
	}
	
	public List<Player> getTourneyPlayers(){
		return tourneyPlayers;
	}
	public boolean isInTourney(){
		return tourney;
	}
	public void setTourney(boolean b){
		this.tourney = b;
		if(!b){
			this.tourneyPlayers.clear();
		}
	}
	public boolean isPaused(){
		return pause;
	}
	public void setPaused(boolean b){
		this.pause = b;
	}
	
	@Override
	public void disable() {
		MKPlayer.clearAll();
		if(track == null){
			return;
		}
		this.toLobby();
		for(MKItemBox box : track.getItems()){
			box.remove();
		}
	}
	
	@Override
	public void next() {
		tickToChange = YPTime.getTime() + 2;
	}
	public void kickAll() {
		Iterator<MKPlayer> it = MKPlayer.getPlayers().iterator();
		while(it.hasNext()){
			MKPlayer p = it.next();
			this.quitPlayer(p);
		}
	}
	@Override
	public List<MKPlayer> getPlayers() {
		return MKPlayer.getPlayers();
	}
	@Deprecated
	@Override
	public MKPlayer getPlayer(Player player) {
		return MKPlayer.getPlayer(player);
	}
}
