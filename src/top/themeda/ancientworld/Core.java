package top.themeda.ancientworld;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.milkbowl.vault.economy.Economy;
import top.themeda.ancientworld.commands.BaseCommand;
import top.themeda.ancientworld.configuration.CheckLanguageVersion;
import top.themeda.ancientworld.configuration.ConfigLoad;
import top.themeda.ancientworld.listener.JoinAndQuit;
import top.themeda.ancientworld.listener.PrepareToLaunch;
import top.themeda.ancientworld.listener.RespawnListener;
import top.themeda.ancientworld.people.PeopleManager;
import top.themeda.ancientworld.runnable.CheckZone;
import top.themeda.ancientworld.runnable.SendTitle;
import top.themeda.ancientworld.teleport.TeleportPointManager;
import top.themeda.ancientworld.teleport.URManager;
import top.themeda.ancientworld.teleport.ZoneManager;
import top.themeda.util.Metrics;

public class Core extends JavaPlugin{
	static Core core;
	static ConfigLoad configload;
	static PeopleManager pmanager;
	static ZoneManager zonemanager;
	static TemporaryData tempdata;
	static TeleportPointManager tmanager;
	static URManager urmanager;
	public static String version; 
	public Economy econ;
	public static ConsoleCommandSender log = Bukkit.getConsoleSender();
	@Override
	public void onEnable() {
		log.sendMessage("§6                      _            ___          __        _     _ " );
		log.sendMessage("§6     /\\              (_)          | \\ \\        / /       | |   | |");
		log.sendMessage("§6    /  \\   _ __   ___ _  ___ _ __ | |\\ \\  /\\  / /__  _ __| | __| |");
		log.sendMessage("§6   / /\\ \\ | '_ \\ / __| |/ _ \\ '_ \\| __\\ \\/  \\/ / _ \\| '__| |/ _` |");
		log.sendMessage("§6  / ____ \\| | | | (__| |  __/ | | | |_ \\  /\\  / (_) | |  | | (_| |");
		log.sendMessage("§6 /_/    \\_\\_| |_|\\___|_|\\___|_| |_|\\__| \\/  \\/ \\___/|_|  |_|\\__,_|");
		log.sendMessage("§6                                                              ");
		log.sendMessage("                §l§7Loading                  ");
		log.sendMessage("    §l§9-----------------------------------   ");
		version = Bukkit.getServer().getClass().getPackage().getName();
		version = version.substring(version.lastIndexOf(".")+1);
		log.sendMessage("    §l§9--§7Server Version:§b"+version);
		log.sendMessage("    §l§9--§7Check Server State......");
		if(Bukkit.getOnlinePlayers().size()!=0) {
			log.sendMessage("    §l§9--§cDo not Load Plugin while Server Running......");
			log.sendMessage("    §l§9--§cUnload Plugin......");
			this.setEnabled(false);
			return;
		}
		log.sendMessage("    §l§9--§2Done!");
		core = this;
		log.sendMessage("    §l§9--§7Instantiate Managers......");
		pmanager = PeopleManager.getInstance();
		tempdata = TemporaryData.getInstance();
		tmanager = TeleportPointManager.getInstance();
		zonemanager = ZoneManager.getInstance();
		urmanager = URManager.getInstance();
		configload = ConfigLoad.getInstance();
		log.sendMessage("    §l§9--§2Done!");
		log.sendMessage("    §l§9--§7Registering Listeners......");
		////////REGISTER LISTENER
		PluginManager plmanager = Bukkit.getPluginManager();
		plmanager.registerEvents(new JoinAndQuit(), this);
		plmanager.registerEvents(new PrepareToLaunch(), this);
		plmanager.registerEvents(new RespawnListener(), this);
		log.sendMessage("    §l§9--§2Done!");
		log.sendMessage("    §l§9--§7Registering Command......");
		////////EXCUTE COMMAND
		getCommand("ancientworld").setExecutor(new BaseCommand());
		getCommand("ancientworld").setTabCompleter(new BaseCommand());
		log.sendMessage("    §l§9--§2Done!");
		log.sendMessage("    §l§9--§7Check Language Version......");
		CheckLanguageVersion.check();
		log.sendMessage("    §l§9--§2Done!");
		log.sendMessage("    §l§9--§7Registering Runnable Task......");
		/////////RUNNABLE
		new BukkitRunnable() {
			@Override
			public void run() {
				configload.saveAllData();
			}
		}.runTaskTimerAsynchronously(this, 20l, 600l);
		new CheckZone().runTaskTimerAsynchronously(this, 20l, 20l);
		new SendTitle().runTaskTimerAsynchronously(this, 0l, 18l);
		log.sendMessage("    §l§9--§2Done!");
		log.sendMessage("    §l§9--§7Trying to HOOK Vault......");
		/////////HOOK Vault
		if(!setupEconomy()) {
			log.sendMessage("      §l§9--§5Vault UNDETECTED or Instanctiate FAIL");
		}
		log.sendMessage("    §l§9--§2Done!");
		////////ACTIVITED BSTATS
		log.sendMessage("    §l§9--§7Trying to Activited bStats......");
		Metrics metrics = new Metrics(this, 10336);
		String bstats = String.format("%-6s", metrics.isEnabled()?"Enable":"Unable");
		log.sendMessage("    §l§9--§7bStats:§c"+bstats);
	}
	@Override
	public void onDisable() {
		if(!this.isEnabled()) return;
		log.sendMessage("§6                      _            ___          __        _     _ " );
		log.sendMessage("§6     /\\              (_)          | \\ \\        / /       | |   | |");
		log.sendMessage("§6    /  \\   _ __   ___ _  ___ _ __ | |\\ \\  /\\  / /__  _ __| | __| |");
		log.sendMessage("§6   / /\\ \\ | '_ \\ / __| |/ _ \\ '_ \\| __\\ \\/  \\/ / _ \\| '__| |/ _` |");
		log.sendMessage("§6  / ____ \\| | | | (__| |  __/ | | | |_ \\  /\\  / (_) | |  | | (_| |");
		log.sendMessage("§6 /_/    \\_\\_| |_|\\___|_|\\___|_| |_|\\__| \\/  \\/ \\___/|_|  |_|\\__,_|");
		log.sendMessage("§6                                                              ");
		log.sendMessage("    §l§9--§7Saving Data......");
		configload.saveAllData();
		log.sendMessage("    §l§9--§2Done!");
		log.sendMessage("    §l§9--§7Unregister Listeners......");
		HandlerList.unregisterAll();
		log.sendMessage("    §l§9--§2Done!");
		log.sendMessage("                §l§cThanks for using                   ");
	}
	//////////Vault标准HOOK格式
	boolean setupEconomy() {
		if(Bukkit.getPluginManager().getPlugin("Vault")==null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}
	
	public static ZoneManager getZoneManager() {
		return zonemanager;
	}
	public static ConfigLoad getConfigManager() {
		return configload;
	}
	public static PeopleManager getPeopleManager() {
		return pmanager;
	}
	public static TemporaryData getTemporartData() {
		return tempdata;
	}
	public static URManager getURManager() {
		return urmanager;
	}
	public static Core getInstance() {
		return core;
	}
	public static TeleportPointManager getTeleportPointManager() {
		return tmanager;
	}
}
