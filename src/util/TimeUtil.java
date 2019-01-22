package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	public static String getTime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		return simpleDateFormat.format(new Date());
	}

}
