package com.renren.ugc.comment.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentVoiceInfo;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.xoa2.api.ugc.voice.comment.model.CreateVoiceCommentControlParam;
import com.renren.xoa2.api.ugc.voice.comment.model.CreateVoiceCommentParam;
import com.renren.xoa2.api.ugc.voice.comment.model.CreateVoiceCommentRequest;
import com.renren.xoa2.api.ugc.voice.comment.model.CreateVoiceCommentResponse;
import com.renren.xoa2.api.ugc.voice.comment.model.GetVoiceCommentBuildParam;
import com.renren.xoa2.api.ugc.voice.comment.model.GetVoiceCommentListParam;
import com.renren.xoa2.api.ugc.voice.comment.model.GetVoiceCommentsListRequest;
import com.renren.xoa2.api.ugc.voice.comment.model.GetVoiceCommentsListResponse;
import com.renren.xoa2.api.ugc.voice.comment.model.UGCTYPE;
import com.renren.xoa2.api.ugc.voice.comment.model.VoiceComment;
import com.renren.xoa2.api.ugc.voice.comment.service.IVoiceCommentCenterService;
import com.renren.xoa2.client.ServiceFactory;

/**
 * @author wangxx
 * 
 *         用来获取各个业务评论的"语音"信息
 * 
 */
// TODO to be deleted!!!
public class CommentVoiceInfoUtil {

    private static final Logger logger = Logger.getLogger(CommentLikeInfoUtil.class);

    private static final String VOICE_DEFAULT_CONTENT = "这是一条语音评论";

    /**
     * key : {@link: CommentType} ,value则是语音评论中心所需要的type {@link: UGCTYPE}
     */
    private Map<CommentType, UGCTYPE> voiceCommentMap = new HashMap<CommentType, UGCTYPE>();

    private static CommentVoiceInfoUtil instance = new CommentVoiceInfoUtil();

    private CommentVoiceInfoUtil() {
        //init
        voiceCommentMap.put(CommentType.Status, UGCTYPE.STATUS);
        voiceCommentMap.put(CommentType.Share, UGCTYPE.SHARE);
        voiceCommentMap.put(CommentType.VoiceStatus, UGCTYPE.VOICE_STATUS);
        voiceCommentMap.put(CommentType.Photo, UGCTYPE.PHOTO);
    }

    public static CommentVoiceInfoUtil getInstance() {
        return instance;
    }

    /**
     * @param type :{@link CommentType} type
     * @param comment : the new comment
     * @param ownerId :entryOwnerId
     * @param entryId : entryId
     * @throws UGCCommentException
     * 
     *         调用语音评论中心的接口，创建一条新的语音评论
     */
    public void createVoiceComment(CommentType type, Comment comment, int ownerId, long entryId)
            throws UGCCommentException {

        if (comment == null || comment.getVoiceInfo() == null) {
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("getCommentVoiceInfo type = " + type);
        }

        //1.判断voiceCommentMap有没有
        UGCTYPE commentType = voiceCommentMap.get(type);

        if (commentType == null) {
            logger.warn(" found  no match commentType in voiceCommentMap type = " + type);
            return;
        }

        CreateVoiceCommentRequest req = new CreateVoiceCommentRequest();
        // 2.组装对象
        CreateVoiceCommentParam doingCommentParam = new CreateVoiceCommentParam();
        doingCommentParam.setLength(comment.getVoiceInfo().getVoiceLength());
        doingCommentParam.setSize(comment.getVoiceInfo().getVoiceSize());
        doingCommentParam.setRate(comment.getVoiceInfo().getVoiceRate());
        doingCommentParam.setUrl(comment.getVoiceInfo().getVoiceUrl());
        doingCommentParam.setOwnerId(ownerId);
        doingCommentParam.setCommentId(comment.getId());
        doingCommentParam.setType(commentType);
        doingCommentParam.setUserId(comment.getAuthorId());
        doingCommentParam.setSourceId(entryId);

        req.setCreateVoiceCommentControlParam(new CreateVoiceCommentControlParam());
        req.setGetVoiceCommentBuildParam(new GetVoiceCommentBuildParam());
        req.setCreateVoiceCommentParam(doingCommentParam);

        //3.调用语音评论中心服务
        try {
            IVoiceCommentCenterService voiceCommentService = ServiceFactory.getService(IVoiceCommentCenterService.class);

            CreateVoiceCommentResponse response = voiceCommentService.createVoiceComment(req);
            if (response.getBaseRep() != null && response.getBaseRep().getErrorInfo() != null) {
                //logger.error("createVoiceComment error|"+response.getBaseRep().getErrorInfo().getCode()
                //        +response.getBaseRep().getErrorInfo().getMsg());
                throw new UGCCommentException("createVoiceComment  error|"
                        + response.getBaseRep().getErrorInfo().getCode()
                        + response.getBaseRep().getErrorInfo().getMsg());
            } else {
                VoiceComment voice = response.getVoiceComment();
                if (voice != null) {

                    CommentVoiceInfo voiceInfo = new CommentVoiceInfo();
                    voiceInfo.setVoiceLength(voice.getLength());
                    voiceInfo.setVoiceRate(voice.getRate());
                    voiceInfo.setVoiceSize(voice.getSize());
                    //语音评论还有可能是绝对路径o(╯□╰)o
                    voiceInfo.setVoiceUrl(voice.getUrl().startsWith("http://") ? voice.getUrl()
                            : UrlUtil.getFullUrl(voice.getUrl()));

                    comment.setVoiceInfo(voiceInfo);
                }
            }

        } catch (Exception e) {
            // logger.error("createVoiceComment error|entryId:"+entryId+",ownerId:"+ownerId+"type:"+type.getValue(),e);
            throw new UGCCommentException("createVoiceComment  error", e);
        }
    }

