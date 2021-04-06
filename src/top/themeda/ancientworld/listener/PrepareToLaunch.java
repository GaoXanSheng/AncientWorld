package top.themeda.ancientworld.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import top.themeda.ancientworld.Core;
import top.themeda.ancientworld.TemporaryData;
import top.themeda.ancientworld.teleport.TeleportPeople;
import top.themeda.ancientworld.teleport.TeleportQueue;
import top.themeda.ancientworld.util.ThemedaUtil;

public class PrepareToLaunch implements Listener{
	@EventHandler
	public void playerReleaseSHIFT(PlayerToggleSneakEvent e) {
		//������л�Ǳ��ʱ���Դ���
		TemporaryData data = Core.getTemporartData();
		Player p = e.getPlayer();
		//�鿴����Ƿ��ڴ��͵ȴ�������
		if(!data.isInTeleportQueue(p.getUniqueId()))return;
		//�������ǽ���Ǳ��״̬��ֱ�ӷ���
		if(e.isSneaking()) return;
		//��ȡ��Ҷ�����Ϣ
		TeleportQueue tpQ = data.getTeleportPoint(p.getUniqueId());
		//��ȡ��ʱɾ��������Ϣ
		data.deleteTeleportQueue(p.getUniqueId());
		String name = tpQ.getPointName();
		Location loc = p.getLocation();
		p.resetTitle();
		//�ж����ͷ���Ƿ��з����赲����
		if(loc.getBlockY()<loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ())) {
			ThemedaUtil.sendMessageTo(p, ThemedaUtil.getTip("Teleport.FailToLaunch"));
			return;
		}
		//���Ե������
		TeleportPeople.launch(p, name,tpQ.isOverpass());
	}
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		//���������ʱ��ʱ����ҴӶ�����ɾ��
		TemporaryData data = Core.getTemporartData();
		Player p = e.getEntity();
		if(data.isInTeleportQueue(p.getUniqueId()))data.deleteTeleportQueue(p.getUniqueId());
	}
}
