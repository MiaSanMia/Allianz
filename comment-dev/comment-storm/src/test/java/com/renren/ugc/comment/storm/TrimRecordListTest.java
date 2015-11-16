/*package com.renren.ugc.comment.storm;

import java.util.Date;

import org.junit.Test;

import com.renren.ugc.comment.storm.model.TrimRecordList;
import com.renren.ugc.comment.storm.utils.TimeUtils;

public class TrimRecordListTest {
	
	@Test
	public void trimRecordListTest(){
		
		String method = "create";
		String method1 = "get";
		
		int maxTime = 3;
		 TrimRecordList list = new TrimRecordList();
		
		for(int i = 0;i<10;i++){
			Date formatDate = TimeUtils.formatToMin(new Date());
	    	 
	    	 list.incr(method, maxTime + i, formatDate.getTime());
	    	 list.incr(method1, maxTime + i + 1, formatDate.getTime());
	    	 //报警
	    	 System.out.println("create:"+list.getAvg(method));
	    	// System.out.println("get:"+list.getAvg(method1));
	    	 
	    	 try {
				Thread.sleep(60 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testTime(){
		Date formatDate = TimeUtils.formatToMin(new Date());
		System.out.println(formatDate.getTime());
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		formatDate = TimeUtils.formatToMin(new Date());
		System.out.println(formatDate.getTime());
	}

}
*/