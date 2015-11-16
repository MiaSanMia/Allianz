package com.renren.ugc.comment.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.renren.ugc.comment.xoa2.*;
import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.renren.ugc.comment.util.CommentControllerUtils;
import com.renren.ugc.comment.util.JSONConvertUtils;
import com.renren.ugc.comment.xoa2.util.CommentError;
import com.renren.ugc.comment.xoa2.util.CommentTypeUtils;
import com.renren.xoa2.ErrorInfo;
import com.renren.xoa2.client.ServiceFactory;
import com.xiaonei.antispam.validator.HtmlPure;
import com.xiaonei.commons.interceptors.access.HostHolder;
import com.xiaonei.commons.interceptors.access.loginrequired.LoginRequired;
import com.xiaonei.xce.useranonymous.UserAnonymousAdapter;

@Path("")
public class CommentController {

    private Logger logger = Logger.getLogger(this.getClass());

    public final String URL_PREFIX = "comment/xoa2";

    @Autowired
    private HostHolder hostHolder;

    /**
     * the timeout to connect XOA2 service in ms, hard code temporarily
     */
    private static final int XOA2_TIMEOUT = Integer.valueOf(System.getProperty(
        "comment.center.xoa2.timeout", "750"));

    /**
     * Get the comment xoa2 service.<br>
     * Note: One service stub is bound to only one xoa2 server. Therefore, you
     * need to re-get a service each time to ensure the load could be
     * distributed to different servers, and thus realize load balance.
     */
    private IXCommentCenter getCommentXOA2Service() {
        return ServiceFactory.getService(IXCommentCenter.class, XOA2_TIMEOUT);
    }

    @Get("ping")
    public String index(Invocation inv) {
        return "@comment controller is working";
    }

    @LoginRequired
    @Post(URL_PREFIX + "/create")
    @com.xiaonei.commons.interceptors.access.annotation.ValidateRequestToken
    public String create(Invocation inv, @Param("type") String type,
        @Param("entryId") long entryId,
        @Param("entryOwnerId") int entryOwnerId,
        @Param("content") @HtmlPure String content,
        @Param("extension") @HtmlPure String extension,
        @Param("replyTo") int replyTo, @Param("whisper") boolean isWhisper,
        @Param("isLinked") boolean isLinked,
        @Param("toCommentId") long toCommentId) {

        int actorId = hostHolder.getUser().getId();

        JSONObject obj = new JSONObject();
        try {

            if (!isLinked) {

                // 普通评论
                CreateCommentRequest req =
                        buildCreateCommentReuest(inv, type, actorId, entryId,
                            entryOwnerId, replyTo, content, extension,
                            isWhisper, toCommentId);

                if (logger.isDebugEnabled()) {
                    logger.debug("create comment with params: " + req);
                }

                CreateCommentResponse resp =
                        getCommentXOA2Service().createComment(req);
                if (resp.isSetComment()) {
                    obj.put(
                        "comment",
                        JSONConvertUtils.comment2Json(resp.getComment(),
                            resp.getEntry()));
                    obj.put("code", "0");
                } else {
                    // CommentControllerUtils.ajaxFail(inv,
                    // resp.getBaseRep().getErrorInfo().getCode(),
                    // resp.getBaseRep().getErrorInfo().getMsg());
                    return CommentControllerUtils.ajaxFail(
                        resp.getBaseRep().getErrorInfo().getCode(),
                        resp.getBaseRep().getErrorInfo().getMsg());
                }
            } else {

                // 关联评论
                CreateLinkedCommentRequest req =
                        buildCreateLinkedCommentReuest(inv, type, actorId,
                            entryId, entryOwnerId, replyTo, content, extension,
                            isWhisper);

                if (logger.isDebugEnabled()) {
                    logger.debug("create linked comment with params: " + req);
                }

                CreateLinkedCommentResponse resp =
                        getCommentXOA2Service().createLinkedComment(req);
                if (resp.isSetComment()) {
                    obj.put(
                        "comment",
                        JSONConvertUtils.comment2Json(resp.getComment(),
                            resp.getEntry()));
                    obj.put("code", "0");
                } else {
                    // CommentControllerUtils.ajaxFail(inv,
                    // resp.getBaseRep().getErrorInfo().getCode(),
                    // resp.getBaseRep().getErrorInfo().getMsg());
                    return CommentControllerUtils.ajaxFail(
                        resp.getBaseRep().getErrorInfo().getCode(),
                        resp.getBaseRep().getErrorInfo().getMsg());
                }
            }

        } catch (Exception e) {
            logger.error("an exception occurs during creating a comment", e);
            // CommentControllerUtils.ajaxFail(inv, "系统错误，请稍后");
            return CommentControllerUtils.ajaxFail("系统错误，请稍后");
        }

        return "@" + obj;
    }

    /**
     * Convert the params passed in to the <code>CreateCommentRequest</code>
     * object . <code>type</code>, <code>entryId</code>,
     * <code>entryOwnerId</code> and <code>content</code> are required params to
     * build a valid <code>CreateCommentRequest</code> object.
     * @throws Exception 
     */
    @SuppressWarnings("rawtypes")
    CreateCommentRequest buildCreateCommentReuest(Invocation inv, String type,
        int actorId, long entryId, int entryOwnerId, int replyTo,
        String content, String extension, boolean isWhisper, long toCommentId) throws Exception {
        if (type == null || entryId == 0 || entryOwnerId == 0
            || StringUtils.isBlank(content)) {
            throw new RuntimeException(
                "Please provide all the required params in the request");
        }

        CreateCommentRequest req = new CreateCommentRequest();

        req.setType(CommentTypeUtils.valueOf(type));
        req.setActorId(actorId);
        req.setEntryId(entryId);
        req.setEntryOwnerId(entryOwnerId);
        req.setContent(content);
        req.setWhisper(isWhisper);
        // 回复给某个人
        req.setReplyToId(replyTo);
        req.setToCommentId(toCommentId);

        // add user Id to extra params for antispam api
        req.putToParams("antispam.userip", inv.getRequest().getRemoteAddr());

        // add additional params
        Map params = inv.getRequest().getParameterMap();
        Set paramKeys = params.keySet();
        Iterator it = paramKeys.iterator();

        while (it.hasNext()) {
            String key = (String) it.next();
            if (CreateCommentRequest._Fields.findByName(key) == null) {
                Object val = ((String[]) params.get(key))[0]; // params should
                                                              // never be empty
                                                              // or null
                req.putToParams(key, String.valueOf(val));
            }
        }
        
        this.doSpecialNeedCreate(req, actorId);

        return req;
    }

