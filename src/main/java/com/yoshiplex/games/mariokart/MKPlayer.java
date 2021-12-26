package com.yoshiplex.games.mariokart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.yoshiplex.Main;
import com.yoshiplex.YPTime;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.games.GamePlayer;
import com.yoshiplex.games.mariokart.effects.MKEffect;
import com.yoshiplex.games.mariokart.effects.MKJumpEffect;
import com.yoshiplex.games.mariokart.effects.MKNoneEffect;
import com.yoshiplex.games.mariokart.items.MKItem;
import com.yoshiplex.games.mariokart.track.MKCheckPoint;
import com.yoshiplex.games.mariokart.tracks.MKTrack;
import com.yoshiplex.teleportation.worlds.WorldMarioKart;
import com.yoshiplex.util.AddFace;
import com.yoshiplex.util.ObjectGetter;
import com.yoshiplex.util.UnloadedLocation;
import com.yoshiplex.util.YPMath;

import me.tigerhix.lib.scoreboard.ScoreboardLib;
import me.tigerhix.lib.scoreboard.common.EntryBuilder;
import me.tigerhix.lib.scoreboard.type.Entry;
import me.tigerhix.lib.scoreboard.type.Scoreboard;
import me.tigerhix.lib.scoreboard.type.ScoreboardHandler;

public class MKPlayer extends GamePlayer{
	private static List<MKPlayer> allPlayers = new ArrayList<>();
	
	private static final List<String> characters = Arrays.asList("mario", "yoshi", "luigi");
	
	private static final int DRIFTVELOCITYDEGREE = -45;
	private static final double WHILEDRIFTINGDEGREECHANGE = 5; // 5
	private static final double TURNAMOUNTPERTICK = 3; // 3
	private static final int SPEEDPERRUN = 15; // does not get run every tick // fastplacers can abuse
	private static final int SPEEDTOSUBTRACT = 3;
	
	private boolean allowMove = false;
	private boolean isDriving = false;
	private boolean isFinished = false;
	private int lastTimeSped = 0; // this variable can only update every 4 seconds.
	private Drift drift = Drift.NONE;
	private double degree = 0;
	private int lastDrift = 0;
	private int lastDriftStart = 0;
	private int speed = 0;
	private YPPlayer player = null;
	private int lap;
	private int place;
	private int finalPlace;
	private boolean wasFinished = false;
	private Minecart cart = null;
	private boolean isInCart = false;
	
	
	private boolean inGame = false;
	private Scoreboard board = null;
	
	private MKItem item = null;
	
	private MKEffect effect = null;
	private MKEffect jump = null;
	
	private int boastUntil = 0; // NOT FOR DRIFTING boasts
	private int lastFly = 0;
	private int lastJump = 0; 
	private int boastWhenOnGround = 0;
	
	
	private MKPlayer(YPPlayer player){
		if(player == null){
			System.out.println("YPERROR: the YPPlayer that was assigned to MKPlayer is null!!!!");
		}
		this.player = player;
		
		allPlayers.add(this);
		resetVariables();
		this.lap = 0;
		this.inGame = false;
		this.allowMove = true;
		
		createScoreboard();
	}
	public void resetVariables(){
		Player p = this.getYPPlayer().toPlayer();
		this.lap = 0;
		this.drift = Drift.NONE;
		this.place = 0;
		this.speed = 0;
		this.lastDrift = 0;
		this.lastTimeSped = 0;
		this.wasFinished = false;
		effect = new MKNoneEffect();
		jump = new MKNoneEffect();
		resetInventory();
		p.setAllowFlight(false);
	}
	public YPPlayer getYPPlayer(){
		return player;
	}
	private String title = "MarioKart";
	private List<Entry> entries = null;
	public void createScoreboard(){
		board = ScoreboardLib.createScoreboard(this.getYPPlayer().toPlayer()).setHandler(new ScoreboardHandler() {
			
			@Override
			public String getTitle(Player arg0) {
				return title;
			}
			
			@Override
			public List<Entry> getEntries(Player arg0) {
				return entries;
			}
		}).setUpdateInterval(2);
		board.activate();
	}
	
