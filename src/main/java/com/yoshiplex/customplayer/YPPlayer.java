package com.yoshiplex.customplayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.yoshiplex.Main;
import com.yoshiplex.ResourcePack;
import com.yoshiplex.YPTime;
import com.yoshiplex.customplayer.entity.YPEntity;
import com.yoshiplex.eventlisteners.PlayerListener;
import com.yoshiplex.parties.Party;
import com.yoshiplex.teleportation.WorldManager;
import com.yoshiplex.teleportation.eventlisteners.PlayerAddress;
import com.yoshiplex.teleportation.worlds.WorldDefault;
import com.yoshiplex.teleportation.worlds.YPWorld;

import net.minecraft.server.v1_9_R2.EntityPlayer;
import net.minecraft.server.v1_9_R2.Packet;
import net.minecraft.server.v1_9_R2.PacketPlayOutResourcePackSend;

public class YPPlayer extends YPEntity<Player> implements CommandSender{
	private static Map<Player, YPPlayer> allPlayers = new HashMap<>();
	
	
	
	private Player player = null;
	private ResourcePack pack = ResourcePack.BLANK;
	private final int tickJoined;
	private YPProfile profile;
	private Main instance;
	private BossBar bar = null;
	private BossBar secondBar = null;

	private int lastMove = 0;
	private int lastClick = 0;
	
	private boolean isFrozen = false;
	
	private Party party = null;
	
	public YPPlayer (Player player){
		super(player);
		//super((CraftServer) ((CraftPlayer) player).getServer(), ((CraftPlayer) player).getHandle());
		this.player = player;
		allPlayers.put(player, this);
		tickJoined = YPTime.getTime();
		profile = YPProfile.getProfile(player.getUniqueId());
		instance = Main.getInstance();
	}
	public Player toPlayer(){
		
		return player;
	}
	public CraftPlayer toCraftPlayer(){
		return (CraftPlayer) player;
	}
	public EntityPlayer toEntityPlayer(){
		return this.toCraftPlayer().getHandle();
	}
	@Override
	public void freeze(boolean b) {
		this.isFrozen = b;
		if(b){
			this.player.setWalkSpeed(0);
		} else {
			this.player.setWalkSpeed(0.2f);
		}
		
	}

