package com.yoshiplex.customevents.messagecancel;

import org.bukkit.Bukkit;
import org.inventivetalent.packetlistener.PacketListenerAPI;
import org.inventivetalent.packetlistener.handler.PacketHandler;
import org.inventivetalent.packetlistener.handler.ReceivedPacket;
import org.inventivetalent.packetlistener.handler.SentPacket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.server.v1_9_R2.ChatModifier;
import net.minecraft.server.v1_9_R2.ChatModifier.ChatModifierSerializer;
import net.minecraft.server.v1_9_R2.ChatTypeAdapterFactory;
import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_9_R2.PacketPlayOutChat;

@SuppressWarnings("deprecation")
public class MessageCancelListener extends PacketHandler{

	private static Gson a;
	static {
	        GsonBuilder gsonbuilder = new GsonBuilder();

	        gsonbuilder.registerTypeHierarchyAdapter(IChatBaseComponent.class, new ChatSerializer());
	        gsonbuilder.registerTypeHierarchyAdapter(ChatModifier.class, new ChatModifierSerializer());
	        gsonbuilder.registerTypeAdapterFactory(new ChatTypeAdapterFactory());
	        a = gsonbuilder.create();
	}
	
	public MessageCancelListener(){
		PacketListenerAPI.addPacketHandler(this);
	}
	
	@Override
	public void onReceive(ReceivedPacket arg0) {
	}

	@Override
	public void onSend(SentPacket packet) {
		if(packet.getPacketName().equals("PacketPlayOutChat")){
			IChatBaseComponent com = (IChatBaseComponent) packet.getPacketValue("a");
			String full = a.toJson(com);
			
			MessageSendEvent event = new MessageSendEvent(full, com, (PacketPlayOutChat)packet.getPacket(), packet.getPlayer());
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled()){
				packet.setCancelled(true);
				return;
			}
			packet.setPacket(event.getPacket());
			
		}
	}

}
