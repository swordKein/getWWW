package kr.kein.getwww.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 날짜 및 시간 관련된 작업을 지원해주는 Utility Class.<br>
 * 
 */
public class DateUtils {

	public static final int YEAR = 1;
	public static final int MONTH = 2;
	public static final int DATE = 3;
	public static final int MONTHFIRST = 4;
	public static final int MONTHEND = 5;

	/**
	 * 입력한 날짜에 해당하는 GregorianCalendar 객체를 반환한다.
	 * 
	 * @param yyyymmdd 날짜 인수
	 * @return GregorianCalendar
	 */
	public static GregorianCalendar getGregorianCalendar(String yyyymmdd) {

		int yyyy = Integer.parseInt(yyyymmdd.substring(0, 4));
		int mm = Integer.parseInt(yyyymmdd.substring(4, 6));
		int dd = Integer.parseInt(yyyymmdd.substring(6));

		GregorianCalendar calendar = new GregorianCalendar(yyyy, mm - 1, dd, 0, 0, 0);

		return calendar;

	}

	/**
	 * <p>
	 * 현재 날짜와 시각을 yyyyMMddhhmmss 형태로 변환 후 return.
	 * 
	 * @return yyyyMMddhhmmss
	 * @see java.util.Date
	 * @see java.util.Locale <p>
	 */
	public static String getLocalDateTime() {
		Date today = new Date();
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		String pattern = "yyyyMMddHHmmss";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern,currentLocale);
		return formatter.format(today);
	}
	
	/**
	 * <p>
	 * 현재 날짜와 시각을 yyyy-MM-dd 형태로 변환 후 return.
	 * 
	 * @return yyyy-MM-dd
	 * @see java.util.Date
	 * @see java.util.Locale <p>
	 */
	public static String getLocalDate2() {
		Date today = new Date();
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern,currentLocale);
		return formatter.format(today);
	}
	

	/**
	 * <p>
	 * 현재 날짜와 시각을 yyyy-MM-dd hh:mm:ss 형태로 변환 후 return.
	 * 
	 * @return yyyy-MM-dd hh:mm:ss
	 * @see java.util.Date
	 * @see java.util.Locale <p>
	 */
	public static String getLocalDateTime3() {
		Date today = new Date();
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		String pattern = "yyyy:MM:dd HH:mm:ss";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern,currentLocale);
		return formatter.format(today);
	}

	/**
	 * <p>
	 *  yyyy-mm-dd 형식의 날짜 문자열을 yyyyMMddhhmmss 형식의 문자열로 반환 
	 * 
	 * @return yyyyMMddhhmmss
	 * @see java.util.Date
	 * @see java.util.Locale <p>
	 */
	public static String getChangeDateTime(String inputTime) {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		String pattern = "yyyyMMddHHmmss";
		SimpleDateFormat formatter2 = new SimpleDateFormat(pattern,currentLocale);
		
		try {
			date = formatter.parse(inputTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return formatter2.format(date);
	}
	
	/**
	 * <p>
	 *  yyyy-mm-dd 형식의 날짜 문자열을 yyyyMMddhhmmss 형식의 문자열로 반환 
	 * 
	 * @return yyyyMMddhhmmss
	 * @see java.util.Date
	 * @see java.util.Locale <p>
	 */
	public static String getChangeDate(String inputTime) {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat formatter2 = new SimpleDateFormat(pattern,currentLocale);
		
		try {
			date = formatter.parse(inputTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return formatter2.format(date);
	}
	
	/**
	 * <p>
	 *  yyyyMMdd 형식의 날짜 문자열을 yyyy-mm-dd 형식의 문자열로 반환 
	 * 
	 * @return yyyyMMddhhmmss
	 * @see java.util.Date
	 * @see java.util.Locale <p>
	 */
	public static String getChangeDate2(String inputTime) {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMdd");
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat formatter2 = new SimpleDateFormat(pattern,currentLocale);
		
		try {
			date = formatter.parse(inputTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return formatter2.format(date);
	}

	/**
	 * <p>
	 * 현재 시각을 hhmmss 형태로 변환 후 return.
	 * 
	 * @return hhmmss
	 * @see java.util.Date
	 * @see java.util.Locale <p>
	 */
	public static String getLocalTime() {
		Date today = new Date();
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		String pattern = "HHmmss";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern,currentLocale);
		return formatter.format(today);

	}

	/**
	 * <p>
	 * 현재 날짜를 yyyyMMdd 형태로 변환 후 return.
	 * 
	 * @return String yyyymmdd  <p>
	 */
	public static String getLocalDate() {
		return getLocalDateTime().substring(0, 8);
	}


	/**
	 * <p>
	 * 지정된 플래그에 따라 연도 , 월 , 일자를 연산한다.
	 * 
	 * @param field 연산 필드
	 * @param amount 더할 수
	 * @param yyyymmdd 연산 대상 날짜
	 * @return 연산된 날짜
	 * @see java.util.Calendar <p>
	 */
	public static String calculateDate(int field, int amount, String yyyymmdd) {

		GregorianCalendar calDate = getGregorianCalendar(yyyymmdd);

		if (field == Calendar.YEAR) {
			calDate.add(GregorianCalendar.YEAR, amount);
		} else if (field == Calendar.MONTH) {
			calDate.add(GregorianCalendar.MONTH, amount);
		} else {
			calDate.add(GregorianCalendar.DATE, amount);
		}

		return getDateFormat(calDate, "yyyyMMdd");

	}
	
	/**
	 * <p>
	 * 지정된 플래그에 따라 연도 , 월 , 일자를 연산한다.
	 * 
	 * @param field 연산 필드
	 * @param amount 더할 수
	 * @param yyyymmdd 연산 대상 날짜
	 * @return 연산된 날짜
	 * @see java.util.Calendar <p>
	 */
	public static String calculateDate2(int field, int amount, String yyyymmdd) {

		GregorianCalendar calDate = getGregorianCalendar(yyyymmdd);

		if (field == Calendar.YEAR) {
			calDate.add(GregorianCalendar.YEAR, amount);
		} else if (field == Calendar.MONTH) {
			calDate.add(GregorianCalendar.MONTH, amount);
		} else {
			calDate.add(GregorianCalendar.DATE, amount);
		}

		return getDateFormat(calDate, "yyyy-MM-dd");

	}
	
	/**
	 * <p>
	 * 지정된 플래그에 따라 연도 , 월 , 일자를 연산한다.
	 * 
	 * @param field 연산 필드
	 * @param amount 더할 수
	 * @param yyyy-MM-dd 연산 대상 날짜
	 * @return 연산된 날짜
	 * @see java.util.Calendar <p>
	 */
	public static String calculateDate_(int field, int amount, String yyyymmdd) {

		GregorianCalendar calDate = getGregorianCalendar(yyyymmdd);

		if (field == Calendar.YEAR) {
			calDate.add(GregorianCalendar.YEAR, amount);
		} else if (field == Calendar.MONTH) {
			calDate.add(GregorianCalendar.MONTH, amount);
		} else {
			calDate.add(GregorianCalendar.DATE, amount);
		}

		return getDateFormat(calDate, "yyyy-MM-dd");

	}
	
	/**
	 * <p>
	 * 지정된 플래그에 따라 연도 , 월 , 일자를 연산한다.
	 * 
	 * @param field 연산 필드
	 * @param amount 더할 수
	 * @param yyyymmdd 연산 대상 날짜시간
	 * @return 연산된 날짜시간
	 * @see java.util.Calendar <p>
	 */
	public static String calculateDateTime(int field, int amount, String yyyymmddhhmiss) {

		SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		try {
			Date date = formatter.parse(yyyymmddhhmiss);
			calendar.setTime(date);
			calendar.add(Calendar.DATE, amount);
		} catch (Exception e) {
		}
		return formatter.format(calendar.getTime());

	}
	
	public static String getDateFromTimestampStr(String timestamp) {
		long unixSeconds = Long.parseLong(timestamp);
		Date date = new Date(unixSeconds*1000L);
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			//GMT(그리니치 표준시 +9 시가 한국의 
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+9"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sdf.format(date);
	}
		
	public static String getDateFromTimestampStr2(String timestamp) {
		long unixSeconds = Long.parseLong(timestamp);
		Date date = new Date(unixSeconds*1000L);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");		
		try {
			//GMT(그리니치 표준시 +9 시가 한국의 
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+9"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sdf.format(date);
	}
	
	public static String getDateFromTimestampStr3(Long timestamp) {
		long unixSeconds = timestamp;
		//Date date = new Date(unixSeconds*1000L);
		Date date = new Date(unixSeconds);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		try {
			//GMT(그리니치 표준시 +9 시가 한국의 
			//sdf.setTimeZone(TimeZone.getTimeZone("GMT+9"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sdf.format(date);
	}
	
	/**
	 * <p>
	 * 지정된 플래그에 따라 연도 , 월 , 일자를 연산한다.
	 * 
	 * @param field 연산 필드
	 * @param amount 더할 수
	 * @param yyyy-mm-dd 연산 대상 날짜시간
	 * @return 연산된 날짜시간
	 * @see java.util.Calendar <p>
	 */
	public static String calculateDateTime2(int field, int amount, String yyyymmddhhmiss) {

		SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmss");
		SimpleDateFormat formatter2 = new SimpleDateFormat ("yyyy-MM-dd-HH-mm-ss");
		Calendar calendar = Calendar.getInstance();
		try {
			Date date = formatter.parse(yyyymmddhhmiss);
			calendar.setTime(date);
			calendar.add(Calendar.DATE, amount);
			
			formatter2.parse(formatter.format(calendar.getTime()));
			
		} catch (Exception e) {
		}
		return formatter2.format(calendar.getTime());

	}

	/**
	 * <p>
	 * 입력된 일자를 더한 주를 구하여 해당 요일을 return한다
	 * 일 - 1 월 2 화 3 수 4 목 5 금 6 토 7
	 * @param yyyymmdd - 년도별
	 * @param addDay -  추가일
	 * @return int  - 연산된 요일 
	 * @see java.util.Calendar <p>
	 */
	public static int calculateDayOfWeek(String yyyymmdd, int addDay) {
		Calendar cal = Calendar.getInstance();
		int new_yy = Integer.parseInt(yyyymmdd.substring(0, 4));
		int new_mm = Integer.parseInt(yyyymmdd.substring(4, 6));
		int new_dd = Integer.parseInt(yyyymmdd.substring(6, 8));

		cal.set(new_yy, new_mm - 1, new_dd);
		cal.add(Calendar.DATE, addDay);

		int week = cal.get(Calendar.DAY_OF_WEEK);
		return week;
	}

	/**
	 * <p>
	 * 입력된 년월의 마지막 일수를 return 한다.
	 * 
	 * @param year
	 * @param month
	 * @return 마지막 일수
	 * @see java.util.Calendar <p>
	 */
	public static int getLastDayOfMonth(int year, int month) {

		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, 1);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);

	}

	/**
	 * <p>
	 * 입력된 년월의 마지막 일수를 return한다
	 * 
	 * @param yyyymm
	 * @return 마지막 일수 <p>

	 */
	public static int getLastDayOfMonth(String yyyymm) {

		Calendar cal = Calendar.getInstance();
		int yyyy = Integer.parseInt(yyyymm.substring(0, 4));
		int mm = Integer.parseInt(yyyymm.substring(4)) - 1;

		cal.set(yyyy, mm, 1);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * <p>
	 * 입력된 날자가 올바른지 확인합니다.
	 * 
	 * @param yyyymmdd
	 * @return boolean <p>

	 */
	public static boolean isCorrect(String yyyymmdd) {
		boolean flag = false;
		if (yyyymmdd.length() < 8)
			return false;
		try {
			int yyyy = Integer.parseInt(yyyymmdd.substring(0, 4));
			int mm = Integer.parseInt(yyyymmdd.substring(4, 6));
			int dd = Integer.parseInt(yyyymmdd.substring(6));
			flag = DateUtils.isCorrect(yyyy, mm, dd);
		} catch (Exception ex) {
			return false;
		}
		return flag;
	}

	/**
	 * <p>
	 * 입력된 날자가 올바른 날자인지 확인합니다.
	 * 
	 * @param yyyy
	 * @param mm
	 * @param dd
	 * @return boolean  <p>

	 */
	public static boolean isCorrect(int yyyy, int mm, int dd) {
		if (yyyy < 0 || mm < 0 || dd < 0)
			return false;
		if (mm > 12 || dd > 31)
			return false;

		String year = "" + yyyy;
		String month = "00" + mm;
		String year_str = year + month.substring(month.length() - 2);
		int endday = DateUtils.getLastDayOfMonth(year_str);

		if (dd > endday)
			return false;

		return true;

	}
	
	/**
	 * check date string validation with an user defined format.
	 * @param s date string you want to check.
	 * @param format string representation of the date format. For example, "yyyy-MM-dd".
	 * @return boolean true 날짜 형식이 맞고, 존재하는 날짜일 때
	 *                 false 날짜 형식이 맞지 않거나, 존재하지 않는 날짜일 때
	 */
	public static boolean isValid(String s, String format) {
	    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat (format, java.util.Locale.getDefault());
	    java.util.Date date = null;
	    try {
	        date = formatter.parse(s);
	    }
	    catch(java.text.ParseException e) {
	        return false;
	    }
	    
	    if ( ! formatter.format(date).equals(s) ){
	        return false;
	    }
	    
	    return true;
	}

	/**
	 * <p>
	 * 두 날짜간의 날짜수를 반환(윤년을 감안함)
	 * 
	 * @param startDate 시작 날짜
	 * @param endDate 끝 날짜
	 * @return 날수
	 * @see java.util.GregorianCalendar <p>
	 */
	public static long getDifferenceOfDays(String startDate, String endDate) {

		GregorianCalendar StartDate = getGregorianCalendar(startDate);
		GregorianCalendar EndDate = getGregorianCalendar(endDate);
		long difer = (EndDate.getTime().getTime() - StartDate.getTime().getTime()) / 86400000;
		return difer;

	}

	/**
	 * <p>
	 * 현재의 요일을 구한다.
	 * 
	 * @return 요일
	 * @see java.util.Calendar <p>
	 */
	public static int getDayOfWeek() {
		Calendar rightNow = Calendar.getInstance();
		int day_of_week = rightNow.get(Calendar.DAY_OF_WEEK);
		return day_of_week;
	}

	/**
	 * <p>
	 * 현재주가 올해 전체의 몇째주에 해당되는지 계산한다.
	 * 
	 * @return 요일
	 * @see java.util.Calendar <p>
	 */
	public static int getWeekOfYear() {
		Locale LOCALE_COUNTRY = Locale.KOREA;
		Calendar rightNow = Calendar.getInstance(LOCALE_COUNTRY);
		//삼성전자 ISO8601 용
//		rightNow.setFirstDayOfWeek(Calendar.MONDAY);
//		rightNow.setMinimalDaysInFirstWeek(4);
		
		int week_of_year = rightNow.get(Calendar.WEEK_OF_YEAR);
		return week_of_year;
	}

	/**
	 * <p>
	 * 현재주가 현재월에 몇째주에 해당되는지 계산한다.
	 * 
	 * @return 요일
	 * @see java.util.Calendar <p>
	 */
	public static int getWeekOfMonth() {
		Locale LOCALE_COUNTRY = Locale.KOREA;
		Calendar rightNow = Calendar.getInstance(LOCALE_COUNTRY);
		int week_of_month = rightNow.get(Calendar.WEEK_OF_MONTH);
		return week_of_month;
	}

	/**
	 * <p>
	 * 입력한 날짜에 해당하는 Calendar 객체를 반환함.
	 * 
	 * @param yyyymmdd
	 * @return Calendar
	 * @see java.util.Calendar <p>
	 */
	public static Calendar getCalendarInstance(String yyyymmdd) {

		Calendar retCal = Calendar.getInstance();

		if (yyyymmdd != null && yyyymmdd.length() == 8) {
			int year = Integer.parseInt(yyyymmdd.substring(0, 4));
			int month = Integer.parseInt(yyyymmdd.substring(4, 6)) - 1;
			int date = Integer.parseInt(yyyymmdd.substring(6));

			retCal.set(year, month, date);
		}
		return retCal;
	}

	
	/**
	 * <p>
	 * 입력받은 시작일과 종료일 사이의 
	 * @param from
	 * @param to
	 * @return     <p>

	 */
	public static String[] getBetweenDays(String from, String to) {
		return getBetweenDaysFormat(from, to, "yyyyMMdd");
	}

	/**
	 * <p>
	 * 시작일부터 종료일까지 사이의 날짜를 배열에 담아 리턴 시작일과 종료일을 모두 포함한다.
	 * 
	 * @param from -  시작일
	 * @param to -  종료일
	 * @param pattern -  날짜 문자 패턴(예: yyyyMMdd, yyyy-MM-dd )
	 * @return String[] - pattern 포멧의 날짜가 담긴 문자열 배열 <p>
	 */
	public static String[] getBetweenDaysFormat(String from, String to, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		ArrayList<String> list = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		Date fmdate = null;
		Date todate = null;

		try {
			fmdate = sdf.parse(from);
			todate = sdf.parse(to);
			cal.setTime(fmdate);
		} catch (Exception e) {
			return null;
		}

		while (fmdate.compareTo(todate) <= 0) {
			list.add(sdf.format(fmdate));
			cal.add(Calendar.DATE, 1);
			fmdate = cal.getTime();
		}

		String[] result = new String[list.size()];
		list.toArray(result);
		return result;
	}
	

	/**
	 * 입력 받은 패턴에 맞게 현재 시각을 반환한다.
	 * 
	 * @param pattern 출력할 패턴
	 * @return 현재 시각
	 */
	public static String getTimeFormat(String pattern) {
		return new SimpleDateFormat(pattern).format(new Date());
	}

	/**
	 * <p>
	 * 입력 받은 Calendar 객체를 입력받은 패턴의 문자열로 반환 
	 * 
	 * @param cal
	 * @return yyyyMMdd  <p>
	 */
	public static String getDateFormat(Calendar cal, String pattern) {
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		SimpleDateFormat formatter = new SimpleDateFormat(pattern,currentLocale);
		return formatter.format(cal.getTime());
	}

	/**
	 * <p>
	 * 입력한 시간을 웹 브라우저에 디스플레이하는 형태로 반환
	 * 
	 * @param yyyymmdd
	 * @return Calendar
	 * @see java.util.Calendar <p>
	 */
	public static String getDisplayDateFormat(String yyyyMMddhhmmss) {
		Calendar cal = getCalendarInstance(yyyyMMddhhmmss.substring(0, 8));
		String fmDate = getDateFormat(cal, "yyyy-MM-dd");
		String hour = yyyyMMddhhmmss.substring(8, 10);
		String minute = yyyyMMddhhmmss.substring(10, 12);
		String second = yyyyMMddhhmmss.substring(12, 14);
		String fmTime = hour + ":" + minute + ":" + second;
		
		return fmDate + " " + fmTime;
	}
	
	/**
	 * 입력한 시간에서 returnType에 맞게 반환
	 * 
	 * @param yyyyMMddhhmmss
	 * @param returnType ( year, month, date, hour, minute, second )
	 * @return type에 맞는 형태
	 * 
	 */
	public static String getUnitDate(String yyyyMMddhhmmss, String returnType) {
		String result = null;
		
		if(returnType == "year") {
			result = yyyyMMddhhmmss.substring(0, 4);
		}
		else if(returnType == "month") {
			result = yyyyMMddhhmmss.substring(4, 6);
			if(Integer.parseInt(result) < 10) {
				result = result.substring(1);
			}
		}
		else if(returnType == "date") {
			result = yyyyMMddhhmmss.substring(6, 8);
			if(Integer.parseInt(result) < 10) {
				result = result.substring(1);
			}
		}
		else if(returnType == "hour") {
			result = yyyyMMddhhmmss.substring(8, 10);
			if(Integer.parseInt(result) < 10) {
				result = result.substring(1);
			}
		}
		else if(returnType == "minute") {
			result = yyyyMMddhhmmss.substring(10, 12);
			if(Integer.parseInt(result) < 10) {
				result = result.substring(1);
			}
		}
		else if(returnType == "second") {
			result = yyyyMMddhhmmss.substring(12, 14);
			if(Integer.parseInt(result) < 10) {
				result = result.substring(1);
			}
		}
		
		return result;
	}


	/*
	 * 시간차를 구한다.
	 */
	public static long getPassTime(String lastVisitTime, String nowVisitTime) {
		
		Calendar startCalendar	= Calendar.getInstance();
		Calendar endCalendar	= Calendar.getInstance();
		
		int start_yyyy	= Integer.parseInt(lastVisitTime.substring(0	, 4));
		int start_MM	= Integer.parseInt(lastVisitTime.substring(4	, 6));
		int start_dd	= Integer.parseInt(lastVisitTime.substring(6	, 8));
		int start_HH	= Integer.parseInt(lastVisitTime.substring(8	, 10));
		int start_mm	= Integer.parseInt(lastVisitTime.substring(10	, 12));
		int start_ss	= Integer.parseInt(lastVisitTime.substring(12	, 14));

		int end_yyyy	= Integer.parseInt(nowVisitTime.substring(0		, 4));
		int end_MM		= Integer.parseInt(nowVisitTime.substring(4		, 6));
		int end_dd		= Integer.parseInt(nowVisitTime.substring(6		, 8));
		int end_HH		= Integer.parseInt(nowVisitTime.substring(8		, 10));
		int end_mm		= Integer.parseInt(nowVisitTime.substring(10	, 12));
		int end_ss		= Integer.parseInt(nowVisitTime.substring(12	, 14));

		// 시작시간.
		startCalendar.set(start_yyyy	, start_MM	, start_dd	, start_HH	, start_mm	, start_ss);
		long startDate = startCalendar.getTimeInMillis();

		// 종료시간.
		endCalendar.set(end_yyyy		, end_MM	, end_dd	, end_HH	, end_mm	, end_ss);
		long endDate = endCalendar.getTimeInMillis();
		
		long millis = endDate - startDate;
		long min = millis / (1000 * 60);
		
		System.out.println("startDate : "+startDate);
		System.out.println("endDate : "+endDate);
		System.out.println("millis : "+millis);
		System.out.println(min + "분 경과.");
		
		return min;
	}
	
	/*
	 * 시간차를 구한다.
	 */
	public static long getPassTime2(String lastVisitTime, String nowVisitTime) throws Exception {
		long min = 0;
		
		if (!"".equals(lastVisitTime) && !"".equals(nowVisitTime)) {
			Calendar startCalendar	= Calendar.getInstance();
			Calendar endCalendar	= Calendar.getInstance();
			
			int start_yyyy	= Integer.parseInt(lastVisitTime.substring(0	, 4));
			int start_MM	= Integer.parseInt(lastVisitTime.substring(4	, 6));
			int start_dd	= Integer.parseInt(lastVisitTime.substring(6	, 8));
			int start_HH	= Integer.parseInt(lastVisitTime.substring(8	, 10));
			int start_mm	= Integer.parseInt(lastVisitTime.substring(10	, 12));
			int start_ss	= Integer.parseInt(lastVisitTime.substring(12	, 14));
	
			int end_yyyy	= Integer.parseInt(nowVisitTime.substring(0		, 4));
			int end_MM		= Integer.parseInt(nowVisitTime.substring(4		, 6));
			int end_dd		= Integer.parseInt(nowVisitTime.substring(6		, 8));
			int end_HH		= Integer.parseInt(nowVisitTime.substring(8		, 10));
			int end_mm		= Integer.parseInt(nowVisitTime.substring(10	, 12));
			int end_ss		= Integer.parseInt(nowVisitTime.substring(12	, 14));
	
			// 시작시간.
			startCalendar.set(start_yyyy	, start_MM	, start_dd	, start_HH	, start_mm	, start_ss);
			long startDate = startCalendar.getTimeInMillis();
	
			// 종료시간.
			endCalendar.set(end_yyyy		, end_MM	, end_dd	, end_HH	, end_mm	, end_ss);
			long endDate = endCalendar.getTimeInMillis();
			
			long millis = endDate - startDate;
			min = millis / (1000 * 60);
			
			System.out.println("startDate : "+startDate);
			System.out.println("endDate : "+endDate);
			System.out.println("millis : "+millis);
			System.out.println(min + "분 경과.");
		} else {
			min = (long) 99999999;
		}
		
		return min;
	}
	
	/**
	 * String -> Date 변환
	 * @param format
	 * @param str_date
	 * @return
	 * @throws Exception
	 */
	public static Date strToDateTime(String format, String str_date) {
		Date result = null;
		try{
			if (str_date == null)
				return null;
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			result = formatter.parse(str_date);
		}catch (Exception e) {
			
		}
		return result;
	}
	
	
	/**
	 * <p>
	 * 주어진 Date type 의 날짜와 시각을 yyyy-MM-dd hh:mm:ss 형태로 변환 후 return.
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 * @see java.util.Date
	 * @see java.util.Locale <p>
	 */
	public static String getDateTime(Date inputDate) {
		Date today = inputDate;
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern,currentLocale);
		return formatter.format(today);
	}

}