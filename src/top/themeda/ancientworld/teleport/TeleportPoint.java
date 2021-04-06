package top.themeda.ancientworld.teleport;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

public class TeleportPoint {
	private String name;
	private Location loc;
	private ItemStack item_consume;
	private double cost=0;
	private boolean closed=false,launch=false,title=true,imm=false,asspawn=false;
	private Particle particle_ground = Particle.FIREWORKS_SPARK,
			particle_follow=Particle.REDSTONE;
	private int length = 800,height=200;
	
	public boolean asRespawnPoint() {
		return asspawn;
	}
	public void setAsSpawn(boolean bo) {
		asspawn=bo;
	}
	/////////LAUNCH IMMEDIATELY
	public boolean launchImmediately() {
		return imm;
	}
	public void setImmediatelyOrNot(boolean bo) {
		imm=bo;
	}
	////////LENGHT
	public int getLengthOfTime() {
		return length;
	}
	public void setLaunchTime(int time) {
		length=time;
	}
	/////////HIGH
	public int getLaunchHeight() {
		return height;
	}
	public void setLaunchHeight(int h) {
		height=h;
	}
	///////TITLE
	public boolean showTitle() {
		return title;
	}
	public void setShowTitleOrNot(boolean bo) {
		title=bo;
	}
	////////PARTICLE
	public Particle getGroundParticle() {
		return particle_ground;
	}
	public Particle getFollowParticle() {
		return particle_follow;
	}
	public void setGroundParticle(Particle pa) {
		particle_ground=pa;
	}
	public void setFollowParticle(Particle pa) {
		particle_follow=pa;
	}
	////////Launch
	public boolean isLaunch() {
		return launch;
	}
	public void setLaunchEnable(boolean b) {
		launch=b;
	}
	///////  NAME
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name=name;
	}
	
	////////// LOCATION
	public Location getLocation() {
		return loc;
	}
	public void setLocation(Location loc) {
		this.loc=loc;
	}
	
	///////// ITEMSTACK
	public ItemStack getConsumeItem() {
		return this.item_consume;
	}
	public void setConsumeItem(ItemStack item) {
		this.item_consume=item;
	}
	
	////////// COST
	public double getCostMoney() {
		return this.cost;
	}
	public void setCostMoney(double cost) {
		this.cost=cost;
	}
	
	/////////////STATE
	public boolean isClosed() {
		return this.closed;
	}
	public void setClosed(boolean bl) {
		this.closed=bl;
	}
}
