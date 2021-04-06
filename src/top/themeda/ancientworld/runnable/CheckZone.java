package top.themeda.ancientworld.runnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import top.themeda.ancientworld.Core;
import top.themeda.ancientworld.people.People;
import top.themeda.ancientworld.teleport.Zone;
import top.themeda.ancientworld.util.ThemedaUtil;

public class CheckZone extends BukkitRunnable{
	@Override
	public synchronized void run() {
		Set<Zone> zones = Core.getZoneManager().getZones();
		for(Player p: Bukkit.getOnlinePlayers()) {
			People people = Core.getPeopleManager().getPeople(p);
			Set<String> punlock = people.getUnlockedTeleportPoint();
			for(Zone zone:zones) {
				//�������Ƿ���Խ������͵�
				Set<String> zunlock = zone.getUnlockedName(p, punlock);
				//�������Ѿ������˸�����ɽ��������д��͵�������
				if(zunlock.isEmpty())continue;
				//Ϊ�����Ӹ�����ɽ��������д��͵�
				punlock.addAll(zunlock);
				people.setUnlockedTeleportPoint(punlock);
				//��ʾ����Ѿ�����
				List<String> words = new ArrayList<>();
				words.addAll(zunlock);
				ThemedaUtil.sendMessageTo((CommandSender)p,ThemedaUtil.listReplace(
						ThemedaUtil.getTip("Teleport.UnlockList")
						, "%list%"
						, words));
				Core.getPeopleManager().savePeopleData(people);
			}
		}
	}
}
