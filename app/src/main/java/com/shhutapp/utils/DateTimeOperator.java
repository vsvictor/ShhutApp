package com.shhutapp.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.shhutapp.R;

import android.content.Context;
import android.util.Log;

public class DateTimeOperator {
	public static Date toDateTime(String date, String format){
		if(date == null || format == null) return null;
		ParsePosition pos = new ParsePosition(0);
		SimpleDateFormat sFormat = new SimpleDateFormat(format);
		Date d = sFormat.parse(date, pos);
		return d;
	}
	public static Date toDateTime(long date, String format){
		Date d = new Date(date);
		return d;
	}
	public static String dateToTimeString(Date aDate) {
		String aFormat = "HH:mm";
		if (aDate == null)
			return "";
		SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
		String stringDate = simpledateformat.format(aDate);
		return stringDate;
	}
	public static String dateToTimeString12(Date aDate) {
		String aFormat = "HH:mm";
		if (aDate == null)
			return "";
		SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
		String stringDate = simpledateformat.format(aDate);
		String[] ss = stringDate.split(":");
		int hh = Integer.parseInt(ss[0]);
		String suff = "AM";
		if(hh>=12){
			hh = hh-12;
			suff = "PM";
		}
		return stringDate+" "+suff;
	}

	public static String numberDayToNameDay(Context context, int number){
		String s = "";
		switch(number){
			case 0:{s=context.getResources().getString(R.string.sun);break;}
			case 1:{s=context.getResources().getString(R.string.mon);break;}
			case 2:{s=context.getResources().getString(R.string.tue);break;}
			case 3:{s=context.getResources().getString(R.string.wed);break;}
			case 4:{s=context.getResources().getString(R.string.thu);break;}
			case 5:{s=context.getResources().getString(R.string.fri);break;}
			case 6:{s=context.getResources().getString(R.string.sat);break;}
			default:{s=" ";}
		}
		return s;
	}
	public static Date afterMinutes(int minutes){
		Date d = Calendar.getInstance().getTime();
		Log.i("Date", DateTimeOperator.dateToTimeString(d));
				d.setTime(d.getTime() + (minutes * 60 * 1000));
		Log.i("add minutes",String.valueOf(minutes*60*1000)+" new date:"+DateTimeOperator.dateToTimeString(d));
		return d;
	}
	public static String minutesToString(int minutes, String h, String m){
		int hours = minutes/60;
		int ost = minutes-(hours*60);
		return String.valueOf(hours)+" "+h+" "+String.valueOf(ost)+" "+m;
	}

	public static String minutesToTimeString(int minutes){
		String s = null;
		int h = minutes/60;
		int min = minutes-(h*60);
		return String.valueOf(h)+":"+String.valueOf(min);
	}
	public static int DateToMinutes(Date d){
		int l = d.getHours();
		int minute = d.getMinutes();
		int res = l*60+minute;
		return res;
	}
}
