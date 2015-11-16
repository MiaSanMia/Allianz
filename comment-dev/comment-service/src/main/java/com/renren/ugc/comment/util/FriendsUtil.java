package com.renren.ugc.comment.util;

import java.util.ArrayList;
import java.util.List;

import com.xiaonei.xce.buddyadapter.buddybyidcache.BuddyByIdCacheAdapter;

/**
 * 获取用户好友
 * @author lei.xu1
 *
 */
public class FriendsUtil {

	/** 一次获取好友的数量 **/
	private static final int ONCE_GET_FRIENDS_COUNT = 3000;
	
	public static List<Integer> getAllFriends(int userId) {
		List<Integer> friendsList = new ArrayList<Integer>();
		int offset = 0;
		List<Integer> onceList = BuddyByIdCacheAdapter.getInstance()
				.getFriendListAsc(userId, offset, ONCE_GET_FRIENDS_COUNT);
		while (onceList.size() >= ONCE_GET_FRIENDS_COUNT) {
			offset = offset + onceList.size();
			friendsList.addAll(onceList);
			onceList = BuddyByIdCacheAdapter.getInstance().getFriendListAsc(
					userId, offset, ONCE_GET_FRIENDS_COUNT);
		}
		if (onceList.size() != 0) {
			friendsList.addAll(onceList);
		}
		return friendsList;
	}
}
