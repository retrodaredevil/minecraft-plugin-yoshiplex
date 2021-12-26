package com.yoshiplex.teleportation;

import com.yoshiplex.Main;
import com.yoshiplex.teleportation.eventlisteners.GameGUIEvents;
import com.yoshiplex.teleportation.eventlisteners.PlayerAddress;
import com.yoshiplex.teleportation.eventlisteners.ping.PingListener;

public class TeleportationManager {
	
	
	
	public TeleportationManager(Main main){
		new GameGUIEvents(main);
		new PlayerAddress();
		Main.getInstance().getCommand("game").setExecutor(new GameCommand());
		new PingListener(main);
	}
	
}
