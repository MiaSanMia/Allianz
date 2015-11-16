package com.renren.ugc.comment.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.service.CommentLogic;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.commons.tools.user.UserUtils;
import com.xiaonei.antispam.AntiSpamUtils;
import com.xiaonei.xce.notify.NotifyAdapter;
import com.xiaonei.xce.notify.NotifyBody;

/**
 * @author wangxx
 * 
 *         "也评论"工具类,之后评论的人向之前评论过该条ugc的好友发送提醒
 * 
 */
public class NotifyAlsoUtil {

	private Logger logger = Logger.getLogger(this.getClass());

	/** 状态评论提醒的log统计 **/
	private static Logger commentNotifyLog = Logger
			.getLogger("comment_notify_log");

	private static NotifyAlsoUtil instance = new NotifyAlsoUtil();

	private Map<CommentType, NotifyInfo> noticeInfoMap = new HashMap<CommentType, NotifyInfo>();

	private NotifyAlsoUtil() {
		noticeInfoMap.put(CommentType.Status, new NotifyInfo(541, 541));
		noticeInfoMap.put(CommentType.CampusPost, new NotifyInfo(1083, 1083));
		noticeInfoMap.put(CommentType.CampusAlbumPost, new NotifyInfo(1084,
				1084));
	}

	public static NotifyAlsoUtil getInstance() {
		return instance;
	}

	public boolean sendAlsoNotice(CommentType type, int actorId,
			int entryOwnerId, long entryId, Comment comment,
			CommentStrategy strategy) {

		if (comment == null) {
			return false;
		}

		// 1.get type and schemaId
		NotifyInfo notifyInfo = noticeInfoMap.get(type);
		if (notifyInfo == null) {
			logger.warn("sendAlsoNotice gettype from noticeInfoMap is null | type:"
					+ type);
			return false;
		}

		// 2.build body
		NotifyBody notifyBody = this.buildNotifyBody(type, actorId,
				entryOwnerId, entryId, comment, notifyInfo, strategy);

		// 3.send
		try {
			if (notifyBody != null) {
				NotifyAdapter.getInstance().dispatch(notifyBody);
			}
		} catch (Exception e) {
			logger.error("NotifyAlsoUtil sendNoticeForCommentedFriends error:",
					e);
			return false;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("the comment created notification is sent");
		}
		return true;
	}

	private NotifyBody buildNotifyBody(CommentType type, int actorId,
			int entryOwnerId, long entryId, Comment comment,
			NotifyInfo notifyInfo, CommentStrategy strategy) {

		if (comment == null || notifyInfo == null) {
			return null;
		}

		NotifyBody notifyBody = new NotifyBody();

		// 1.notify的共用部分
		notifyBody.setType(notifyInfo.getType());
		notifyBody.setSchemaId(notifyInfo.getSchemaId());
		notifyBody.setFromId(actorId);
		notifyBody.setTime(comment.getCreatedTime());
		notifyBody.setOwner(entryOwnerId);
		notifyBody.setSource(entryId);

		// 2.填充好友
		fillNotifyBodyForCommentedFriends(type, actorId, entryOwnerId, entryId,
				comment, notifyInfo, strategy, notifyBody);

		boolean ret = false;
		// 3. 填充每个业务特有字段
		switch (type) {
		case Status:
			ret = buildStatusNotifyBody(actorId, entryOwnerId, entryId,
					comment, strategy, notifyBody);
			break;
		case CampusPost:
		case CampusAlbumPost:
			ret = buildCampusAlsoNotifyBody(actorId, entryOwnerId, entryId,
					comment, strategy, notifyBody);
			break;
		default:
			logger.warn("no valid type in getBusiParams | type:" + type);
		}

		return ret ? notifyBody : null;
	}

