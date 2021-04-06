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
	 * 传送玩家
	 * @param p 要传送的玩家
	 * @param tpPointName 要去的传送点名称
	 * @param overpass 是否越权且无消耗
	 */
	public static void tp(People p,String tpPointName,boolean overpass) {
		if(!p.getPlayer().isOnline())return;
		Player player = Bukkit.getPlayer(p.getPlayer().getUniqueId());
		tp(player,tpPointName,overpass);
	}
	/**
	 * 传送玩家
	 * @param player 要传送的玩家
	 * @param tpPointName 要去的传送点名称
	 * @param overpass 是否越权且无消耗
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
	 * 进行弹射传送
	 * @param p 要进行弹射传送的玩家
	 * @param tpPointName 要去的传送点名称
	 * @param overpass 是否越权且无消耗
	 */
	public static void launch(Player p,String tpPointName,boolean overpass) {
		TeleportPoint tpP = Core.getTeleportPointManager().getTeleportPiont(tpPointName);
		if(tpP==null)return;
		launch(p,tpP,overpass);
	}
	/**
	 * 进行弹射传送
	 * @param p 要进行弹射传送的玩家
	 * @param tpP 要去的传送点
	 * @param overpass 是否越权且无消耗
	 */
	public static void launch(Player p,TeleportPoint tpP,boolean overpass) {
		//check again
		//再次检查玩家是否物品充足并及时扣除
		//以防玩家在自我准备弹射的过程中转移所需消耗
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
		
		
		//新建一个向量 用来弹射
		Vector vec_=new Vector();
		//因为涉及到延时 所以用异步
		new BukkitRunnable(){
			@Override
			public void run() {
				Location loc = p.getLocation();
				int time = tpP.getLengthOfTime(),
						high = tpP.getLaunchHeight();
				//设置弹射高度
				vec_.setY(high);
				//低头
				loc.setPitch(90f);
				p.teleport(loc);
				//开始弹射 在地面生成粒子
				p.spawnParticle(tpP.getGroundParticle(), loc, 18);
				p.setVelocity(vec_);
				//开始生成弹射后的跟随粒子
				for(int i=0;i<time;i++) {
					p.spawnParticle(tpP.getFollowParticle(), p.getLocation(), 9);
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						Core.log.sendMessage(ThemedaUtil.failMSG);
						e.printStackTrace();
					}
				}
				//正式传送并通知玩家
				p.teleport(tpP.getLocation());
				ThemedaUtil.sendMessageTo((CommandSender)p,ThemedaUtil.listReplace(ThemedaUtil.getTip("Teleport.Success")
						, "%name%"
						, tpP.getName()));
			}
		}.runTaskAsynchronously(Core.getInstance());
	}
}
