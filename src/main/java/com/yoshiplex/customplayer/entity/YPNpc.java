package com.yoshiplex.customplayer.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

public class YPNpc extends YPEntity<Entity>{

	private static NPCRegistry reg;
	static{
		reg = CitizensAPI.getNPCRegistry();
	}
	private NPC npc;
	private String name;
	
	private YPNpc(String name){
		super(EntityType.PLAYER);
		this.name = name;
		this.updateEntity();
	}
	private void updateEntity(){
		if(npc == null){
			return;
		}
		super.e = npc.getEntity();
	}
	
	@Override
	public void spawn(Location loc) {
		npc = reg.createNPC(EntityType.PLAYER, name);
		this.updateEntity();
		
		try{
			super.spawn(loc);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public void teleport(Location tp) {
		npc.teleport(tp, TeleportCause.PLUGIN);
	}
	
	public NPC getNpc(){
		return npc;
	}
	
}
