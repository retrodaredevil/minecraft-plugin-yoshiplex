package com.yoshiplex.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.util.Vector;

import com.connorlinfoot.titleapi.TitleAPI;
import com.yoshiplex.Main;
import com.yoshiplex.YPTime;
import com.yoshiplex.customplayer.YPPlayer;

import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_9_R2.PacketPlayOutCustomSoundEffect;
import net.minecraft.server.v1_9_R2.SoundCategory;

public class YPLibrary {
	
	private static YPListener listener = null;
	
	public static void playSound(Player p, String sound){
		playSound(p, sound, 1, 1);
	}
	public static void playSound(Player p, String sound, float volume, float pitch){
		Location l = p.getLocation();
		PacketPlayOutCustomSoundEffect packet = new PacketPlayOutCustomSoundEffect(sound, SoundCategory.VOICE, l.getBlockX(), 
				l.getBlockY(), l.getBlockZ(), volume, pitch);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		
	}
	public static void playSound(YPPlayer p, String sound){
		playSound(p.toPlayer(), sound);
	}
	public static void playSound(List<Player> list, String sound, float volume){
		for(Player p : list){
			playSound(p, sound, volume, 1);
		}
		
	}
	public static void playSound(List<Player> list, String sound){
		for(Player p : list){
			playSound(p, sound);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void sendTitle(Player p, String title, String subtitle, int stay){
		TitleAPI.sendFullTitle(p, 0, stay, 0, title, subtitle);
	}
	public static String getMinutesSecondsFormat(int ticks){
		int seconds = (int) (ticks - YPTime.getTime()) / 20;
		int minutes = (int) seconds / 60;
		seconds %= 60;
		String secondsMessage = seconds + "";
		if(secondsMessage.length() <= 1){
			secondsMessage = "0" + seconds;
		}
		return minutes + ":" + secondsMessage;
	}

	public static IChatBaseComponent getMessage(String message) {
		return ChatSerializer.a("{\"text\": \"" + message + "\"}");
	}
	public static boolean containsAny(String s, List<String> any){
		for(String a : any){
			if(s.contains(a)){
				return true;
			}
		}
		return false;
	}
	private static final Vector zero = new Vector(0, 0, 0);
	public static boolean isVectorAboutZero(Vector v){
		return isLessThan(v, 0.1);
	}
	public static boolean isLessThan(Vector v, double than){
		return v.distanceSquared(zero) < Math.pow(than, 2);
/*		double than2 = than * -1;
		
		double x = v.getX();
		double y = v.getY();
		double z = v.getZ();
		if(x == 0 && y == 0 && z == 0){
			return true;
		}
		boolean xx = false;
		boolean yy = false;
		boolean zz = false;
		if(x > 0){
			xx = x < than;
		} else {
			xx = x > than2;
		}
		

		if(y > 0){
			yy = y < than;
		} else {
			yy = y > than2;
		}
		

		if(z > 0){
			zz = z < than;
		} else {
			zz = z > than2;
		}
		
		return xx && yy && zz;*/
	}
	public static void fireCommand(CommandExecutor com, PlayerCommandPreprocessEvent e){
		listener.get(e).execute(com);
	}
//	public static String fromCom(Object com){
//		Class<?> cs = ReflectedObject.getClassFromString(YPServer.getPackageName(), "ChatSerializer");
//		return ChatSerializer.a(com);
//	}
	public static void init(Main main) throws CalledTwiceException{
		if(listener != null){
			throw new CalledTwiceException("Should only be called once by main.");
		}
		listener = new YPListener(main);
	}
	private static class YPListener implements Listener{
		
		private Map<PlayerCommandPreprocessEvent, CommandHolder> map = new HashMap<>();
		
		public YPListener(Main main){
			Bukkit.getPluginManager().registerEvents(this, main);
			
		}
		
		@EventHandler(priority = EventPriority.LOWEST)
		public void onCmd(PlayerCommandPreprocessEvent e){
			map.put(e, new CommandHolder(e));
		}
		public CommandHolder get(PlayerCommandPreprocessEvent e){
			return map.get(e);
		}
		
	}
	private static class CommandHolder{
		
		
		private final CommandSender sender;
		private Command cmd = null;
		private final String label;
		private final String[] args;
		
		private CommandHolder(PlayerCommandPreprocessEvent e){
			String[] split = e.getMessage().split(" ");
			
			args = new String[split.length - 1];
			
			for(int i = 0; i < split.length; i++){
				if(i == 0){
					continue;
				}
				args[i - 1] = split[i];
			}
			label = split[0].replaceFirst("/", "");
			sender = e.getPlayer();
			SimpleCommandMap map = (SimpleCommandMap) (new ReflectedObject(Bukkit.getServer()).getMethod("getCommandMap")).invoke().toObject();
			outerloop:
			for(Command c : map.getCommands()){
				if(c.getName().equalsIgnoreCase(label)){
					cmd = c;
					break;
				}
				for(String a : c.getAliases()){
					if(a.equalsIgnoreCase(label)){
						cmd = c;
						break outerloop;
					}
				}
			}
			if(cmd == null){
				cmd = new Command(label, "blank description", "/" + label, new ArrayList<String>()) {
					
					@Override
					public boolean execute(CommandSender arg0, String arg1, String[] arg2) {
						return false;
					}
				};
			}
		}

		public void execute(CommandExecutor c) {
			c.onCommand(sender, cmd, label, args);
		}
		
	}
}