    @LoginRequired
    @Post(URL_PREFIX + "/create/voice")
    @com.xiaonei.commons.interceptors.access.annotation.ValidateRequestToken
    public String createVoice(Invocation inv, @Param("type") String type,
        @Param("entryId") long entryId,
        @Param("entryOwnerId") int entryOwnerId,
        @Param("url") @HtmlPure String voiceUrl, @Param("len") int voiceLength,
        @Param("size") int voiceSize, @Param("rate") int voiceRate,
        @Param("replyTo") int replyTo, @Param("whispter") boolean isWhisper) {

        int actorId = hostHolder.getUser().getId();
        CreateVoiceCommentRequest req =
                buildCreateVoiceCommentReuest(inv, type, actorId, entryId,
                    entryOwnerId, replyTo, voiceUrl, voiceLength, voiceSize,
                    voiceRate, isWhisper);

        if (logger.isDebugEnabled()) {
            logger.debug("create comment with params: " + req);
        }

        JSONObject obj = new JSONObject();
        try {
            CreateVoiceCommentResponse resp =
                    getCommentXOA2Service().createVoiceComment(req);
            if (resp.isSetComment()) {
                obj.put(
                    "comment",
                    JSONConvertUtils.comment2Json(resp.getComment(),
                        resp.getEntry()));
                obj.put("code", CommentError.SUCCESS);
            } else {
                ErrorInfo error = resp.getBaseRep().getErrorInfo();
                // CommentControllerUtils.ajaxFail(inv, error.getCode(),
                // error.getMsg());
                return CommentControllerUtils.ajaxFail(error.getCode(),
                    error.getMsg());
            }

        } catch (Exception e) {
            logger.error("an exception occurs during creating a comment", e);
            // CommentControllerUtils.ajaxFail(inv, "系统错误，请稍后");
            return CommentControllerUtils.ajaxFail("系统错误，请稍后");
        }
        return "@" + obj;
    }

    /**
     * Convert the params passed in to the <code>CreateCommentRequest</code>
     * object . <code>type</code>, <code>entryId</code>,
     * <code>entryOwnerId</code> and <code>content</code> are required params to
     * build a valid <code>CreateCommentRequest</code> object.
     */
    @SuppressWarnings("rawtypes")
    CreateVoiceCommentRequest buildCreateVoiceCommentReuest(Invocation inv,
        String type, int actorId, long entryId, int entryOwnerId, int replyTo,
        String voiceUrl, int voiceLength, int voiceSize, int voiceRate,
        boolean isWhisper) {
        if (type == null || entryId == 0 || entryOwnerId == 0
            || StringUtils.isBlank(voiceUrl)) {
            throw new RuntimeException(
                "Please provide all the required params in the request");
        }

        CreateVoiceCommentRequest req = new CreateVoiceCommentRequest();

        req.setType(CommentTypeUtils.valueOf(type));
        req.setActorId(actorId);
        req.setEntryId(entryId);
        req.setEntryOwnerId(entryOwnerId);
        req.setReplyToId(replyTo);
        req.setVoiceUrl(voiceUrl);
        req.setVoiceLength(voiceLength);
        req.setVoiceSize(voiceSize);
        req.setVoiceRate(voiceRate);
        req.setWhisper(isWhisper);

        // add user Id to extra params for antispam api
        req.putToParams("antispam.userip", inv.getRequest().getRemoteAddr());

        // add additional params
        Map params = inv.getRequest().getParameterMap();
        Set paramKeys = params.keySet();
        Iterator it = paramKeys.iterator();

        while (it.hasNext()) {
            String key = (String) it.next();
            if (CreateVoiceCommentRequest._Fields.findByName(key) == null) {
                Object val = ((String[]) params.get(key))[0]; // params should
                                                              // never be empty
                                                              // or null
                req.putToParams(key, String.valueOf(val));
            }
        }

        return req;
    }

    @Get(URL_PREFIX)
    public String getList(Invocation inv, @Param("type") String type,
        @Param("entryId") long entryId,
        @Param("entryOwnerId") int entryOwnerId, @Param("offset") int offset,
        @Param("border") long borderCommentId, @Param("limit") int limit,
        @Param("desc") boolean isDesc) {

        int actorId;
        if (hostHolder.getUser() != null) {
            actorId = hostHolder.getUser().getId();
        } else {
            actorId = 0;
        }

        GetCommentListResponse resp;
        JSONObject obj = new JSONObject();
        try {
            GetCommentListRequest req =
                    buildGetCommentListRequest(inv, type, actorId, entryId,
                        entryOwnerId, offset,
                        /* set pageNum to -1 to use offset */-1, borderCommentId,
                        limit, isDesc);
            
            resp = getCommentXOA2Service().getCommentList(req);
            if (resp.isSetCommentList()) {
                List<Comment> commentList = resp.getCommentList();

                if (commentList != null && commentList.size() >= 0) {
                    obj.put("hasMore", resp.isMore());
                    obj.put("hasPre", resp.isPre());
                    obj.put(
                        "comments",
                        JSONConvertUtils.buildJSON(commentList, resp.getEntry()));
                    obj.put("commentCount", resp.getCommentListSize());
                    obj.put("nextOffset", resp.isMore() ? offset + limit : 0);
                } else {
                    obj.put("commentCount", 0);
                    obj.put("hasMore", "false");
                    obj.put("hasPre", "false");
                    obj.put("comments", "[]");
                    obj.put("nextOffset", 0);
                }
                obj.put("commentTotalCount", resp.getTotalCount());
                obj.put("code", CommentError.SUCCESS);

                // 用rose自己的string输出替换直接输出
                // 因为发现用原生态的httpServletResponse会出现中文乱码
                // inv.getResponse().getWriter().print(obj);
            } else {
                ErrorInfo error = resp.getBaseRep().getErrorInfo();

                // CommentControllerUtils.ajaxFail(inv, error.getCode(),
                // error.getMsg());
                return CommentControllerUtils.ajaxFail(error.getCode(),
                    error.getMsg());
            }
        } catch (Exception e) {
            logger.error("an exception occurs during getting comment list", e);
            // CommentControllerUtils.ajaxFail(inv, "系统错误，请稍后");
            return CommentControllerUtils.ajaxFail("系统错误，请稍后");
        }
        return "@" + obj;
    }

