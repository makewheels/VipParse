package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class FileUtil {

	/**
	 * 保存字符串到文件
	 * 
	 * @param folder
	 * @param filename
	 * @param content
	 */
	public static void saveToFile(String folder, String filename, String content) {
		FileOutputStream fop = null;
		File file;
		try {
			file = new File(folder + File.separator + filename);
			fop = new FileOutputStream(file);
			if (!file.exists()) {
				file.createNewFile();
			}
			byte[] contentInBytes = content.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fop.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param url
	 * @param folderPath
	 * @param filename
	 */
	public static void download(String url, String folderPath, String filename) {
		try {
			int b = 0;
			URLConnection connection = new URL(url).openConnection();
			InputStream inputStream = connection.getInputStream();
			FileOutputStream fOutputStream = new FileOutputStream(folderPath + File.separator + filename);
			byte[] buffer = new byte[1204];
			while ((b = inputStream.read(buffer)) != -1) {
				fOutputStream.write(buffer, 0, b);
			}
			fOutputStream.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	public static void mergeFiles(String outFile, String[] files) {
		FileChannel outChannel = null;
		try {
			outChannel = new FileOutputStream(outFile).getChannel();
			for (String f : files) {
				Charset charset = Charset.forName("utf-8");
				CharsetDecoder chdecoder = charset.newDecoder();
				CharsetEncoder chencoder = charset.newEncoder();
				FileChannel fc = new FileInputStream(f).getChannel();
				ByteBuffer bb = ByteBuffer.allocate(1024 * 8);
				CharBuffer charBuffer = chdecoder.decode(bb);
				ByteBuffer nbuBuffer = chencoder.encode(charBuffer);
				while (fc.read(nbuBuffer) != -1) {
					bb.flip();
					nbuBuffer.flip();
					outChannel.write(nbuBuffer);
					bb.clear();
					nbuBuffer.clear();
				}
				fc.close();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (outChannel != null) {
					outChannel.close();
				}
			} catch (IOException ignore) {
			}
		}
	}

}
