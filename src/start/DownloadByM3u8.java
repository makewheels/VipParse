package start;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import util.DownloadRunnable;
import util.FileUtil;

public class DownloadByM3u8 {

	public static void main(String[] args) throws Exception {
		String savePath = Config.FOLDER_PATH;
		String filename = Config.FILENAME;
		String baseUrl = Config.BASE_URL;
		// 解析流文件
		BufferedReader bufferedReader = new BufferedReader(
				new FileReader(DownloadByM3u8.class.getResource("/m3u8.txt").getPath()));
		List<String> m3u8List = new ArrayList<>();
		String str;
		while ((str = bufferedReader.readLine()) != null) {
			if (str.startsWith("#") == false) {
				m3u8List.add(str);
			}
		}
		bufferedReader.close();
		int totalPieces = m3u8List.size();
		// 下载碎片
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		for (int i = 1; i <= totalPieces; i++) {
			FileUtil.download(baseUrl + m3u8List.get(i - 1), savePath, filename + "_" + i + ".ts");
//			System.out.println(i);
			executorService
					.execute(new DownloadRunnable(baseUrl + m3u8List.get(i - 1), savePath, filename + "_" + i + ".ts"));
		}
		executorService.shutdown();
	}

}