    @Get(URL_PREFIX + "/friends")
    public String getFriendsCommentList(Invocation inv,
        @Param("type") String type, @Param("entryId") long entryId,
        @Param("entryOwnerId") int entryOwnerId, @Param("offset") int offset,
        @Param("border") long borderCommentId, @Param("limit") int limit,
        @Param("global") boolean fromGlobal, @Param("desc") boolean isDesc) {

        int actorId;
        if (hostHolder.getUser() != null) {
            actorId = hostHolder.getUser().getId();
        } else {
            actorId = 0;
        }

        GetFriendsCommentListRequest req =
                buildGetFriendsCommentListRequest(inv, type, actorId, entryId,
                    entryOwnerId, offset, limit, fromGlobal, isDesc);

        GetFriendsCommentListResponse resp;
        JSONObject obj = new JSONObject();
        try {
            resp = getCommentXOA2Service().getFriendsCommentList(req);
            if (resp.isSetCommentList()) {
                List<Comment> commentList = resp.getCommentList();
                if (resp.isSetCommentList() && resp.getCommentListSize() > 0) {
                    obj.put("hasMore", resp.isMore());
                    obj.put("comments",
                        JSONConvertUtils.buildJSON(commentList, null));
                    obj.put("commentCount", resp.getCommentListSize());
                } else {
                    obj.put("commentCount", 0);
                    obj.put("hasMore", "false");
                    obj.put("comments", "[]");
                }
                obj.put("code", CommentError.SUCCESS);
            } else {
                ErrorInfo error = resp.getBaseRep().getErrorInfo();
                // CommentControllerUtils.ajaxFail(inv, error.getCode(),
                // error.getMsg());
                return CommentControllerUtils.ajaxFail(error.getCode(),
                    error.getMsg());
            }
        } catch (Exception e) {
            logger.error("an exception occurs during getting comment list", e);
            // CommentControllerUtils.ajaxFail(inv, "系统错误，请稍后");
            return CommentControllerUtils.ajaxFail("系统错误，请稍后");
        }
        return "@" + obj;
    }

    @Get(URL_PREFIX + "/global")
    public String getGlobalCommentList(Invocation inv,
        @Param("type") String type, @Param("entryId") long entryId,
        @Param("entryOwnerId") int entryOwnerId, @Param("offset") int offset,
        @Param("limit") int limit, @Param("desc") boolean isDesc) {

        int actorId;
        if (hostHolder.getUser() != null) {
            actorId = hostHolder.getUser().getId();
        } else {
            actorId = 0;
        }

        GetGlobalCommentListRequest req =
                buildGetGlobalCommentListRequest(inv, type, actorId, entryId,
                    entryOwnerId, offset, limit, isDesc);

        GetGlobalCommentListResponse resp;
        JSONObject obj = new JSONObject();
        try {
            resp = getCommentXOA2Service().getGlobalCommentList(req);
            if (resp.isSetCommentList()) {
                List<Comment> commentList = resp.getCommentList();
                if (resp.isSetCommentList() && resp.getCommentListSize() > 0) {
                    obj.put("hasMore", resp.isMore());
                    obj.put("comments",
                        JSONConvertUtils.buildJSON(commentList, null));
                    obj.put("commentCount", resp.getCommentListSize());
                } else {
                    obj.put("commentCount", 0);
                    obj.put("hasMore", "false");
                    obj.put("comments", "[]");
                }
                obj.put("commentTotalCount", resp.getTotalCount());
                obj.put("code", CommentError.SUCCESS);
            } else {
                ErrorInfo error = resp.getBaseRep().getErrorInfo();
                // CommentControllerUtils.ajaxFail(inv, error.getCode(),
                // error.getMsg());
                return CommentControllerUtils.ajaxFail(error.getCode(),
                    error.getMsg());
            }
        } catch (Exception e) {
            logger.error(
                "an exception occurs during getting global comment list", e);
            // CommentControllerUtils.ajaxFail(inv, "系统错误，请稍后");
            return CommentControllerUtils.ajaxFail("系统错误，请稍后");
        }
        return "@" + obj;
    }

    private GetGlobalCommentListRequest buildGetGlobalCommentListRequest(
        Invocation inv, String type, int actorId, long entryId,
        int entryOwnerId, int offset, int limit, boolean isDesc) {
        if (type == null || entryId == -1 || entryOwnerId == -1) {
            throw new RuntimeException(
                "Please provide all the required params in the request");
        }

        if (offset < 0) {
            offset = 0;
        }

        GetGlobalCommentListRequest req = new GetGlobalCommentListRequest();
        req.setType(CommentTypeUtils.valueOf(type));
        req.setActorId(actorId);
        req.setEntryId(entryId);
        req.setEntryOwnerId(entryOwnerId);
        req.setDesc(isDesc);
        req.setOffset(offset);
        req.setLimit(limit);

        // add additional params
        Map params = inv.getRequest().getParameterMap();
        Set paramKeys = params.keySet();
        Iterator it = paramKeys.iterator();

        while (it.hasNext()) {
            String key = (String) it.next();
            if (GetGlobalCommentListRequest._Fields.findByName(key) == null) {
                Object val = ((String[]) params.get(key))[0]; // params should
                                                              // never be empty
                                                              // or null
                req.putToParams(key, String.valueOf(val));
            }
        }

        return req;
    }

