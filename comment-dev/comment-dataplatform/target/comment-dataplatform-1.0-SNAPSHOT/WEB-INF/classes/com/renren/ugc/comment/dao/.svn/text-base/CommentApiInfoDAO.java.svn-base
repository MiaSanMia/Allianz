package com.renren.ugc.comment.dao;

import java.util.Date;
import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.renren.ugc.comment.model.CommentAccessLogEntry;

@DAO
public interface CommentApiInfoDAO {
	
	 String TABLE = " comment_api_info ";
	
	 String DB_FIELDS = " id,type,method,count,max_time,avg_time,miss_count,timeout_count,exception_count,ip,date ";
	 
	 @SQL("SELECT " + DB_FIELDS + " FROM " + TABLE
	            + " WHERE method=:method and date between :beginTime and :endTime")
	 public List<CommentAccessLogEntry> getCommentLogInfos(@SQLParam("method") String method,
			 @SQLParam("beginTime") String beginTime,@SQLParam("endTime") String endTime);
	
	

}
