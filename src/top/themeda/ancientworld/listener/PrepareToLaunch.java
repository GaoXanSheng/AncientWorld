package top.themeda.ancientworld.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import top.themeda.ancientworld.Core;
import top.themeda.ancientworld.TemporaryData;
import top.themeda.ancientworld.teleport.TeleportPeople;
import top.themeda.ancientworld.teleport.TeleportQueue;
import top.themeda.ancientworld.util.ThemedaUtil;

public class PrepareToLaunch implements Listener{
	@EventHandler
	public void playerReleaseSHIFT(PlayerToggleSneakEvent e) {
		//当玩家切换潜行时尝试传送
		TemporaryData data = Core.getTemporartData();
		Player p = e.getPlayer();
		//查看玩家是否在传送等待队列中
		if(!data.isInTeleportQueue(p.getUniqueId()))return;
		//如果玩家是进入潜行状态则直接返回
		if(e.isSneaking()) return;
		//获取玩家队列信息
		TeleportQueue tpQ = data.getTeleportPoint(p.getUniqueId());
		//获取后及时删除队列信息
		data.deleteTeleportQueue(p.getUniqueId());
		String name = tpQ.getPointName();
		Location loc = p.getLocation();
		p.resetTitle();
		//判断玩家头顶是否有方块阻挡弹射
		if(loc.getBlockY()<loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ())) {
			ThemedaUtil.sendMessageTo(p, ThemedaUtil.getTip("Teleport.FailToLaunch"));
			return;
		}
		//尝试弹射玩家
		TeleportPeople.launch(p, name,tpQ.isOverpass());
	}
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		//当玩家死亡时及时将玩家从队列中删除
		TemporaryData data = Core.getTemporartData();
		Player p = e.getEntity();
		if(data.isInTeleportQueue(p.getUniqueId()))data.deleteTeleportQueue(p.getUniqueId());
	}
}