    @Get(URL_PREFIX + "/page/{pageNum:\\d+}")
    public String getListByPage(Invocation inv, @Param("type") String type,
        @Param("entryId") long entryId,
        @Param("entryOwnerId") int entryOwnerId, @Param("pageNum") int pageNum,
        @Param("pageCount") int pageCount, @Param("desc") boolean isDesc) {
        if (pageNum < 0) {
            pageNum = 0;
        }

        int actorId;
        if (hostHolder.getUser() != null) {
            actorId = hostHolder.getUser().getId();
        } else {
            actorId = 0;
        }

        GetCommentListResponse resp;
        JSONObject obj = new JSONObject();
        try {
            GetCommentListRequest req =
                    buildGetCommentListRequest(inv, type, actorId, entryId,
                        entryOwnerId,
                        /* set offset to -1 to use pageNum */-1, pageNum,
                        /* set borderCommentId to 0 as invalid */0, pageCount,
                        isDesc);
            
            resp = getCommentXOA2Service().getCommentList(req);
            if (resp.isSetCommentList()) {
                List<Comment> commentList = resp.getCommentList();
                if (commentList != null && commentList.size() >= 0) {
                    obj.put("hasMore", resp.isMore());
                    obj.put(
                        "comments",
                        JSONConvertUtils.buildJSON(commentList, resp.getEntry()));
                    obj.put("commentCount", resp.getCommentListSize());
                } else {
                    obj.put("hasMore", "false");
                    obj.put("comments", "[]");
                }
                obj.put("code", CommentError.SUCCESS);
            } else {
                ErrorInfo error = resp.getBaseRep().getErrorInfo();
                // CommentControllerUtils.ajaxFail(inv, error.getCode(),
                // error.getMsg());
                return CommentControllerUtils.ajaxFail(error.getCode(),
                    error.getMsg());
            }
        } catch (Exception e) {
            logger.error("an exception occurs during getting comment list", e);
            // CommentControllerUtils.ajaxFail(inv, "系统错误，请稍后");
            return CommentControllerUtils.ajaxFail("系统错误，请稍后");
        }
        return "@" + obj;
    }

    @Get(URL_PREFIX + "/headtail")
    public String getHeadAndTailCommentList(Invocation inv, @Param("type") String type, @Param("entryId") long entryId,
         @Param("entryOwnerId") int entryOwnerId, @Param("headLimit") int headLimit, @Param("tailLimit") int tailLimit,
         @Param("desc") boolean isDesc) {
        int actorId;
        if (hostHolder.getUser() != null) {
            actorId = hostHolder.getUser().getId();
        } else {
            actorId = 0;
        }

        GetHeadAndTailCommentListRequest req =
                buildGetHeadAndTailCommentListRequest(inv, type, actorId, entryId, entryOwnerId, headLimit, tailLimit, isDesc);

        GetHeadAndTailCommentListResponse resp;
        JSONObject obj = new JSONObject();
        try {
            resp = getCommentXOA2Service().getHeadAndTailCommentList(req);
            if (resp.isSetHeadCommentList() || resp.isSetTailCommentList()) {
                obj.put("hasMore", resp.isMore());
                if (resp.isSetHeadCommentList() && resp.getHeadCommentListSize() > 0) {
                    obj.put("headComments", JSONConvertUtils.buildJSON(resp.getHeadCommentList(), resp.getEntry()));
                } else {
                    obj.put("headComments", "[]");
                }

                if (resp.isSetTailCommentList() && resp.getTailCommentListSize() > 0) {
                    obj.put("tailComments", JSONConvertUtils.buildJSON(resp.getTailCommentList(), resp.getEntry()));
                } else {
                    obj.put("tailComments", "[]");
                }

                obj.put("commentTotalCount", resp.getTotalCount());
                obj.put("code", CommentError.SUCCESS);
            } else {
                ErrorInfo error = resp.getBaseRep().getErrorInfo();
                return CommentControllerUtils.ajaxFail(error.getCode(), error.getMsg());
            }
        } catch (Exception e) {
            logger.error("an exception occurs during getting head and tail comment list", e);
            return CommentControllerUtils.ajaxFail("系统错误，请稍后");
        }
        return "@" + obj;
    }

    private GetHeadAndTailCommentListRequest buildGetHeadAndTailCommentListRequest(Invocation inv,
            String type, int actorId, long entryId, int entryOwnerId, int headLimit, int tailLimit, boolean desc) {
        if (type == null || entryId <= 0 || entryOwnerId <= 0) {
            throw new RuntimeException("Please provide all the required params in the request");
        }

        GetHeadAndTailCommentListRequest req = new GetHeadAndTailCommentListRequest();
        req.setType(CommentTypeUtils.valueOf(type));
        req.setActorId(actorId);
        req.setEntryId(entryId);
        req.setEntryOwnerId(entryOwnerId);
        if (headLimit > 0)  {
            req.setHeadLimit(headLimit);
        }

        if (tailLimit > 0) {
            req.setTailLimit(tailLimit);
        }
        req.setDesc(desc);

        // add additional params
        Map params = inv.getRequest().getParameterMap();
        Set paramKeys = params.keySet();
        Iterator it = paramKeys.iterator();

        while (it.hasNext()) {
            String key = (String) it.next();
            if (GetCommentListRequest._Fields.findByName(key) == null) {
                Object val = ((String[]) params.get(key))[0]; // params should
                // never be empty
                // or null
                req.putToParams(key, String.valueOf(val));
            }
        }

        return req;
    }

