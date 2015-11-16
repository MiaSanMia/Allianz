package com.renren.ugc.comment.util;

import com.xiaonei.platform.component.xfeed.helper.FeedDefinition;

/**
 * @author wangxx
 *
 *  新鲜事工具类
 */
public class FeedUtil {
	
	/**
	 * 短视频新鲜事类型
	 */
	public static final int FEED_TYPE_SHORT_VIDEO = 1011;
	
	/**
	 * 相册新鲜赛类型
	 */
	public static final int FEED_TYPE_ALBUM = FeedDefinition.PHOTO_PUBLISH_MORE;
	
	/**
	 * 日志新鲜事类型
	 */
	public static final int FEED_TYPE_BLOG = FeedDefinition.BLOG_PUBLISH;
	
	/**
	 * 照片回复新鲜事类型
	 */
	public static final int FEED_TYPE_PHOTO_REPLY = FeedDefinition.PHOTO_REPLY;

}
