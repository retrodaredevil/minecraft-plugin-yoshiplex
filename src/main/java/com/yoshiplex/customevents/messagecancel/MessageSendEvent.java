package com.yoshiplex.customevents.messagecancel;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.PacketPlayOutChat;

public class MessageSendEvent extends Event implements Cancellable{

	private static HandlerList list = new HandlerList();
	
	private boolean cancelled = false;
	private String json;
	private IChatBaseComponent com;
	private PacketPlayOutChat packet;
	private Player p;
	
	public MessageSendEvent(String json, IChatBaseComponent com, PacketPlayOutChat packet, Player p){
		this.json = json;
		this.com = com;
		this.packet = packet;
		this.p = p;
	}
	

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean b) {
		this.cancelled = b;
	}

	@Override
	public HandlerList getHandlers() {
		return list;
	}
	public static HandlerList getHandlerList(){
		return list;
	}


	public String getJson() {
		return json;
	}


//	public void setJson(String json) {
//		this.json = json;
//	}




//	public void setMessage(String message) {
//		this.json = "{\"text\": \"" + message + "\"}";
//		this.resetExtracted();
//	}


	public IChatBaseComponent getCom() {
		return com;
	}


//	public void setCom(IChatBaseComponent com) {
//		this.com = com;
//	}


	public PacketPlayOutChat getPacket() {
		return packet;
	}


	public void setPacket(PacketPlayOutChat packet) {
		this.packet = packet;
	}


	public Player getPlayer() {
		return p;
	}


}
