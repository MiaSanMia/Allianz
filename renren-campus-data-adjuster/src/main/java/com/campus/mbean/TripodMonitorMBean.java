package com.campus.mbean;
/** 
 * @author meng.liu 
 * @date 2014-8-5 下午3:39:18 
 */
public interface TripodMonitorMBean {
	
	public String getFeedListByUser();
	public void getFeedListFromTripodByUser(int userId,int schoolId);
	
	public String getFeedListBySchool();
	public void getFeedListFromTripodBySchool(int schoolId);
	
	public String getReadListByUser();
	public void getReadListFromTripodByUser(int userId);
	
	public String getRemoveListByUser();
	public void getRemoveListFromTripodByUser(int userId);
	
	public long getLastIdForUser();
	public void getLastIdFromTripodForUser(int userId, int schoolId);
	
	public void zSetRemove(String key);
	
	public void clearTripod(String prefix, int beginIndex, int endIndex);
	
	public void clearTripodBySingleKey(String key);

}
