package top.themeda.ancientworld.listener;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import top.themeda.ancientworld.Core;
import top.themeda.ancientworld.people.People;

public class JoinAndQuit implements Listener{
	@EventHandler
	public void onPlayerLogin_Check(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		People people = Core.getPeopleManager().getPeople(p);
		if(people==null) {
			//如果玩家没有people实例则创建新的people实例
			Core.getPeopleManager().createNewPeople(p);
			Core.getPeopleManager().savePeopleData(Core.getPeopleManager().getPeople(p));
		}
	}
	@EventHandler
	public void onPlayerLogin_Message(PlayerLoginEvent e) {
		//离线信息
		//用于发送People#sendMessageInAnyCase玩家离线时的消息
		Player p = e.getPlayer();
		FileConfiguration cfg = Core.getConfigManager().getMessageCFG();
		ConfigurationSection cs = cfg.getConfigurationSection(p.getName());
		if(cs == null) return;
		for(String line:cs.getKeys(false)) {
			for(String word:cs.getStringList(line)) {
				p.sendMessage(word);
			}
			cs.set(line, null);
		}
		Core.getConfigManager().saveTempMessage();
	}
}