    /**
     * Convert the params passed in to the <code>GetCommentListRequest</code>
     * object . <code>type</code>, <code>entryId</code>,
     * <code>entryOwnerId</code> are required params to build a valid
     * <code>GetCommentListRequest</code> object. If <code>offset</code> is
     * minus, it will be auto set to 0. If <code>limit</code> <= 0 or >100, it
     * will be auto set to 30.
     * @throws Exception 
     */
    GetCommentListRequest buildGetCommentListRequest(Invocation inv,
        String type, int actorId, long entryId, int entryOwnerId, int offset,
        int pageNum, long borderCommentId, int limit, boolean isDesc) throws Exception {
        if (type == null || entryId == -1 || entryOwnerId == -1) {
            throw new RuntimeException(
                "Please provide all the required params in the request");
        }

        if (offset < 0) {
            offset = 0;
        }

        GetCommentListRequest req = new GetCommentListRequest();
        req.setType(CommentTypeUtils.valueOf(type));
        req.setActorId(actorId);
        req.setEntryId(entryId);
        req.setEntryOwnerId(entryOwnerId);
        req.setDesc(isDesc);
        if (borderCommentId != 0) {
            req.setBorderCommentId(borderCommentId);
        }
        if (pageNum >= 0) {
            req.setPageNum(pageNum);
        } else {
            req.setOffset(offset);
        }
        req.setLimit(limit);

        // add additional params
        Map params = inv.getRequest().getParameterMap();
        Set paramKeys = params.keySet();
        Iterator it = paramKeys.iterator();

        while (it.hasNext()) {
            String key = (String) it.next();
            if (GetCommentListRequest._Fields.findByName(key) == null) {
                Object val = ((String[]) params.get(key))[0]; // params should
                                                              // never be empty
                                                              // or null
                req.putToParams(key, String.valueOf(val));
            }
        }
        
        this.doSpecialNeedGetList(req, actorId);

        return req;
    }

    /**
     * Convert the params passed in to the
     * <code>GetFriendsCommentListRequest</code> object . <code>type</code> ,
     * <code>entryId</code>, <code>entryOwnerId</code> are required params to
     * build a valid <code>GetCommentListRequest</code> object. If
     * <code>offset</code> is minus, it will be auto set to 0. If
     * <code>limit</code> <= 0 or >100, it will be auto set to 30.
     */
    GetFriendsCommentListRequest buildGetFriendsCommentListRequest(
        Invocation inv, String type, int actorId, long entryId,
        int entryOwnerId, int offset, int limit, boolean includeGlobalComment,
        boolean isDesc) {
        if (type == null || entryId == -1 || entryOwnerId == -1 || actorId == 0) {
            throw new RuntimeException(
                "Please provide all the required params in the request");
        }

        if (offset < 0) {
            offset = 0;
        }

        GetFriendsCommentListRequest req = new GetFriendsCommentListRequest();
        req.setType(CommentTypeUtils.valueOf(type));
        req.setActorId(actorId);
        req.setEntryId(entryId);
        req.setEntryOwnerId(entryOwnerId);
        req.setDesc(isDesc);
        req.setOffset(offset);
        req.setLimit(limit);
        // req.setIncludeGlobalComment(includeGlobalComment);

        // add additional params
        Map params = inv.getRequest().getParameterMap();
        Set paramKeys = params.keySet();
        Iterator it = paramKeys.iterator();

        while (it.hasNext()) {
            String key = (String) it.next();
            if (GetFriendsCommentListRequest._Fields.findByName(key) == null) {
                Object val = ((String[]) params.get(key))[0]; // params should
                                                              // never be empty
                                                              // or null
                req.putToParams(key, String.valueOf(val));
            }
        }

        return req;
    }

    @LoginRequired
    @Post(URL_PREFIX + "/{commentId:\\d+}/remove")
    @com.xiaonei.commons.interceptors.access.annotation.ValidateRequestToken
    public String remove(Invocation inv, @Param("type") String type,
        @Param("entryId") long entryId,
        @Param("entryOwnerId") int entryOwnerId,
        @Param("commentId") long commentId) {
        int actorId;
        if (hostHolder.getUser() != null) {
            actorId = hostHolder.getUser().getId();
        } else {
            actorId = 0;
        }

        RemoveCommentResponse resp;
        JSONObject obj = new JSONObject();
        try {
            RemoveCommentRequest req =
                    buildRemoveCommentRequest(inv, type, actorId, entryId,
                        entryOwnerId, commentId);
            
            resp = getCommentXOA2Service().removeComment(req);
            if (resp.isSetBaseRep()
                && resp.getBaseRep().getErrorInfo().getCode() != CommentError.SUCCESS) {
                // CommentControllerUtils.ajaxFail(inv,
                // resp.getBaseRep().getErrorInfo().getCode(),
                // resp.getBaseRep().getErrorInfo().getMsg());
                return CommentControllerUtils.ajaxFail(
                    resp.getBaseRep().getErrorInfo().getCode(),
                    resp.getBaseRep().getErrorInfo().getMsg());
            }

            if (resp.isSetRemoved()) {
                obj.put("removed", resp.isRemoved());
            } else {
                obj.put("removed", "false");
            }
            obj.put("code", CommentError.SUCCESS);
            // inv.getResponse().getWriter().print(obj);
        } catch (Exception e) {
            logger.error("an exception occurs during getting comment list", e);
            // CommentControllerUtils.ajaxFail(inv, "系统错误，请稍后");
            return CommentControllerUtils.ajaxFail("系统错误，请稍后");
        }
        return "@" + obj;
    }

