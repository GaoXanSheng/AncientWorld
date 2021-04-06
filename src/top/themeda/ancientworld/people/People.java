package top.themeda.ancientworld.people;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import top.themeda.ancientworld.Core;

public class People {
	private OfflinePlayer people;
	private String country,vocation,bindrespawnpoint;
	private Set<String> unlocked_tele = new HashSet<>();
	///////////////PLAYER
	public void setPlayer(OfflinePlayer player) {
		people = player;
	}
	public OfflinePlayer getPlayer() {
		return this.people;
	}
	public void setRespawnPoint(String name) {
		
	}
	public String getRespawnPoint() {
		return bindrespawnpoint;
	}
	//////////////COUNTRY
	public void setCountry(String name) {
		this.country=name;
	}
	public String getCountry() {
		return this.country;
	}
	/////////////SEND MESSAGE
	public void sendMessage(List<String> words) {
		if(!people.isOnline())return;
		Player p = (Player)people;
		for(String line:words) {
			p.sendMessage(line);
		}
	}
	public void sendMessage(String word) {
		if(!people.isOnline())return;
		Player p = (Player)people;
		p.sendMessage(word);
	}
	public void sendMessageInAnyCase(List<String> words) {
		if(!people.isOnline()) {
			Core.getConfigManager().getMessageCFG().set(people.getName()+"."+System.currentTimeMillis()+"",words);
			Core.getConfigManager().saveTempMessage();
		}else {
			Player p = (Player)people;
			for(String line:words) {
				p.sendMessage(line);
			}
		}
	}
	public void sendMessageInAnyCase(String word) {
		if(!people.isOnline()) {
			Core.getConfigManager().getMessageCFG().set(people.getName()+"."+System.currentTimeMillis()+"",word);
			Core.getConfigManager().saveTempMessage();
		}else {
			Player p = (Player)people;
			p.sendMessage(word);
		}
	}
	////////////////UNLOCKED TELEPORT POINT
	public void setUnlockedTeleportPoint(Set<String> set) {
		this.unlocked_tele=set;
	}
	public void setUnlockedTeleportPoint(List<String> list) {
		Set<String> sets = new HashSet<>();
		for(String line:list) {
			sets.add(line);
		}
		this.unlocked_tele=sets;
	}
	public Set<String> getUnlockedTeleportPoint(){
		return this.unlocked_tele;
	}
	//////////add point
	public void addTeleportPoint(String name) {
		this.unlocked_tele.add(name);
	}
	public void removeTeleportPoint(String name) {
		this.unlocked_tele.remove(name);
	}
	////////////////VOCATION
	public void setVocation(String vocation) {
		this.vocation=vocation;
	}
	public String getVocation() {
		return this.vocation;
	}
}
