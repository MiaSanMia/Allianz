package com.renren.ugc.comment.storm.dao;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.renren.ugc.comment.storm.alarm.gauss.GaussVO;

/** 
 * @author meng.liu 
 * @date 2014-5-27 下午7:29:22 
 */
public class GaussDAO {
	
	private static final String BUSINESS_FILE = "/data/web/storm/storm-0.7.0/data/business";
	private static final Log logger = LogFactory.getLog(GaussDAO.class);
	
	/**
	 * 加载所有的business+ip
	 * 这个操作感觉读DB的话没有必要，完全是固定的信息，直接写在本地文件里面读出来
	 * @return
	 */
	public static List<String> loadAllBusiness() {
		
	    LineNumberReader lnr;
	    try {
	    	lnr = new LineNumberReader(new FileReader(new File(BUSINESS_FILE)));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Collections.EMPTY_LIST;
		}
	    
	    List<String> businessWithIp = new ArrayList<String>();
	    
		String line = null;
		do{
			try {
				line = lnr.readLine();
			} catch (Exception e) {
				logger.error("read line from businessFile failed");
			}
			if(StringUtils.isNotBlank(line)) {
				logger.info("read new line:" + line);
				String[] parts = line.split(":");
				if(parts.length == 2) {
					
					String business = parts[0];
					String ipPart = parts[1];
					
					String[] ips = ipPart.split(",");
					
					for(String ip:ips) {
						businessWithIp.add(business + "_" + ip);
					}
					
				} else {
					logger.error("data format error" );
					continue;
				}
			}
			
					
		} while(StringUtils.isNotBlank(line));
		
		return businessWithIp;
	}
	
	public static List<GaussVO> loadAllGaussInfo() {
		
		List<GaussVO> gaussList = new ArrayList<GaussVO>();
		for(int i=0;i<24;i++) {
			GaussVO gaussXoa = new GaussVO();
			gaussXoa.setKey("comment-xoa_" + i);
			gaussXoa.setDx(0.24);
			gaussXoa.setEx(4);
			
			gaussList.add(gaussXoa);
			
			GaussVO gaussWeb = new GaussVO();
			gaussWeb.setKey("comment-web_" + i);
			gaussWeb.setDx(0.24);
			gaussWeb.setEx(3);
			
			gaussList.add(gaussWeb);
			
		}
		
		return gaussList;
		
	}
	
	public static List<GaussVO> queryGaussInfo(GaussVO gauss) {
		List<GaussVO> list = new ArrayList<GaussVO>();
		list.add(gauss);
		return list;
	}
	
	public static void updateGaussInfo(GaussVO gauss) {
		
	}
	
	public static void insertGaussInfo(GaussVO gauss) {
		
	}

}