    @Post(URL_PREFIX + "/{commentId:\\d+}/removeGlobalComment")
    public String removeGlobalComment(Invocation inv,
        @Param("type") String type, @Param("entryId") long entryId,
        @Param("entryOwnerId") int entryOwnerId,
        @Param("commentId") long commentId) {

        // TODO must login to remove global comment?
        int actorId;
        if (hostHolder.getUser() != null) {
            actorId = hostHolder.getUser().getId();
        } else {
            actorId = 0;
        }

        RemoveGlobalCommentRequest req =
                buildRemoveGlobalCommentRequest(inv, type, actorId, entryId,
                    entryOwnerId, commentId);

        RemoveGlobalCommentResponse resp;
        JSONObject obj = new JSONObject();
        try {
            resp = getCommentXOA2Service().removeGlobalComment(req);
            if (resp.isSetBaseRep()
                && resp.getBaseRep().getErrorInfo().getCode() != CommentError.SUCCESS) {
                obj =
                        CommentControllerUtils.ajaxFail(inv,
                            resp.getBaseRep().getErrorInfo().getCode(),
                            resp.getBaseRep().getErrorInfo().getMsg());
                return "@" + obj;
            }

            if (resp.isSetRemoved()) {
                obj.put("removed", resp.isRemoved());
            } else {
                obj.put("removed", "false");
            }
            obj.put("code", CommentError.SUCCESS);
            // inv.getResponse().getWriter().print(obj);
        } catch (Exception e) {
            logger.error("an exception occurs during getting comment list", e);
            // CommentControllerUtils.ajaxFail(inv, "系统错误，请稍后");
            return CommentControllerUtils.ajaxFail("系统错误，请稍后");
        }
        return "@" + obj;
    }

    /**
     * Convert the params passed in to the <code>RemoveCommentRequest</code>
     * object . <code>type</code>, <code>entryId</code>,
     * <code>entryOwnerId</code> and <code>commentId</code> are required params
     * to build a valid <code>RemoveCommentRequest</code> object.
     */
    RemoveGlobalCommentRequest buildRemoveGlobalCommentRequest(Invocation inv,
        String type, int actorId, long entryId, int entryOwnerId, long commentId) {
        if (type == null || entryId <= 0 || entryOwnerId <= 0 || commentId <= 0) {
            throw new RuntimeException(
                "Please provide all the required params in the request");
        }

        RemoveGlobalCommentRequest req = new RemoveGlobalCommentRequest();
        req.setType(CommentTypeUtils.valueOf(type));
        req.setActorId(actorId);
        req.setEntryId(entryId);
        req.setEntryOwnerId(entryOwnerId);
        req.setCommentId(commentId);

        // add additional params
        Map params = inv.getRequest().getParameterMap();
        Set paramKeys = params.keySet();
        Iterator it = paramKeys.iterator();

        while (it.hasNext()) {
            String key = (String) it.next();
            if (RemoveCommentRequest._Fields.findByName(key) == null) {
                Object val = ((String[]) params.get(key))[0];
                req.putToParams(key, String.valueOf(val));
            }
        }

        return req;
    }

    /**
     * Convert the params passed in to the <code>RemoveCommentRequest</code>
     * object . <code>type</code>, <code>entryId</code>,
     * <code>entryOwnerId</code> and <code>commentId</code> are required params
     * to build a valid <code>RemoveCommentRequest</code> object.
     * @throws Exception 
     */
    RemoveCommentRequest buildRemoveCommentRequest(Invocation inv, String type,
        int actorId, long entryId, int entryOwnerId, long commentId) throws Exception {
        if (type == null || entryId <= 0 || entryOwnerId <= 0 || commentId <= 0) {
            throw new RuntimeException(
                "Please provide all the required params in the request");
        }

        RemoveCommentRequest req = new RemoveCommentRequest();
        req.setType(CommentTypeUtils.valueOf(type));
        req.setActorId(actorId);
        req.setEntryId(entryId);
        req.setEntryOwnerId(entryOwnerId);
        req.setCommentId(commentId);

        // add additional params
        Map params = inv.getRequest().getParameterMap();
        Set paramKeys = params.keySet();
        Iterator it = paramKeys.iterator();

        while (it.hasNext()) {
            String key = (String) it.next();
            if (RemoveCommentRequest._Fields.findByName(key) == null) {
                Object val = ((String[]) params.get(key))[0];
                req.putToParams(key, String.valueOf(val));
            }
        }
        
        this.doSpecialNeedRemove(req, actorId);

        return req;
    }

    @LoginRequired
    @Post(URL_PREFIX + "/{commentId:\\d+}/recover")
    @com.xiaonei.commons.interceptors.access.annotation.ValidateRequestToken
    public String recover(Invocation inv, @Param("type") String type,
        @Param("entryId") long entryId,
        @Param("entryOwnerId") int entryOwnerId,
        @Param("commentId") long commentId) {
        int actorId;
        if (hostHolder.getUser() != null) {
            actorId = hostHolder.getUser().getId();
        } else {
            actorId = 0;
        }

        RecoverCommentRequest req =
                buildRecoverCommentRequest(inv, type, actorId, entryId,
                    entryOwnerId, commentId);

        RecoverCommentResponse resp;
        JSONObject obj = new JSONObject();
        try {
            resp = getCommentXOA2Service().recoverComment(req);
            if (resp.isSetBaseRep()
                && resp.getBaseRep().getErrorInfo().getCode() != CommentError.SUCCESS) {
                // CommentControllerUtils.ajaxFail(inv,
                // resp.getBaseRep().getErrorInfo().getCode(),
                // resp.getBaseRep().getErrorInfo().getMsg());
                return CommentControllerUtils.ajaxFail(
                    resp.getBaseRep().getErrorInfo().getCode(),
                    resp.getBaseRep().getErrorInfo().getMsg());
            }

            if (resp.isSetRecovered()) {
                obj.put("recovered", resp.isRecovered());
            } else {
                obj.put("recovered", "false");
            }
            obj.put("code", CommentError.SUCCESS);
        } catch (Exception e) {
            logger.error("an exception occurs during getting comment list", e);
            // CommentControllerUtils.ajaxFail(inv, "系统错误，请稍后");
            return CommentControllerUtils.ajaxFail("系统错误，请稍后");
        }
        return "@" + obj;
    }

