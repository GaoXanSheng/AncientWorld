package top.themeda.ancientworld.teleport;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import top.themeda.ancientworld.util.ThemedaUtil;
import top.themeda.ancientworld.util.Verify;

public class Zone {
	private String name;
	private Location zone1,zone2;
	private Set<UnlockRequire> unlock = new HashSet<>();
	private List<String> cmds;
	private Particle particle;
	private int particle_type;
	public Zone() {
		zone1=new Location(Bukkit.getWorlds().get(0), 0, -300, 0);
		zone2=new Location(Bukkit.getWorlds().get(0), 0, -300, 0);
		cmds = new ArrayList<>();
	}
	public void setCommands(List<String> cmds) {
		this.cmds=cmds;
	}
	public List<String> getCommands(){
		return cmds;
	}
	public void setParticle(Particle part) {
		particle=part;
	}
	public void setParticle(String name) {
		try {
			particle=Particle.valueOf(name);
		}catch(Exception e) {
			ThemedaUtil.sendErrorMessage("Particle type: "+name+" do not exist");
		}
	}
	public Particle getParticle() {
		return particle;
	}
	public int getParticleType() {
		return particle_type;
	}
	public void setParticleType(int type) {
		particle_type=type;
	}
	public Location getLoc1() {
		return zone1;
	}
	public Location getLoc2() {
		return zone2;
	}
	public void setLoc1(Location zone1) {
		this.zone1=zone1;
	}
	public void setLoc2(Location zone2) {
		this.zone2=zone2;
	}
	public Set<UnlockRequire> getUnlockRequire(){
		return this.unlock;
	}
	public void setUnlockRequire(Set<UnlockRequire> names) {
		this.unlock=names;
	}
	public void addUnlockRequire(UnlockRequire name) {
		this.unlock.add(name);
	}
	public void removeUnlockRequire(String name) {
		Iterator<UnlockRequire> it = unlock.iterator();
		while(it.hasNext()) {
			UnlockRequire uq = it.next();
			if(uq.getPointName().equalsIgnoreCase(name)) {
				unlock.remove(uq);
				break;
			}
		}
	}
	public void setZoneName(String name) {
		this.name=name;
	}
	public String getZoneName() {
		return this.name;
	}
	public boolean isPlayerInside(Player p) {
		if(getLoc1()==null||getLoc2()==null)return false;
		Location loc = p.getLocation(),loc1 = getLoc1(),loc2=getLoc2();
		if(!loc.getWorld().equals(loc1.getWorld())) return false;
		double x = loc.getX(),y=loc.getY(),z=loc.getZ(),
				x1 = loc1.getX(),x2=loc2.getX(),
				y1= loc1.getY(),y2=loc2.getY(),
				z1= loc1.getZ(),z2=loc2.getZ();
		//判定算法
		if((x>x1&&x>x2)||(x<x1&&x<x2))return false;
		if((y>y1&&y>y2)||(y<y1&&y<y2))return false;
		if((z>z1&&z>z2)||(z<z1&&z<z2))return false;
		return true;
	}
	public boolean isLocationInside(Location loc) {
		if(getLoc1()==null||getLoc2()==null)return false;
		Location loc1 = getLoc1(),loc2=getLoc2();
		if(!loc.getWorld().equals(loc1.getWorld())) return false;
		double x = loc.getX(),y=loc.getY(),z=loc.getZ(),
				x1 = loc1.getX(),x2=loc2.getX(),
				y1= loc1.getY(),y2=loc2.getY(),
				z1= loc1.getZ(),z2=loc2.getZ();
		//判定算法
		if((x>x1&&x>x2)||(x<x1&&x<x2))return false;
		if((y>y1&&y>y2)||(y<y1&&y<y2))return false;
		if((z>z1&&z>z2)||(z<z1&&z<z2))return false;
		return true;
	}
	/**
	 * 达成解锁要求而解锁的传送点名称的集合
	 * @param p
	 * @param unlock_Player
	 * @return
	 */
	public Set<String> getUnlockedName(Player p,Set<String> unlock_Player) {
		Set<String> names = new HashSet<>();
		if(!Verify.isUnlockAll(unlock_Player, unlock)) {
			for(UnlockRequire uq:unlock) {
				if(uq.couldUnlockForPlayer(p)) names.add(uq.getPointName());
			}
		}
		return names;
	}
	/**
	 * 该区域所有可解锁的解锁要求名称
	 * @return
	 */
	public Set<String> getUnlockName() {
		Set<String> list= new HashSet<>();
		for(UnlockRequire uq:unlock) {
			list.add(uq.getUnlockRequireName());
		}
		return list;
	}
}
