package com.yoshiplex.teleportation.eventlisteners.ping;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.inventivetalent.packetlistener.PacketListenerAPI;
import org.inventivetalent.packetlistener.handler.PacketHandler;
import org.inventivetalent.packetlistener.handler.ReceivedPacket;
import org.inventivetalent.packetlistener.handler.SentPacket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.mojang.authlib.GameProfile;
import com.yoshiplex.Main;
import com.yoshiplex.YPTime;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.util.ReflectedObject;
import com.yoshiplex.reload.ReloadManager;
import com.yoshiplex.teleportation.WorldManager;
import com.yoshiplex.teleportation.worlds.WorldHub;
import com.yoshiplex.util.YPLibrary;

import net.minecraft.server.v1_9_R2.ChatModifier;
import net.minecraft.server.v1_9_R2.ChatTypeAdapterFactory;
import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.ServerPing;
import net.minecraft.server.v1_9_R2.ServerPing.ServerData;
import net.minecraft.server.v1_9_R2.ServerPing.ServerPingPlayerSample;
public class PingListener extends PacketHandler {
	private static GsonBuilder build = new GsonBuilder();
	private static final Gson a;
	
	static{
		build.registerTypeAdapter(ServerPing.ServerData.class, new OverridenServerDataSerializer())
				.registerTypeAdapter(ServerPing.ServerPingPlayerSample.class, new OverridenServerSampleSerializer())
				.registerTypeAdapter(ServerPing.class, new OverridenServerPingSerializer())
				.registerTypeHierarchyAdapter(IChatBaseComponent.class, new IChatBaseComponent.ChatSerializer())
				.registerTypeHierarchyAdapter(ChatModifier.class, new ChatModifier.ChatModifierSerializer())
				.registerTypeAdapterFactory(new ChatTypeAdapterFactory());
		
		
		a = build.create();
		
	}
	private static final UUID blankid = new UUID(0,0);
	private static final GameProfile newline = new GameProfile(blankid, "\n");
	private static final GameProfile playersline = new GameProfile(blankid, ChatColor.YELLOW + ChatColor.BOLD.toString() + "players:");
	private static int pingCount = 0;
	
	
	
	private final static boolean debug = false;
	private static List<String> accessed = new ArrayList<>();
	private static int used = 0;
	private static boolean generate_motd = true;
	private static String motd = "";
	
	private int lastPinged = 0;
	
	
	@SuppressWarnings("deprecation")
	public PingListener(final Main main) {
		PacketListenerAPI.addPacketHandler(this);
		pingCount = main.getConfig().getInt("pingcount");
		Runnable run = new Runnable() {
			
			@Override
			public void run() {
				generate_motd = main.getConfig().getBoolean("generate-motd");
				motd = main.getConfig().getString("motd");
			}
		};
		run.run();
		ReloadManager.registerReload(run);
		
	}

	@Override
	public void onSend(SentPacket packet) {
		
		if (debug) {
			System.out.println("A packet was sent! Name: " + packet.getPacketName());
		}
		if (packet.getPacketName().equals("PacketStatusOutServerInfo")) {
			//ReflectedObject pack = new ReflectedObject(packet.getPacket());
			Gson aa = (Gson) ReflectedObject.getFromStaticField(packet.getPacket().getClass(), "a");
			if(aa != a){
				ReflectedObject.setStaticField(packet.getPacket().getClass(), "a", a);
			}
			if(debug){
				System.out.println("field is equal: " + (a == aa));
			}
//			ReflectedObject ping = new ReflectedObject(packet.getPacketValue("b"));
//			ping.get("c").setField("b", 10);


		}
	}

	@Override
	public void onReceive(ReceivedPacket packet) {
		if (debug) {
			System.out.println("A packet was received! Name: " + packet.getPacketName());
		}
		if (packet.getPacketName().equals("PacketHandshakingInSetProtocol")) {
			
			ReflectedObject pack = new ReflectedObject(packet.getPacket());
			String hostname = (String) pack.get("hostname").toObject();
			if (debug) {
				System.out.println("After the receiving of a PacketHandshakingInSetProtocol, String hostname is " + hostname);
			}
			if (lastPinged + 3 < YPTime.getTime()) {
				accessed = new ArrayList<>();
				used = 0;
				pingCount++;
			}
			lastPinged = YPTime.getTime();
//			if(hostname.contains("data")){
//				List<String> chat = Main.getChatLog().getConfig().getStringList("log");
//				accessed.add(chat.get(chat.size() - 1));
//			} else {
			accessed.add(hostname);
			
			
		}

	}
	

	public static class OverridenServerPingSerializer extends ServerPing.Serializer{
		
