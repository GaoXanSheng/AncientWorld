package top.themeda.ancientworld.util;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import top.themeda.ancientworld.Core;
import top.themeda.ancientworld.people.People;
import top.themeda.ancientworld.teleport.UnlockRequire;
import top.themeda.ancientworld.teleport.Zone;

public class Verify {
	public static boolean isSaveLocation(Location loc) {
		//克隆一份坐标
		Location loc_clone = loc.clone();
		//获取到该坐标的第一格(玩家腿部位置)
		Block b0,b1 = loc_clone.getBlock(),b2;
		//获取该坐标的底座(地面)
		loc_clone.setY(loc.getBlockY()-1);
		b0 = loc_clone.getBlock();
		//获取该坐标的第二个(玩家身体和头部位置)
		loc_clone.setY(loc.getBlockY()+1);
		b2 = loc_clone.getBlock();
		//判断底座是否实心且第一格和第二格为空
		if(b0.isEmpty()||!b1.isEmpty()||!b2.isEmpty()) return false;
		return true;
	}
	@Deprecated
	public static boolean isInAABB(Location loc,Zone zone) {
		//判断玩家是否在zone区域中
		if(zone.getLoc1()==null||zone.getLoc2()==null)return false;
		Location loc1 = zone.getLoc1(),loc2=zone.getLoc2();
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
	public static boolean isPlayerUnlockThis(Player p,String name) {
		People pe = Core.getPeopleManager().getPeople(p);
		for(String line: pe.getUnlockedTeleportPoint()){
			if(line.equalsIgnoreCase(name)) return true;
		}
		return false;
	}
	public static boolean isAdvancementDone(Player p,String name) {
		Advancement ad = ThemedaUtil.getAdvancementBySimpleName(name);
		if(ad==null)return false;
		return p.getAdvancementProgress(ad).isDone();
	}
	/**
	 * 玩家是否已经解锁该要求的传送点
	 * @param unlock_zone
	 * @param unlock_Player
	 * @return
	 */
	public static boolean isUnlocked(UnlockRequire unlock_zone,Set<String> unlock_Player) {
		for(String name : unlock_Player) {
			if(unlock_zone.getPointName().equalsIgnoreCase(name))return true; 
		}
		return false;
	}
	/**
	 * 该集合中玩家还未达到要求的解锁点
	 * @param unlock_Player
	 * @param unlock_zone
	 * @return
	 */
	public static Set<UnlockRequire> getLockedPoint(Set<String> unlock_Player,Set<UnlockRequire> unlock_zone){
		Set<UnlockRequire> uqs = new HashSet<>();
		if(isUnlockAll(unlock_Player, unlock_zone))return uqs;
		for(UnlockRequire uq : unlock_zone) {
			if(!isUnlocked(uq,unlock_Player))uqs.add(uq);
		}
		return uqs;
	}
	/**
	 * 玩家是否已经解锁全部要求
	 * @param unlock_Player
	 * @param unlock_zone
	 * @return
	 */
	public static boolean isUnlockAll(Set<String> unlock_Player,Set<UnlockRequire> unlock_zone) {
		for(UnlockRequire uq : unlock_zone) {
			if(!isUnlocked(uq,unlock_Player))return false;
		}
		return true;
	}
}
