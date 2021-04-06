package top.themeda.ancientworld.teleport;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;

import top.themeda.ancientworld.util.ThemedaUtil;

public class UnlockRequire {
	private String pointname,name;
	private Set<String> permissions=new HashSet<>(),advancements=new HashSet<>();
	private boolean all_permission=false,all_advancement=false;
	public UnlockRequire(String name) {
		this.name=name;
	}
	public void setPointName(String name) {
		pointname=name;
	}
	public void setPermissions(Set<String> perms) {
		permissions=perms;
	}
	public void addPermission(String perm) {
		permissions.add(perm);
	}
	public void removePermission(String perm) {
		permissions.remove(perm);
	}
	public void clearPermission() {
		permissions.clear();
	}
	public void setAdvancements(Set<String> advs) {
		advancements=advs;
	}
	public void addAdvancement(String adv) {
		advancements.add(adv);
	}
	public void removeAdvancements(String adv) {
		advancements.remove(adv);
	}
	public void clearAdvancements() {
		advancements.clear();
	}
	public void setNeedAllPermissions(boolean bo) {
		all_permission=bo;
	}
	public void setNeedAllAdvancements(boolean bo) {
		all_advancement=bo;
	}
	
	public Set<String> getPermissions() {
		return permissions;
	}
	public Set<String> getAdvancements() {
		return advancements;
	}
	public boolean isNeedAllPermission() {
		return all_permission;
	}
	public boolean isNeedAllAdvancements() {
		return all_advancement;
	}
	public String getPointName() {
		return pointname;
	}
	public String getUnlockRequireName() {
		return name;
	}
	public boolean couldUnlockForPlayer(Player p) {
		boolean per=false,ads=false;
		for(String one:permissions) {
			if(p.hasPermission(one)) {
				per=true;
			}else if(all_permission){
				return false;
			}
		}
		for(String ad:advancements) {
			Advancement adv = ThemedaUtil.getAdvancementBySimpleName(ad);
			if(p.getAdvancementProgress(adv).isDone()) {
				ads=true;
			}else if(all_advancement) {
				return false;
			}
		}
		if(!permissions.isEmpty()&&!per)return false;
		if(!advancements.isEmpty()&&!ads)return false;
		return true;
	}
}
