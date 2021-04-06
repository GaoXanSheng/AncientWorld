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
	public static String errorMSG = "��6[AncientWorld] An error occurred! But it doesn't important��r";
	public static String failMSG = "��c[AncientWorld] An error occurred! It influence this plugin run normally��r";
	/**
	 * ͨ�����ֻ�ȡ�������ʵ��
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
	 * ��ȡָ������ѽ����Ĵ��͵�
	 * @param p ���
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
	 * ��list�е��ض������滻Ϊitem
	 * @param p �滻���list���͸��ĸ����
	 * @param list Ҫ���滻��list
	 * @param reg �滻����
	 * @param item �滻��Ʒ
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
	 * ͨ�������ȡnms ���ڿ�汾
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
	 * �滻list�е�ָ������Ϊtext
	 * @param list Ҫ���滻��list
	 * @param reg �滻����
	 * @param text �滻����
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
	 * �滻list�е�ָ������Ϊ�����list�е�����
	 * @param list Ҫ���滻��list
	 * @param reg �滻����
	 * @param text �滻����
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
	 * ���л�location
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
	 * �����л�location
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
	 * ��commandsender����list����Ϣ
	 * @param sender
	 * @param list
	 */
	public static void sendMessageTo(CommandSender sender,List<String> list) {
		for(String line:list) sender.sendMessage(line);
	}
	/**
	 * ˳���� ��Ҫ��Ҫд����ҷ���list
	 * @param sender
	 * @param line
	 */
	public static void sendMessageTo(CommandSender sender,String line) {
		sender.sendMessage(line);
	}
	/**
	 * ��ȡ�����ļ��еĽڵ�
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
	 * ����Ƿ����㹻�����Ʒ
	 * @param p ����ĸ����
	 * @param items ��Ҫ����Ʒ
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
		//��ȡ��ұ���
		PlayerInventory inv = p.getInventory();
		//������
		int amount = items.getAmount(),
				amount_total=amount;;
		//�½�һ����������Ӧ�Ե�����Ʒ������������
		List<ItemStack> inv_items = new ArrayList<ItemStack>();
		for(ItemStack item :inv.getContents()) {
			if(item==null)continue;
			if(item.isSimilar(items)) {
				//������Ʒ�������� �����һ����Ʒ�������¼
				if(amount_total-item.getAmount()>0) {
					amount-=item.getAmount();
					inv_items.add(item);
				}else {
					//�����Ʒ���� ֱ�ӿ۳�
					item.setAmount(item.getAmount()-amount_total);
					//���
					return true;
				}
			}
		}
		//����¼�ڲ����Ʒ������Ȼ����ʱ
		if(amount>0) return false;
		//��¼����Ʒ�����㹻ʱ
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
		Core.log.sendMessage("��6[AncientWorld] ");
	}
	public static void sendErrorMessage(String msg) {
		Core.log.sendMessage(failMSG);
		Core.log.sendMessage("��c[AncientWorld] ");
	}
}