	public void updateScoreboard(String time){
		
		MKManager manager = MKManager.getManager();
		String name = ChatColor.DARK_GREEN.toString();
		if(manager.getTrack() != null){
			name += manager.getTrack().getTrackName();
		} else {
			name += "MarioKart";
		}
		name = name + " " + time;
		EntryBuilder build = new EntryBuilder();
		this.title = name;
		build.next(ChatColor.YELLOW + ChatColor.BOLD.toString() + " Place: " + ChatColor.RESET + ChatColor.RED + place);
		build.next(ChatColor.YELLOW + ChatColor.BOLD.toString() + " Lap: " + ChatColor.RESET + ChatColor.RED + lap);
		if(manager.getRaceManager() != null){
			build.blank();
			build.blank();
			build.next(ChatColor.GRAY + "Places:");
			for(MKPlayer player : manager.getRaceManager().getTopPlayers()){
				ChatColor color = ChatColor.YELLOW;
				if(player == this){
					color = ChatColor.GREEN;
				}
				build.next(color + player.getYPPlayer().toPlayer().getName() + ChatColor.AQUA + ChatColor.BOLD.toString() + " " + player.getPlace());
			}
		} else {
			build.blank();
			build.next(ChatColor.YELLOW + "New match");
			build.next(ChatColor.YELLOW + "starting soon.");
		}

		
		//build.next(ChatColor.YELLOW + "Top Three:");
//		for(String top : topThree){
//			build.next(top);
//		}
		entries = build.build();


	}
	
	public void onItemPickup(){ // Item is not yet given. This should only be triggered when a player is in a mystery box.
		
	}
	
	public void setFinished(boolean finished){
		this.isFinished = finished;
	}
	public boolean isFinished(){
		return isFinished;
	}
	public boolean isOnRoad(){
		return MKManager.getManager().isOnRoad(this);
	}
	public void setLap(int lap){
		this.lap = lap;
	}
	public int getLap(){
		return lap;
	}
	
	public MKEffect getEffect(){
		return effect;
	}
	public void setEffect(MKEffect effect){
		this.effect = effect;
		
	}
	
	public MKItem getItem(){
		return item;
	}
	public void setItem(MKItem item){
		this.item = item;
		resetInventory();
	}
	
	public void onRaceStart(){
		this.allowMove = true;
		this.isDriving = true;
		if(Math.abs((lastTimeSped + (2 * 20) - YPTime.getTime())) <= 10){
			this.speed = 100;
			this.lastDrift = YPTime.getTime();
			this.lastDriftStart = YPTime.getTime() - (4 * 20);
		}
	}

