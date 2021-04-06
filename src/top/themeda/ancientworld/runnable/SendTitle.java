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
					//ͨ�����͵����ƻ�ȡ���͵�ʵ��
					TeleportPoint tps = Core.getTeleportPointManager().getTeleportPiont(
							Core.getTemporartData().getTeleportPoint(p.getUniqueId()).getPointName());
					if(tps==null) {
						continue;
					}
					//�ô��͵���ʾ��ʽΪtitle����message
					if(tps.showTitle()) {
						p.sendTitle(Core.getConfigManager().getLanguaneCFG().getString("Teleport.PrepareToLaunch"),
								"", 4, 10, 3);
					}else {
						ThemedaUtil.sendMessageTo(p, ThemedaUtil.getTip("Teleport.PrepareToLaunch"));
					}
				}catch(Exception e) {
					//���ö�������û��Ŀ������
					Core.getTemporartData().deleteTeleportQueue(p.getUniqueId());
					Core.log.sendMessage(ThemedaUtil.errorMSG);
					e.printStackTrace();
				}
			}
		}
	}
}