		@Override
		public ServerPing a(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
				throws JsonParseException {
			return super.a(jsonElement, type, jsonDeserializationContext);
		}
		@Override
		public JsonElement a(ServerPing serverPing, Type type, JsonSerializationContext jsonSerializationContext) {
			int size = Bukkit.getOnlinePlayers().size();
			
			ReflectedObject ping = new ReflectedObject(serverPing);
			String hostname = null;

			if (used < accessed.size()) {
				hostname = accessed.get(used);
			} else {
				hostname = WorldHub.getInstance().getMOTD();
			}

			used++;
			if (debug) {
				System.out.println("The spot is " + used);
			}
			{
				ReflectedObject playerSample = ping.get("b");
				if (playerSample.toObject() == null) {
					System.out.println("Player sample object is null");
				}
				String open = ChatColor.GREEN + "open";
				if (Bukkit.hasWhitelist()) {
					open = ChatColor.RED + "closed";
				}
			
				List<GameProfile> profiles = new LinkedList<GameProfile>();
				profiles.add(new GameProfile(blankid, ChatColor.YELLOW + "Welcome! The server is " + open + "."));
				profiles.add(newline);
				String prefix = "There are ";
				String plural = "s";
				if (size == 1) {
					plural = "";
					prefix = "There is ";
				}
				profiles.add(new GameProfile(blankid, ChatColor.YELLOW + prefix + size + " player" + plural + " online!"));
			
				if (size > 0) {
					profiles.add(newline);
					profiles.add(playersline);
					int i = 0;
					for (Player p : Bukkit.getOnlinePlayers()) {
						if(i >= 10){
							profiles.add(new GameProfile(blankid, ChatColor.GREEN + "And " + (size - 10) + " more"));
							break;
						}
						YPPlayer yp = YPPlayer.getYPPlayer(p);
						profiles.add(new GameProfile(p.getUniqueId(), p.getDisplayName() + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + " - " + yp.getYPWorld().getDisplayName()));
						i++;
					}
				}
				profiles.add(newline);
				profiles.add(new GameProfile(blankid, ChatColor.GREEN + "Join yoshiplex.com to get news and"));
				profiles.add(new GameProfile(blankid, ChatColor.GREEN + "  updates for the server!"));
				profiles.add(newline);
				profiles.add(new GameProfile(blankid, ChatColor.LIGHT_PURPLE + "Ping count: " + ChatColor.RED + ChatColor.BOLD + pingCount));
				GameProfile[] files = new GameProfile[profiles.size()];
				for(int i = 0; i < profiles.size(); i ++){
					files[i] = profiles.get(i);
				}
				playerSample.setField("c", files);
			}
			if (hostname == null) {
				hostname = WorldHub.getInstance().getMOTD();
			}
			if(generate_motd){
				ping.getMethod("setMOTD").invoke(YPLibrary.getMessage(WorldManager.getSpawnForAddress(hostname).getMOTD()));
			} else {
				ping.getMethod("setMOTD").invoke(YPLibrary.getMessage(motd));
			}
			
			ReflectedObject serverData = ping.get("c");
			String isopen;
			if(Bukkit.hasWhitelist()){
				isopen = ChatColor.GREEN + "{private} ";
			} else{
				isopen = ChatColor.RED  + "1.9/1.10 ";
			}
			
			String message = isopen + ChatColor.GREEN.toString() + ChatColor.GOLD + size + "/" + ChatColor.GREEN + "50";
			if(Bukkit.hasWhitelist()){
				message = ChatColor.stripColor(message);
			}
			
			serverData.setField("a", message);
			serverData.setField("b", 1); // this is the version 
			if (debug) {
				System.out.println("We have gotten all the data! Line 49 PingListener.");
			}

			return super.a(serverPing, type, jsonSerializationContext);
			
		}

	}
	public static class OverridenServerDataSerializer extends ServerPing.ServerData.Serializer{
		
		@Override
        public JsonElement a(ServerData serverData, Type type, JsonSerializationContext jsonSerializationContext) {
            return super.a(serverData, type, jsonSerializationContext);
        }
		
	}
	public static class OverridenServerSampleSerializer extends ServerPing.ServerPingPlayerSample.Serializer {
		@Override
		public JsonElement a(ServerPingPlayerSample serverPingPlayerSample, Type type,
				JsonSerializationContext jsonSerializationContext) {
			
			JsonObject jsonObject = (JsonObject) super.a(serverPingPlayerSample, type, jsonSerializationContext);

			return jsonObject;
			
		}
	}
	public static int getPingCount(){
		return pingCount;
	}
	

}
