package com.renren.ugc.comment.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
	
	public static String getFormatMins(String hour,String day) throws ParseException{
		
		//hour字段把秒变成0
		int index = hour.lastIndexOf(":");
		
		if(index == -1){
			throw new RuntimeException("param invalid ");
		}
		
		String result = day + " " + hour.substring(0, index)+":00";
		 
		 return result;
	}

}
