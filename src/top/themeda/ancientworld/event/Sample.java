package top.themeda.ancientworld.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class Sample extends Event{
	private static final HandlerList handlers = new HandlerList();
	@Override
	public HandlerList getHandlers() {return handlers;}
	public static HandlerList getHandlerList(){return handlers;}

}
