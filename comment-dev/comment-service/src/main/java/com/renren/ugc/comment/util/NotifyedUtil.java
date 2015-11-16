package com.renren.ugc.comment.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.xoa2.CommentType;
import com.xiaonei.platform.core.model.User;
import com.xiaonei.platform.core.opt.ice.WUserAdapter;
import com.xiaonei.xce.notify.NotifyAdapter;
import com.xiaonei.xce.notify.NotifyBody;

/**
 * @author wangxx 
 * "被"评论发送通知工具类
 */
public class NotifyedUtil {

    private Logger logger = Logger.getLogger(this.getClass());

    private static NotifyedUtil instance = new NotifyedUtil();

    private Map<CommentType, NotifyInfo> noticeInfoMap =
            new HashMap<CommentType, NotifyInfo>();

    private NotifyedUtil(){
        noticeInfoMap.put(CommentType.CampusPost, new NotifyInfo(NotifyConstants.CAMPUS_REPLYED, NotifyConstants.CAMPUS_REPLYED));
        noticeInfoMap.put(CommentType.CampusAlbumPost, new NotifyInfo(NotifyConstants.CAMPUS_ALBUM_REPLYED_OTHER, NotifyConstants.CAMPUS_ALBUM_REPLYED_OTHER));
    }

    public static NotifyedUtil getInstance() {
        return instance;
    }

    public boolean sendNotice(CommentType type, int actorId, int entryOwnerId,
        long entryId, Comment comment, CommentStrategy strategy) {

        if (comment == null) {
            return false;
        }

        // 1.get type and schemaId, type = schemId,so get only one just ok
        NotifyInfo notifyInfo = noticeInfoMap.get(type);
        if (notifyInfo == null) {
            logger.warn("sendNotice gettype from noticeInfoMap is null | type:"
                        + type);
            return false;
        }

        // 2.buildNotifyBody
        NotifyBody notifyBody =
                this.buildNotifyBody(type, actorId, entryOwnerId, entryId,
                    comment, notifyInfo, strategy);

        // 3.send
        try {
            if (notifyBody != null) {
                NotifyAdapter.getInstance().dispatch(notifyBody);
            }
        } catch (Exception e) {
            logger.error("NotifyedUtil sendNotice error:", e);
            return false;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("the comment created notification is sent");
        }
        return true;
    }
    
    /**
     * @param type
     * @param actorId
     * @param entryOwnerId
     * @param entryId
     * @param comment
     * @return 由于每个业务的通知走的模板都不一样，而且key值也不一样，因此这里做个统一
     */
    private NotifyBody buildNotifyBody(CommentType type, int actorId,
        int entryOwnerId, long entryId, Comment comment, NotifyInfo notifyInfo,
        CommentStrategy strategy) {

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

        boolean ret = false;
        // 2. build notifyBody
        switch (type) {
            	
            case CampusPost:
            	ret = buildCampusNotifyedBody(actorId, entryOwnerId, entryId, comment, notifyBody, strategy);
            	break;
            case CampusAlbumPost:
            	ret = buildCampusAlbumNotifyedBody(actorId, entryOwnerId, entryId, comment, notifyBody, strategy);
            	break;
            default:
                logger.warn("no valid type in getBusiParams | type:" + type);
        }
        
        return ret ? notifyBody : null;

    }
    
    /**
     * @param actorId
     * @param entryOwnerId
     * @param entryId
     * @param comment
     * @param notifyBody
     * @param strategy
     * @return
     * 
     *  构建校园论坛的notifyBody
     */
    private boolean buildCampusNotifyedBody(int actorId, int entryOwnerId,
            long entryId, Comment comment, NotifyBody notifyBody,
            CommentStrategy strategy) {

            notifyBody.setValue("from_name", comment.getAuthorName());
//            notifyBody.setValue("title",
//                EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_TITLE));
           //加上评论id
            notifyBody.setValue("title_url",
                    EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_URL)+"?comment="+comment.getId());
            
            //因为评论里可能有图片，评论这里自己解析区分"文字"和"图片"
            //2014-04-09,传给消息中心的imgUrl字段表示帖子的第一张图片
            String text = ImgJsonUtil.getCampusText(comment.getContent());
            //String imgUrl = ImgJsonUtil.getCampusPic(comment.getContent());
            String imgUrl =  EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_HEADURL);
            
            notifyBody.setValue("reply_content",text);
            notifyBody.setValue("imgUrl",imgUrl);
            notifyBody.setValue("floor", comment.getId()+"");
            
           //2014-04-22当标题为空时,截取帖子内容的最前的一段字符,填充到话题的标题
            String title =  EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_TITLE);
            if(StringUtils.isEmpty(title)){
            	String content = EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_CONTENT);
            	int trimLength = content.length() > CommentCenterConsts.MAX_CAMPUS_TRIM_LENGTH ?  CommentCenterConsts.MAX_CAMPUS_TRIM_LENGTH : content.length();
            	title = content.substring(0, trimLength);
            }
            notifyBody.setValue("title",title);

            Set<Integer> toIdSet = new HashSet<Integer>();
            if (comment.getToUserId() > 0) {
                toIdSet.add(comment.getToUserId());
            }
            //toIdSet.add(entryOwnerId);

            for (Integer toId : toIdSet) {
                notifyBody.addToid(toId);
            }

            return true;
        }
    
    /**
     * @param actorId
     * @param entryOwnerId
     * @param entryId
     * @param comment
     * @param notifyBody
     * @param strategy
     * @return
     * 
     *  构建校园贴图论坛的notifyBody
     */
    private boolean buildCampusAlbumNotifyedBody(int actorId, int entryOwnerId,
            long entryId, Comment comment, NotifyBody notifyBody,
            CommentStrategy strategy) {

            notifyBody.setValue("from_name", comment.getAuthorName());
            notifyBody.setValue("title",
                EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_TITLE));
            //加上评论id
            notifyBody.setValue("title_url",
                    EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_URL)+"?comment="+comment.getId());
            
            //因为评论里可能有图片，评论这里自己解析区分"文字"和"图片"
            //2014-04-09,传给消息中心的imgUrl字段表示帖子的第一张图片
            String text = ImgJsonUtil.getCampusText(comment.getContent());
            //String imgUrl = ImgJsonUtil.getCampusPic(comment.getContent());
            String imgUrl =  EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_HEADURL);
            
            notifyBody.setValue("reply_content",text);
            notifyBody.setValue("imgUrl",imgUrl);
            notifyBody.setValue("floor", comment.getId()+"");
            
            //取得ownerId的名字
            User host = WUserAdapter.getInstance().get(entryOwnerId);
            notifyBody.setValue("other_name",host.getName());

            Set<Integer> toIdSet = new HashSet<Integer>();
            if (comment.getToUserId() > 0) {
                toIdSet.add(comment.getToUserId());
            }
            //toIdSet.add(entryOwnerId);

            for (Integer toId : toIdSet) {
                notifyBody.addToid(toId);
            }

            return true;
        }
    

}