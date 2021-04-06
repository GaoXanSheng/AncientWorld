package top.themeda.ancientworld.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import top.themeda.ancientworld.Core;
import top.themeda.ancientworld.teleport.TeleportPoint;
import top.themeda.ancientworld.teleport.UnlockRequire;
import top.themeda.ancientworld.teleport.Zone;
import top.themeda.ancientworld.util.Verify;

public class BaseCommand implements TabExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length==0) return false;
		switch(args[0]) {
		case"reload":
			ReloadCommand.parseCommand(sender, cmd, args);
			return true;
		case"tp":
		case"teleport":
			TeleportCommand.parseCommand(sender, cmd, args);
			return true;
		case"zone":
			ZoneCommand.parseCommand(sender, cmd, args);
			return true;
		case"ur":
		case"unlockrequire":
			URCommand.parseCommand(sender, cmd, args);
			return true;
		}
		return false;
	}
	@Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> list = new ArrayList<>();
		if(args.length==1) {
			list.add("teleport");
			list.add("zone");
			list.add("unlockrequire");
			if(sender.isOp())list.add("reload");
		}
		if(args.length==2) {
			switch(args[0]) {
			case"reload":
				break;
			case"tp":
			case"teleport":
				if(sender instanceof Player) {
					for(TeleportPoint tpp:Core.getTeleportPointManager().getTeleportPoint()) {
						if(sender.hasPermission("ancientworld.command.teleport.admin")||sender.isOp()||Verify.isPlayerUnlockThis((Player)sender, tpp.getName())) {
							list.add(tpp.getName());
						}
					}
				}
				if(sender.hasPermission("ancientworld.command.teleport.admin")||sender.isOp()) {
					list.add("create");
					list.add("delete");
					list.add("lock");
					list.add("unlock");
					for(Player p:Bukkit.getOnlinePlayers()) {
						list.add(p.getName());
					}
				}
				if(sender.hasPermission("ancientworld.command.teleport.list")||sender.isOp()) {
					list.add("help");
					list.add("list");
				}
				break;
			case"ur":
			case"unlockrequire":
				if(sender.hasPermission("ancientworld.command.unlockrequire")||sender.isOp()) {
					list.add("help");
					list.add("create");
					list.add("list");
					list.add("delete");
					for(UnlockRequire ur:Core.getURManager().getUnlockRequires()) {
						list.add(ur.getUnlockRequireName());
					}
				}
				break;
			case"zone":
				if(sender.hasPermission("ancientworld.command.unlockrequire")||sender.isOp()) {
					list.add("help");
					list.add("create");
					list.add("list");
					list.add("delete");
					for(Zone zone:Core.getZoneManager().getZones()) {
						list.add(zone.getZoneName());
					}
				}
				break;
			}
		}
		return list;
    }
}
