package com.renren.ugc.comment.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.renren.ugc.comment.model.CommentAccessLogEntry;

public class GetFieldsUtils {
	
	private static Map<String,GetField> maps = new HashMap<String,GetField>();
	
	static {
		maps.put("count", new CountField());
		maps.put("maxTime", new MaxTimeField());
		maps.put("avgTime", new AvgTimeField());
		maps.put("missCount", new MissCountField());
		maps.put("timeoutCount", new TimeoutCountField());
		maps.put("exceptionCount", new ExceptionCountField());
	}
	
	public static int getValue(CommentAccessLogEntry entry,String key){
		
		int value = 0;
		
		if(maps.containsKey(key)){
			GetField method = maps.get(key);
			value = method.getValue(entry);
		}
		
		return value;
	}
	
	interface GetField {
		public int getValue(CommentAccessLogEntry entry);
	}

	static class CountField implements GetField{
		@Override
		public int getValue(CommentAccessLogEntry entry) {
			return entry != null ? entry.getCount() : 0;
		}
	}
	
	static class MaxTimeField implements GetField{
		@Override
		public int getValue(CommentAccessLogEntry entry) {
			return entry != null ? entry.getMaxTime() : 0;
		}
	}
	
	static class AvgTimeField implements GetField{
		@Override
		public int getValue(CommentAccessLogEntry entry) {
			return entry != null ? entry.getAvgTime() : 0;
		}
	}
	
	static class MissCountField implements GetField{
		@Override
		public int getValue(CommentAccessLogEntry entry) {
			return entry != null ? entry.getMissCount() : 0;
		}
	}
	
	static class TimeoutCountField implements GetField{
		@Override
		public int getValue(CommentAccessLogEntry entry) {
			return entry != null ? entry.getTimeoutCount() : 0;
		}
	}
	
	static class ExceptionCountField implements GetField{
		@Override
		public int getValue(CommentAccessLogEntry entry) {
			return entry != null ? entry.getExceptionCount() : 0;
		}
	}

}