    /**
     * Convert the params passed in to the <code>RemoveCommentRequest</code>
     * object . <code>type</code>, <code>entryId</code>,
     * <code>entryOwnerId</code> and <code>commentId</code> are required params
     * to build a valid <code>RemoveCommentRequest</code> object.
     */
    RecoverCommentRequest buildRecoverCommentRequest(Invocation inv,
        String type, int actorId, long entryId, int entryOwnerId, long commentId) {
        if (type == null || entryId <= 0 || entryOwnerId <= 0 || commentId <= 0) {
            throw new RuntimeException(
                "Please provide all the required params in the request");
        }

        RecoverCommentRequest req = new RecoverCommentRequest();
        req.setType(CommentTypeUtils.valueOf(type));
        req.setActorId(actorId);
        req.setEntryId(entryId);
        req.setEntryOwnerId(entryOwnerId);
        req.setCommentId(commentId);

        return req;
    }

    @LoginRequired
    @Post(URL_PREFIX + "/{commentId:\\d+}/voice/inc")
    public String increaseVoiceCommentPlayCount(Invocation inv,
        @Param("type") String type, @Param("entryId") long entryId,
        @Param("entryOwnerId") int entryOwnerId,
        @Param("commentId") long commentId, @Param("inc") int increment) {
        int actorId;
        if (hostHolder.getUser() != null) {
            actorId = hostHolder.getUser().getId();
        } else {
            actorId = 0;
        }

        IncreaseVoiceCommentPlayCountRequest req =
                buildIncreaseVoiceCommentPlayCountRequest(inv, type, actorId,
                    entryId, entryOwnerId, commentId, increment);

        IncreaseVoiceCommentPlayCountResponse resp;
        JSONObject obj = new JSONObject();
        try {
            resp = getCommentXOA2Service().increaseVoiceCommentPlayCount(req);
            if (resp.isSetBaseRep()
                && resp.getBaseRep().getErrorInfo().getCode() != CommentError.SUCCESS) {
                // CommentControllerUtils.ajaxFail(inv,
                // resp.getBaseRep().getErrorInfo().getCode(),
                // resp.getBaseRep().getErrorInfo().getMsg());
                return CommentControllerUtils.ajaxFail(
                    resp.getBaseRep().getErrorInfo().getCode(),
                    resp.getBaseRep().getErrorInfo().getMsg());
            }

            if (resp.isSetPlayCount()) {
                obj.put("playCount", resp.getPlayCount());
            } else {
                obj.put("playCount", "0");
            }
            obj.put("code", CommentError.SUCCESS);
        } catch (Exception e) {
            logger.error("an exception occurs during getting comment list", e);
            // CommentControllerUtils.ajaxFail(inv, "系统错误，请稍后");
            return CommentControllerUtils.ajaxFail("系统错误，请稍后");
        }
        return "@" + obj;
    }

    /**
     * Convert the params passed in to the <code>RemoveCommentRequest</code>
     * object . <code>type</code>, <code>entryId</code>,
     * <code>entryOwnerId</code> and <code>commentId</code> are required params
     * to build a valid <code>RemoveCommentRequest</code> object.
     */
    IncreaseVoiceCommentPlayCountRequest buildIncreaseVoiceCommentPlayCountRequest(
        Invocation inv, String type, int actorId, long entryId,
        int entryOwnerId, long commentId, int increment) {
        if (type == null || entryId <= 0 || entryOwnerId <= 0 || commentId <= 0) {
            throw new RuntimeException(
                "Please provide all the required params in the request");
        }

        IncreaseVoiceCommentPlayCountRequest req =
                new IncreaseVoiceCommentPlayCountRequest();
        req.setType(CommentTypeUtils.valueOf(type));
        req.setActorId(actorId);
        req.setEntryId(entryId);
        req.setEntryOwnerId(entryOwnerId);
        req.setCommentId(commentId);

        if (increment > 0) {
            req.setIncrement(increment);
        }

        // add additional params
        Map params = inv.getRequest().getParameterMap();
        Set paramKeys = params.keySet();
        Iterator it = paramKeys.iterator();

        while (it.hasNext()) {
            String key = (String) it.next();
            if (IncreaseVoiceCommentPlayCountRequest._Fields.findByName(key) == null) {
                Object val = ((String[]) params.get(key))[0];
                req.putToParams(key, String.valueOf(val));
            }
        }

        return req;
    }

    /**
     * Convert the params passed in to the <code>CreateCommentRequest</code>
     * object . <code>type</code>, <code>entryId</code>,
     * <code>entryOwnerId</code> and <code>content</code> are required params to
     * build a valid <code>CreateCommentRequest</code> object.
     */
    @SuppressWarnings("rawtypes")
    CreateLinkedCommentRequest buildCreateLinkedCommentReuest(Invocation inv,
        String type, int actorId, long entryId, int entryOwnerId, int replyTo,
        String content, String extension, boolean isWhisper) {
        if (type == null || entryId == 0 || entryOwnerId == 0
            || StringUtils.isBlank(content)) {
            throw new RuntimeException(
                "Please provide all the required params in the request");
        }

        CreateLinkedCommentRequest req = new CreateLinkedCommentRequest();

        req.setType(CommentTypeUtils.valueOf(type));
        req.setActorId(actorId);
        req.setEntryId(entryId);
        req.setEntryOwnerId(entryOwnerId);
        req.setReplyToId(replyTo);
        req.setContent(content);
        req.setWhisper(isWhisper);

        // add user Id to extra params for antispam api
        req.putToParams("antispam.userip", inv.getRequest().getRemoteAddr());

        // add additional params
        Map params = inv.getRequest().getParameterMap();
        Set paramKeys = params.keySet();
        Iterator it = paramKeys.iterator();

        while (it.hasNext()) {
            String key = (String) it.next();

            if (CreateLinkedCommentRequest._Fields.findByName(key) == null) {
                Object val = ((String[]) params.get(key))[0]; // params should
                                                              // never be empty
                                                              // or null

                req.putToParams(key, String.valueOf(val));
            }
        }

        // build empty linkedList
        List<CommentLinkedInfo> commentLinkedInfos =
                new ArrayList<CommentLinkedInfo>();
        req.setCommentLinkedInfos(commentLinkedInfos);

        return req;
    }

