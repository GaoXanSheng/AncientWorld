package top.themeda.ancientworld.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import top.themeda.ancientworld.Core;
import top.themeda.ancientworld.people.People;
import top.themeda.ancientworld.teleport.TeleportPeople;
import top.themeda.ancientworld.teleport.TeleportPoint;
import top.themeda.ancientworld.util.ThemedaUtil;
import top.themeda.ancientworld.util.Verify;

public class TeleportCommand {
	static FileConfiguration msg = Core.getConfigManager().getLanguaneCFG();
	public static void parseCommand(CommandSender sender,Command cmd,String[] args) {
		String arg = String.join(" ", args),pointname = "";
		People p = null;
		boolean self = false;
		//                      /aw tp [player] <name> -n
		if(Pattern.matches("(tp|teleport) create [\\u4e00-\\u9fa5\\w-_]+", arg)) {
			//�������͵�
			if(!(sender instanceof Player)){
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("PlayerOnly")
						, "%cmd%"
						, "/"+cmd.getLabel() + " " +arg));
				return;
			}
			Player player = (Player)sender;
			if(!player.isOp()&&!player.hasPermission("ancientworld.command.teleport.admin")) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.getTip("HaveNoPermission"));
				return;
			}
			pointname = args[2];
			TeleportPoint tps = Core.getTeleportPointManager().getTeleportPiont(pointname);
			if(tps!=null) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.CreateFailA")
						, "%name%"
						, pointname));
				return;
			}
			Location loc = player.getLocation();
			TeleportPoint newtps = new TeleportPoint();
			newtps.setName(pointname);
			newtps.setLocation(loc);
			Core.getTeleportPointManager().addTeleportPoint(newtps);
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.CreateSuccess")
					, "%name%"
					, pointname));
		}else if(Pattern.matches("(tp|teleport) delete [\\u4e00-\\u9fa5\\w-_]+", arg)) {
			//ɾ�����͵�
			if(sender instanceof Player){
				Player player = (Player)sender;
				if(!player.isOp()&&!player.hasPermission("ancientworld.command.teleport.admin")) {
					ThemedaUtil.sendMessageTo(sender,ThemedaUtil.getTip("HaveNoPermission"));
					return;
				}
			}
			pointname = args[2];
			TeleportPoint tpp = Core.getTeleportPointManager().getTeleportPiont(pointname);
			if(tpp==null) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.LocationNotExist")
						, "%name%"
						, pointname));
				return;
			}
			Core.getTeleportPointManager().deleteTeleportPoint(pointname);
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.DeleteS")
					, "%name%"
					, pointname));
		}else if(Pattern.matches("(tp|teleport) [\\u4e00-\\u9fa5\\w-_]+ asspawn (true|false)", arg)) {
			if(sender instanceof Player){
				Player player = (Player)sender;
				if(!player.isOp()&&!player.hasPermission("ancientworld.command.teleport.admin")) {
					ThemedaUtil.sendMessageTo(sender,ThemedaUtil.getTip("HaveNoPermission"));
					return;
				}
			}
			pointname = args[1];
			TeleportPoint tpp = Core.getTeleportPointManager().getTeleportPiont(pointname);
			if(tpp==null) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.LocationNotExist")
						, "%name%"
						, pointname));
				return;
			}
			tpp.setAsSpawn(args[3].equalsIgnoreCase("true"));
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.AsSpawnState")
					, "%state%"
					, args[3]));
			return;
		}else if(Pattern.matches("(tp|teleport) [\\u4e00-\\u9fa5\\w-_]+ set item", arg)) {
			//Ϊ���͵�args[1]���ô�������Ҫ����Ʒ
			if(sender instanceof Player){
				Player player = (Player)sender;
				if(!player.isOp()&&!player.hasPermission("ancientworld.command.teleport.admin")) {
					ThemedaUtil.sendMessageTo(sender,ThemedaUtil.getTip("HaveNoPermission"));
					return;
				}
			}
			pointname = args[1];
			TeleportPoint tpp = Core.getTeleportPointManager().getTeleportPiont(pointname);
			if(tpp==null) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.LocationNotExist")
						, "%name%"
						, pointname));
				return;
			}
			if(!(sender instanceof Player)) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("PlayerOnly")
						, "%cmd%"
						, "/"+cmd.getLabel() + " " + arg));
				return;
			}
			Player pl = (Player)sender;
			ItemStack item = null;
			try {
				//��ȡ��������ϵ���Ʒ��Ϊ��������Ʒ
				item = pl.getInventory().getItemInMainHand().clone();
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.ItemSetS")
						, "%name%"
						, pointname));
			}catch(Exception e) {
				//����������û�ж���������Ϊ��
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.ItemCLearS")
						, "%name%"
						, pointname));
			}
			tpp.setConsumeItem(item);
			Core.getConfigManager().saveTeleportCFG();
		}else if(Pattern.matches("(tp|teleport) (lock|unlock) [\\u4e00-\\u9fa5\\w-_]+ [\\u4e00-\\u9fa5\\w-_]+", arg)) {
			//Ϊ���args[2]����/�رմ��͵�args[3]
			if(sender instanceof Player){
				Player player = (Player)sender;
				if(!player.isOp()&&!player.hasPermission("ancientworld.command.teleport.admin")) {
					ThemedaUtil.sendMessageTo(sender,ThemedaUtil.getTip("HaveNoPermission"));
					return;
				}
			}
			pointname = args[3];
			p = Core.getPeopleManager().getPeople(args[2]);
			if(p==null) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("PlayerNotFound")
						, "%player%"
						, args[2]));
				return;
			}
			//�Ƿ�Ϊ�Լ�����/�������͵�
			//���ڼ���Ƿ�෢��һ����Ϣ
			self = p.getPlayer().getName().equals(sender.getName())?true:false;
			if(args[1].equalsIgnoreCase("unlock")) {
				p.addTeleportPoint(pointname);
				if(!self)ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(
						ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.UnlockedPointA")
						, "%name%"
						, pointname),"%player%",args[2]));
				ThemedaUtil.sendMessageTo(Bukkit.getPlayer(p.getPlayer().getUniqueId()),ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.UnlockedPointB")
						, "%name%"
						, pointname));
			}else {
				p.removeTeleportPoint(pointname);
				if(!self)ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(
						ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.LockedPointA")
						, "%name%"
						, pointname),"%player%",args[2]));
				ThemedaUtil.sendMessageTo(Bukkit.getPlayer(p.getPlayer().getUniqueId()),ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.LockedPointB")
						, "%name%"
						, pointname));
			}
			Core.getPeopleManager().savePeopleData(p);
			//////////////////
		}else if(Pattern.matches("(tp|teleport) [\\u4e00-\\u9fa5\\w-_]+ set (on|off)", arg)) {
			//���ô��͵�args[1]�Ƿ������䴫��ģʽ
			if(sender instanceof Player){
				Player player = (Player)sender;
				if(!player.isOp()&&!player.hasPermission("ancientworld.command.teleport.admin")) {
					ThemedaUtil.sendMessageTo(sender,ThemedaUtil.getTip("HaveNoPermission"));
					return;
				}
			}
			pointname = args[1];
			TeleportPoint tpp = Core.getTeleportPointManager().getTeleportPiont(pointname);
			if(tpp==null) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.LocationNotExist")
						, "%name%"
						, pointname));
				return;
			}
			tpp.setLaunchEnable(args[3].equalsIgnoreCase("on"));
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(
					ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.PointLaunchState")
					, "%name%"
					, pointname),"%state%",args[3]));
		}else if(Pattern.matches("(tp|teleport) [\\u4e00-\\u9fa5\\w-_]+ set (open|close)", arg)) {
			//���ô��͵�args[1]�Ƿ���
			if(sender instanceof Player){
				Player player = (Player)sender;
				if(!player.isOp()&&!player.hasPermission("ancientworld.command.teleport.admin")) {
					ThemedaUtil.sendMessageTo(sender,ThemedaUtil.getTip("HaveNoPermission"));
					return;
				}
			}
			pointname = args[1];
			TeleportPoint tpp = Core.getTeleportPointManager().getTeleportPiont(pointname);
			if(tpp==null) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.LocationNotExist")
						, "%name%"
						, pointname));
				return;
			}
			tpp.setClosed(args[3].equalsIgnoreCase("close"));
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(
					ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.PointState")
					, "%name%"
					, pointname),"%state%",args[3]));
		}else if(Pattern.matches("(tp|teleport) [\\u4e00-\\u9fa5\\w-_]+ set cost (\\d+.\\d+|\\d+)", arg)) {
			//���ô��͵�args[1]�Ĵ�����Ҫ���Ķ��ٽ��
			if(sender instanceof Player){
				Player player = (Player)sender;
				if(!player.isOp()&&!player.hasPermission("ancientworld.command.teleport.admin")) {
					ThemedaUtil.sendMessageTo(sender,ThemedaUtil.getTip("HaveNoPermission"));
					return;
				}
			}
			pointname = args[1];
			TeleportPoint tpp = Core.getTeleportPointManager().getTeleportPiont(pointname);
			if(tpp==null) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.LocationNotExist")
						, "%name%"
						, pointname));
				return;
			}
			double amount = Double.valueOf(args[4]);
			tpp.setCostMoney(amount);
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(
					ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.CostState")
					, "%name%"
					, pointname),"%amount%",amount+""));
		}else if(Pattern.matches("(tp|teleport) [\\u4e00-\\u9fa5\\w-_]+ [\\u4e00-\\u9fa5\\w-_]+ -n", arg)) {
			//����ĳ�����args[1]�����͵�args[2]���������Ƿ�Ȩ�޻�����Ʒ����
			if(sender instanceof Player){
				Player player = (Player)sender;
				if(!player.isOp()&&!player.hasPermission("ancientworld.command.teleport.admin")) {
					ThemedaUtil.sendMessageTo(sender,ThemedaUtil.getTip("HaveNoPermission"));
					return;
				}
			}
			p = Core.getPeopleManager().getPeople(args[1]);
			self = p.getPlayer().getName().equals(sender.getName())?true:false;
			pointname = args[2];
			teleportWith(sender,p,pointname,args,true,self);
		}else if(Pattern.matches("(tp|teleport) [\\u4e00-\\u9fa5\\w-_]+ [\\u4e00-\\u9fa5\\w-_]+", arg)) {
			//����ĳ�����args[1]�����͵�args[2]
			if(sender instanceof Player){
				Player player = (Player)sender;
				if(!player.isOp()&&!player.hasPermission("ancientworld.command.teleport.admin")) {
					ThemedaUtil.sendMessageTo(sender,ThemedaUtil.getTip("HaveNoPermission"));
					return;
				}
			}
			p = Core.getPeopleManager().getPeople(args[1]);
			self = p.getPlayer().getName().equals(sender.getName())?true:false;
			pointname = args[2];
			teleportWith(sender,p,pointname,args,false,self);
		}else if(Pattern.matches("(tp|teleport) (help|\\?)", arg)) {
			if(sender instanceof Player){
				Player player = (Player)sender;
				if(!player.isOp()&&!player.hasPermission("ancientworld.command.teleport.list")) {
					ThemedaUtil.sendMessageTo(sender,ThemedaUtil.getTip("HaveNoPermission"));
					return;
				}
			}
			//         /aw tp help
			//         /aw tp list
			//         /aw tp create <name>
			//         /aw tp delete <name>
			//         /aw tp [player] <name> [-n]
			List<String> helpmsg = new ArrayList<>();
			helpmsg.add("/ancient tp help"+msg.getString("Teleport.Help1"));
			helpmsg.add("/ancient tp list"+msg.getString("Teleport.Help2"));
			helpmsg.add("/ancient tp create <"+msg.getString("TeleportPoint")+">"+msg.getString("Teleport.Help3"));
			helpmsg.add("/ancient tp delete <"+msg.getString("TeleportPoint")+">"+msg.getString("Teleport.Help4"));
			helpmsg.add("/ancient tp "+" ["+msg.getString("Player")+"] <"+msg.getString("TeleportPoint")+"> [-n]"+msg.getString("Teleport.Help5"));
			helpmsg.add("/ancient tp <lock/unlock> "+" <"+msg.getString("Player")+"> <"+msg.getString("TeleportPoint")+"> [-n]"+msg.getString("Teleport.Help6"));
			helpmsg.add("/ancient tp <"+msg.getString("TeleportPoint")+"> set item"+msg.getString("Teleport.Help7"));
			helpmsg.add("/ancient tp <"+msg.getString("TeleportPoint")+"> set <open/close>"+msg.getString("Teleport.Help8"));
			helpmsg.add("/ancient tp <"+msg.getString("TeleportPoint")+"> set <on/off>"+msg.getString("Teleport.Help10"));
			helpmsg.add("/ancient tp <"+msg.getString("TeleportPoint")+"> set cost <amount>"+msg.getString("Teleport.Help9"));
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.HelpList")
					, "%list%"
					, helpmsg));
			return;
		}else if(Pattern.matches("(tp|teleport) list", arg)) {
			if(!(sender instanceof Player)) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("PlayerOnly")
						, "%cmd%"
						, "/"+cmd.getName() + arg));
				return;
			}
			if(sender instanceof Player){
				Player player = (Player)sender;
				if(!player.isOp()&&!player.hasPermission("ancientworld.command.teleport.list")) {
					ThemedaUtil.sendMessageTo(sender,ThemedaUtil.getTip("HaveNoPermission"));
					return;
				}
			}
			Player pl = (Player)sender;
			List<String> lines = msg.getStringList("Teleport.List");
			for(String line : lines) {
				if(!line.contains("%list%")) {
					pl.sendMessage(line);
				}else {
					Set<TeleportPoint> tps = Core.getTeleportPointManager().getTeleportPoint();
					for(TeleportPoint z:tps) {
						p = Core.getPeopleManager().getPeople(pl);
						Location loc = z.getLocation();
						//���л����͵���Ϣ
						String details = loc.getBlockX()+","+loc.getBlockY()+","+loc.getBlockZ(),
								state = Core.getConfigManager().getConfig().getString("ShowTeleportDetial.PreviewList"),
								costmoney = z.getCostMoney()+"",
								item = "ITEM";
						//����Ƿ���Ҫ���β��ֻ���ȫ�����͵���Ϣ
						if(state.equalsIgnoreCase("NOTHING")) {
							details = "***,**,***";
							costmoney = "****";
						}else if(state.equalsIgnoreCase("EXPLORE")){
							boolean bo = p.getUnlockedTeleportPoint().contains(z.getName());
							details = bo?details:"**,**,**";
							costmoney = bo?costmoney:"****";
							item = bo?"%item%":"ITEM";
						}
						String word =msg.getString("PointName")+(z.isClosed()?"��7"+z.getName()+"��r":z.getName())
						+msg.getString("Location")+details,
						word2=msg.getString("Cost")+costmoney+msg.getString("MoneySuffix")+"  "
						+item;
						List<String> words = new ArrayList<String>();
						words.add(word2);
						pl.sendMessage(word);
						ThemedaUtil.listReplaceAndSend(pl,words,"%item%",z.getConsumeItem());
					}
				}
				
			}
			return;
		}else if(Pattern.matches("(tp|teleport) [\\u4e00-\\u9fa5\\w-_]+ -n", arg)) {
			if(sender instanceof Player){
				Player player = (Player)sender;
				if(!player.isOp()&&!player.hasPermission("ancientworld.command.teleport.admin")) {
					ThemedaUtil.sendMessageTo(sender,ThemedaUtil.getTip("HaveNoPermission"));
					return;
				}
			}
			if(!(sender instanceof Player)) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.PlayerOnly")
						, "%cmd%"
						, "/"+cmd.getLabel() + " " + args[0] + " ["+msg.getString("Player")+"] <"+msg.getString("TeleportPoint")+">"));
				return;
			}
			p = Core.getPeopleManager().getPeople((Player)sender);
			pointname = args[1];
			teleportWith(sender,p,pointname,args,true,true);
		}else if(Pattern.matches("(tp|teleport) [\\u4e00-\\u9fa5\\w-_]+", arg)) {
			if(sender instanceof Player){
				Player player = (Player)sender;
				if(!player.isOp()&&!player.hasPermission("ancientworld.command.teleport.use")) {
					ThemedaUtil.sendMessageTo(sender,ThemedaUtil.getTip("HaveNoPermission"));
					return;
				}
			}
			if(!(sender instanceof Player)) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.PlayerOnly")
						, "%cmd%"
						, "/"+cmd.getLabel() + " " + args[0] + " ["+msg.getString("Player")+"] <"+msg.getString("PointName")+">"));
				return;
			}
			p = Core.getPeopleManager().getPeople((Player)sender);
			pointname = args[1];
			teleportWith(sender,p,pointname,args,false,true);
		}else {
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("WrongCommand")
					, "%cmd%"
					, "/"+cmd.getLabel() + " " + args[0] + " ["+msg.getString("Player")+"] <"+msg.getString("PointName")+">"));
			return;
		}
	}
	/**
	 * �������봫����ҵ����͵�
	 * @param sender ������� ����ͬʱ�ǵڶ�������(��Ҫ�����͵����)
	 * @param p Ҫ�����͵����
	 * @param pointname Ҫȥ�Ĵ��͵�����
	 * @param args ָ�����
	 * @param overpass �Ƿ�ԽȨ��������
	 * @param self �������Ƿ񱻴������Լ�����
	 */
	static void teleportWith(CommandSender sender,People p, String pointname,String[] args,boolean overpass,boolean self) {
		TeleportPoint tps = Core.getTeleportPointManager().getTeleportPiont(pointname);
		if(tps==null) {
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.LocationNotExist")
					, "%name%"
					, pointname));
			return;
		}
		//�Ƿ�ԽȨ
		if(!overpass) {
			if(!p.getUnlockedTeleportPoint().contains(pointname)) {
				if(self) {
					ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.UnlockedA")
							, "%name%"
							, pointname));
				}else {
					ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.listReplace(
							ThemedaUtil.getTip("Teleport.UnlockedB")
							, "%player%"
							, p.getPlayer().getName()), "%name%", pointname));
				}
				return;
			}
		}
		//����ǰ���
		if(tps.isClosed()) {
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.Colsed")
					, "%name%"
					, args[0]));
			return;
		}
		if(!Verify.isSaveLocation(tps.getLocation())) {
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.Fail")
					, "%name%"
					, args[0]));
			return;
		}
		////////////////////////
		if(!p.getPlayer().isOnline()) {
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("PlayerNotOnline")
					, "%player%"
					, args[0]));
			return;
		}
		Player pl = Bukkit.getPlayer(p.getPlayer().getUniqueId());
		////////VERIFY COST AND CONSUME
		//��֤����
		//��Ϊ�����޷�ȷ������Ƿ���Ĵ��� ���Բ��۳������Ʒ
		if(Core.getInstance().econ!=null&&tps.getCostMoney()!=0&&!overpass) {
			if(!Core.getInstance().econ.has(p.getPlayer(), tps.getCostMoney())) {
				ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.FailA")
						, "%name%"
						, pointname),"%amount%",tps.getCostMoney()+""));
				return;
			}
		}
		if(tps.getConsumeItem()!=null&&!overpass) {
			if(!ThemedaUtil.hasEnoughItem(pl, tps.getConsumeItem())) {
				ThemedaUtil.listReplaceAndSend(pl,ThemedaUtil.listReplace(
						ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.FailB")
						,"%name%"
						,pointname)
						,"%amount%"
						,tps.getConsumeItem().getAmount()+"")
						, "%item%"
						, tps.getConsumeItem());
				return;
			}
		}
		TeleportPeople.tp(p, pointname,overpass);
	}
}
