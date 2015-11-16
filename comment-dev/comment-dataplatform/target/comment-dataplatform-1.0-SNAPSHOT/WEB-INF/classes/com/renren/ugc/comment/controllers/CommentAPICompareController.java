package com.renren.ugc.comment.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.renren.ugc.comment.dao.CommentApiInfoDAO;
import com.renren.ugc.comment.model.CommentAccessLogEntry;
import com.renren.ugc.comment.model.MapValue;
import com.renren.ugc.comment.util.GetFieldsUtils;
import com.renren.ugc.comment.util.TimeUtils;

@Path("")
public class CommentAPICompareController {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private CommentApiInfoDAO commentApiInfoDAO;
	
	  @Get("index1")
	    public String index(Invocation inv){
	        Date date = new Date();
	        
	        java.text.DateFormat format = new java.text.SimpleDateFormat( "HH:mm:ss");  
	        String formatDate = format.format(date);;
	        
	        inv.addModel("beginDate",formatDate);
	        inv.addModel("endDate",formatDate);
	        
	        JSONArray array = new JSONArray();
	        
	        inv.addModel("arr", array);
	        
	        return "apicompare";
	    }
	
	@Get("search1")
    public String search(Invocation inv,@Param("beginDate") String beginDateStr, @Param("endDate") String endDateStr,
            @Param("cDate1") String cDate1, @Param("cDate2") String cDate2,@Param("queryJiekou") String queryJiekou,@Param("queryZhibiao") String queryZhibiao){
		
		JSONArray array = new JSONArray();
		DateFormat format = new SimpleDateFormat("HH:mm:ss");
		double max = 0;
		
		try{
			
			//1.formate date
			String dateBegin1 = TimeUtils.getFormatMins(beginDateStr, cDate1);
			String dateEnd1 = TimeUtils.getFormatMins(endDateStr, cDate1);
			String dateBegin2 = TimeUtils.getFormatMins(beginDateStr, cDate2);
			String dateEnd2 = TimeUtils.getFormatMins(endDateStr, cDate2);
			
			//1.get
			List<CommentAccessLogEntry> infos1 = commentApiInfoDAO.getCommentLogInfos(queryJiekou, dateBegin1, dateEnd1);
			List<CommentAccessLogEntry> infos2 = null;
			if(StringUtils.isNotEmpty(cDate2)){
				infos2 = commentApiInfoDAO.getCommentLogInfos(queryJiekou, dateBegin2, dateEnd2);
			}
				
			//2.join
			Map<String,MapValue> countMaps1 = buildDataMap(infos1,queryZhibiao);
			Map<String,MapValue> countMaps2 = buildDataMap(infos2,queryZhibiao);
	 
			//3.ouput
			if(countMaps1 != null){
				Iterator itor = countMaps1.entrySet().iterator();
				while(itor.hasNext()){
					Entry entry = (Entry)itor.next();
					MapValue value =  (MapValue)entry.getValue();
					
					JSONObject json = new JSONObject();
					json.put("day", entry.getKey());
					json.put("count1", value.getAvg());
					max = value.getAvg() > max ? value.getAvg() : max;
					
					if(countMaps2 != null && countMaps2.containsKey(entry.getKey())){
						value = countMaps2.get(entry.getKey());
						json.put("count2", value.getAvg());
						max = value.getAvg() > max ? value.getAvg() : max;
					}
					
					array.put(json);
				}
			}

		} catch (Exception e){
			logger.error(e.getMessage());
		}
		
		inv.addModel("arr", array);
		inv.addModel("max", 1.5 * max);
		
		return "apicompare";
	}
	
	private Map<String,MapValue> buildDataMap(List<CommentAccessLogEntry> infos,String queryZhibiao){
		
		if(CollectionUtils.isEmpty(infos)){
			return Collections.EMPTY_MAP;
		}
		//只用"时分秒"作为key
		DateFormat format = new SimpleDateFormat("HH:mm:ss");
		
		Map<String,MapValue> countMaps = new HashMap<String,MapValue>();
		for(CommentAccessLogEntry info:infos){
			String dateKey = format.format(info.getDate());
			int data = GetFieldsUtils.getValue(info, queryZhibiao);
			if(countMaps.containsKey(dateKey)){
				MapValue value = countMaps.get(dateKey);
				value.inc(data);
			} else {
				countMaps.put(dateKey, new MapValue(data));
			}
		}
		return countMaps;
	}

}
