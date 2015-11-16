package com.renren.ugc.comment.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.renren.campus.util.CampusJsonUtil;

/**
 * @author wangxx
 *
 *  评论里有图的json解析工具类
 */
public class ImgJsonUtil {
	
	private static Logger logger = Logger.getLogger(ImgJsonUtil.class);
	
	public static String getCampusText(String content){
		String text = "";
		if(StringUtils.isEmpty(content)){
			return text;
		}
		content = content.replaceAll("&quot;", "\"");
		try{
			text = CampusJsonUtil.getText(content);
		} catch (Exception pe){
			logger.error("ImgJsonUtil getCampusText error,content="+content);
		}
		return text;
	}
	
	public static String getCampusPic(String content){
		String text = "";
		if(StringUtils.isEmpty(content)){
			return text;
		}
		content = content.replaceAll("&quot;", "\"");
		try{
			JSONArray array = CampusJsonUtil.getImgArr(content);
			if(array != null && array.size() > 0){
				JSONObject object = (JSONObject)array.get(0);
				text = object.getString(CampusJsonUtil.MAIN_URL);
			}
		} catch (Exception pe){
			logger.error("ImgJsonUtil getCampusPic error,content="+content);
		}
		
		return text;
	}

}
