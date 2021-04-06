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
					// 如果是文件则将文件添加到结果列表中
					result.add(file);
				} else {
					// 如果是文件夹，则递归调用本方法，然后把所有的文件加到结果列表中
					result.addAll(searchFiles(file, keyword));
				}
			}
		}
		return result;
	}
}