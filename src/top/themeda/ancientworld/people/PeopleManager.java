package top.themeda.ancientworld.people;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import top.themeda.ancientworld.Core;
import top.themeda.ancientworld.util.ThemedaUtil;

public class PeopleManager {
	Set<People> peoples = new HashSet<>();
	static PeopleManager instance = new PeopleManager();
	private PeopleManager() {}
	public static PeopleManager getInstance() {return instance;}
	/////////PEOPLE
	public Set<People> getPeoples(){
		return peoples;
	}
	public People getPeople(OfflinePlayer player) {
		return getPeople(player.getUniqueId());
	}
	public People getPeople(Player player) {
		return getPeople(player.getUniqueId());
	}
	public People getPeople(String name) {
		return getPeople(ThemedaUtil.getOfflinePlayerByName(name));
	}
	public People getPeople(UUID uid) {
		for(People p:peoples) {
			if(p.getPlayer().getUniqueId().equals(uid))return p;
		}
		return null;
	}
	public void setPeoples(Set<People> peoples) {
		this.peoples=peoples;
	}
	public void createNewPeople(Player p) {
		People people = new People();
		///////INITIALIZE  4
		people.setPlayer(p);
		people.setCountry(null);
		people.setVocation(null);
		people.setUnlockedTeleportPoint(new HashSet<>());
		this.peoples.add(people);
	}
	public void saveAllPeople() {
		for(People p : peoples) {
			File file_pe = new File(Core.getInstance().getDataFolder(),"Data/People/"+p.getPlayer().getUniqueId()+".yml");
			if(!file_pe.exists()) {
				file_pe.getParentFile().mkdirs();
				try {
					file_pe.createNewFile();
				} catch (IOException e) {
					Core.log.sendMessage(ThemedaUtil.failMSG);
					e.printStackTrace();
				}
			}
			FileConfiguration data = YamlConfiguration.loadConfiguration(file_pe);
			data.set("Country", p.getCountry());
			data.set("Vocation", p.getVocation());
			data.set("RespawnPoint", p.getRespawnPoint());
			List<String> list = new ArrayList<>();
			list.addAll(p.getUnlockedTeleportPoint());
			data.set("Unlocked.Teleport", list);
			try {
				data.save(file_pe);
			} catch (IOException e) {
				Core.log.sendMessage(ThemedaUtil.failMSG);
				e.printStackTrace();
			}
		}
	}
	public void savePeopleData(People p) {
		File file_pe = new File(Core.getInstance().getDataFolder(),"Data/People/"+p.getPlayer().getUniqueId()+".yml");
		if(!file_pe.exists()) {
			file_pe.getParentFile().mkdirs();
			try {
				file_pe.createNewFile();
			} catch (IOException e) {
				Core.log.sendMessage(ThemedaUtil.failMSG);
				e.printStackTrace();
			}
		}
		FileConfiguration data = YamlConfiguration.loadConfiguration(file_pe);
		data.set("Country", p.getCountry());
		data.set("Vocation", p.getVocation());
		List<String> list = new ArrayList<>();
		list.addAll(p.getUnlockedTeleportPoint());
		data.set("Unlocked.Teleport", list);
		try {
			data.save(file_pe);
		} catch (IOException e) {
			Core.log.sendMessage(ThemedaUtil.failMSG);
			e.printStackTrace();
		}
	}
}
