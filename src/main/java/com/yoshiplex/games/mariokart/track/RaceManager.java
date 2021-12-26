package com.yoshiplex.games.mariokart.track;

import java.util.ArrayList;
import java.util.List;

import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.tracks.MKTrack;
import com.yoshiplex.util.UnloadedLocation;

public class RaceManager {
	private MKTrack track = null;
	private List<MKPlayer> places = new ArrayList<>();
	private List<MKPlayer> finalPlaces = new ArrayList<>();
	
	public RaceManager(MKTrack track){
		this.track = track;
		updatePlaces();
		
	}
	public void updatePlaces(){
		places = new ArrayList<>();
		List<MKDist> distances = new ArrayList<>();
		int place = 0;
		for(MKPlayer p : MKPlayer.getPlayers()){
			if(!p.isInGame()){
				continue;
			}
			int passed = 0;
			if(p.isFinished()){
				if(!p.wasFinished() || !finalPlaces.contains(p)){
					finalPlaces.add(p);
					p.setWasFinished(true);
				} 
				continue;
			}
		
			for(MKCheckPoint point : track.getCheckPoints()){
				if(point.isPassedFor(p, p.getLap())){
					passed++;
				} else {
					break;
				}
			}
			double toNext = 0;
			UnloadedLocation next = null;
			if(passed + 1 >= track.getCheckPoints().size() || passed == 0){
				next = track.getCheckPoints().get(0).getCenter();
			} else {
				next = track.getCheckPoints().get(passed - 1).getCenter();
			}
			toNext = next.distance(p.getYPPlayer().toPlayer().getLocation());
			MKDist distance = new MKDist(p.getLap(), passed, (int) Math.round(toNext), p);
			distances.add(distance); 
		}
		for(MKPlayer finalPlayer : finalPlaces){ // place was NOT incremented above.
			if(!places.contains(finalPlayer)){
				places.add(finalPlayer);
				place++;
				finalPlayer.setPlace(place);
			}
		}
		for(int lap = track.getLapAmount(); lap >= 0; lap--){
			for(int pass = track.getCheckPoints().size(); pass >= 0; pass--){
				for(int intDist = 0; intDist < 200 ; intDist++){
					for(MKDist dist : distances){
						MKPlayer p = dist.getOwner();
						if(dist.getLap() == lap && dist.getCheckPoint() == pass && dist.getDistanceToNext() == intDist && !p.isFinished() && !finalPlaces.contains(p)){
							places.add(p);
							place++;
							if(place != places.size()){
								System.out.println("places.size() != place. Line 68 RaceManager.java");
							}
							//dist.getOwner().sendMessage("Debug: lap: " + dist.getLap() + ", checkpoint#: " + dist.getCheckPoint() + ", tonext: " + dist.getDistanceToNext());
							if(p.getPlace() != place){
								p.setPlace(place);
							}
						}
					}
				}
			}
		}
		
	}
	
	public List<MKPlayer> getTopPlayers(){
		return places;
	}
	public int getPlace(MKPlayer p){
		int i = 0;
		for(MKPlayer player : places){
			i++;
			if(player == p){
				return i;
			}
		}
		return 0;
	}
	public MKPlayer getPlayer(int place){
		return places.get(place - 1);
	}
}
