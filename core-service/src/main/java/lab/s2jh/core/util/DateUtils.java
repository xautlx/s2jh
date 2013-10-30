package lab.s2jh.core.util;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	public final static String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

	public final static Format DEFAULT_TIME_FORMATER = new SimpleDateFormat(DEFAULT_TIME_FORMAT);

	public final static Format DEFAULT_DATE_FORMATER = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

	public static String formatDate(Date date) {
		if (date == null) {
			return null;
		}
		return DEFAULT_DATE_FORMATER.format(date);
	}

	public static String formatTime(Date date) {
		if (date == null) {
			return null;
		}
		return DEFAULT_TIME_FORMATER.format(date);
	}

	public static String getHumanDisplayForTimediff(Long diffMillis) {
		if (diffMillis == null) {
			return "";
		}
		long day = diffMillis / (24 * 60 * 60 * 1000);
		long hour = (diffMillis / (60 * 60 * 1000) - day * 24);
		long min = ((diffMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long se = (diffMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		StringBuilder sb = new StringBuilder();
		if (day > 0) {
			sb.append(day + "D");
		}
		DecimalFormat df = new DecimalFormat("00");
		sb.append(df.format(hour) + ":");
		sb.append(df.format(min) + ":");
		sb.append(df.format(se));
		return sb.toString();
	}
}
