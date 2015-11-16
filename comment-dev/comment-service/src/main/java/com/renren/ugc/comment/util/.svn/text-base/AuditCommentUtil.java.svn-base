package com.renren.ugc.comment.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.xoa2.CommentType;
import com.xiaonei.audit.api.AuditApiClient;
import com.xiaonei.audit.api.AuditApiException;
import com.xiaonei.audit.api.AuditApiPolicy;
import com.xiaonei.audit.api.AuditApiService;
import com.xiaonei.audit.api.IAuditApiClient;
import com.xiaonei.audit.api.IAuditApiClient.VoiceCommentUgcType;

/**
 * 送审的封装类
 */
/**
 * @author wangxx
 * 
 */
@Service
public class AuditCommentUtil implements InitializingBean {

    @Autowired
    private AuditApiService auditApiClient;

    private final IAuditApiClient auditApiClientXOA2;

    private static AuditCommentUtil instance;

    private Map<CommentType, VoiceCommentUgcType> voiceAuditTypeMap = new HashMap<CommentType, VoiceCommentUgcType>();

    public static AuditCommentUtil getInstance() {
        return instance;
    }

    public final Logger logger = Logger.getLogger(this.getClass());

    /**
     * 语音评论的审核阀值
     */
    public static final int VOICE_COMMENT_AUDIT_LIMIT = 50;

    private AuditCommentUtil() {
        voiceAuditTypeMap.put(CommentType.Status, VoiceCommentUgcType.Status);
        voiceAuditTypeMap.put(CommentType.Share, VoiceCommentUgcType.Share);
        voiceAuditTypeMap.put(CommentType.VoiceStatus, VoiceCommentUgcType.VoiceStatus);
        voiceAuditTypeMap.put(CommentType.Photo, VoiceCommentUgcType.Photo);

        this.auditApiClientXOA2 = AuditApiClient.getInstance(1000); // 超时设置为1000毫秒
    }

    // the code to get audit api when using XOA2. now we're using the jar audit lib
    //    private static IAuditApiClient getAuditApiClient() {
    //        if (auditApiClient == null) {
    //            auditApiClient = AuditApiClient.getInstance(1000);//超时是1m
    //        }
    //        return auditApiClient;
    //    }

    /**
     * 送审
     * 
     * @param comm
     * @param strategy
     * @param auditReplyType
     * @Param
     */
    public boolean doAudit(CommentType type, Comment comm, CommentStrategy strategy) {
        logger.info("do online audit:" + comm.getEntry().getId());
        boolean auditResult = false;
        try {

            switch (type) {
                case Generic:
                    auditResult = auditApiClient.auditFeedComment(AuditApiPolicy.PostFirst,
                            comm.getAuthorId(), new Date(), comm.getEntry().getId(),
                            comm.getEntry().getOwnerId(), comm.getEntry().getType(), comm.getId(),
                            comm.getContent(), "", strategy.getParams());
                    break;
                case SongBookComment:

                    auditResult = auditApiClient.auditMusicBookComment(AuditApiPolicy.PostFirst,
                            comm.getAuthorId(), new Date(), comm.getEntry().getId(),
                            comm.getEntry().getOwnerId(), comm.getId(), comm.getContent());
                    break;
                case Video:
                    auditResult = auditApiClient.auditVideoComment(AuditApiPolicy.PostFirst,
                            comm.getAuthorId(), new Date(), comm.getEntry().getId(),
                            comm.getEntry().getOwnerId(), comm.getId(), comm.getContent());
                    break;
                case Blog:
                    auditResult = auditApiClientXOA2.auditCommentCenter(AuditApiPolicy.PostFirst,
                            comm.getAuthorId(), new Date(), comm.getId(), buildAuditString(comm),
                            type.ordinal(), comm.getContent(), comm.getAuthorName(),
                            comm.getAuthorHead(), comm.getOriginalContent(), comm.getWhipserToId(),
                            comm.getToUserId(), "");
                    break;
                case Status:
                    auditResult = auditApiClientXOA2.auditCommentCenter(AuditApiPolicy.PostFirst,
                            comm.getAuthorId(), new Date(), comm.getId(), buildAuditString(comm),
                            type.ordinal(), comm.getContent(), comm.getAuthorName(),
                            comm.getAuthorHead(), comm.getOriginalContent(), comm.getWhipserToId(),
                            comm.getToUserId(), "");
                    break;
                case Share:
                case ShareAlbumPhoto:
                    auditResult = auditApiClientXOA2.auditCommentCenter(AuditApiPolicy.PostFirst,
                            comm.getAuthorId(), new Date(), comm.getId(), buildAuditString(comm),
                            type.ordinal(), comm.getContent(), comm.getAuthorName(),
                            comm.getAuthorHead(), comm.getOriginalContent(), comm.getWhipserToId(),
                            comm.getToUserId(), "");
                    break;
                case Album:
                    auditResult = auditApiClientXOA2.auditCommentCenter(AuditApiPolicy.PostFirst,
                            comm.getAuthorId(), new Date(), comm.getId(), buildAuditString(comm),
                            type.ordinal(), comm.getContent(), comm.getAuthorName(),
                            comm.getAuthorHead(), comm.getOriginalContent(), comm.getWhipserToId(),
                            comm.getToUserId(), "");
                    break;
                case ShortVideo:
                	auditResult = auditApiClientXOA2.auditCommentCenter(AuditApiPolicy.PostFirst,
                            comm.getAuthorId(), new Date(), comm.getId(), buildAuditString(comm),
                            type.ordinal(), comm.getContent(), comm.getAuthorName(),
                            comm.getAuthorHead(), comm.getOriginalContent(), comm.getWhipserToId(),
                            comm.getToUserId(), "");
                	break;
                case Meet:
                    auditResult = auditApiClient.auditFeedComment(AuditApiPolicy.PostFirst,
                            comm.getAuthorId(), new Date(), comm.getEntry().getId(),
                            comm.getEntry().getOwnerId(), comm.getEntry().getType(), comm.getId(),
                            comm.getContent(), "", strategy.getParams());
                    break;
                default:
                    logger.info("commen type " + type.toString() + " has no proper audit API");
                    auditResult = true;

            }

        } catch (AuditApiException e) {
            throw new UGCCommentException("can't do on-line audit", e);
        } catch (TException e) {
            throw new UGCCommentException("can't do on-line audit", e);
        } catch (Exception e) {
            throw new UGCCommentException("can't do on-line audit", e);
        }
        return auditResult;
    }

