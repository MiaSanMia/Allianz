package com.renren.ugc.comment.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.statistics.StatisticsHelper;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.xoa2.api.campus.model.CampusFeed;
import com.renren.xoa2.api.campus.model.IsAdminRequest;
import com.renren.xoa2.api.campus.model.IsAdminResponse;
import com.renren.xoa2.api.campus.service.IAdminService;
import com.renren.xoa2.client.ServiceFactory;
import com.xiaonei.xce.useranonymous.UserAnonymousAdapter;

/**
 * @author wangxx
 * 
 *         校园主页的一些shit功能
 * 
 */
public class AlbumPostUtil {

	private static final Logger logger = Logger.getLogger(AlbumPostUtil.class);
	
	public static final String RESOURCE_OWNERID_PATH = "/owner/id";
	
	public static final String TITLE_PATH  = "/title";
	
	public static final String THUMBURL_PATH = "/thumbUrl";
	
	public static final String SHARE_NAME_PATH = "/name";
	
	public static final String SHARE_TIME_PATH = "/time";
	
	public static final String SHARE_IMG_PATH = "/tinyimg";
	
	private static final HashMap<Integer,String> campusFeedMap = new HashMap<Integer,String>();
	
	static {
		campusFeedMap.put(301, "blog");		//校园主页新鲜事日志类型
		campusFeedMap.put(201, "album"); //校园主页新鲜事相册类型
		campusFeedMap.put(202, "photo"); //校园主页新鲜事照片类型
	}
	
	public static String getFeedStringType(int sType){
		return campusFeedMap.containsKey(sType) ? campusFeedMap.get(sType) : "";
	}

	public static boolean isValidAlbumPostType(CommentType type, int actorId,
			int entryOwnerId) {
		return type == CommentType.CampusAlbumPost && actorId != entryOwnerId;
	}

	public static List<Integer> buildAlbumPostAuthorIds(final int actorId,
			final int entryOwnerId) {
		return new ArrayList<Integer>() {
			{
				add(entryOwnerId);
				add(actorId);
			}
		};
	}

	public static List<Integer> buildAlbumPostToUserIds(final int actorId,
			final int entryOwnerId) {
		return new ArrayList<Integer>() {
			{
				add(entryOwnerId);
				add(actorId);
				add(0);
			}
		};
	}

	public <T> T getServiceXOA2(final Class<T> serviceClass, final int timeout) {
		return ServiceFactory.getService(serviceClass, timeout);
	}

	private final int XOA_TIMEOUT = 500;

	private IAdminService adminService;

	private static AlbumPostUtil instance = new AlbumPostUtil();

	public static AlbumPostUtil getInstance() {
		return instance;
	}

	private AlbumPostUtil() {
		adminService = getServiceXOA2(IAdminService.class, XOA_TIMEOUT);
	}

	public boolean canDeleteComment(CommentType type, int actorId,
			CommentStrategy strategy, int entryOwnerId, int authorId) {

		long start = System.nanoTime();
		boolean success = false;

		// 1判断entryOwnerId
		success = this.judgeByAnonymousId(actorId, entryOwnerId);
		if (success) {
			return success;
		}
		// 判断authorId
		success = this.judgeByAnonymousId(actorId, authorId);
		if (success) {
			return success;
		}

		// 2.init
		int schoolId = EntryConfigUtil.getInt(strategy,
				EntryConfig.ENTRY_SCHOOL_ID);
		int moduleId = EntryConfigUtil.getInt(strategy,
				EntryConfig.ENTRY_PARENT_ID);

		// 3.判断是不是"学校超级"管理员
		IsAdminRequest request = new IsAdminRequest();
		request.setSchoolId(schoolId);
		request.setModuleId(moduleId);
		request.setUserId(actorId);

		try {
			IsAdminResponse response = adminService.isAdmin(request);
			if (response.getBaseRep() != null
					&& response.getBaseRep().getErrorInfo() != null) {
				throw new UGCCommentException(response.getBaseRep()
						.getErrorInfo().getCode(), "campus isadmin error"
						+ response.getBaseRep().getErrorInfo().getMsg());
			}
			return response.isModuleAdmin || response.isSchoolAdmin
					|| response.isSuperAdmin;
		} catch (UGCCommentException ue) {
			throw ue;
		} catch (Exception e) {
			logger.error("campus  isadmin error|actorId:" + actorId
					+ "|schoolId:" + schoolId + "|moduleId:" + moduleId, e);
		}

		long end = System.nanoTime();
		StatisticsHelper.invokePostIsAdmin((end - start)
				/ StatisticsHelper.NANO_TO_MILLIS, success);

		return false;
	}

	private boolean judgeByAnonymousId(int actorId, int userId) {

		if (actorId == 0 || userId == 0) {
			return false;
		}

		try {
			if (UserAnonymousAdapter.getInstance().isAnonymous(userId)) {
				userId = UserAnonymousAdapter.getInstance().getRealUserId(
						userId);
			}
			if (actorId == userId) {
				if (logger.isDebugEnabled()) {
					logger.debug("AlbumPostUtil delete success by actorId = "
							+ actorId);
				}
				return true;
			}
		} catch (Exception e) {
			logger.error("AlbumPostUtil canDeleteComment error", e);
		}
		return false;
	}

	/**
	 * @param document
	 * @param feedNodeName
	 * @param path
	 * @return
	 * 
	 *  根据路径获取新鲜事内容
	 */
	public static String getFeedContentByPath(Document document, String feedNodeName, String path) {
		String content = "";
		if (document == null || StringUtils.isBlank(feedNodeName)) {
			return content;
		}

		List list = document.selectNodes("/f/" + feedNodeName + path);
		try {
			Iterator iterator = list.iterator();
			if (iterator.hasNext()) {
				Element e = (Element) iterator.next();
				content = e.getText();
			}
		} catch (Exception e) {
			logger.error(
					"getFeedResourceUserId error,path = " +  (feedNodeName + path) + ", list = " + list.toString(), e);
		}
		return content;
	}
	
	/**
	 * @param document
	 * @param feedNodeName
	 * @param path
	 * @return
	 * 
	 *  根据路径获取新鲜事内容
	 */
	public static String getFeedContentByPath(Document document,  String path) {
		String content = "";
		if (document == null) {
			return content;
		}

		List list = document.selectNodes("/f/" + path);
		try {
			Iterator iterator = list.iterator();
			if (iterator.hasNext()) {
				Element e = (Element) iterator.next();
				content = e.getText();
			}
		} catch (Exception e) {
			logger.error(
					"getFeedContentByPath error,path = " + path + ", list = " + list.toString(), e);
		}
		return content;
	}
	
	public static Document getFeedContentDocument(CampusFeed feed) {
        if (feed == null || StringUtils.isBlank(feed.getContent())) {
            return null;
        }

        try {
            Document document = DocumentHelper.parseText(feed.getContent());
            return document;
        } catch (DocumentException e) {
            logger.error(String.format("getContentDocument schoolId:%d|feedId:%d",
                    feed.getSchoolId(), feed.getId()), e);
        }
        return null;
    }
	
	public static  int getCampusRealUserId(int actorId) {
		int realId = 0;
		try {
			if (UserAnonymousAdapter.getInstance().isAnonymous(actorId)) {
				realId = UserAnonymousAdapter.getInstance().getRealUserId(
						actorId);
			}
		} catch (Exception e) {
			logger.error("AlbumPostUtil getCampusRealUserId error", e);
		}
		return realId;
	}

}
