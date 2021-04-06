package top.themeda.ancientworld.teleport;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Location;

import top.themeda.ancientworld.Core;

public class TeleportPointManager {
	private Set<TeleportPoint> teleports = new HashSet<>();
	static TeleportPointManager instance = new TeleportPointManager();
	private TeleportPointManager() {}
	public void addTeleportPoint(TeleportPoint point) {
		teleports.add(point);
		Core.getConfigManager().saveTeleportCFG();
	}
	public static TeleportPointManager getInstance() {return instance;}
	////////TELEPORT POINT
	public void setTeleportPoint(Set<TeleportPoint> locs) {
		this.teleports=locs;
	}
	public Set<TeleportPoint> getTeleportPoint(){
		return this.teleports;
	}
	public TeleportPoint getTeleportPiont(String name) {
		for(TeleportPoint each:teleports) {
			if(each.getName().equalsIgnoreCase(name))return each;
		}
		return null;
	}
	public void deleteTeleportPoint(String name) {
		Iterator<TeleportPoint> it = teleports.iterator();
		while(it.hasNext()) {
			TeleportPoint tp = it.next();
			if(tp.getName().equalsIgnoreCase(name)) {
				teleports.remove(tp);
				Core.getConfigManager().getTeleportCFG().set(name, null);
				Core.getConfigManager().saveTeleportCFG();
				break;
			}
		}
	}
	public TeleportPoint getClosestPoint(Location loc) {
		double distance=0d;
		TeleportPoint tp = null;
		for(TeleportPoint each:teleports) {
			double tempdistance = loc.distance(each.getLocation());
			distance=distance==0d?tempdistance:distance;
			if(tempdistance<distance) {
				tp=each;
			}
		}
		return tp;
	}
}
