package top.themeda.ancientworld.configuration;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import top.themeda.ancientworld.Core;
import top.themeda.ancientworld.util.ThemedaUtil;

public class CheckLanguageVersion {
	public static void check() {
		String version = Core.getConfigManager().getLanguaneCFG().getString("version");
		int count = 0;
		switch(version) {
		case"en-1":
			count+=updateToEn_2();
		case"en-2":
			count+=updateToEn_3();
		}
		if(count!=0) {
			//��������������ļ������������ļ�
			Core.getConfigManager().reloadLanguage();
			Core.log.sendMessage("      ��l��9--��7Update Language Success! Got "+count);
		}
	}
	static int updateToEn_3(){
		File file_language_en = new File(Core.getInstance().getDataFolder(),"language_en.yml"),
				file_language_zh_cn = new File(Core.getInstance().getDataFolder(),"language_zh_cn.yml");
		FileConfiguration lan_en = YamlConfiguration.loadConfiguration(file_language_en),
				lan_zh_cn = YamlConfiguration.loadConfiguration(file_language_zh_cn);
		
		
		lan_en.set("version", "en-3");
		
		

		lan_zh_cn.set("version", "en-3");
		
		
		try {
			lan_zh_cn.save(file_language_zh_cn);
			lan_en.save(file_language_en);
		} catch (IOException e) {
			Core.log.sendMessage(ThemedaUtil.failMSG);
			e.printStackTrace();
		}
		return 5;
	}
	static int updateToEn_2() {
		File file_language_en = new File(Core.getInstance().getDataFolder(),"language_en.yml"),
				file_language_zh_cn = new File(Core.getInstance().getDataFolder(),"language_zh_cn.yml");
		FileConfiguration lan_en = YamlConfiguration.loadConfiguration(file_language_en),
				lan_zh_cn = YamlConfiguration.loadConfiguration(file_language_zh_cn);
		//for english
		lan_en.set("version", "en-2");
		lan_en.set("Teleport.PointLaunchState", "Launch State of Point %name% set as %state% !");
		lan_en.set("Teleport.PrepareToLaunch", "Press and Release SHIFT to Launch");
		lan_en.set("Teleport.FailToLaunch", "Launch Fail cause some BLOCK is above");
		lan_en.set("Teleport.Help10", "   -set launch mode on or off");
		//for chinese
		lan_zh_cn.set("version", "en-2");
		lan_zh_cn.set("Teleport.PointLaunchState", "���͵� %name% �ĵ���״̬������Ϊ %state% ��   ");
		lan_zh_cn.set("Teleport.PrepareToLaunch", "���²��ɿ�SHIFT����ʼ����");
		lan_zh_cn.set("Teleport.FailToLaunch", "ͷ�����ڵ����޷�����");
		lan_zh_cn.set("Teleport.Help10", "   -���ô��͵㵯��ģʽ<on/off>");
		try {
			lan_zh_cn.save(file_language_zh_cn);
			lan_en.save(file_language_en);
		} catch (IOException e) {
			Core.log.sendMessage(ThemedaUtil.failMSG);
			e.printStackTrace();
		}
		return 4;
	}
}