	private boolean buildStatusNotifyBody(int actorId, int entryOwnerId,
			long entryId, Comment comment, CommentStrategy strategy,
			NotifyBody notifyBody) {

		if (comment == null || notifyBody == null) {
			return false;
		}

		notifyBody.setValue("from_name", comment.getAuthorName());
		notifyBody.setValue("owner_name", UserUtils.getUser(entryOwnerId)
				.getName());
		notifyBody.setValue("doing_id", String.valueOf(entryId));
		notifyBody.setValue(
				"doing_content",
				AntiSpamUtils.getInstance().htmlSecurityEscape(
						strategy.getEntry().getEntryProps()
								.get(EntryConfig.ENTRY_CONTENT)));
		notifyBody.setValue("replied_id", String.valueOf(comment.getId()));
		notifyBody.setValue("feed_stype", strategy.getEntry().getEntryProps()
				.get(EntryConfig.ENTRY_STYPE));
		notifyBody.setValue("feed_source", String.valueOf(entryId));
		notifyBody.setValue("feed_actor", String.valueOf(entryOwnerId));
		notifyBody.setValue("from_pic", comment.getAuthorHead());
		notifyBody.setValue("reply_content", comment.getContent());

		return true;
	}

	/**
	 * @param type
	 * @param actorId
	 * @param entryOwnerId
	 * @param entryId
	 * @param comment
	 * @param notifyInfo
	 * @param strategy
	 * @param notifyBody
	 * @return
	 * 
	 *         获取"好友"填充到"也评论"的notifyBody中去
	 * 
	 */
	private NotifyBody fillNotifyBodyForCommentedFriends(CommentType type,
			int actorId, int entryOwnerId, long entryId, Comment comment,
			NotifyInfo notifyInfo, CommentStrategy strategy,
			NotifyBody notifyBody) {

		if (notifyBody == null) {
			return null;
		}

		CommentLogic commentLogic = strategy.getCommentLogic();
		List<Integer> friends = commentLogic.getFriendsList(actorId, entryId,
				entryOwnerId, null, type);

		// 根据产品策略，一天只能发5次，这里要过滤
		// jira地址: http://jira.d.xiaonei.com/browse/COMMENT-1
		List<Integer> validUserIds = commentLogic.filterAuthorByFrequency(type,
				actorId, entryId, entryOwnerId, comment.getToUserId(), friends,
				strategy);

		if (CollectionUtils.isEmpty(validUserIds)) {
			logger.warn("send friend notify but userids list is empty|actorId:"
					+ actorId + "|entryId:" + entryId + "|entryOwnerId:"
					+ entryOwnerId + "|type:" + type);
			return null;
		}

		for (Integer toTolder : validUserIds) {
			notifyBody.addToid(toTolder);
			if (type.getValue() == CommentType.Status.getValue()) {
				commentNotifyLog.info("addToid " + actorId + " " + toTolder);
			}
		}
		return notifyBody;
	}

	public static boolean isSupportAlsoNotice(CommentType type) {
		return instance.noticeInfoMap.containsKey(type);
	}

	private boolean buildCampusAlsoNotifyBody(int actorId, int entryOwnerId,
			long entryId, Comment comment, CommentStrategy strategy,
			NotifyBody notifyBody) {

		if (comment == null || notifyBody == null) {
			return false;
		}
		
		// 因为评论里可能有图片，评论这里自己解析区分"文字"和"图片"
		String text = ImgJsonUtil.getCampusText(comment.getContent());
		String imgUrl = EntryConfigUtil.getString(strategy,EntryConfig.ENTRY_HEADURL);
		// 2014-04-22当标题为空时,截取帖子内容的最前的一段字符,填充到话题的标题
		String title = EntryConfigUtil.getString(strategy,EntryConfig.ENTRY_TITLE);
		if (StringUtils.isEmpty(title)) {
			String content = EntryConfigUtil.getString(strategy,EntryConfig.ENTRY_CONTENT);
			title = StringUtils.left(content, CommentCenterConsts.MAX_ALSO_COMMENT_COUNT);
		}

		notifyBody.setValue("from_name", comment.getAuthorName());
		notifyBody.setValue("owner_name", UserUtils.getUser(entryOwnerId).getName());
		notifyBody.setValue("title_url",EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_URL)+ "?comment=" + comment.getId());
		notifyBody.setValue("reply_content", text);
		notifyBody.setValue("imgUrl", imgUrl);
		notifyBody.setValue("floor", comment.getId() + "");
		notifyBody.setValue("title", title);

		return true;
	}

}
