package top.themeda.ancientworld.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import top.themeda.ancientworld.teleport.Zone;

public class PlayerLeaveZoneEvent extends Event{
	private static final HandlerList handlers = new HandlerList();
	@Override
	public HandlerList getHandlers() {return handlers;}
	public static HandlerList getHandlerList(){return handlers;}
	
	//-----------------
	private Player player;
	private Zone zone;
	public PlayerLeaveZoneEvent(Player p,Zone z) {
		this.player=p;
		this.zone=z;
	}
	public Player getPlayer() {
		return player;
	}
	public Zone getZone() {
		return zone;
	}
}
