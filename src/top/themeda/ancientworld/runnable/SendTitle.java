package top.themeda.ancientworld.runnable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import top.themeda.ancientworld.Core;
import top.themeda.ancientworld.teleport.TeleportPoint;
import top.themeda.ancientworld.util.ThemedaUtil;

public class SendTitle extends BukkitRunnable{
	@Override
	public void run() {
		for(Player p:Bukkit.getOnlinePlayers()) {
			if(Core.getTemporartData().isInTeleportQueue(p.getUniqueId())) {
				try {
					//通过传送点名称获取传送点实例
					TeleportPoint tps = Core.getTeleportPointManager().getTeleportPiont(
							Core.getTemporartData().getTeleportPoint(p.getUniqueId()).getPointName());
					if(tps==null) {
						continue;
					}
					//该传送点提示方式为title还是message
					if(tps.showTitle()) {
						p.sendTitle(Core.getConfigManager().getLanguaneCFG().getString("Teleport.PrepareToLaunch"),
								"", 4, 10, 3);
					}else {
						ThemedaUtil.sendMessageTo(p, ThemedaUtil.getTip("Teleport.PrepareToLaunch"));
					}
				}catch(Exception e) {
					//放置队列中有没有目标的玩家
					Core.getTemporartData().deleteTeleportQueue(p.getUniqueId());
					Core.log.sendMessage(ThemedaUtil.errorMSG);
					e.printStackTrace();
				}
			}
		}
	}
}