    /*
     * ================== old rest apis for compatibility only
     * ====================
     */
    private final String oldUrlPrefix =
            "comment/xoa1/{owner:[0-9]+}/{type:[\\w]+}-{id:[0-9]+}/comment";

    @LoginRequired
    @Post(oldUrlPrefix + "/create")
    @com.xiaonei.commons.interceptors.access.annotation.ValidateRequestToken
    public String oldCreate(Invocation inv, @Param("type") String type,
        @Param("id") long entryId, @Param("owner") int entryOwner,
        @Param("to") int replyTo, @Param("content") @HtmlPure String content,
        @Param("extension") @HtmlPure String extension,
        @Param("only_to_me") boolean isWhisper,
        @Param("isLinked") boolean isLinked,
        @Param("toCommentId") long toCommentId) {
        return create(inv, type, entryId, entryOwner, content, extension,
            replyTo, isWhisper, isLinked, toCommentId);
    }

    @Get(oldUrlPrefix)
    public String oldGetList(Invocation inv, @Param("type") String type,
        @Param("id") long entryId, @Param("owner") int entryOwner,
        @Param("count") int queryCount, @Param("border") long commentId) {
        return getList(inv, type, entryId, entryOwner, -1, commentId,
            queryCount, false);
    }

    @Get(oldUrlPrefix + "/page")
    public String oldGetListByPage(Invocation inv, @Param("type") String type,
        @Param("id") long entryId, @Param("owner") int entryOwner,
        @Param("count") int queryCount, @Param("page") int page) {
        return getListByPage(inv, type, entryId, entryOwner, page, queryCount,
            false);
    }

    @LoginRequired
    @Post(oldUrlPrefix + "/{commentId:[0-9]+}/remove")
    @com.xiaonei.commons.interceptors.access.annotation.ValidateRequestToken
    public String oldRemove(Invocation inv, @Param("type") String type,
        @Param("id") long entryId, @Param("owner") int entryOwner,
        @Param("commentId") long commentId) {
        return remove(inv, type, entryId, entryOwner, commentId);
    }
    
    /**
     * @param req
     * @param actorId
     * @throws Exception
     * 
     *  增加评论的时候一些特殊处理逻辑
     */
    private void doSpecialNeedCreate(CreateCommentRequest req,int actorId) throws Exception{
    	
    	//如果是匿名id的话,actorId需要为匿名的
        if(req.getParams().containsKey("isAnonymous") &&
        		"true".equalsIgnoreCase(req.getParams().get("isAnonymous"))){
        	int anonymousId = 0;
        	try {
        		anonymousId = UserAnonymousAdapter.getInstance().getAnonymousUserId(actorId);
				req.setActorId(anonymousId);
				//匿名帖子需要保留当时的author信息
				req.getParams().put("saveAuthorInfo", "true");
			} catch (Exception e) {
				logger.error("getAnonymousUserId error,actorId = "+actorId,e);
				throw new Exception("anonymoususer error");
			}
        	if(anonymousId == 0){
        		logger.error("getAnonymousUserId is null,actorId = "+actorId);
				throw new Exception("anonymoususer is null");
        	}
        }
        
    }
    
    /**
     * @param req
     * @param actorId
     * @throws Exception
     * 
     *  删除评论的时候一些特殊处理逻辑
     */
    private void doSpecialNeedRemove(RemoveCommentRequest req,int actorId) throws Exception{
    	
    	//如果是匿名id的话,actorId需要为匿名的
        if(req.getParams().containsKey("isAnonymous") &&
        		"true".equalsIgnoreCase(req.getParams().get("isAnonymous"))){
        	int anonymousId = 0;
        	try {
        		anonymousId = UserAnonymousAdapter.getInstance().getAnonymousUserId(actorId);
				req.setActorId(anonymousId);
			} catch (Exception e) {
				logger.error("getAnonymousUserId error,actorId = "+actorId,e);
				throw new Exception("anonymoususer error");
			}
        	if(anonymousId == 0){
        		logger.error("getAnonymousUserId is null,actorId = "+actorId);
				throw new Exception("anonymoususer is null");
        	}
        }
        
    }
    
    /**
     * @param req
     * @param actorId
     * @throws Exception
     * 
     * 获取评论的时候一些特殊处理逻辑
     */
    private void doSpecialNeedGetList(GetCommentListRequest req,int actorId) throws Exception{
    	
    	//如果是匿名id的话,actorId需要为匿名的
        if(req.getParams().containsKey("isAnonymous") &&
        		"true".equalsIgnoreCase(req.getParams().get("isAnonymous"))){
        	int anonymousId = 0;
        	try {
        		anonymousId = UserAnonymousAdapter.getInstance().getAnonymousUserId(actorId);
				req.setActorId(anonymousId);
			} catch (Exception e) {
				logger.error("getAnonymousUserId error,actorId = "+actorId,e);
				throw new Exception("anonymoususer error");
			}
        	if(anonymousId == 0){
        		logger.error("getAnonymousUserId is null,actorId = "+actorId);
				throw new Exception("anonymoususer is null");
        	}
        }
        
    }
}
