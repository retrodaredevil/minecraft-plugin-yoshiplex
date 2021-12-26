package com.yoshiplex.games.slitherio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.yoshiplex.YPTime;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.games.GamePlayer;
import com.yoshiplex.games.slitherio.skin.SSkin;
import com.yoshiplex.teleportation.worlds.WorldSlitherio;
import com.yoshiplex.util.Hitbox;
import com.yoshiplex.util.UnloadedLocation;
import com.yoshiplex.util.YPMath;

import de.inventivegames.hologram.Hologram;
import de.inventivegames.hologram.HologramAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.SlimeWatcher;


public class SPlayer extends GamePlayer{
	private static final double TURNAMOUNTPERTICK = 15;
	private static final double LENGTHLOSSWHILESPEEDING = 0.1;
	private static final double MINIMUMBOASTINGLENGTH = 10 + LENGTHLOSSWHILESPEEDING;

	private YPPlayer player;
	private float length;
	private List<UnloadedLocation> spots = new ArrayList<>();
	private List<UnloadedLocation> visited = new ArrayList<>();
	private List<Sheep> body = new ArrayList<>();
	private Map<Sheep, Hitbox> map = new HashMap<>();
	private float degree = 0;
	private SManager manager;
	private SSkin skin;
	private int lastClick = 0;
	private Hologram h;
	
	
	private boolean killed = false;
	
	private List<MobDisguise> slimes = new ArrayList<>();
	private boolean disguise = false;
	
	public SPlayer(final YPPlayer player, SManager manager){
		this.player = player;
		this.manager = manager;
		skin = new SSkin(this);
		this.player.freeze();
		h = HologramAPI.createHologram(this.player.getLocation().add(0, 1, 0), this.getHologramText());
		h.spawn();
		h.setAttachedTo(this.player.toPlayer());
//		h.addViewHandler(new ViewHandler() {
//			
//			@Override
//			public String onView(Hologram holo, Player p, String text) {
//				if(p == player.toPlayer()){
//					return "";
//				}
//				return text;
//			}
//		});
	}

	private String getHologramText(){
		return ChatColor.GRAY + this.player.getName() + "   " + ChatColor.RED + "" + Math.round(this.length);
	}
	public void onLeave(){
		manager.removeScoreboard(this);
		this.player.freeze(false);
		h.despawn();
		WorldSlitherio.getInstance().giveItems(this.player);
	}
	public Hitbox getBox(Sheep s){
		return map.get(s);
	}
	public Sheep getHead(){
		if(body.size() == 0){
			return null;
		}
		return body.get(0);
	}
	public void resetVariables(){
		this.length = 5;
		this.degree = this.getPlayerYaw();
	}
	public void quit(){
		for(Sheep s : body){
			s.remove();
		}
		Location tele = this.getHead().getLocation();
		tele.setY(this.manager.getArena().getYDeathSpawn());
		this.player.toPlayer().teleport(tele);
	}
	public boolean hasPart(Sheep s){
		return body.contains(s);
	}
	public void removeSlimes(){
		for(Sheep s : body){
			s.remove();
		}
		body = new ArrayList<>();
	}
	public boolean isBoasting(){
		return (player.toPlayer().isSneaking() || YPTime.getTime() - this.lastClick < 6)&& length >= MINIMUMBOASTINGLENGTH;
	}
	public void setLastClickToNow(){
		this.lastClick = YPTime.getTime();
	}
	
