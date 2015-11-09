package com.campus.mbean;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.campus.cache.TripodClient;
import com.renren.campus.biz.PiazzaBiz;
import com.renren.campus.model.CampusFeed;

/** 
 * @author meng.liu 
 * @date 2014-8-5 下午3:42:25 
 */
public class TripodMonitor implements TripodMonitorMBean {
	
	private static Log logger = LogFactory.getLog(TripodMonitor.class);
	private static String feedListForUser = "come on, give me value from tripod~~~";
	private static String feedListForSchool = "come on, give me value from tripod~~~";
	private static String readListForUser = "come on, give me value from tripod~~~";
	private static String removeListForUser = "come on, give me value from tripod~~~";
	private static long lastId = 0;
	
	private static TripodClient tripodClient = TripodClient.getInstance();
	private static PiazzaBiz piazzaBiz = new PiazzaBiz();

	@Override
	public String getFeedListByUser() {
		return feedListForUser;
	}

	@Override
	public void getFeedListFromTripodByUser(int userId,int schoolId) {
		
		List<CampusFeed> feedList = piazzaBiz.getListByUser(userId, schoolId, 0, -1);
		if(feedList != null && feedList.size() > 0) {
			StringBuilder builder = new StringBuilder();
			for(int i=0;i<feedList.size();i++) {
				CampusFeed feed = feedList.get(i);
				builder.append("["+i+"]" + String.format("feedId%d,sType%d,resourceId%d,commentCount%d,likeCount%d,sortFactor[%.2f]",
						feed.getId(),feed.getStype(),feed.getSourceId(),feed.getCommentCount(),feed.getLikeCount(), feed.getSortFactor()));
			}
			feedListForUser = builder.toString();
		} else {
			feedListForUser = null;
		}
		
	}

	@Override
	public void clearTripod(String prefix, int beginIndex, int endIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearTripodBySingleKey(String key) {
		tripodClient.remove(key);
	}

	@Override
	public String getFeedListBySchool() {
		return feedListForSchool;
	}

	@Override
	public void getFeedListFromTripodBySchool(int schoolId) {
		
		try {
			List<CampusFeed> feedList = piazzaBiz.getListBySchool(schoolId);
			if(feedList != null && feedList.size() > 0) {
				StringBuilder builder = new StringBuilder();
				for(int i=0;i<feedList.size();i++) {
					CampusFeed feed = feedList.get(i);
					builder.append("["+i+"]" + String.format("feedId%d,sType%d,resourceId%d,likeCount%d,commentCount%d",
							feed.getId(),feed.getStype(),feed.getSourceId(),feed.getLikeCount(),feed.getCommentCount()));
				}
				feedListForSchool = builder.toString();
			} else {
				feedListForSchool = null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
	}

	@Override
	public String getReadListByUser() {
		return readListForUser;
	}

	@Override
	public void getReadListFromTripodByUser(int userId) {
		List<Long> readList = piazzaBiz.getReadList(userId);
		
		if(readList != null && readList.size() > 0) {
			
			StringBuilder builder = new StringBuilder();
			for(int i=0;i<readList.size();i++) {
				builder.append("[" + i + "]" + readList.get(i));
			}
			readListForUser = builder.toString();
		} else {
			readListForUser = null;
		}
		
	}

	@Override
	public String getRemoveListByUser() {
		return removeListForUser;
	}

	@Override
	public void getRemoveListFromTripodByUser(int userId) {
		List<Long> removeList = piazzaBiz.getRemovedList(userId);
		
		if(removeList != null && removeList.size() > 0) {
			
			StringBuilder builder = new StringBuilder();
			for(int i=0;i<removeList.size();i++) {
				builder.append("[" + i + "]" + removeList.get(i));
			}
			removeListForUser = builder.toString();
		} else {
			removeListForUser = null;
		}
		
	}

	@Override
	public long getLastIdForUser() {
		return lastId;
	}

	@Override
	public void getLastIdFromTripodForUser(int userId, int schoolId) {
		lastId = piazzaBiz.getLastIdOfList(userId, schoolId);
	}

	@Override
	public void zSetRemove(String key) {
		
	}
	
	

}
