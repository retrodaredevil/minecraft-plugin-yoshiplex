package com.yoshiplex.packetlisteners;

import org.inventivetalent.packetlistener.PacketListenerAPI;
import org.inventivetalent.packetlistener.handler.PacketHandler;
import org.inventivetalent.packetlistener.handler.ReceivedPacket;
import org.inventivetalent.packetlistener.handler.SentPacket;

import com.yoshiplex.util.ReflectedObject;


public class AbilitiesListener extends PacketHandler{

	@SuppressWarnings("deprecation")
	public AbilitiesListener(){
		PacketListenerAPI.addPacketHandler(this);
	}

	@Override
	public void onReceive(ReceivedPacket arg0) {
	}

	@Override
	public void onSend(SentPacket pack) {
		if(pack.getPacketName().equals("PacketPlayOutAbilities")){
			ReflectedObject packet = new ReflectedObject(pack.getPacket());
			float f = (float) packet.get("f").toObject();
			System.out.println("PacketPlayOutAbilities sent with f = " + f + " e = " + (float) packet.get("e").toObject());
		}
	}
	
}
