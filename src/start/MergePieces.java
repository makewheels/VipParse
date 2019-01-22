package start;

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
		String filename = Config.FILENAME;
		int totalPieces = new File(folderPath).list().length;
		// 文件碎片集合
		String[] filePiecesList = new String[totalPieces];
		for (int i = 1; i <= totalPieces; i++) {
			filePiecesList[i - 1] = folderPath + File.separator + filename + "_" + i + ".ts";
		}
		// 合并
		FileUtil.mergeFiles(folderPath + File.separator + filename + ".ts", filePiecesList);
		// 删除所有碎片
		for (String filepath : filePiecesList) {
			new File(filepath).delete();
		}
	}

}
