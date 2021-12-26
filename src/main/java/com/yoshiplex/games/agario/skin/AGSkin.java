package com.yoshiplex.games.agario.skin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.EntityType;

import com.yoshiplex.games.agario.AGPlayer;

public class AGSkin {

	private List<EntityType> types = new ArrayList<>();
	
	private AGPlayer p;
	
	public AGSkin(AGPlayer player){
		this.p = player;
		this.update();
	}
	public AGSkin(String code){
		this.update(code);
	}
	public void update(){
		if (p == null){
			return;
		}
		this.update(p.getYPPlayer().getConfigSection().getString("agar.skin"));
	}
	public void update(String s){
		if(s == null){
			return;
		}
		String[] split = s.split("-");
		for(String t : split){
			switch(t){
			case "1":
				types.add(EntityType.MAGMA_CUBE);
				break;
			default:
				types.add(EntityType.SLIME);
				break;
			}
		}
	}
	
	public EntityType getType(int i){
		if(types.size() == 0){
			return EntityType.SLIME;
		}
		while(i >= types.size()){
			i-=types.size();
		}
		return types.get(i);
	}
	public int size() {
		return types.size();
	}
	
}
