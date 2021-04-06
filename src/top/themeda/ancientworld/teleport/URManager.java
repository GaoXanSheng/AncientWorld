package top.themeda.ancientworld.teleport;

import java.util.HashSet;
import java.util.Set;

public class URManager {
	private Set<UnlockRequire> urs = new HashSet<>();
	static URManager instance = new URManager();
	private URManager() {}
	public void addUnlockRequire(UnlockRequire ur) {
		urs.add(ur);
	}
	public void removeUnlockRequire(UnlockRequire ur) {
		urs.remove(ur);
	}
	public static URManager getInstance() {return instance;}

	public void setUnlockRequireManager(Set<UnlockRequire> urs) {
		this.urs=urs;
	}
	public Set<UnlockRequire> getUnlockRequires(){
		return this.urs;
	}
	public UnlockRequire getUnlockRequire(String name) {
		for(UnlockRequire each:urs) {
			if(each.getUnlockRequireName().equalsIgnoreCase(name))return each;
		}
		return null;
	}
}
