package com.renren.ugc.comment.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.model.shortvideo.constants.IShortvideoServiceConstants;

/**
 * 生成url的md5值，取全站评论时会用到
 * @author lei.xu1
 * @since 2013-09-20
 */
public class UrlMd5Util {
	
	private static final Map<CommentType, String> urlPatternMap = new HashMap<CommentType, String>();
	
	static {
		urlPatternMap.put(CommentType.Blog, "http://blog.renren.com/GetEntry.do?id=%d&owner=%d");
		urlPatternMap.put(CommentType.Photo, "http://photo.renren.com/photo/%d/photo-%d");
		urlPatternMap.put(CommentType.Album, "http://photo.renren.com/photo/%d/album-%d");
		urlPatternMap.put(CommentType.ShortVideo, IShortvideoServiceConstants.TERMINAL_FORMAT_URL_STRING);
	}
	
	public static String getUrl(CommentType type, int entryOwnerId, long entryId) {
		String pattern = urlPatternMap.get(type);
		if(pattern == null) {
			return null;
		}
		//实属无奈，blog的url是entryId在前，entryOwnerId在后,跟其他业务的都不一样
		if(type == CommentType.Blog) {
			return String.format(urlPatternMap.get(type), entryId, entryOwnerId);
		}
		return String.format(urlPatternMap.get(type), entryOwnerId, entryId);
	}
	
	public static String getUrlMd5(String url) {
		if(url == null) {
			return null;
		}
		return DigestUtils.md5Hex(url);
	}
	
	public static String getUrlMd5(CommentType type, int entryOwnerId, long entryId) {
		String url = getUrl(type, entryOwnerId, entryId);
		return getUrlMd5(url);
	}
}
