package com.renren.ugc.comment.storm.dao;

import java.util.ArrayList;
import java.util.List;

/** 
 * @author meng.liu 
 * @date 2014-5-27 下午8:43:04 
 */
public class ErrorLogDAO {
	
	public static List<Integer> queryMessageCount(String key) {
		
		List<Integer> countList = new ArrayList<Integer>();
		countList.add(3);
		countList.add(4);
		countList.add(3);
		countList.add(5);
		countList.add(6);
		countList.add(5);
		countList.add(4);
		countList.add(7);
		countList.add(9);
		countList.add(4);
		
		return countList;
		
	}

}
