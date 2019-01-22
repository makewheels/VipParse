package mango;

import java.io.File;

import util.FileUtil;

/**
 * 合并碎片
 * 
 * @author Administrator
 *
 */
public class MergePieces {

	public static void main(String[] args) {
		String folderPath = Config.FOLDER_PATH;
		File[] folders = new File(folderPath).listFiles();
		for (File folder : folders) {
			String filename = folder.getName();
			int size = folder.list().length;
			String[] list = new String[size];
			for (int i = 0; i < list.length; i++) {
				list[i] = folderPath + File.separator + filename + File.separator + filename + "_" + (i + 1) + ".ts";
			}
			// 合并
			FileUtil.mergeFiles(folderPath + File.separator + "out" + File.separator + filename + ".ts", list);
		}
		System.out.println("done");
	}

}