	private void setDrifting(Drift drift){
		if(!effect.canDrift()){
			this.drift = Drift.NONE;
			return;
		}
		this.drift = drift;
		if(drift != Drift.NONE){
			lastDriftStart = YPTime.getTime();
		}
	}
	public Vector getVelocity(int currentSpeed){
		if(!allowMove){
			return new Vector(0, 0, 0);
		}
		if(!this.isOnRoad() && !effect.ignoreOffRoad()){
			drift = Drift.NONE;
		}
		//Player p = this.getYPPlayer().toPlayer();
		//float pitch = (float) this.pitchDegree; // make sure that the variable degree is (player_location.getPitch() + 90)
		float yaw = (float) getDegree();
		if(jump instanceof MKJumpEffect){
			yaw = jump.adjustDegree((int) degree);
			degree = yaw;
		}
//		double x = Math.sin(pitch) * Math.cos(yaw);
//		double y = Math.sin(pitch) * Math.sin(yaw);
//		double z = Math.cos(pitch);
		
		
//		double xzLen = Math.cos(pitch);
//		double x = xzLen * Math.cos(yaw);
//		double y = Math.sin(pitch); 
//		double z = xzLen * Math.sin(-yaw);
		
		double radians = Math.toRadians(yaw);

		double radius = 0;
		boolean onRoad = this.isOnRoad();
		for(int i = 0;i<=140 && (onRoad || i <= 50 || effect.ignoreOffRoad());i+=10){
			if(i <= currentSpeed){
				radius = i * 0.01;
			}
		}
		if(!onRoad && !effect.ignoreOffRoad()){
			if(currentSpeed > 80){
				currentSpeed = 80;
			} else if(speed > 70){
				currentSpeed--;
			} else if(speed > 50){
				currentSpeed--;
			}
			speed = currentSpeed;
		}
		boolean usingDriftBoast = false;
		if((onRoad || effect.ignoreOffRoad()) && lastDrift + (20 * 1.5) >= YPTime.getTime() && lastDriftStart != 0 && lastDriftStart + (20 * 3) <= YPTime.getTime()){
			radius+=0.3;
			currentSpeed+=30;
			usingDriftBoast = true;
			//p.sendMessage(ChatColor.GREEN + "Drift boast");
		}
		if(!usingDriftBoast && getYPPlayer().isOnGround()){
			int time = YPTime.getTime();
			if(boastWhenOnGround != 0){
				boastUntil =  time + boastWhenOnGround;
				boastWhenOnGround = 0;
			}
			if(boastUntil > time){
				radius+=0.3;
				currentSpeed+=30;
			}
		}
		//ActionBarAPI.sendActionBar(this.getYPPlayer().toPlayer(), ChatColor.RED.toString() + Math.floor(radius * 100));
		double x = Math.cos(radians) * radius;
		double y = -1;
		double z = Math.sin(radians) * radius;
		y = effect.getYVelocity((int)y);
		y = jump.getYVelocity((int)y);
		Vector vector = new Vector(x,y,z);
		//Vector vector = new Location(null, 0, 0, 0, getFinal(yaw, 90), pitch).getDirection().setY(1);//.multiply(speed / 100);
//		if(YPTime.getTime() < 20 * 5 * 60){
//			//this.getYPPlayer().toPlayer().sendMessage("x: " + x+ " y: " + y + " z " + z +" yaw: " + yaw + " radianpitch " + pitch + " getdegree: " + getDegree() + " yourpitch: " + getPlayerPitch() + " speed: " + speed + " direction: " + this.getYPPlayer().toPlayer().getLocation().getDirection());
//			this.getYPPlayer().toPlayer().sendMessage("degree " + degree + " getDegree " + getDegree() + " speed: " + speed + " drift: " + drift.toString() + " dir: " + drift.toDir());
//		}
		return vector;
		
	}
	
