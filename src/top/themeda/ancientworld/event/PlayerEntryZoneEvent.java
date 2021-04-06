package top.themeda.ancientworld.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import top.themeda.ancientworld.teleport.Zone;

public class PlayerEntryZoneEvent extends PlayerLeaveZoneEvent{
	private static final HandlerList handlers = new HandlerList();
	@Override
	public HandlerList getHandlers() {return handlers;}
	public static HandlerList getHandlerList(){return handlers;}
	
	//-----------------
	private Player player;
	private Zone zone_to;
	public PlayerEntryZoneEvent(Player p,Zone to,Zone from) {
		super(p,from);
		this.player=p;
		this.zone_to=to;
	}
	public Player getPlayer() {
		return player;
	}
	public Zone getTo() {
		return zone_to;
	}
}
