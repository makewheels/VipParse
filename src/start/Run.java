package start;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;

import bean.M3u8Response;
import util.FileUtil;
import util.WebUtil;

public class Run {

	public static void main(String[] args) throws Exception {
		// 保存路径
		String savePath = "D:\\TARGET_FOLDER\\out";
		// 解析网站
		// http://www.yn-dove.cn/
		String url = "http://www.yn-dove.cn/play.php?play=/tv/Q4RrbX7lRGDsNH.html";
		Document document = Jsoup.connect(url).get();
		Elements linkList = document.getElementsByClass("num-tab-main g-clear js-tab").get(0).getElementsByTag("a");
		// 所有集
		// for (Element element : linkList) {
		Element element = linkList.get(2);
		// 每一集
		String id = element.attr("id");
		String href = element.attr("href");
		String postResult = WebUtil.sendPost("https://yun.odflv.com/odflv2/api1.php",
				"time=1544104343&key=09ed112d7bacd00abd4fb2951c8e46f9&url=" + URLEncoder.encode(href, "utf-8"));
		String m3u8Url = JSON.parseObject(URLDecoder.decode(postResult, "utf-8"), M3u8Response.class).getUrl();
		String[] lineArr = WebUtil.sendGet(m3u8Url).split("\n");
		String rootUrl = m3u8Url.substring(0, m3u8Url.indexOf("_mp4/") + 5);
		int totalPieces = 0;
		for (String line : lineArr) {
			if (line.startsWith("#") == false) {
				totalPieces++;
			}
		}
		// 下载碎片
		int count = 1;
		for (String line : lineArr) {
			if (line.startsWith("#") == false) {
				System.out.println(id + " " + count + "/" + totalPieces);
				FileUtil.download(rootUrl + line, savePath, id + "_" + count + ".ts");
				count++;
			}
		}
		// 文件碎片集合
		String[] filePiecesList = new String[totalPieces];
		for (int i = 1; i <= totalPieces; i++) {
			filePiecesList[i - 1] = savePath + File.separator + id + "_" + i + ".ts";
		}
		// 合并
		FileUtil.mergeFiles(savePath + File.separator + id + ".ts", filePiecesList);
		// 删除所有碎片
		for (String filepath : filePiecesList) {
			new File(filepath).delete();
		}
		// }
	}

}