	@Override
	public YPPlayer getYPPlayer() {
		return player;
	}
	@Override
	public void run() {
		Player p = this.player.toPlayer();
		if(this.length < 10){
			length = 10;
		}
		if(YPTime.getTime() % 20 <= 1){
			p.removePotionEffect(PotionEffectType.INVISIBILITY);
			p.removePotionEffect(PotionEffectType.JUMP);
			p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2 * 20, 1, false, false));
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2 * 20, 128, false, false));
		}
		{

			h.setText(this.getHologramText());
		}
		{ // update the degree
			float yaw = this.getPlayerYaw();
			for (int i = 0; (i < TURNAMOUNTPERTICK)
					&& (degree < yaw || degree > yaw + 180) && !(degree + 180 < yaw); i++) {
				degree++;
			}
			for (int i = 0; (i < TURNAMOUNTPERTICK)
					&& (degree > yaw || degree + 180 < yaw) && !(degree > yaw + 180); i++) {
				degree--;
			}
			degree = YPMath.getFinal(degree, 0);
		}
		{
			if(this.isBoasting()){
				length -= LENGTHLOSSWHILESPEEDING;
			}
		}
		
		{ // update the slimes n stuff
			UnloadedLocation current = player.getLocation().clone();
			
			current.setYaw(this.degree - 90);
			current.setPitch(0);
//			while(visited.size() + 5 > length){
//				visited.remove(0);
//			}
			if(this.isBoasting() && visited.size() > 5){
				UnloadedLocation add = current.getMidpoint(visited.get(visited.size() - 1));
				add.setY(manager.getArena().getYSpawnIn());
				visited.add(add); // get the second to last index added in (Probably a previous current)
																				// this is behind current // that's why we add it first
			}
			visited.add(current);
			spots.clear();
//			while (length >= visited.size()) {
//				visited.add(current.clone());
//			}

			for (int i = 0; i < visited.size(); i++) {
				if (i > length) {
					break;
				}
				int j = visited.size() - (i + 1);
				UnloadedLocation left = visited.get(j); // gets the last value
														// on visited
				if (i <= length) {
					spots.add(left);
				}
			}
			this.updateSheep();
		
		}
		{ // check if other people should die
			for(SPlayer sp : manager.getPlayers()){
				if(sp == this || sp.isKilled()){
					continue;
				}
				Sheep s = sp.getHead();
				if(s == null || this.getHead() == null || this.getBox(this.getHead()) == null){
					continue;
				}
				Hitbox box = sp.getBox(s);
				if(box.intersects(this.getBox(s))){
					if(sp.length > this.length){
						sp.kill();
					} else if(this.length > sp.length){
						this.kill();
					}
				}
				boolean intersects = false;
				String message = "";
				for(Sheep body : body){
					Hitbox part = this.getBox(body);
					if(box.intersects(part)){
						intersects = true;
						message = ChatColor.YELLOW + "You were killed by " + ChatColor.RED + this.getYPPlayer().toPlayer().getName().toUpperCase();
						break;
					}
					
				}
				if(intersects){
					this.player.sendMessage(ChatColor.YELLOW + "You killed " + sp.getYPPlayer().getName() + "!");
					int money = Math.round(sp.length / 100);
					if(money != 0){
						this.player.addCoins(money);
						this.player.sendMessage(ChatColor.GREEN + "You got " + money + " coin(s)");
					}
					sp.kill();
					sp.getYPPlayer().toPlayer().sendMessage(message);
				}
			}
		}
