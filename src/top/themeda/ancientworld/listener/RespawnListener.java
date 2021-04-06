package top.themeda.ancientworld.listener;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import top.themeda.ancientworld.Core;
import top.themeda.ancientworld.people.People;
import top.themeda.ancientworld.teleport.TeleportPoint;
import top.themeda.ancientworld.teleport.UnlockRequire;
import top.themeda.ancientworld.teleport.Zone;

public class RespawnListener implements Listener{
	FileConfiguration config = Core.getConfigManager().getConfig();
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		if(!config.getBoolean("Function.RespawnPoint.Enable",false)) return;
		People p = Core.getPeopleManager().getPeople(e.getPlayer());
		String point_name = p.getRespawnPoint();
		if(!config.getBoolean("Function.RespawnPoint.RespawnNearby",false)) {
			if(config.getBoolean("Function.RespawnPoint.ZoneFirst",false)) {
				Zone zone = Core.getZoneManager().getZoneByLocation(e.getPlayer().getLocation());
				if(zone!=null) {
					for(UnlockRequire ur:zone.getUnlockRequire()) {
						TeleportPoint tp=Core.getTeleportPointManager().getTeleportPiont(ur.getPointName());
						if(!tp.asRespawnPoint()) continue;
						e.setRespawnLocation(tp.getLocation());
						return;
					}
				}
			}
			if(point_name!=null&&!point_name.equalsIgnoreCase("")) {
				TeleportPoint tp = Core.getTeleportPointManager().getTeleportPiont(point_name);
				if(tp!=null) {
					e.setRespawnLocation(tp.getLocation());
					return;
				}
			}
		}
		TeleportPoint tp = Core.getTeleportPointManager().getClosestPoint(e.getPlayer().getLocation());
		e.setRespawnLocation(tp.getLocation());
		return;
	}
}
