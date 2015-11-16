package com.renren.ugc.comment.storm.alarm.strategy;
/** 
 * @author meng.liu 
 * @date 2014-5-23 下午5:56:45 
 */
public enum BusinessType {
	
	COMMENT_XOA("comment-xoa"),
	COMMENT_WEB("comment-web"),
	LIKE_XOA("like-xoa"),
	LIKE_WEB("like-web"),
	SHARE_XOA("share-xoa"),
	SHARE_WEB("share-web");
	
	private String business;
	
	private BusinessType(String business) {
		this.business = business;
	}
	
	public static BusinessType getBusinessType(String business) {
		
		for(BusinessType entry:BusinessType.values()) {
			if(business.equals(entry.business)) {
				return entry;
			}
		}
		return null;
		
	}

}
