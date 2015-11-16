package com.renren.ugc.comment.feed.util;

import com.renren.ugc.comment.xoa2.CommentType;
import org.apache.log4j.Logger;

/**
 * Access
 * 
 * @author jiankuan.xing
 * 
 */
public class AccessLogger {

    private static Logger logger = Logger.getLogger("ugc.comment.access");

    private final static int NA = -1; // means "not available integer"

    private final static String NA_STR = ""; // means "not available string"

    /**
     * @param count  count is the (headLimit << 8) & tailLimit
     */
    public static void logGetCommentsForFeed(int actorId,
            long execTime, int count, int errorCode) {
        doLog(CommentType.Feed, actorId, "getCommentsForFeed", NA, NA, NA, NA_STR, NA, NA, NA,
                execTime, errorCode);
    }

    public static void logGetCommentsForFeedCount(int actorId, int count, long execTime, int errorCode) {
        doLog(CommentType.Feed, actorId, "getCommentsForFeedCount", NA, NA, NA, NA_STR, NA, count,
                NA, execTime, errorCode);
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
