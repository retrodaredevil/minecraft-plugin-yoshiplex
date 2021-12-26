package com.yoshiplex.teleportation.worlds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import com.yoshiplex.Main;
import com.yoshiplex.ResourcePack;
import com.yoshiplex.YPTime;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.rainbow.RainbowText;
import com.yoshiplex.util.UnloadedLocation;

public abstract class YPWorld implements Runnable{
	//private final List<Color> colors = Arrays.asList(Color.RED, Color.ORANGE, Color.YELLOW, Color.LIME, Color.OLIVE, Color.GREEN, Color.AQUA, Color.BLUE, Color.NAVY, Color.PURPLE, Color.FUCHSIA);
	private static Map<Player, Integer> lastWorldChange = new HashMap<>();
	private static final List<BarColor> colors = Arrays.asList(BarColor.RED, BarColor.YELLOW, BarColor.GREEN, BarColor.BLUE, BarColor.PURPLE, BarColor.PINK);
	private static final List<BarStyle> styles = 
			Arrays.asList(BarStyle.SEGMENTED_6, BarStyle.SEGMENTED_6, BarStyle.SEGMENTED_6, BarStyle.SEGMENTED_6, BarStyle.SEGMENTED_6, BarStyle.SEGMENTED_10, BarStyle.SEGMENTED_10, BarStyle.SEGMENTED_10, BarStyle.SEGMENTED_12, BarStyle.SEGMENTED_12, BarStyle.SEGMENTED_12, BarStyle.SEGMENTED_12, BarStyle.SEGMENTED_20, BarStyle.SEGMENTED_20, BarStyle.SEGMENTED_20, BarStyle.SEGMENTED_20, BarStyle.SEGMENTED_20, BarStyle.SEGMENTED_20, BarStyle.SOLID, BarStyle.SOLID, BarStyle.SOLID, BarStyle.SOLID, BarStyle.SOLID, BarStyle.SOLID,BarStyle.SOLID);
	
	private static final int TICKSUNTILCOLORCHANGE = 5;
	protected final String DEFAULTBOSSBARMESSAGE = "PLAY.YOSHIPLEX.COM";
	
	protected String name;
	protected UnloadedLocation spawn;
	protected ResourcePack pack;
	protected String displayName = "unknown";
	protected RainbowText rText = new RainbowText(DEFAULTBOSSBARMESSAGE, ChatColor.BOLD.toString());
	protected String bossBarMessage = DEFAULTBOSSBARMESSAGE;
	private int lastColorChange = 0;
	private BarColor color = BarColor.RED;
	private int spot = 0;
	private BossBar bar = Bukkit.createBossBar(this.bossBarMessage, color, BarStyle.SOLID);
	private boolean movedRainbow = false;
	public boolean isIn(YPPlayer p){
		return inWorld().contains(p);
	}
	public World getWorld(){
		
		return Bukkit.getWorld(name);
	}
	public UnloadedLocation getSpawn(){
		return spawn;
	}
	public UnloadedLocation getSpawn(String command){
		return spawn;
	}
	
