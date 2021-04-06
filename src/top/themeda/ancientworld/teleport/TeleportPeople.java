package top.themeda.ancientworld.teleport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.milkbowl.vault.economy.EconomyResponse;
import top.themeda.ancientworld.Core;
import top.themeda.ancientworld.people.People;
import top.themeda.ancientworld.util.ThemedaUtil;

public class TeleportPeople {
	/**
	 * �������
	 * @param p Ҫ���͵����
	 * @param tpPointName Ҫȥ�Ĵ��͵�����
	 * @param overpass �Ƿ�ԽȨ��������
	 */
	public static void tp(People p,String tpPointName,boolean overpass) {
		if(!p.getPlayer().isOnline())return;
		Player player = Bukkit.getPlayer(p.getPlayer().getUniqueId());
		tp(player,tpPointName,overpass);
	}
	/**
	 * �������
	 * @param player Ҫ���͵����
	 * @param tpPointName Ҫȥ�Ĵ��͵�����
	 * @param overpass �Ƿ�ԽȨ��������
	 */
	public static void tp(Player player,String tpPointName,boolean overpass) {
		TeleportPoint tpP = Core.getTeleportPointManager().getTeleportPiont(tpPointName);
		Location loc = tpP.getLocation();
		if(loc==null)return;
		if(tpP.isLaunch()) {
			if(!tpP.launchImmediately()) {
				Core.getTemporartData().addTeleportQueue(player.getUniqueId(), tpPointName);
				return;
			}
			if(loc.getBlockY()<loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ())) {
				ThemedaUtil.sendMessageTo(player, ThemedaUtil.getTip("Teleport.FailToLaunch"));
				return;
			}
			launch(player,tpP,overpass);
			return;
		}
		if(Core.getInstance().econ!=null&&tpP.getCostMoney()!=0&&!overpass) {
			EconomyResponse r = Core.getInstance().econ.withdrawPlayer(player, tpP.getCostMoney());
			if(!r.transactionSuccess()) {
				ThemedaUtil.sendMessageTo(player,ThemedaUtil.listReplace(ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.FailA")
						, "%name%"
						, tpP.getName()),"%amount%",tpP.getCostMoney()+""));
				return;
			}
		}
		if(tpP.getConsumeItem()!=null&&!overpass) {
			if(!ThemedaUtil.takeItemsFromPlayer(player, tpP.getConsumeItem())) {
				ThemedaUtil.listReplaceAndSend(player,ThemedaUtil.listReplace(
						ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.FailB")
						,"%name%"
						,tpP.getName())
						,"%amount%"
						,tpP.getConsumeItem().getAmount()+"")
						, "%item%"
						, tpP.getConsumeItem());
				return;
			}
		}
		player.teleport(loc);
		
	}
	/**
	 * ���е��䴫��
	 * @param p Ҫ���е��䴫�͵����
	 * @param tpPointName Ҫȥ�Ĵ��͵�����
	 * @param overpass �Ƿ�ԽȨ��������
	 */
	public static void launch(Player p,String tpPointName,boolean overpass) {
		TeleportPoint tpP = Core.getTeleportPointManager().getTeleportPiont(tpPointName);
		if(tpP==null)return;
		launch(p,tpP,overpass);
	}
	/**
	 * ���е��䴫��
	 * @param p Ҫ���е��䴫�͵����
	 * @param tpP Ҫȥ�Ĵ��͵�
	 * @param overpass �Ƿ�ԽȨ��������
	 */
	public static void launch(Player p,TeleportPoint tpP,boolean overpass) {
		//check again
		//�ٴμ������Ƿ���Ʒ���㲢��ʱ�۳�
		//�Է����������׼������Ĺ�����ת����������
		if(Core.getInstance().econ!=null&&tpP.getCostMoney()!=0&&!overpass) {
			EconomyResponse r = Core.getInstance().econ.withdrawPlayer(p, tpP.getCostMoney());
			if(!r.transactionSuccess()) {
				ThemedaUtil.sendMessageTo(p,ThemedaUtil.listReplace(ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.FailA")
						, "%name%"
						, tpP.getName()),"%amount%",tpP.getCostMoney()+""));
				return;
			}
		}
		if(tpP.getConsumeItem()!=null&&!overpass) {
			if(!ThemedaUtil.takeItemsFromPlayer(p, tpP.getConsumeItem())) {
				ThemedaUtil.listReplaceAndSend(p,ThemedaUtil.listReplace(
						ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.FailB")
						,"%name%"
						,tpP.getName())
						,"%amount%"
						,tpP.getConsumeItem().getAmount()+"")
						, "%item%"
						, tpP.getConsumeItem());
				return;
			}
		}
		
		
		//�½�һ������ ��������
		Vector vec_=new Vector();
		//��Ϊ�漰����ʱ �������첽
		new BukkitRunnable(){
			@Override
			public void run() {
				Location loc = p.getLocation();
				int time = tpP.getLengthOfTime(),
						high = tpP.getLaunchHeight();
				//���õ���߶�
				vec_.setY(high);
				//��ͷ
				loc.setPitch(90f);
				p.teleport(loc);
				//��ʼ���� �ڵ�����������
				p.spawnParticle(tpP.getGroundParticle(), loc, 18);
				p.setVelocity(vec_);
				//��ʼ���ɵ����ĸ�������
				for(int i=0;i<time;i++) {
					p.spawnParticle(tpP.getFollowParticle(), p.getLocation(), 9);
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						Core.log.sendMessage(ThemedaUtil.failMSG);
						e.printStackTrace();
					}
				}
				//��ʽ���Ͳ�֪ͨ���
				p.teleport(tpP.getLocation());
				ThemedaUtil.sendMessageTo((CommandSender)p,ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.Success")
						, "%name%"
						, tpP.getName()));
			}
		}.runTaskAsynchronously(Core.getInstance());
	}
}
