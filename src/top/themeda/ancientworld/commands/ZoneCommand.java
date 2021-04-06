package top.themeda.ancientworld.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import top.themeda.ancientworld.Core;
import top.themeda.ancientworld.people.People;
import top.themeda.ancientworld.teleport.UnlockRequire;
import top.themeda.ancientworld.teleport.Zone;
import top.themeda.ancientworld.util.ThemedaUtil;

public class ZoneCommand{
	public static void parseCommand(CommandSender sender,Command cmd,String[] args) {
		FileConfiguration msg = Core.getConfigManager().getLanguaneCFG();
		//           /aw zone create <name>
		//           /aw zone list
		//           /aw zone <name> <loc1/loc2>
		//           /aw zone <name> list
		//           /aw zone <name> add <name>
		//           /aw zone <name> remove <name>
		//           /aw zone delete <name>
		String arg = String.join(" ", args),zonename = "";
		if(sender instanceof Player){
			Player player = (Player)sender;
			People p = Core.getPeopleManager().getPeople(player);
			if(!player.isOp()&&!player.hasPermission("ancientworld.command.zone")) {
				p.sendMessage(ThemedaUtil.getTip("HaveNoPermission"));
				return;
			}
		}
		if(Pattern.matches("zone create [\\u4e00-\\u9fa5\\w-_]+", arg)) {
			//创建一个新的区域
			zonename=args[2];
			Zone zone = Core.getZoneManager().getZone(zonename);
			if(zone!=null) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Zone.AlreadyExist")
						, "%name%"
						, zonename));
				return;
			}
			zone = new Zone();
			zone.setZoneName(zonename);
			Core.getZoneManager().addZone(zone);
			Core.getConfigManager().saveZoneData();
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Zone.CreateSuccess")
					, "%name%"
					, zonename));
			return;
		}else if(Pattern.matches("zone list", arg)) {
			//查看所有区域列表
			List<String> lines = msg.getStringList("Zone.ZoneList");
			for(String line : lines) {
				if(!line.contains("%list%")) {
					sender.sendMessage(line);
				}else {
					Set<Zone> zones = Core.getZoneManager().getZones();
					for(Zone z:zones) {
						Location loc1 = z.getLoc1(),loc = z.getLoc2();
						String details = loc.getBlockX()+","+loc.getBlockY()+","+loc.getBlockZ()+" | "
								+loc1.getBlockX()+","+loc1.getBlockY()+","+loc1.getBlockZ();
						String word =msg.getString("ZoneName")+z.getZoneName()
						+msg.getString("Location")+details,
						word2=msg.getString("UnlockPointList")+String.join(",", z.getUnlockName());
						sender.sendMessage(word);
						sender.sendMessage(word2);
					}
				}
				
			}
			return;
		}else if(Pattern.matches("zone [\\u4e00-\\u9fa5\\w-_]+ (loc1|loc2)", arg)) {
			//设置区域的两个坐标
			//这一步是必须的 否则该区域不成立
			zonename=args[1];
			Zone zone = Core.getZoneManager().getZone(zonename);
			if(zone==null) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Zone.NotExist")
						, "%name%"
						, zonename));
				return;
			}
			if(!(sender instanceof Player)) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("PlayerOnly")
						, "%cmd%"
						, "/"+cmd.getLabel() + " " +arg));
				return;
			}
			Player p = (Player)sender;
			Location loc = p.getLocation(); 
			if(args[2].contains("1")) {
				zone.setLoc1(loc);
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(
						ThemedaUtil.listReplace(ThemedaUtil.getTip("Zone.LocationS")
						, "%name%"
						, zonename),"%arg%", "loc1"));
			}else {
				zone.setLoc2(loc);
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(
						ThemedaUtil.listReplace(ThemedaUtil.getTip("Zone.LocationS")
						, "%name%"
						, zonename),"%arg%","loc2"));
			}
			Core.getConfigManager().saveZoneData();
			return;
		}else if(Pattern.matches("zone [\\u4e00-\\u9fa5\\w-_]+ add [\\u4e00-\\u9fa5\\w-_]+", arg)) {
			//添加某个可被解锁的点到该区域
			//添加后玩家可以通过到达该区域解锁该点
			zonename=args[1];
			Zone zone = Core.getZoneManager().getZone(zonename);
			if(zone==null) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Zone.NotExist")
						, "%name%"
						, zonename));
				return;
			}
			UnlockRequire ur = Core.getURManager().getUnlockRequire(args[3]);
			if(ur==null) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("UnlockRequest.NotFound")
						, "%name%"
						, zonename));
				return;
			}
			zone.addUnlockRequire(ur);
			Core.getConfigManager().saveZoneData();
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(
					ThemedaUtil.listReplace(ThemedaUtil.getTip("Zone.AddPointS")
					, "%zone%"
					, zonename),"%point%",args[3]));
			return;
		}else if(Pattern.matches("zone [\\u4e00-\\u9fa5\\w-_]+ remove [\\u4e00-\\u9fa5\\w-_]+", arg)) {
			//将某个可被解锁的点从该区域移除
			//移除后玩家不可以通过到达该区域解锁该点
			zonename=args[1];
			Zone zone = Core.getZoneManager().getZone(zonename);
			if(zone==null) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Zone.NotExist")
						, "%name%"
						, zonename));
				return;
			}
			zone.removeUnlockRequire(args[3]);
			Core.getConfigManager().saveZoneData();
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(
					ThemedaUtil.listReplace(ThemedaUtil.getTip("Zone.RemovePointS")
					, "%zone%"
					, zonename),"%point%",args[3]));
			return;
		}else if(Pattern.matches("zone delete [\\u4e00-\\u9fa5\\w-_]+", arg)) {
			//删除某个区域
			zonename=args[2];
			Zone zone = Core.getZoneManager().getZone(zonename);
			if(zone==null) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Zone.NotExist")
						, "%name%"
						, zonename));
				return;
			}
			Core.getZoneManager().removeZone(zone);
			Core.getConfigManager().saveZoneData();
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Zone.DeleteZoneS")
					, "%name%"
					, zonename));
			return;
		}else if(Pattern.matches("zone (help|\\?)", arg)) {
			//帮助列表  以下为指令
			//           /aw zone create <name>
			//           /aw zone list
			//           /aw zone <name> list
			//           /aw zone <name> add <name>
			//           /aw zone <name> remove <name>
			//           /aw zone delete <name>
			List<String> helpmsg = new ArrayList<>();
			helpmsg.add("/ancient zone list"+msg.getString("Zone.Help1"));
			helpmsg.add("/ancient zone create <name>"+msg.getString("Zone.Help2"));
			helpmsg.add("/ancient zone <name> list"+msg.getString("Zone.Help3"));
			helpmsg.add("/ancient zone <name> add <point>"+msg.getString("Zone.Help4"));
			helpmsg.add("/ancient zone <name> remove <point>"+msg.getString("Zone.Help5"));
			helpmsg.add("/ancient zone delete <name>"+msg.getString("Zone.Help6"));
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Zone.HelpList")
					, "%list%"
					, helpmsg));
			return;
		}else {
			//如果都不符合就让他去看帮助列表
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("WrongCommand")
					, "%cmd%"
					, "/"+cmd.getLabel() +" zone help"));
			return;
		}
	}
}
