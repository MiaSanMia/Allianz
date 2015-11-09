package com.campus.cache.util;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

/** 
 * 打印model对象的各个字段信息使用的工具类，功能类似于JSONSerializer.toJSON(model)一般在debug时使用
 * @author meng.liu 
 * @date 2014-3-5 下午10:54:57 
 */
public class Model2StringUtil {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	public static String transModel2String(Object o) {  
	    StringBuffer sb = new StringBuffer();  
	    sb.append(o.getClass().getName() + "[");  
	    Field[] farr = o.getClass().getDeclaredFields();  
	    for (Field field : farr) {  
	        try {  
	            field.setAccessible(true);  
	            sb.append(field.getName());  
	            sb.append("=");  
	            if (field.get(o) != null && field.get(o) instanceof Date) {  
	                // 日期的处理  
	                sb.append(sdf.format(field.get(o)));  
	            } else {  
	                sb.append(field.get(o));  
	            }  
	            sb.append("|");  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    }  
	    sb.append("]");  
	    return sb.toString();  
	}  

}