	public String getName(){
		return name;
	}
	public ResourcePack getPack(){
		return pack;
	}
	public void onAboutToTeleport(YPPlayer player){
		lastWorldChange.put(player.toPlayer(), YPTime.getTime());
	}
	public String getDisplayName(){
		return displayName;
	}
	@Override
	public void run() {
		int place = rText.getPlace();
		rText = new RainbowText(this.bossBarMessage, ChatColor.BOLD.toString());
		rText.setPlace(place);
		if (!movedRainbow) {
			if (this.moveRainbowRight()) {
				rText.moveRainbowRight();
			} else {
				rText.moveRainbow();
			}
			movedRainbow = true;
		} else {
			movedRainbow = false;
		}
		if(lastColorChange == 0 || lastColorChange + TICKSUNTILCOLORCHANGE <= YPTime.getTime()){
			spot++;
			if(spot >= colors.size()){
				spot = 0;
			}
			color = colors.get(spot);
			lastColorChange = YPTime.getTime();
		}
		bar.setTitle(rText.getText());
		bar.setColor(this.color);
		
		int s = YPTime.getTime();
		while(s >= styles.size()){
			s-= styles.size();
		}
		
		bar.setStyle(styles.get(s));
		
	}
	protected boolean moveRainbowRight(){
		return true;
	}
	
	
	public List<YPPlayer> inWorld(){
		List<YPPlayer> r = new ArrayList<>();
		for(Player player : this.getWorld().getPlayers()){
			r.add(YPPlayer.getYPPlayer(player));
		}
		return r;
	}
	public void giveItems(YPPlayer player){
		Player p = player.toPlayer();
		p.getInventory().clear();
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bm = (BookMeta) book.getItemMeta();
		bm.setAuthor(ChatColor.GREEN + "retrodaredevil");
		bm.setTitle(ChatColor.GRAY + "Getting Started");
		bm.addPage("This book will help you get started. \nTo get to the hub you can do " + ChatColor.GREEN + "/hub" + ChatColor.RESET + "\nMore essential commands follow:\n" + ChatColor.GREEN + "/leave\n/game\n/msg\n" + ChatColor.RESET + "More info follows.", "On this server we have global and local chat. Any chat you type will go to your world only unless you do " + ChatColor.GREEN + "/g <message>\n" + ChatColor.RESET + "Global chat is formatted with the server of the sender.");
		book.setItemMeta(bm);
		
		p.getInventory().addItem(book);
		ItemStack compass = new ItemStack(Material.COMPASS);
		compass.getItemMeta().setDisplayName(ChatColor.GREEN + "Compass\n" + ChatColor.GRAY + "" + ChatColor.BOLD + "||" + ChatColor.RESET + "" + ChatColor.AQUA + "TELEPORT" + "" +  ChatColor.GRAY + "" + ChatColor.BOLD + "||");
		p.getInventory().addItem(new ItemStack(compass));
		p.setCompassTarget(this.spawn);
	}

	public void tpFrom(@Nullable YPWorld last, final YPPlayer player){ // may not be needed
		this.resetStats(player, false);
		
	}
	public void tp(YPPlayer p, @Nullable String command){
		if(command == null){
			p.toPlayer().teleport(spawn);
			
			return;
		}
		p.toPlayer().teleport(this.getSpawn(command));
	}
	public String getNotAllowedLeaveReason(){
		return ChatColor.RED + "You can't leave this world right now. Try doing /leave";
	}
	public String getNotAllowedComeReason(){
		return ChatColor.RED + "You can't come to this world right now.";
	}
	public void resetStats(final YPPlayer p, final boolean force){
		Player player = p.toPlayer();
		for(PotionEffect effect : player.getActivePotionEffects()){
			player.removePotionEffect(effect.getType());
		}
		new BukkitRunnable() {
			
			@Override
			public void run() {
				p.setPack(pack, force);
			}
		}.runTaskLater(Main.getInstance(), 3 * 20);
	}
	public String getMOTD(){
		return getFirstLine() + "\n" + ChatColor.GREEN + "Sorry, the server has it's whitelist on right now.";
	}
	
	
	
	public boolean canLeave(YPPlayer player){
		return player.getParty() == null || player.getParty().getLeader() == player;
	}
	public void leaveTo(YPWorld next, YPPlayer player){ // should fire next's tpFrom
		next.tpFrom(this, player);
	}
	public abstract boolean canComeTo(YPPlayer player);
	public abstract boolean useCompassToOpenMenu();
	public abstract boolean shouldCome(String command);
	public abstract boolean shouldTeleportOnLogin(String address);
	
	public abstract String getComeCommand();
	
	public static int lastTeleported(YPPlayer p){
		if(lastWorldChange.get(p.toPlayer()) == null){
			lastWorldChange.put(p.toPlayer(), 0);
		}
		return lastWorldChange.get(p.toPlayer());
	}
	
	
	public static String getFirstLine(){
		String line = new RainbowText("-----", ChatColor.STRIKETHROUGH.toString()).getText();
		return ("\t" + line + ChatColor.RESET + ChatColor.GREEN + ChatColor.BOLD + "YoshiPlex" + ChatColor.RESET + line + ChatColor.RESET + ChatColor.DARK_GREEN + "\t\t\t\tyoshiplex.com").replace("\t", "    ");
	}
	public BossBar getBossBar() {
		return bar;
	}

}
