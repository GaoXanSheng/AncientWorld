package top.themeda.ancientworld.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import top.themeda.ancientworld.Core;
import top.themeda.ancientworld.people.People;
import top.themeda.ancientworld.teleport.UnlockRequire;
import top.themeda.ancientworld.util.ThemedaUtil;
import top.themeda.ancientworld.util.Verify;

public class URCommand {
	public static void parseCommand(CommandSender sender,Command cmd,String[] args) {
		FileConfiguration msg = Core.getConfigManager().getLanguaneCFG();
		if(sender instanceof Player) {
			//如果是玩家尝试重载插件则检查其权限
			Player p = (Player)sender;
			People pl = Core.getPeopleManager().getPeople(p);
			if(!p.isOp()&&!p.hasPermission("ancientworld.command.reload")) {
				pl.sendMessage(ThemedaUtil.getTip("HaveNoPermission"));
				return;
			}
		}
		if(args.length==1) {
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("WrongCommand")
					, "%cmd%"
					, "/"+cmd.getLabel() +" unlockrequire/ur help"));
			return;
		}
		String arg = String.join(" ", args);
		if(arg.matches("(unlockrequest|ur) (help|\\?)")) {
			//send message
			if(sender instanceof Player){
				Player player = (Player)sender;
				if(!player.isOp()&&!player.hasPermission("ancientworld.command.teleport.list")) {
					ThemedaUtil.sendMessageTo(sender,ThemedaUtil.getTip("HaveNoPermission"));
					return;
				}
			}
 			List<String> helpmsg = new ArrayList<>();
			helpmsg.add("/ancient ur help"+msg.getString("UnlockRequire.Help1"));
			helpmsg.add("/ancient ur list"+msg.getString("UnlockRequire.Help2"));
			helpmsg.add("/ancient ur create <"+msg.getString("UnlockRequire_word")+">"+msg.getString("UnlockRequire.Help3"));
			helpmsg.add("/ancient ur delete <"+msg.getString("UnlockRequire_word")+">"+msg.getString("UnlockRequire.Help4"));
			helpmsg.add("/ancient ur <"+msg.getString("UnlockRequire_word")+"> detail"+msg.getString("UnlockRequire.Help5"));
			helpmsg.add("/ancient ur <"+msg.getString("UnlockRequire_word")+"> setpoint <"+msg.getString("TeleportPoint")+">"+msg.getString("UnlockRequire.Help6"));
			helpmsg.add("/ancient ur <"+msg.getString("UnlockRequire_word")+"> addp <"+msg.getString("Permission_word")+">"+msg.getString("UnlockRequire.Help7"));
			helpmsg.add("/ancient ur <"+msg.getString("UnlockRequire_word")+"> addad <"+msg.getString("Advancement_word")+">"+msg.getString("UnlockRequire.Help8"));
			helpmsg.add("/ancient ur <"+msg.getString("UnlockRequire_word")+"> removep <"+msg.getString("Permission_word")+">"+msg.getString("UnlockRequire.Help9"));
			helpmsg.add("/ancient ur <"+msg.getString("UnlockRequire_word")+"> removead <"+msg.getString("Advancement_word")+">"+msg.getString("UnlockRequire.Help10"));
			helpmsg.add("/ancient ur <"+msg.getString("UnlockRequire_word")+"> clearp"+msg.getString("UnlockRequire.Help11"));
			helpmsg.add("/ancient ur <"+msg.getString("UnlockRequire_word")+"> clearad"+msg.getString("UnlockRequire.Help12"));
			helpmsg.add("/ancient ur <"+msg.getString("UnlockRequire_word")+"> needallp <TRUE/FALSE>"+msg.getString("UnlockRequire.Help13"));
			helpmsg.add("/ancient ur <"+msg.getString("UnlockRequire_word")+"> needallad <TRUE/FALSE>"+msg.getString("UnlockRequire.Help14"));
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("UnlockRequire.HelpList")
					, "%list%"
					, helpmsg));
			return;
		}else if(arg.matches("(unlockrequest|ur) list")) {
			List<String> lines = msg.getStringList("Teleport.List");
			for(String line : lines) {
				if(!line.contains("%list%")) {
					sender.sendMessage(line);
				}else {
					List<String> list = new ArrayList<>();
					for(UnlockRequire ur:Core.getURManager().getUnlockRequires()) {
						list.add(ur.getUnlockRequireName());
					}
					ThemedaUtil.sendMessageTo(sender,list);
				}
			}
			return;
		}else if(arg.matches("(unlockrequest|ur) create [\\u4e00-\\u9fa5\\w-_]+")) {
			UnlockRequire ur = Core.getURManager().getUnlockRequire(args[2]);
			if(ur!=null) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("UnlockRequire.CreateF")
						, "%name%"
						, args[1]));
				return;
			}
			ur=new UnlockRequire(args[2]);
			Core.getURManager().addUnlockRequire(ur);
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("UnlockRequire.CreateS")
					, "%name%"
					, args[1]));
			return;
		}else if(arg.matches("(unlockrequest|ur) delete [\\u4e00-\\u9fa5\\w-_]+")) {
			UnlockRequire ur = Core.getURManager().getUnlockRequire(args[2]);
			if(ur==null) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("UnlockRequire.NotFound")
						, "%name%"
						, args[1]));
				return;
			}
			Core.getURManager().removeUnlockRequire(ur);
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("UnlockRequire.DeleteS")
					, "%name%"
					, args[1]));
			return;
		}
		UnlockRequire ur = Core.getURManager().getUnlockRequire(args[1]);
		if(ur==null) {
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("UnlockRequire.NotFound")
					, "%name%"
					, args[1]));
			return;
		}
		if(arg.matches("(unlockrequest|ur) [\\u4e00-\\u9fa5\\w-_]+ setpoint [\\u4e00-\\u9fa5\\w-_]+")) {
			ur.setPointName(args[3]);
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("UnlockRequire.SetPointS")
					, "%name%"
					, args[3]));
		}else if(arg.matches("(unlockrequest|ur) [\\u4e00-\\u9fa5\\w-_]+ (addpermission|addp) [\\u4e00-\\u9fa5\\w-_]+")) {
			ur.addPermission(args[3]);
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("UnlockRequire.AddPerS")
					, "%arg%"
					, args[3]));
		}else if(arg.matches("(unlockrequest|ur) [\\u4e00-\\u9fa5\\w-_]+ (addadvancement|addad) [\\u4e00-\\u9fa5\\w-_]+")) {
			ur.addAdvancement(args[3]);
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("UnlockRequire.AddAdvS")
					, "%arg%"
					, args[3]));
		}else if(arg.matches("(unlockrequest|ur) [\\u4e00-\\u9fa5\\w-_]+ (removepermission|removep) [\\u4e00-\\u9fa5\\w-_]+")) {
			ur.removePermission(args[3]);
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("UnlockRequire.RemovePers")
					, "%arg%"
					, args[3]));
		}else if(arg.matches("(unlockrequest|ur) [\\u4e00-\\u9fa5\\w-_]+ (removeadvancement|removead) [\\u4e00-\\u9fa5\\w-_]+")) {
			ur.removeAdvancements(args[3]);
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("UnlockRequire.RemoveAdvs")
					, "%arg%"
					, args[3]));
		}else if(arg.matches("(unlockrequest|ur) [\\u4e00-\\u9fa5\\w-_]+ (detail)")) {
			if(!(sender instanceof Player)) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("PlayerOnly")
						, "%cmd%"
						, "/"+cmd.getName() + arg));
				return;
			}
			Player p = (Player)sender;
			if(!p.isOp()&&!p.hasPermission("ancientworld.command.unlockrequery.detail")) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.getTip("HaveNoPermission"));
				return;
			}
			List<String> lines = msg.getStringList("UnlockRequire.Detail");
			for(String line : lines) {
				if(!line.contains("%perms%")&&!line.contains("%advs%")) {
					line=line.replace("%name%", ur.getUnlockRequireName());
					line=line.replace("%pointname%", ur.getPointName());
					line=line.replace("%allperm%", ur.isNeedAllPermission()?"§a"+msg.getString("True"):"§c"+msg.getString("False"));
					line=line.replace("%alladv%", ur.isNeedAllAdvancements()?"§a"+msg.getString("True"):"§c"+msg.getString("False"));
					sender.sendMessage(line);
				}else {
					Set<String> perms = ur.getPermissions(),
							advs = ur.getAdvancements();
					if(line.contains("perms")) {
						List<String> perms_c = new ArrayList<>();
						for(String perm:perms) {
							if(p.hasPermission(perm)) {
								perms_c.add("§a"+perm);
							}else {
								perms_c.add("§c"+perm);
							}
						}
						ThemedaUtil.sendMessageTo(p, perms_c);
					}else {
						List<String> advs_c = new ArrayList<>();
						for(String adv:advs) {
							if(Verify.isAdvancementDone(p, adv)) {
								advs_c.add("§a"+adv);
							}else {
								advs_c.add("§c"+adv);
							}
						}
						ThemedaUtil.sendMessageTo(p, advs_c);
					}
				}
			}
		}else if(arg.matches("(unlockrequest|ur) [\\u4e00-\\u9fa5\\w-_]+ (clearpermission|clearp)")) {
			ur.clearPermission();
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.getTip("UnlockRequire.ClearPerS"));
		}else if(arg.matches("(unlockrequest|ur) [\\u4e00-\\u9fa5\\w-_]+ (clearadvancement|clearad)")) {
			ur.clearAdvancements();
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.getTip("UnlockRequire.ClearAdvS"));
		}else if(arg.matches("(unlockrequest|ur) [\\u4e00-\\u9fa5\\w-_]+ (needallpermission|needallp) (true|false)")) {
			ur.setNeedAllPermissions(args[3].equalsIgnoreCase("true"));
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("UnlockRequire.NeedAllP")
					, "%state%"
					, args[3]));
		}else if(arg.matches("(unlockrequest|ur) [\\u4e00-\\u9fa5\\w-_]+ (needalladvancement|needallad) (true|false)")) {
			ur.setNeedAllAdvancements(args[3].equalsIgnoreCase("true"));
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("UnlockRequire.NeedALLA")
					, "%state%"
					, args[3]));
		}
	}
}
