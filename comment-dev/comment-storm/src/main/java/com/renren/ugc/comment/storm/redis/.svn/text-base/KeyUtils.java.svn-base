package com.renren.ugc.comment.storm.redis;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.renren.ugc.comment.storm.model.StatType;


/**
 * @author xulei
 * @date 2013-8-8
 * @email lei.xu1@renren-inc.com
 * @tags 缓存中存储数据的key生成类
 */
public class KeyUtils {
	
	/**
	 * 产生 获取api相关性信息的 key
	 * @param statType 类型
	 * @param content 需要转换key的内容
	 * @param date 时间
	 * @return
	 */
	public static String getKey(StatType statType, String content, Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		StringBuilder sb = new StringBuilder();
		sb.append(statType.getKeyPrefix());
		sb.append(":");
		sb.append(content);
		sb.append(":");
		sb.append(format.format(date));
		return sb.toString();
	}

}
