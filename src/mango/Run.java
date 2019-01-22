package mango;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSON;

import util.DownloadRunnable;
import util.WebUtil;

public class Run {

	public static void main(String[] args) throws UnsupportedEncodingException {
		// 开始和结束
		int start = 31;
		int end = 40;
		// 拿到源url列表
		List<String> mangoUrlList = JSON
				.parseArray(WebUtil.sendGet("http://v.sigu.me/souce/arr.php?bing=0&play=tv/detail/54865.html"))
				.toJavaList(String.class);
		// 线程池
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		// 逐个下载列表所有视频
		for (int i = start - 1; i < end; i++) {
			// 源站单个视频url
			String mangoUrl = mangoUrlList.get(i);
			// 根据单个视频id，获取m3u8的url
			String m3u8Url = JSON
					.parseObject(WebUtil.sendPost("https://api.bbbbbb.me/yunjx2/api.php", "id=" + mangoUrl),
							M3u8Response.class)
					.getUrl();
			m3u8Url = URLDecoder.decode(m3u8Url, "utf-8");
			// 根据m3u8Url获取m3u8文本
			String[] m3u8TextArray = WebUtil.sendGet(m3u8Url).split("\n");
			List<String> m3u8List = new ArrayList<>();
			for (String line : m3u8TextArray) {
				if (line.startsWith("#") == false) {
					m3u8List.add(line);
				}
			}
			// 截取baseUrl，规则：第8次出现'/'之后
			int index = 0;
			int count = 0;
			for (int j = 0; j < m3u8Url.length(); j++) {
				if (m3u8Url.charAt(j) == '/') {
					count++;
					if (count == 8) {
						index = j;
						break;
					}
				}
			}
			// 每个碎片前的baseUrl
			String baseUrl = m3u8Url.substring(0, index + 1);
			// 下载碎片
			for (int j = 0; j < m3u8List.size(); j++) {
				executorService.execute(new DownloadRunnable(baseUrl + m3u8List.get(j),
						Config.FOLDER_PATH + File.separator + (i + 1), (i + 1) + "_" + (j + 1) + ".ts"));
			}
		}
		executorService.shutdown();
	}

}
