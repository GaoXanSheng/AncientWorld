package top.themeda.ancientworld.configuration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import top.themeda.ancientworld.Core;
import top.themeda.ancientworld.people.People;
import top.themeda.ancientworld.teleport.TeleportPoint;
import top.themeda.ancientworld.teleport.UnlockRequire;
import top.themeda.ancientworld.teleport.Zone;
import top.themeda.ancientworld.util.ThemedaUtil;
import top.themeda.util.SearchFiles;

public class ConfigLoad {
	/////////文件定义区
	File teleportlocs = new File(Core.getInstance().getDataFolder(),"Data/TeleportPoint.yml"),
			file_message = new File(Core.getInstance().getDataFolder(),"Data/TempMessage.yml"),
			file_zone = new File(Core.getInstance().getDataFolder(),"Data/Zone.yml"),
			file_config = new File(Core.getInstance().getDataFolder(),"config.yml"),
			file_language_en = new File(Core.getInstance().getDataFolder(),"language_en.yml"),
			file_language_zh_cn = new File(Core.getInstance().getDataFolder(),"language_zh_cn.yml");
	/////////
	FileConfiguration tele_cfg,config,language,message,zone;
	/////////初始化私有构造器
	public static ConfigLoad instance = new ConfigLoad();
	private ConfigLoad() {
		reloadAll();
	}
	public static ConfigLoad getInstance() {return instance;}
	public void reloadAll() {
		Core.log.sendMessage("§4    §l§9--§7Loading Configuration , Data and so on......");
		if(!file_config.exists())Core.getInstance().saveDefaultConfig();
		if(!file_language_en.exists()) {Core.getInstance().saveResource("language_en.yml", false);}
		if(!file_language_zh_cn.exists()) {Core.getInstance().saveResource("language_zh_cn.yml", false);}
		config = YamlConfiguration.loadConfiguration(file_config);
		tele_cfg = YamlConfiguration.loadConfiguration(teleportlocs);
		reloadLanguage();
		message = YamlConfiguration.loadConfiguration(file_message);
		zone = YamlConfiguration.loadConfiguration(file_zone);
		reloadPeople();
		reloadZoneData();
		reloadLocs();
	}
	public void reloadLanguage() {
		String language_version = config.getString("Language");
		switch (language_version) {
		case"zh_cn":
			language = YamlConfiguration.loadConfiguration(file_language_zh_cn);
			break;
		case"en":
		default:
			language = YamlConfiguration.loadConfiguration(file_language_en);
		}
	}
	public void saveAllData() {
		this.saveZoneData();
		this.saveTempMessage();
		this.saveTeleportCFG();
		Core.getPeopleManager().saveAllPeople();
		
	}
	public FileConfiguration getConfig() {
		return this.config;
	}
	public FileConfiguration getTeleportCFG() {
		return this.tele_cfg;
	}
	public FileConfiguration getMessageCFG() {
		return this.message;
	}
	public FileConfiguration getLanguaneCFG() {
		return this.language;
	}
	public void saveTeleportCFG() {
		for(TeleportPoint loc:Core.getTeleportPointManager().getTeleportPoint()) {
			Location loca = loc.getLocation();
			String name = loc.getName();
			ItemStack item_consume=loc.getConsumeItem();
			double cost = loc.getCostMoney();
			tele_cfg.set(name+".world", loca.getWorld().getName());
			tele_cfg.set(name+".x", loca.getX());
			tele_cfg.set(name+".y", loca.getY());
			tele_cfg.set(name+".z", loca.getZ());
			tele_cfg.set(name+".Item.Consume", item_consume);
			tele_cfg.set(name+".cost", cost);
			tele_cfg.set(name+".close", loc.isClosed());
			///////change
			tele_cfg.set(name+".launch.enable", loc.isLaunch());
			tele_cfg.set(name+".launch.title", loc.showTitle());
			tele_cfg.set(name+".launch.lenght", loc.getLengthOfTime());
			tele_cfg.set(name+".launch.height", loc.getLaunchHeight());
			tele_cfg.set(name+".launch.immediately", loc.launchImmediately());
			tele_cfg.set(name+".particle.ground", loc.getGroundParticle().name());
			tele_cfg.set(name+".particle.follow", loc.getFollowParticle().name());
 		}
		try {
			this.tele_cfg.save(teleportlocs);
		} catch (IOException e) {
			Core.log.sendMessage(ThemedaUtil.failMSG);
			e.printStackTrace();
		}
	}
	public void saveTempMessage() {
		try {
			this.message.save(file_message);
		} catch (IOException e) {
			Core.log.sendMessage(ThemedaUtil.failMSG);
			e.printStackTrace();
		}
	}
	public void saveZoneData() {
		for(Zone zone:Core.getZoneManager().getZones()) {
			String name = zone.getZoneName();
			this.zone.set(name+".Location.loc1", ThemedaUtil.serliaizeLocation(zone.getLoc1()));
			this.zone.set(name+".Location.loc2", ThemedaUtil.serliaizeLocation(zone.getLoc2()));
			for(UnlockRequire ur:zone.getUnlockRequire()) {
				String subkey = ur.getUnlockRequireName();
				this.zone.set(name+".UnlockRequire."+subkey+".pointname", ur.getPointName());
				this.zone.set(name+".UnlockRequire."+subkey+".advancements", ur.getAdvancements());
				this.zone.set(name+".UnlockRequire."+subkey+".permissions", ur.getPermissions());
				this.zone.set(name+".UnlockRequire."+subkey+".all_permissions", ur.isNeedAllPermission());
				this.zone.set(name+".UnlockRequire."+subkey+".all_advancements", ur.isNeedAllAdvancements());
			}
			//List<String> list = new ArrayList<>();
			//list.addAll(zone.getUnlockName());
			//this.zone.set(name+".pointname", list);
		}
		try {
			zone.save(file_zone);
		} catch (IOException e) {
			Core.log.sendMessage(ThemedaUtil.failMSG);
			e.printStackTrace();
		}
	}
	public void reloadZoneData() {
		Set<Zone> zones = new HashSet<>();
		for(String key:this.zone.getKeys(false)) {
			Zone z = new Zone();
			z.setZoneName(key);
			z.setLoc1(ThemedaUtil.unserliaizeLocation(zone.getStringList(key+".Location.loc1")));
			z.setLoc2(ThemedaUtil.unserliaizeLocation(zone.getStringList(key+".Location.loc2")));
			ConfigurationSection urcs = zone.getConfigurationSection(key+".UnlockRequire");
			for(String subkey : urcs.getKeys(false)) {
				UnlockRequire ur = new UnlockRequire(subkey);
				ur.setPointName(zone.getString(subkey+".pointname"));
				Set<String> list = new HashSet<>();
				list.addAll(zone.getStringList(subkey+".advancements"));
				ur.setAdvancements(list);
				list.clear();
				list.addAll(zone.getStringList(subkey+".permissions"));
				ur.setPermissions(list);
				ur.setNeedAllPermissions(zone.getBoolean(subkey+".all_permissions"));
				ur.setNeedAllAdvancements(zone.getBoolean(subkey+".all_advancements"));
				z.addUnlockRequire(ur);
			}
			zones.add(z);
		}
		Core.getZoneManager().setZoneManager(zones);
		Core.log.sendMessage("      §l§9--§7Zone has been Reloaded Successful! Got "+zones.size());
	}
	//////////////获取玩家信息
	public void reloadPeople() {
		Set<People> peoples = new HashSet<People>();
		File file_people = new File(Core.getInstance().getDataFolder(),
				"Data/People");
		List<File> files = SearchFiles.searchFiles(file_people, ".yml");
		for(File file : files) {
			FileConfiguration cfg_people = YamlConfiguration.loadConfiguration(file);
			UUID uid =null;
			try {
				uid = UUID.fromString(file.getName().replace(".yml", ""));
			}catch(Exception e){
				Core.log.sendMessage(ThemedaUtil.failMSG);
				continue;
			}
			OfflinePlayer player = Bukkit.getOfflinePlayer(uid);
			People people = new People();
			///////INITIALIZE  4
			people.setPlayer(player);
			people.setCountry(cfg_people.getString("Country"));
			people.setVocation(cfg_people.getString("Vocation"));
			people.setUnlockedTeleportPoint(cfg_people.getStringList("Unlocked.Teleport"));
			people.setRespawnPoint(cfg_people.getString("RespawnPoint"));
			peoples.add(people);
		}
		Core.getPeopleManager().setPeoples(peoples);
		Core.log.sendMessage("      §l§9--§7People has been Reloaded Successful! Got "+peoples.size());
	}
	///////////////////////
	public void reloadLocs() {
		Set<TeleportPoint> temp_locs = new HashSet<>();
		for(String line : tele_cfg.getKeys(false)) {
			Location loc = Bukkit.getWorlds().get(0).getSpawnLocation();
			ItemStack item_consume = tele_cfg.getItemStack(line+".Item.Consume");
			loc.setWorld(Bukkit.getWorld(tele_cfg.getString(line+".world")));
			loc.setX(tele_cfg.getDouble(line+".x"));
			loc.setY(tele_cfg.getDouble(line+".y"));
			loc.setZ(tele_cfg.getDouble(line+".z"));
			TeleportPoint tp = new TeleportPoint();
			tp.setConsumeItem(item_consume);
			tp.setCostMoney(tele_cfg.getDouble(line+".cost"));
			tp.setLocation(loc);
			tp.setName(line);tp.setClosed(tele_cfg.getBoolean(line+".close"));
			/////change
			tp.setLaunchEnable(tele_cfg.getBoolean(line+".launch.enable",true));
			tp.setImmediatelyOrNot(tele_cfg.getBoolean(line+".launch.immediately",false));
			tp.setShowTitleOrNot(tele_cfg.getBoolean(line+".launch.title",true));
			tp.setLaunchHeight(tele_cfg.getInt(line+".launch.height",200));
			tp.setLaunchTime(tele_cfg.getInt(line+".launch.lenght",800));
			try {
				Particle par_ground = Particle.valueOf(tele_cfg.getString(line+".particle.ground")),
						par_follow = Particle.valueOf(tele_cfg.getString(line+".particle.follow"));
				tp.setGroundParticle(par_ground);tp.setFollowParticle(par_follow);
			}catch(Exception e) {
				Core.log.sendMessage("      §l§9--§7Particle Type for "+line+" is UNIDENTIFIED");
			}
			temp_locs.add(tp);
		}
		Core.getTeleportPointManager().setTeleportPoint(temp_locs);
		Core.log.sendMessage("      §l§9--§7Teleport Point has been Reloaded Successful! Got "+temp_locs.size());
	}
}