package com.renren.ugc.comment.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.util.share.ShareMatteredUgcInfoBeanBuilder;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.matter.model.MatterActionType;
import com.renren.ugc.matter.model.MatteredUgcInfo;
import com.renren.ugc.matter.model.UgcType;
import com.renren.ugc.matter.xoa.api.MatterToMeService;
import com.renren.xoa.lite.ServiceFactories;
import com.renren.xoa.lite.ServiceFuture;

/**
 * @author wangxx
 * 
 *         与我相关相关操作
 * 
 */
public class MatterToMeUtil {

    private static final Logger logger = Logger.getLogger(CommentLikeInfoUtil.class);

    private Map<CommentType, Integer> matterUgcTypeMap = new HashMap<CommentType, Integer>();

    private MatterToMeService matterToMeService = null;

    private static MatterToMeUtil instance = new MatterToMeUtil();

    private MatterToMeUtil() {
        matterToMeService = ServiceFactories.getFactory().getService(MatterToMeService.class);
        //init
        matterUgcTypeMap.put(CommentType.Photo, UgcType.PHOTO.ordinal());
        matterUgcTypeMap.put(CommentType.Blog, UgcType.BLOG.ordinal());
        matterUgcTypeMap.put(CommentType.Status, UgcType.STATUS.ordinal());
        matterUgcTypeMap.put(CommentType.Album, UgcType.ALBUM.ordinal());
    }

    public static MatterToMeUtil getInstance() {
        return instance;
    }

    /**
     * @param type
     * @param comment
     * @param entryOwnerId
     * @param entryId
     * @param actorId
     * @param userIdList
     * 
     *        保存"与我相关"的数据
     */
    public boolean saveMatterTomeData(CommentType type, Comment comment, int entryOwnerId,
            long entryId, int actorId, List<Integer> userIdList, CommentStrategy strategy) {

        MatteredUgcInfo matteredUgcInfo = getCommentMatterUgcInfo(type, comment, entryOwnerId,
                entryId, userIdList, strategy);
        try {
            ServiceFuture<Void> serviceFuture = matterToMeService.saveData(actorId, matteredUgcInfo);
            XoaClientAdapter.doSubmit2(serviceFuture);
        } catch (Exception e) {
            logger.error("MatterToMeUtil saveData error | entryOwnerId:" + entryOwnerId + ",entryId"
                    + entryId, e);
            return false;
        }
        return true;
    }

    private MatteredUgcInfo getCommentMatterUgcInfo(CommentType type, Comment comment,
            int entryOwnerId, long entryId, List<Integer> userIdList, CommentStrategy strategy) {

        //1. get control and isEncrypt
        int control = EntryConfigUtil.getInt(strategy, EntryConfig.ENTRY_CONTROL);
        boolean isEncrypt = EntryConfigUtil.getBoolean(strategy,
                EntryConfig.ENTRY_IS_PASSWORD_PROTECTED);

        MatteredUgcInfo matteredUgcInfo = new MatteredUgcInfo();
        if (type == CommentType.Share || type == CommentType.ShareAlbumPhoto) {
            matteredUgcInfo = ShareMatteredUgcInfoBeanBuilder.build(comment, entryOwnerId, entryId,
                    userIdList, strategy);
        } else {
            //1. get control and isEncrypt
            //int control = EntryConfigUtil.getControl(comment);
            //boolean isEncrypt = EntryConfigUtil.getIsPasswordProtected(comment);

            matteredUgcInfo.setActionType(MatterActionType.REPLAY);
            matteredUgcInfo.setAuthType(control);

            matteredUgcInfo.setCommentAuthorId(comment.getAuthorId());
            matteredUgcInfo.setCommentedUgcAuthorId(comment.getToUserId());
            matteredUgcInfo.setCommentedUgcId(comment.getToCommentId());
            matteredUgcInfo.setCommentId(comment.getId());
            //没有
            matteredUgcInfo.setEncrypt(isEncrypt);

            if (userIdList != null && userIdList.size() != 0) {
                matteredUgcInfo.setAtUserIdList(userIdList);
            }
            matteredUgcInfo.setWhisper(comment.getWhipserToId() > 0);
            matteredUgcInfo.setOwnerId(entryOwnerId);
            matteredUgcInfo.setUgcId(entryId);
            matteredUgcInfo.setUgcType(matterUgcTypeMap.get(type));
        }

        return matteredUgcInfo;
    }

}
