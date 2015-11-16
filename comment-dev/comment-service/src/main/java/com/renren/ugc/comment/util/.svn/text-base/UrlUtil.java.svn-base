package com.renren.ugc.comment.util;

import com.xiaonei.platform.core.opt.OpiConstants;
import com.xiaonei.platform.storage.Allocator;

/**
 * @author wangxx
 * 
 * url工具类
 *
 */
public class UrlUtil {
	
	private static final String EMPTY_STRING = "";

	private UrlUtil() {
	}
	
	/**
	 * 根据相对路径拼出绝对路径
	 *
	 * @param path
	 * @return
	 */
	public static String getFullUrl(String path) {
		if (path == null || path.equals("")) {
			return EMPTY_STRING;
		}
		String url = null;
		try {
			url = Allocator.getInstance().locate(path);
			if (url == null) {
				if (path.startsWith("5q")) {
					String flag = path.substring(3);
					String domain = getPhotoDomain(flag);
					url = domain + "/" + flag;
				} else {
					if (path.startsWith("pic001")) {
						url = OpiConstants.urlPic001 + "/" + path;
					} else if (path.startsWith("pic002")) {
						url = OpiConstants.urlPic002 + "/" + path;
					} else {
						url = OpiConstants.urlImg + "/photos/" + path;
						try {
							String abc = path.substring(0, 8);
							if (Integer.parseInt(abc) >= 20070101) {
								url = OpiConstants.urlHead + "/photos/" + path;
							}
						} catch (Exception e) {
							// e.printStackTrace();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (url == null) {
			return EMPTY_STRING;
		}
		return url;
	}
	
	private static String getPhotoDomain(String flag) {
		if (flag.startsWith("c02")) {
			return OpiConstants.urlPhoto2Album;
		} else {
			return OpiConstants.urlPhotoAlbum;
		}
	}

}
