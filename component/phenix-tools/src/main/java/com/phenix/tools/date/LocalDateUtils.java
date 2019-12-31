package com.phenix.tools.date;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期工具类
 * @author wuluodan
 * @date 2018/5/22
 */
public class LocalDateUtils {

	/** 年月日 显示格式 */
	public static String DATE_TO_STRING_PATTERN = "yyyyMMdd";

	/** 年-月-日 显示格式 */
	public static String DATE_TO_STRING_SHORT_PATTERN = "yyyy-MM-dd";
	
	public static DateTimeFormatter dateTimeFormatter;
	
	/**
	 * 日期转字符串
	 * @param date 日期
	 * @param pattern 日期格式
	 * @return
	 */
	public static String dateToString(LocalDate date, String pattern) {
		if (null == date) {
			return null;
		}
		dateTimeFormatter = DateTimeFormatter.ofPattern(pattern == null ? DATE_TO_STRING_SHORT_PATTERN : pattern);
		return dateTimeFormatter.format(date);
	}
	
	/**
	 * 日期转字符串
	 * @param date 日期
	 * @return
	 */
	public static String dateToString(LocalDate date) {
		if (null == date) {
			return null;
		}
		return dateToString(date, null);
	}
	
	/**
	 * 字符串转日期
	 * @param date 日期
	 * @param pattern 日期格式
	 * @return
	 */
	public static LocalDate stringToDate(String date, String pattern) {
		if (null == date || "".equals(date)) {
			return null;
		}
		dateTimeFormatter = DateTimeFormatter.ofPattern(pattern == null ? DATE_TO_STRING_SHORT_PATTERN : pattern);
		return LocalDate.parse(date, dateTimeFormatter);
	}
	
	/**
	 * 字符串转日期
	 * @param date 日期
	 * @return
	 */
	public static LocalDate stringToDate(String date) {
		if (null == date) {
			return null;
		}
		return stringToDate(date, null);
	}


	/**
	 * 字符串转日期
	 * @param localDate 日期
	 * @return
	 */
	public static Date localDateToDate(LocalDate localDate) {
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
		java.util.Date date = Date.from(instant);
		return date;
	}

}
