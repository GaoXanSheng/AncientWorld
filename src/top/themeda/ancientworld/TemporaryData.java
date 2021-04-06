package top.themeda.ancientworld;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import top.themeda.ancientworld.teleport.TeleportQueue;

public class TemporaryData {
	private static TemporaryData instance = new TemporaryData();
	private static Set<TeleportQueue> queue_tp = new HashSet<>();
	private TemporaryData() {}
	public static TemporaryData getInstance() {
		return instance;
	}
	/**
	 * 将一个玩家添加到传送队列中等待
	 * @param uid 玩家UUID
	 * @param name 要去的传送点名称
	 * 默认越权为false
	 */
	public void addTeleportQueue(UUID uid,String name) {
		queue_tp.add(new TeleportQueue(uid,name,false));
	}
	/**
	 * 将一个玩家添加到传送队列中等待
	 * @param uid 玩家UUID
	 * @param name 要去的传送点名称
	 * @param overpass 是否越权且无消耗
	 */
	public void addTeleportQueue(UUID uid,String name,boolean overpass) {
		queue_tp.add(new TeleportQueue(uid,name,overpass));
	}
	/**
	 * 从传送队列中删除一个玩家的信息
	 * @param uid
	 */
	public void deleteTeleportQueue(UUID uid) {
		TeleportQueue tq = null;
		for(TeleportQueue one:queue_tp) {
			if(one.getUUID().equals(uid))tq=one;
		}
		if(tq==null)return;
		queue_tp.remove(tq);
	}
	/**
	 * 查看玩家是否在传送队列中
	 * @param uid
	 * @return
	 */
	public boolean isInTeleportQueue(UUID uid) {
		for(TeleportQueue one:queue_tp) {
			if(one.getUUID().equals(uid))return true;
		}
		return false;
	}
	/**
	 * 获取玩家传送队列信息 如果玩家不在队列中则返回null
	 * @param uid
	 * @return
	 */
	public TeleportQueue getTeleportPoint(UUID uid) {
		for(TeleportQueue one:queue_tp) {
			if(one.getUUID().equals(uid))return one;
		}
		return null;
	}
}
