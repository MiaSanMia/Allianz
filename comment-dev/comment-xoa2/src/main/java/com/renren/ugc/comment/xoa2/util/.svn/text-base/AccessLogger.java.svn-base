package com.renren.ugc.comment.xoa2.util;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.xoa2.CommentType;

/**
 * Access
 * 
 * @author jiankuan.xing
 * 
 */
public class AccessLogger {

    private static Logger logger = Logger.getLogger("ugc.comment.access");

    private final static int FIRST_PAGE = 1;

    private final static int NOT_FIRST_PAGE = 0;

    private final static int NA = -1; // means "not available integer"

    private final static String NA_STR = ""; // means "not available string"

    public static void logCreateComment(CommentType type, int actorId, long entryId,
            int entryOwnerId, long execTime, int errorCode) {
        doLog(type, actorId, "createComment", entryId, entryOwnerId, NA, NA_STR, NA, NA, NA,
                execTime, errorCode);
    }
    
    public static void logCreateCommentByList(CommentType type, long entryId,
            int entryOwnerId, long execTime, int errorCode) {
        doLog(type, 0, "createCommentByList", entryId, entryOwnerId, NA, NA_STR, NA, NA, NA,
                execTime, errorCode);
    }

    public static void logCreateVoiceComment(CommentType type, int actorId, long entryId,
            int entryOwnerId, long execTime, int errorCode) {
        doLog(type, actorId, "createVoiceComment", entryId, entryOwnerId, NA, NA_STR, NA, NA, NA,
                execTime, errorCode);
    }

    public static void logCreateLinkedComment(CommentType type, int actorId, long entryId,
            int entryOwnerId, long execTime, int errorCode) {
        doLog(type, actorId, "createLinkedComment", entryId, entryOwnerId, NA, NA_STR, NA, NA, NA,
                execTime, errorCode);
    }

    public static void logGetCommentList(CommentType type, int actorId, long entryId,
            int entryOwnerId, int limit, int count, boolean isFirstPage, long execTime,
            int errorCode) {
        doLog(type, actorId, "getCommentList", entryId, entryOwnerId, NA, NA_STR, limit, count,
                isFirstPage ? FIRST_PAGE : NOT_FIRST_PAGE, execTime, errorCode);
    }

    public static void logGetGlobalCommentList(CommentType type, int actorId, String urlmd5,
            int limit, int count, boolean isFirstPage, long execTime, int errorCode) {
        doLog(type, actorId, "getGlobalCommentList", NA, NA, NA, urlmd5, limit, count,
                isFirstPage ? FIRST_PAGE : NOT_FIRST_PAGE, execTime, errorCode);
    }

    public static void logGetFriendsCommentList(CommentType type, int actorId, long entryId,
            int entryOwnerId, int limit, int count, boolean isFirstPage, long execTime,
            int errorCode) {
        doLog(type, actorId, "getFriendsCommentList", entryId, entryOwnerId, NA, NA_STR, limit,
                count, isFirstPage ? FIRST_PAGE : NOT_FIRST_PAGE, execTime, errorCode);
    }

    /**
     * Here limit=headLimt + tailLimit, count = headCount + tailCount
     */
    public static void logGetHeadAndTailCommentList(CommentType type, int actorId, long entryId,
            int entryOwnerId, int headLimit, int tailLimit, int headCount, int limitCount,
            long execTime, int errorCode) {
        doLog(type, actorId, "getHeadAndTailCommentList", entryId, entryOwnerId, NA, NA_STR,
                headLimit + tailLimit, headCount + limitCount, NA, execTime, errorCode);
    }

    public static void logGetComment(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, long execTime, int errorCode) {
        doLog(type, actorId, "getComment", entryId, entryOwnerId, commentId, NA_STR, NA, NA, NA,
                execTime, errorCode);
    }

    public static void logRemoveComment(CommentType type, int actorId, long entryId,
            int entryOwnerId, long commentId, long execTime, int errorCode) {
        doLog(type, actorId, "removeComment", entryId, entryOwnerId, commentId, NA_STR, NA, NA, NA,
                execTime, errorCode);
    }

    public static void logRecoverComment(CommentType type, int actorId, long entryId,
            int entryOwnerId, long commentId, long execTime, int errorCode) {
        doLog(type, actorId, "recoverComment", entryId, entryOwnerId, commentId, NA_STR, NA, NA,
                NA, execTime, errorCode);
    }

    public static void logGetCommentCount(CommentType type, int actorId, long entryId,
            int entryOwnerId, int count, long execTime, int errorCode) {
        doLog(type, actorId, "getCommentCount", entryId, entryOwnerId, NA, NA_STR, NA, count,
                NA, execTime, errorCode);
    }

    /**
     * here we use "count" field to record the number of counts that's
     * retrieved
     */
    public static void logGetCommentCountBatch(CommentType type, int actorId, long entryId,
            int entryOwnerId, int count, long execTime, int errorCode) {
        doLog(type, actorId, "getCommentBatch", entryId, entryOwnerId, NA, NA_STR, NA,
                count, NA, execTime, errorCode);
    }

    /**
     * here we use "count" field to record the "increment"
     */
    public static void logIncreaseVoicePlayCount(CommentType type, int actorId, long entryId,
            int entryOwnerId, long commentId, int increment, long execTime, int errorCode) {
        doLog(type, actorId, "increaseVoicePlayCount", entryId, entryOwnerId, commentId, NA_STR,
                NA, increment, NA, execTime, errorCode);
    }

    /**
     * We only log the first comment id in this case. limit means the
     * number of requesting comment Ids count means the actual number of
     * returned comments
     */
    public static void logGetMultiComments(CommentType type, int actorId, long entryId,
            int entryOwnerId, long commentId, int limit, int count, long execTime, int errorCode) {
        doLog(type, actorId, "getMultiComments", entryId, entryOwnerId, commentId, NA_STR, limit,
                count, NA, execTime, errorCode);
    }

    private static void doLog(CommentType type, int actorId, String method, long entryId,
            int entryOwnerId, long commentId, String urlmd5, int limit, int count, int isFirstPage,
            long execTime, int errorCode) {
        String accessLog = String.format("%d|%d|%s|%s|%s|%s|%s|%s|%s|%s|%d|%d", 
                type.getValue(),
                actorId, 
                method, 
                (entryId == NA) ? "" : String.valueOf(entryId),
                (entryOwnerId == NA) ? "" : String.valueOf(entryOwnerId), 
                (commentId == NA) ? "" : String.valueOf(commentId),
                urlmd5,
                (limit == NA) ? "" : String.valueOf(limit),
                (count == NA) ? "" : String.valueOf(count),
                (isFirstPage == NA) ? "" : String.valueOf(isFirstPage),
                execTime, 
                errorCode);
        logger.info(accessLog);
    }
}