	public void setJump(boolean allow) {
		if (allow) {
			player.removePotionEffect(PotionEffectType.JUMP);
		} else {
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128, false, false));

		}
	}
	
	public void setClientWalkSpeed(float speed){
		float s = player.getWalkSpeed();
        EntityPlayer p = ((CraftPlayer)player).getHandle();
        p.abilities.walkSpeed = speed / 2f;
        p.updateAbilities();
        p.abilities.walkSpeed = s / 2F;
	}
	public boolean isStill(){
		return this.lastMove + 2 < YPTime.getTime();
	}
	public void setMovedNow(){ // fired in playerlistener
		this.lastMove = YPTime.getTime();
	}
	
	public void setLastClickNow(){
		this.lastClick = YPTime.getTime();
	}
	public int getLastClick() {
		return lastClick;
	}
	
	public boolean isFrozen(){
		return isFrozen;
	}
	
	public ResourcePack getPack(){
		return pack;
	}
	public int getTickJoined(){
		return tickJoined;
	}
	public void setMainBar(BossBar bar){
		if(bar == null){
			if(this.bar != null){
				this.bar.removePlayer(this.player);
				this.bar = null;
			}
			return;
		}
		if(this.bar != null){
			this.bar.removePlayer(this.player);
		}
		bar.addPlayer(this.player);
		this.bar = bar;
	}
	public void setSecondBar(BossBar bar){
		if(bar == null){
			if(this.secondBar != null){
				this.secondBar.removePlayer(this.player);
				this.secondBar = null;
			}
			return;
		}
		if(this.secondBar != null){
			this.secondBar.removePlayer(this.player);
		}
		bar.addPlayer(this.player);
		this.secondBar = bar;
	}

	public void setPack(ResourcePack pack, boolean force) {
		if(pack == null || pack == ResourcePack.NOAPPLY || pack.toString() == null){
			return;
		}
		if(this.player.isSneaking() && !force){
			return;
		}
		if(this.pack != pack || force){
			this.sendResourcePack(pack.getLink());
			this.pack = pack;
		}
	}
	public void setParty(Party p) {
		this.party = p;
	}
	public Party getParty(){
		return this.party;
	}
	protected void sendResourcePack(String link){
		PacketPlayOutResourcePackSend packet = new PacketPlayOutResourcePackSend(link, "time" + YPTime.getTime());
		this.sendPacket(packet);
	}
	public ConfigurationSection getConfigSection(){
		return instance.getConfig().getConfigurationSection(this.getConfigPath());
	}

	public String getConfigPath(){
		return profile.getConfigPath();
	}
	public YPWorld getYPWorld(){
		YPWorld world = WorldManager.getWorld(this.toPlayer().getWorld().getName());
		
		if(world == null){
			world = WorldDefault.get(this.toPlayer().getWorld().getName());
		}
		
		return world;
	}
	public void sendPacket(Packet<?> packet){
		if(!(this.player instanceof CraftPlayer)){
			System.out.println("YPERROR: A player's instance is not a CraftPlayer! Name: " + player.getName());
			return;
		}
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
	public boolean isOnGround(){
		return getLocation().subtract(0, 0.1, 0).getBlock().getType() != Material.AIR;
	}
	public String getDomainAddress(){
		return PlayerAddress.getAddress(this.toPlayer().getUniqueId());
	}
	public YPWorld getSpawnedIn(){
		return WorldManager.getSpawnForAddress(getDomainAddress());
	}
	public YPProfile getYPProfile(){
		return profile;
	}
	public void addCoins(double add){
		instance.getEcon().depositPlayer(this.player, add);
	}
	public void addCoins(double add, boolean boast){
		if(!boast){
			PlayerListener.waitThenChange(this.toPlayer(), 3);
		}
		this.addCoins(add);
	}
	public void removeCoins(double remove){
		instance.getEcon().withdrawPlayer(this.player, remove);
	}
	public boolean hasEnoughCoins(double price){
		return this.getCoinBalance() >= price;
	}
	public double getCoinBalance(){
		return instance.getEcon().getBalance(this.player);
	}
	public void addOneups(int add){
		this.getConfigSection().set("oneups", this.getOneupBalance() + add);
	}
	public boolean hasEnoughOneups(int price){
		return this.getOneupBalance() >= price;
	}
	public int getOneupBalance(){
		return this.getConfigSection().getInt("oneups");
	}
	
	public static YPPlayer getYPPlayer(Player p){
		if(p == null){
			return null;
		}
		YPPlayer r = null;
		if(allPlayers.get(p) == null){
			r = new YPPlayer(p);
		} else {
			r = allPlayers.get(p);
		}
		if(r == null || r.toPlayer() == null){
			r = new YPPlayer(p);
		}
		
		return r;
	}
	public static YPPlayer getYPPlayer(String name){
		return getYPPlayer(Bukkit.getPlayer(name));
	}
	@Override
	public PermissionAttachment addAttachment(Plugin arg0) {
		return this.player.addAttachment(arg0);
	}
	@Override
	public PermissionAttachment addAttachment(Plugin arg0, int arg1) {
		return this.player.addAttachment(arg0, arg1);
	}
	@Override
	public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2) {
		return this.player.addAttachment(arg0, arg1, arg2);
	}
	@Override
	public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2, int arg3) {
		return this.player.addAttachment(arg0, arg1, arg2, arg3);
	}
	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return this.player.getEffectivePermissions();
	}
	@Override
	public boolean hasPermission(String arg0) {
		return this.player.hasPermission(arg0);
	}
	@Override
	public boolean hasPermission(Permission arg0) {
		return this.player.hasPermission(arg0);
	}
	@Override
	public boolean isPermissionSet(String arg0) {
		return this.player.isPermissionSet(arg0);
	}
	@Override
	public boolean isPermissionSet(Permission arg0) {
		return this.player.isPermissionSet(arg0);
	}
	@Override
	public void recalculatePermissions() {
		this.player.recalculatePermissions();
	}
	@Override
	public void removeAttachment(PermissionAttachment arg0) {
		this.player.removeAttachment(arg0);
	}
	@Override
	public boolean isOp() {
		return this.player.isOp();
	}
	@Override
	public void setOp(boolean arg0) {
		this.player.setOp(arg0);
	}
	@Override
	public String getName() {
		return this.player.getName();
	}
	@Override
	public Server getServer() {
		return this.player.getServer();
	}
	@Override
	public void sendMessage(String[] arg0) {
		this.player.sendMessage(arg0);
	}
	@Override
	public void sendMessage(String arg0) {
		this.player.sendMessage(arg0);
	}
	public void removeEffects() {
		for(PotionEffect e : this.player.getActivePotionEffects()){
			this.player.removePotionEffect(e.getType());
		}
	}
}