//		String send = "slimes.size: "  + slimes.size() + " degree: " + degree + "\nvisited.size: " + visited.size() + " spots.size: " + spots.size() + "\nlength: " + length + "\n ";
//		p.sendMessage(send);
		p.setVelocity(this.getVelocity());
		if(this.disguise){
			this.updateDisguises();
		}
	}
	
	public void setSkin(SSkin skin){
		this.skin = skin;
	}
	public SSkin getSkin(){
		return this.skin;
	}
	
	public float getLength() {
		return this.length;
	}
	public boolean isKilled(){
		return killed;
	}
	public void addLength(float amount){
		this.length += amount;
	}
	 
	private boolean shouldFire(int i, boolean boasting, boolean[] shouldFire){
		if(!boasting){
			return false;
		}
		
		
		int c = i - (int) Math.round(YPTime.getTime() / 2);
		
		while (c < 0){
			c+=shouldFire.length;
		}
		while(c >= shouldFire.length){
			c-=shouldFire.length;
		}
		return shouldFire[c];
	}
	private void updateSheep(){
		boolean boasting = this.isBoasting();
		int spawned = 0;
		final boolean[] fire1 = {true, false, false, false, false};
		final boolean[] fire2 = {true, false, false, false, false, false, false, false, false, false, false};
		for(int i = 0; i < spots.size(); i++){
			if(i > length){
				continue;
			}
			UnloadedLocation loc = spots.get(i).clone(); // the front of spots is the front of the snake.
			loc.setY(this.manager.getArena().getYSpawnIn());
			Sheep s = null;
			boolean fire = this.shouldFire(i, boasting, fire1);
			if(this.shouldFire(i, boasting,  fire2)){
				loc.add(0, 0.49, 0);
			}
			if(i < body.size()){
				s = body.get(i);
				s.teleport(loc);
			} else if(spawned <= 1){
				s = (Sheep) Bukkit.getWorld(player.getLocation().getWorldName()).spawnEntity(loc, EntityType.SHEEP); // this.skin.getType(i)
				s.setBaby();
				s.setAgeLock(true);
				map.put(s, new Hitbox(s));
				body.add(s);
				spawned++;
			}
			if (s != null) {
				if (fire) {
					s.setFireTicks(2);
				} else {
					s.setFireTicks(0);
				}
				s.setColor(this.skin.getColor(i));
				
			}
		}
		Vector v = new Vector(0, 0, 0);
		
		for(int i = 0; i < body.size(); i++){
			Sheep s = body.get(i);
			
			if(i > length){
				manager.getAManager().spawnItem(s.getLocation(), 1F, false);
				s.remove();
				body.remove(s);
			} else {
				UnloadedLocation loc = UnloadedLocation.fromLocation(s.getLocation());
				Block current = loc.getBlock();
				for(Block b : loc.getNearbyBlocks(1)){
					if(b.getY() != current.getY()){
						continue;
					}
					if(b.getType() != Material.AIR){
						this.kill();
						this.player.toPlayer().sendMessage(ChatColor.YELLOW + "You were killed by " + ChatColor.RED + "THE WALL");
						break;
					}
				}
			}
			s.setVelocity(v);
		}
	}
	public void kill(){
		Location tele = this.getYPPlayer().getLocation();
		tele.setY(manager.getArena().getYDeathSpawn());
		this.player.toPlayer().teleport(tele);
		for(Sheep s : body){
			manager.getAManager().spawnItem(s.getLocation(), 1, false);
			s.remove();
		}
		body = new ArrayList<>();
		this.killed = true;
		
	}
	public float getPlayerYaw(){
		return YPMath.getFinal(this.getYPPlayer().toPlayer().getLocation().getYaw(), 90);
		
	}
	public Vector getVelocity(){
		float yaw = degree;
		double radians = Math.toRadians(yaw);

		double radius = 0.25;
		if(this.isBoasting()){
			radius = 0.5;
		}
		double x = Math.cos(radians) * radius;
		double y = -1;
		double z = Math.sin(radians) * radius;
		Vector vector = new Vector(x,y,z);
		return vector;
		
	}
	public void setSlimes(boolean set){
		this.disguise = set;
		if(set){
			for(Entity e : this.player.toPlayer().getWorld().getEntities()){
				
				if(e.getType() == EntityType.SHEEP){
					Sheep s = (Sheep) e;
					DisguiseType type = null;
					switch(s.getColor()){
					case BLACK: case RED:
						type = DisguiseType.MAGMA_CUBE;
						break;
					default:
						type = DisguiseType.SLIME;
						break;
					}
					if(type == null){
						type = DisguiseType.SLIME;
					}
					
					MobDisguise d = new MobDisguise(type);
					SlimeWatcher w = new SlimeWatcher(d);
					d.setWatcher(w);
					d.addPlayer(this.player.toPlayer());
					d.setEntity(s);
					slimes.add(d);
					this.updateSlime(d);
					
				}
				
			}
			
			
			
			
		} else{
			
			for(MobDisguise d : slimes){
				d.removeDisguise();
				d.removePlayer(this.player.toPlayer());
			}
			
			
			
			
		}
	}
	
	public void updateDisguises(){
		for(MobDisguise d : slimes){
			this.updateSlime(d);
		}
	}
	
	private void updateSlime(MobDisguise d){
		SlimeWatcher w = (SlimeWatcher) d.getWatcher();
		if(w == null){
			System.out.println("A SlimeWatcher is null SPlayer.java");
		}
		w.setSize(1);
		w.setBurning(((Sheep) d.getEntity()).getFireTicks() != 0);
	}

	public boolean isSlimesSet() {
		return this.disguise;
	}
	

}