	private float getFinal(float number, double add){
		return YPMath.getFinal(number, add);
	}
	public int getSpeed(){
		
		return speed;
	}
	public float getDegree(){
		return getFinal((float) Math.floor(degree), (DRIFTVELOCITYDEGREE * drift.toDir()));
	}
	private void updateForDrift(){
		this.degree = getFinal((float)degree, WHILEDRIFTINGDEGREECHANGE * drift.toDir());
	}
	public void startDrift(){
		final float yaw = getPlayerYaw();
		new BukkitRunnable() {
			int ran = 0;
			@Override
			public void run() {
				float now  = getPlayerYaw();
				if(!getYPPlayer().toPlayer().isSneaking()){
					onFly();
					this.cancel();
					return;
				}
				if(ran >= 10 && getSpeed() > 10){
					if((yaw < now || yaw > now + 180) && !(yaw + 180 < now)){
						setDrifting(Drift.RIGHT);
					} else if((yaw > now || yaw + 180 < now) && !(yaw > now + 180)){
						setDrifting(Drift.LEFT);
					} else {
						setDrifting(Drift.NONE);
					}
					this.cancel();
				}
				ran++;
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	public void endDrift(){
		if(drift != Drift.NONE){
			lastDrift = YPTime.getTime();
		}
		setDrifting(Drift.NONE);
	}
	public void setDriving(boolean driving){
		this.isDriving = driving;
	}
	public boolean isDriving(){
		return isDriving;
	}
	public float getPlayerYaw(){
		return getFinal(this.getYPPlayer().toPlayer().getLocation().getYaw(), 90);
		
	}
	public int getPlace(){
		return place;
	}
	public void setPlace(int place){
		this.place = place;
	}
	
	public int getFinalPlace(){
		return finalPlace;
	}
	public void setFinalPlace(int place){
		this.finalPlace = place;
	}
	public boolean wasFinished(){
		return wasFinished;
	}
	public void setWasFinished(boolean finished){
		wasFinished = finished;
	}
	
	public void onLeave(){

		if(this.board != null){
			this.board.deactivate();
		}
		this.player.freeze(false);
		WorldMarioKart.getInstance().giveItems(this.player);
	}
//	public Chicken getChicken(){
//		if(!inVehicle){
//			return null;
//		}
//		if(chicken == null){
//			Player p = this.getYPPlayer().toPlayer();
//			chicken = (Chicken)p.getWorld().spawnEntity(p.getLocation(), EntityType.CHICKEN);
//		}
//		return chicken;
//	}
//	private Chicken chicken = null;
	
	@Override
	public void run() {
		if(item != null && !item.isInUse()){
			item = null;
		}
		if(!inGame){
			return;
		}
		Player p = this.getYPPlayer().toPlayer();
		if (isDriving) {
			if(!this.player.isFrozen()){
				this.player.freeze();
			}
			p.setAllowFlight(true);
			UnloadedLocation loc = this.getYPPlayer().getLocation();
			if(loc.relativeType(AddFace.DOWN)== Material.IRON_BLOCK && !(jump instanceof MKJumpEffect)){
				jump = new MKJumpEffect(this);
				lastJump = YPTime.getTime();
			}
			jump = jump.getNextEffect();
				// if(!isEnteredInVehicle){
				// enterVehicle();
				// }
			if(effect != null){
				effect = effect.getNextEffect();
			} else {
				effect = new MKNoneEffect();
			}
			int currentSpeed = effect.adjustSpeed(speed);
			if(effect.changeSpeedVariable()){
				speed = currentSpeed;
			}
			currentSpeed = jump.adjustSpeed(currentSpeed);
			if(jump.changeSpeedVariable()){
				speed = currentSpeed;
			}
			Vector set = getVelocity(currentSpeed);
			p.setVelocity(set);

			
			updateForDrift();
			float yaw = getPlayerYaw();
			if (effect.canTurn()) {
				for (int i = 0; (i < TURNAMOUNTPERTICK || (speed < 5 && i < 180))
						&& (degree < yaw || degree > yaw + 180) && !(degree + 180 < yaw); i++) {
					degree++;
				}
				for (int i = 0; (i < TURNAMOUNTPERTICK || (speed < 5 && i < 180))
						&& (degree > yaw || degree + 180 < yaw) && !(degree > yaw + 180); i++) {
					degree--;
				}
			}
			if (currentSpeed > SPEEDTOSUBTRACT) {
				speed -= SPEEDTOSUBTRACT;
			}
			degree = getFinal((float) degree, 0);
			if (!this.getYPPlayer().toPlayer().isSneaking()) {
				endDrift();
			}
			
			if(item != null && p.getInventory().getItem(2) != null && p.getInventory().getItem(2).getType() != Material.AIR){
				ActionBarAPI.sendActionBar(p, ChatColor.YELLOW + "Item: " + p.getInventory().getItem(2).getItemMeta().getDisplayName());
			}
			
		} else {
			if(player.isFrozen()){
				player.freeze(false);
			}
			p.setAllowFlight(false);
			degree = getPlayerYaw();
			if(!(effect instanceof MKNoneEffect)){
				effect = new MKNoneEffect();
			}
		}
	}
	
	public void resetInventory(){
		if(this.getYPPlayer() == null || this.getYPPlayer().toPlayer() == null){
			return;
		}
		Player p = this.getYPPlayer().toPlayer();
		p.getInventory().clear();
		p.getInventory().addItem(ObjectGetter.getItem(Material.WOOD_SWORD, 1, ChatColor.RED + "Steering Wheel (Shoot item forwards)", false));
		p.getInventory().addItem(ObjectGetter.getItem(Material.IRON_SWORD, 1, ChatColor.RED + "Steering Wheel (Shoot item backwards)", false));
		if(this.getItem() != null && item.useItem()){
			p.getInventory().addItem(item.getHeldItem());
		}
	}
	
	public void onFly(){ // for when a player tries to fly     (you do something) // 
		int time = YPTime.getTime();
		if(time - lastFly > 5 && time - lastJump < 9 && boastWhenOnGround == 0){
			boastWhenOnGround = 30;
			MKSound.TRICK.send(this, true);
			final MKPlayer t = this;
			new BukkitRunnable() {
				
				@Override
				public void run() {
					MKSound.CHAR_TRICK.send(t, true);
				}
			}.runTaskLater(Main.getInstance(), 5);
		}
	}
	
	public Minecart getCart(){
		return cart;
	}
	
	@Deprecated // does not work for players
	public void enterCart(){
		if(cart != null){
			cart.remove();
		}
		cart = (Minecart)this.getYPPlayer().getLocation().getWorld().spawnEntity(this.getYPPlayer().getLocation(), EntityType.MINECART);
		cart.setPassenger(this.getYPPlayer().toPlayer());
		isInCart = true;
	}
	public void leaveCart(){
		if(cart == null){
			return;
		}
		Location loc = cart.getLocation();
		cart.setPassenger(null);
		this.getYPPlayer().toPlayer().teleport(loc);
		cart.remove();
		isInCart = false;
	}
	public boolean cancelLeave(Minecart cart){
		if(this.cart == null || cart == null) return false;
		return this.cart == cart;
	}
	public boolean isInCart(){
		return isInCart;
	}
	
	public boolean canGoToNextLap(MKTrack track){
		boolean toReturn = true;
		for(MKCheckPoint point : track.getCheckPoints()){
			if(!point.isPassedFor(this, this.getLap())){
				toReturn = false;
				break;
			}
		}
		return toReturn;
	}
	public void speed(){
		speed+=SPEEDPERRUN;
		if(speed > 100){
			speed = 100;
		}
		if(lastTimeSped + (4 * 20) >= YPTime.getTime()){
			lastTimeSped = YPTime.getTime();
		}
	}
	public void setInGame(boolean in){
		inGame = in;
	}
	public boolean isInGame(){
		return inGame;
	}
	public boolean isMoveAllowed(){
		return allowMove;
	}
	public void setAllowedMove(boolean allow){
		this.allowMove = allow;
	}
	public void sendMessage(String message){
		this.getYPPlayer().toPlayer().sendMessage(message);
	}
	public static List<MKPlayer> getPlayers(){
		if(allPlayers == null){
			allPlayers = new ArrayList<>();
		}
		return allPlayers;
	}
	public static void removePlayer(MKPlayer p) {
		allPlayers.remove(p);
		p.setInGame(false);
	}
	public static void addPlayer(MKPlayer p){
		allPlayers.add(p);
	}
	public static MKPlayer getPlayer (Player p){
		for(MKPlayer player : allPlayers){
			if(player.getYPPlayer() == null){
				continue;
			}
			if(player.getYPPlayer().toPlayer() == p){
				return player;
			}
		}
		return null;
	}
	public static MKPlayer getNewMKPlayer(Player p){
		YPPlayer yp = YPPlayer.getYPPlayer(p);
		if(yp == null){
			System.out.println("YPERROR: yp is null line 572 MKPlayer.java");
		} else if(yp.toPlayer() == null){
			System.out.println("YPERROR: A YPPlayer object's Player is null line 576 MKPlayer.java");
		} else if(!yp.toPlayer().isOnline()){
			System.out.println("YPERROR: A YPPlayer object's Player isn't online!! line 578 MKPlayer.java");
		}
		
		return new MKPlayer(yp);
	}
	public static void clearAll(){
		allPlayers = new ArrayList<>();
	}
	public void respawn(Location spawn) {
		this.getYPPlayer().toPlayer().teleport(spawn);
		this.speed = 0;
		this.degree = getPlayerYaw();
		this.effect = new MKNoneEffect();
		this.jump = new MKNoneEffect();
		this.item = null;
		resetInventory();
	}
	public String getCharacter() {
		String path = this.getYPPlayer().getConfigPath() + ".mariokart.character";
		String r = Main.getConfigVar().getString(path);
		if(r == null){
			Main.getConfigVar().set(path, "none");
			return "none";
		}
		return r;
	}
	public static void setCharacter(YPPlayer p, String character){
		String path = p.getConfigPath() + ".mariokart.character";
		Main.getConfigVar().set(path, character);
		Main.getInstance().saveConfig();
	}
	public static List<String> getAvailableCharacters(){
		return characters;
	}
}
