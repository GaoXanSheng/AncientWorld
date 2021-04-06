package top.themeda.ancientworld.teleport;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;

public class ZoneManager {
	private Set<Zone> zones = new HashSet<>();
	static ZoneManager instance = new ZoneManager();
	private ZoneManager() {}
	public void addZone(Zone zone) {
		zones.add(zone);
		///SAVE ZONE
	}
	public void removeZone(Zone zone) {
		zones.remove(zone);
	}
	public static ZoneManager getInstance() {return instance;}

	public void setZoneManager(Set<Zone> locs) {
		this.zones=locs;
	}
	public Set<Zone> getZones(){
		return this.zones;
	}
	public Zone getZone(String name) {
		for(Zone each:zones) {
			if(each.getZoneName().equalsIgnoreCase(name))return each;
		}
		return null;
	}
	public Zone getZoneByLocation(Location loc) {
		for(Zone each:zones) {
			if(each.isLocationInside(loc))return each;
		}
		return null;
	}
}