    public boolean doVoiceAudit(CommentType type, Comment comm, CommentStrategy strategy) {
        logger.info("do online audit:" + comm.getEntry().getId());
        boolean auditResult = false;

        //1.判断有没有审核类型
        VoiceCommentUgcType auditType = voiceAuditTypeMap.get(type);

        if (auditType == null) {
            logger.warn("found  no match prefix in likePrefixMap type = " + type);
            return auditResult;
        }

        //2.返回结果，注意传给审核的是commentId
        try {
            auditResult = auditApiClient.auditCommonCommentVoice(auditType,
                    AuditApiPolicy.PostFirst, comm.getAuthorId(), new Date(),
                    comm.getEntry().getId(), comm.getEntry().getOwnerId(), comm.getId(),
                    comm.getVoiceInfo().getVoiceUrl(), comm.getVoiceInfo().getVoiceLength(),
                    comm.getVoiceInfo().getVoiceSize(), comm.getVoiceInfo().getVoiceRate());

        } catch (AuditApiException e) {
            throw new UGCCommentException("can't do on-line audit", e);
        } catch (TException e) {
            throw new UGCCommentException("can't do on-line audit", e);
        } catch (Throwable e) {
            throw new UGCCommentException("can't do on-line audit", e);
        }
        return auditResult;
    }

    private String buildAuditString(Comment comm) {
        if (comm == null || comm.getEntry() == null) {
            return "";
        }

        Entry entry = comm.getEntry();
        JSONObject json = new JSONObject();
        json.put("entryId", entry.getId());
        json.put("entryOwnerId", entry.getOwnerId());

        return json.toString();
    }

    //    private boolean doAlbumAudit(Comment comment,CommentStrategy strategy){
    //        
    //        if(strategy == null || strategy.getEntry() == null || strategy.getEntry().getEntryProps() == null){
    //            logger.warn("doAlbumAudit getentry is null");
    //            return false;
    //        }
    //        
    //        Entry entry = strategy.getEntry();
    //        
    //        final AuditAlbumView albumView = new AuditAlbumView();
    //        albumView.setAlbumId(entry.getId());
    //        albumView.setAlbumName(EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_TITLE));
    //        albumView.setAlbumUrl(EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_HEADURL));
    //        albumView.setOwnerId(entry.getOwnerId());
    //        //取作者名字
    //        final User owner = WUserAdapter.getInstance().get(ownerId);
    //        albumView.setOwnerName();
    //        albumView.setBizType(AuditAlbumView.AUDIT_NOMAL_ALBUM);
    //        albumView.setContent(album.getAlbumAudit().getSuspectedWord());
    //        AuditLogic.getInstance().saveAuditAlbumView(albumView);
    //    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AuditCommentUtil.instance = this;
    }
}
