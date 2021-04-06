package top.themeda.ancientworld.teleport;

import java.util.UUID;

public class TeleportQueue {
	private UUID uid;
	private String name;
	private boolean overpass;
	public TeleportQueue(UUID uid,String name,boolean overpass) {
		this.uid=uid;
		this.name=name;
		this.overpass=overpass;
	}
	public String getPointName() {
		return this.name;
	}
	public boolean isOverpass() {
		return this.overpass;
	}
	public UUID getUUID() {
		return this.uid;
	}
}
