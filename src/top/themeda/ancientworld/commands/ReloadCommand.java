package top.themeda.ancientworld.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import top.themeda.ancientworld.Core;
import top.themeda.ancientworld.people.People;
import top.themeda.ancientworld.util.ThemedaUtil;

public class ReloadCommand {
	public static void parseCommand(CommandSender sender,Command cmd,String[] args) {
		if(sender instanceof Player) {
			//如果是玩家尝试重载插件则检查其权限
			Player p = (Player)sender;
			People pl = Core.getPeopleManager().getPeople(p);
			if(!p.isOp()&&!p.hasPermission("ancientworld.command.reload")) {
				pl.sendMessage(ThemedaUtil.getTip("HaveNoPermission"));
				return;
			}
		}
		if(args.length!=1||!args[0].equalsIgnoreCase("reload")) {
			ThemedaUtil.sendMessageTo(sender,ThemedaUtil.listReplace(ThemedaUtil.getTip("WrongCommand")
					, "%cmd%"
					, "/"+cmd.getLabel() +" reload"));
			return;
		}
		Core.getConfigManager().reloadAll();
		ThemedaUtil.sendMessageTo(sender,ThemedaUtil.getTip("PluginReloaded"));
	}
}
