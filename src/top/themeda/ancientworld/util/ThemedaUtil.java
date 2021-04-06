package top.themeda.ancientworld.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import top.themeda.ancientworld.Core;
import top.themeda.ancientworld.people.People;
import top.themeda.ancientworld.teleport.TeleportPoint;

public class ThemedaUtil {
	public static String errorMSG = "§6[AncientWorld] An error occurred! But it doesn't important§r";
	public static String failMSG = "§c[AncientWorld] An error occurred! It influence this plugin run normally§r";
	/**
	 * 通过名字获取离线玩家实例
	 * @param name
	 * @return
	 */
	public static OfflinePlayer getOfflinePlayerByName(String name) {
		OfflinePlayer[] ops = Bukkit.getOfflinePlayers();
		for(OfflinePlayer op : ops) {
			if(op.getName().equalsIgnoreCase(name)) return op;
		}
 		return null;
	}
	public static Advancement getAdvancementBySimpleName(String space,String name) {
		Iterator<Advancement> advancements = Bukkit.advancementIterator();
		while(advancements.hasNext()) {
			Advancement ad = advancements.next();
			if(ad.getKey().getNamespace().equalsIgnoreCase(space)
					&&ad.getKey().getKey().equalsIgnoreCase(name))
				return ad;
		}
		return null;
	}
	public static Advancement getAdvancementBySimpleName(String name) {
		Iterator<Advancement> advancements = Bukkit.advancementIterator();
		while(advancements.hasNext()) {
			Advancement ad = advancements.next();
			if(ad.getKey().getKey().equalsIgnoreCase(name))return ad;
		}
		return null;
	}
	/**
	 * 获取指定玩家已解锁的传送点
	 * @param p 玩家
	 * @return
	 */
	public static Set<TeleportPoint> getUnlockedPoint(People p){
		Set<TeleportPoint> points = new HashSet<>();
		for(TeleportPoint tp:Core.getTeleportPointManager().getTeleportPoint()) {
			if(p.getUnlockedTeleportPoint().contains(tp.getName()))points.add(tp);
		}
		return points;
	}
	/**
	 * 将list中的特定符号替换为item
	 * @param p 替换完的list发送给哪个玩家
	 * @param list 要被替换的list
	 * @param reg 替换规则
	 * @param item 替换物品
	 */
	public static void listReplaceAndSend(Player p,List<String> list,String reg,ItemStack item){
		String nbttags = "{id:\"minecraft:air\"}";
		try {
			Class<?> NBTTag = getNMSClass("NBTTagCompound"),
					craftItem = getOBCBClass("inventory.CraftItemStack");
			Object tag= NBTTag.getConstructor().newInstance();
			Method asNMSMethod = craftItem.getMethod("asNMSCopy", new Class[] {ItemStack.class});
			Class<?> nmsItemStack = asNMSMethod.getReturnType();
			Method saveMethod = nmsItemStack.getMethod("save", NBTTag);
			nbttags = saveMethod.invoke(asNMSMethod.invoke(null, new Object[] {item}), new Object[] {tag}).toString();
		}catch(Exception e) {
			Core.log.sendMessage("Serialize Item Fail - "+ThemedaUtil.errorMSG);
		}
		for(String line:list) {
			if(line.contains(reg)) {
				TextComponent tc = new TextComponent(line.substring(0,line.indexOf(reg))),
						tc2 = new TextComponent(Core.getConfigManager().getLanguaneCFG().getString("ConsumeItem")),
						tc3 = new TextComponent(line.substring(line.indexOf(reg)+reg.length()));
				tc2.setHoverEvent(new HoverEvent(Action.SHOW_ITEM,new ComponentBuilder(nbttags).create()));
				tc.addExtra(tc2);
				tc.addExtra(tc3);
				p.spigot().sendMessage(tc);
			}else {
				p.sendMessage(line);
			}
		}
	}
	/**
	 * 通过反射获取nms 用于跨版本
	 * @param name
	 * @return
	 */
	public static Class<?> getNMSClass(String name) {
		try {
			return Class.forName("net.minecraft.server."+Core.version+"."+name);
		} catch (ClassNotFoundException e) {
			Core.log.sendMessage(failMSG);
			e.printStackTrace();
			return null;
		}
	}
	public static Class<?> getOBCBClass(String name) {
		try {
			return Class.forName("org.bukkit.craftbukkit."+Core.version+"."+name);
		} catch (ClassNotFoundException e) {
			Core.log.sendMessage(failMSG);
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 替换list中的指定符号为text
	 * @param list 要被替换的list
	 * @param reg 替换规则
	 * @param text 替换内容
	 * @return
	 */
	public static List<String> listReplace(List<String> list,String reg,String text){
		List<String> newlist = new ArrayList<String>();
		for(String line:list) {
			line = line.replace(reg, text);
			newlist.add(line);
		}
		return newlist;
	}
	/**
	 * 替换list中的指定符号为另外的list中的内容
	 * @param list 要被替换的list
	 * @param reg 替换规则
	 * @param text 替换内容
	 * @return
	 */
	public static List<String> listReplace(List<String> list,String reg,List<String> text){
		List<String> newlist = new ArrayList<String>();
		for(String line:list) {
			if(line.contains(reg)) {
				for(String word:text) {
					newlist.add(word);
				}
				continue;
			}
			newlist.add(line);
		}
		return newlist;
	}
	/**
	 * 序列化location
	 * @param loc
	 * @return
	 */
	public static List<String> serliaizeLocation(Location loc){
		if(loc==null)return null;
		List<String> list = new ArrayList<>();
		list.add(loc.getWorld().getName());
		list.add(String.valueOf(loc.getBlockX()));
		list.add(String.valueOf(loc.getBlockY()));
		list.add(String.valueOf(loc.getBlockZ()));
		return list;
	}
	/**
	 * 反序列化location
	 * @param list
	 * @return
	 */
	public static Location unserliaizeLocation(List<String> list) {
		if(list.size()!=4||!(list.get(0) instanceof String)) {
			throw new RuntimeException("IllegalLocationType");
		}
		Location loc = Bukkit.getWorlds().get(0).getSpawnLocation();
		World world = Bukkit.getWorld(list.get(0));
		if(world==null)return null;
		loc.setWorld(world);
		try {
			loc.setX(Double.valueOf(list.get(1)));
			loc.setY(Double.valueOf(list.get(2)));
			loc.setZ(Double.valueOf(list.get(3)));
			return loc;
		}catch(Exception e) {
			return null;
		}
	}
	/**
	 * 向commandsender发送list的信息
	 * @param sender
	 * @param list
	 */
	public static void sendMessageTo(CommandSender sender,List<String> list) {
		for(String line:list) sender.sendMessage(line);
	}
	/**
	 * 顺带的 主要是要写向玩家发送list
	 * @param sender
	 * @param line
	 */
	public static void sendMessageTo(CommandSender sender,String line) {
		sender.sendMessage(line);
	}
	/**
	 * 获取语言文件中的节点
	 * @param key
	 * @return
	 */
	public static List<String> getTip(String key){
		List<String> words = new ArrayList<>();
		words = Core.getConfigManager().getLanguaneCFG().getStringList(key);
		if(!words.isEmpty())return words;
		words.add(Core.getConfigManager().getLanguaneCFG().getString(key));
		return words;
	}
	/**
	 * 玩家是否有足够多的物品
	 * @param p 检测哪个玩家
	 * @param items 所要的物品
	 * @return
	 */
	public static boolean hasEnoughItem(Player p,ItemStack items) {
		int amount_total=0;
		for(ItemStack item : p.getInventory()) {
			if(item==null)continue;
			if(!item.isSimilar(items)) continue;
			if(item.getAmount()<items.getAmount()) {
				amount_total += item.getAmount();
				continue;
			}
			return true;
		}
		if(amount_total>=items.getAmount()) return true;
		return false;
	}
	public static boolean takeItemsFromPlayer(Player p,ItemStack items) {
		//获取玩家背包
		PlayerInventory inv = p.getInventory();
		//计数器
		int amount = items.getAmount(),
				amount_total=amount;;
		//新建一个集合用于应对单堆物品数量不足的情况
		List<ItemStack> inv_items = new ArrayList<ItemStack>();
		for(ItemStack item :inv.getContents()) {
			if(item==null)continue;
			if(item.isSimilar(items)) {
				//处理物品数量问题 如果这一堆物品不足则记录
				if(amount_total-item.getAmount()>0) {
					amount-=item.getAmount();
					inv_items.add(item);
				}else {
					//这堆物品充足 直接扣除
					item.setAmount(item.getAmount()-amount_total);
					//完成
					return true;
				}
			}
		}
		//当记录在册的物品数量仍然不足时
		if(amount>0) return false;
		//记录的物品数量足够时
		amount=amount_total;
		for(ItemStack item:inv_items) {
			if(item.getAmount()-amount>0) {
				item.setAmount(item.getAmount()-amount);
				return true;
			}else {
				amount-=item.getAmount();
				item.setAmount(0);
			}
		}
		return true;
	}
	public static void sendWarnMessage(String msg) {
		Core.log.sendMessage(errorMSG);
		Core.log.sendMessage("§6[AncientWorld] ");
	}
	public static void sendErrorMessage(String msg) {
		Core.log.sendMessage(failMSG);
		Core.log.sendMessage("§c[AncientWorld] ");
	}
}
