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
	 * ��һ�������ӵ����Ͷ����еȴ�
	 * @param uid ���UUID
	 * @param name Ҫȥ�Ĵ��͵�����
	 * Ĭ��ԽȨΪfalse
	 */
	public void addTeleportQueue(UUID uid,String name) {
		queue_tp.add(new TeleportQueue(uid,name,false));
	}
	/**
	 * ��һ�������ӵ����Ͷ����еȴ�
	 * @param uid ���UUID
	 * @param name Ҫȥ�Ĵ��͵�����
	 * @param overpass �Ƿ�ԽȨ��������
	 */
	public void addTeleportQueue(UUID uid,String name,boolean overpass) {
		queue_tp.add(new TeleportQueue(uid,name,overpass));
	}
	/**
	 * �Ӵ��Ͷ�����ɾ��һ����ҵ���Ϣ
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
	 * �鿴����Ƿ��ڴ��Ͷ�����
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
	 * ��ȡ��Ҵ��Ͷ�����Ϣ �����Ҳ��ڶ������򷵻�null
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
