package com.renren.ugc.comment.storm.alarm.strategy;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** 
 * @author meng.liu 
 * @date 2014-5-23 下午5:48:03 
 */
public class AlarmConfig {
	
	private static Map<BusinessType,AlarmConfig> configMap = new ConcurrentHashMap<BusinessType, AlarmConfig>();
	private static final String EMAIL_TAIL = "@renren-inc.com";
	
	
	static {
		AlarmConfig config = new AlarmConfig();
		config.setBusinessName("comment-xoa");
		
		ArrayList<String> phoneList = new ArrayList<String>();
		phoneList.add("15210833640");
		phoneList.add("18612685646");
		config.setPhoneList(phoneList);
		
		ArrayList<String> emailList = new ArrayList<String>();
		emailList.add("meng.liu" + EMAIL_TAIL);
		config.setEmailList(emailList);
		
		configMap.put(BusinessType.COMMENT_XOA, config);
	}
	
	static {
		AlarmConfig config = new AlarmConfig();
		config.setBusinessName("comment-web");
		
		ArrayList<String> phoneList = new ArrayList<String>();
		phoneList.add("15210833640");
		phoneList.add("18612685646");
		config.setPhoneList(phoneList);
		
		ArrayList<String> emailList = new ArrayList<String>();
		emailList.add("meng.liu" + EMAIL_TAIL);
		config.setEmailList(emailList);
		
		configMap.put(BusinessType.COMMENT_WEB, config);
	}
	
	
	
	public static AlarmConfig getAlarmConfig(BusinessType businessType) {
		return configMap.get(businessType);
	}
	
	
	private String businessName;
	private ArrayList<String> phoneList;
	private ArrayList<String> emailList;
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public ArrayList<String> getPhoneList() {
		return phoneList;
	}
	public void setPhoneList(ArrayList<String> phoneList) {
		this.phoneList = phoneList;
	}
	public ArrayList<String> getEmailList() {
		return emailList;
	}
	public void setEmailList(ArrayList<String> emailList) {
		this.emailList = emailList;
	}
	

}