    /**
     * @param type :{@link CommentType} type
     * @param ugcComments : a list of comments
     * @param ownerId :entryOwnerId
     * @param entryId : entryId
     * @throws UGCCommentException
     * 
     *         获取到的语音信息放在{@link : Comment} voiceInfo中
     */
    public void getCommentVoiceInfo(CommentType type, List<Comment> ugcComments, int ownerId,
            long entryId) throws UGCCommentException {

        //1.把非语音评论过滤掉
        List<Long> voiceIds = new ArrayList<Long>();
        for (Comment c : ugcComments) {
            if (c.isVoiceComment()) {
                voiceIds.add(c.getIdIfHasOldUgc());
            }
        }
        
        if (voiceIds.size() == 0) {
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("getCommentVoiceInfo type = " + type);
        }

        //2.判断voiceCommentMap有没有
        UGCTYPE commentType = voiceCommentMap.get(type);

        if (commentType == null) {
            logger.warn(" found  no match commentType in voiceCommentMap type = " + type);
            return;
        }

        //3.调用"语音评论中心"的接口
        GetVoiceCommentsListRequest req = new GetVoiceCommentsListRequest();
        GetVoiceCommentListParam getVoiceCommentParam = new GetVoiceCommentListParam();
        getVoiceCommentParam.setOwnerId(ownerId);

        getVoiceCommentParam.setCommentIds(voiceIds);
        getVoiceCommentParam.setType(commentType);
        getVoiceCommentParam.setSourceId(entryId);
        req.setGetVoiceCommentBuildParam(new GetVoiceCommentBuildParam());
        req.setGetVoiceCommentParam(getVoiceCommentParam);

        try {
            IVoiceCommentCenterService voiceCommentService = ServiceFactory.getService(IVoiceCommentCenterService.class);

            GetVoiceCommentsListResponse response = voiceCommentService.getVoiceCommentsList(req);
            if (response.getBaseRep() != null && response.getBaseRep().getErrorInfo() != null) {
                logger.error("getVoiceCommentsList error|"
                        + response.getBaseRep().getErrorInfo().getCode()
                        + response.getBaseRep().getErrorInfo().getMsg());
            } else {
                if (response.getVoiceComments() != null) {
                    //4.把结果写入到Comment中去
                    // 这里由于没有返回结果可能顺序与以前的不一致，因此采用了双层循环，由于量很少，因此效率可以接受
                    VoiceComment voice = null;
                    for (int i = 0; i < ugcComments.size(); ++i) {
                        for (int j = 0; j < response.getVoiceComments().size(); ++j) {
                            if (ugcComments.get(i) != null
                                    && (voice = response.getVoiceComments().get(j)) != null) {
                                if (ugcComments.get(i).getIdIfHasOldUgc() == response.getVoiceComments().get(
                                        j).getId()) {

                                    CommentVoiceInfo voiceInfo = new CommentVoiceInfo();
                                    voiceInfo.setVoiceLength(voice.getLength());
                                    voiceInfo.setVoiceRate(voice.getRate());
                                    voiceInfo.setVoiceSize(voice.getSize());
                                    //语音评论还有可能是绝对路径o(╯□╰)o
                                    voiceInfo.setVoiceUrl(voice.getUrl().startsWith("http://") ? voice.getUrl()
                                            : UrlUtil.getFullUrl(voice.getUrl()));

                                    ugcComments.get(i).setVoiceInfo(voiceInfo);
                                    ugcComments.get(i).setContent(VOICE_DEFAULT_CONTENT);
                                    break;
                                }
                            } // end for not null
                        } // end for j loop
                    } // end for i loop
                } // end for response.getVoiceComments() != null
            }
        } catch (Exception e) {
            logger.error("getVoiceCommentsList error|entryId:" + entryId + ",ownerId:" + ownerId
                    + ",type:" + type.getValue(), e);
        }
    }

}
