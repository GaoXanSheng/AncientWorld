package top.themeda.ancientworld.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import top.themeda.ancientworld.Core;
import top.themeda.ancientworld.event.PlayerEntryZoneEvent;
import top.themeda.ancientworld.event.PlayerLeaveZoneEvent;
import top.themeda.ancientworld.teleport.Zone;

public class PlayerChangeStateEvent implements Listener{
	@EventHandler
	public void onPlayerLevelChange(PlayerLevelChangeEvent e) {
		
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Zone zone_from = Core.getZoneManager().getZoneByLocation(e.getFrom()),
				zone_to = Core.getZoneManager().getZoneByLocation(e.getTo());
		if(zone_from==null) {
			if(zone_to!=null)
				Bukkit.getPluginManager().callEvent(new PlayerEntryZoneEvent(e.getPlayer(),zone_to,null));
		}else {
			if(zone_to==null){
				Bukkit.getPluginManager().callEvent(new PlayerLeaveZoneEvent(e.getPlayer(),zone_from));
			}else {
				Bukkit.getPluginManager().callEvent(new PlayerEntryZoneEvent(e.getPlayer(),zone_to,zone_from));
				Bukkit.getPluginManager().callEvent(new PlayerLeaveZoneEvent(e.getPlayer(),zone_from));
			}
			
		}
	}
}
