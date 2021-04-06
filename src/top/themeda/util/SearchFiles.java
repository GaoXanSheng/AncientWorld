package top.themeda.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class SearchFiles{
	public static List<File> searchFiles(File folder, String keyword) {
		List<File> result = new ArrayList<File>();
		if (folder.isFile()) result.add(folder);
		File[] subFolders = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.isDirectory()) {
					return true;
				}
				if (file.getName().toLowerCase().contains(keyword)) {
					return true;
				}
				return false;
			}
		});
		
		if (subFolders != null) {
			for (File file : subFolders) {
				if (file.isFile()) {
					// ������ļ����ļ���ӵ�����б���
					result.add(file);
				} else {
					// ������ļ��У���ݹ���ñ�������Ȼ������е��ļ��ӵ�����б���
					result.addAll(searchFiles(file, keyword));
				}
			}
		}
		return result;
	}
}