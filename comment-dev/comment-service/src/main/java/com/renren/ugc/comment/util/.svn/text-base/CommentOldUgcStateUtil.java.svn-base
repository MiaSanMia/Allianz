package com.renren.ugc.comment.util;

/**
 * UGC迁移时的临时Flag代码，表示原始数据表的Flag. 完全迁移后会删除。
 * 
 * @author wangxx
 * 
 */
public class CommentOldUgcStateUtil {

    private final static int PHOTO_HAS_VOICE = 1 << 0;

    private final static int PHOTO_HAS_TAG = 1 << 2;

    private final static int STATUS_HAS_VOICE = 1 << 0;
    
    private final static int URL_HAS_VOICE = 1 << 0;

    public final static String PHOTO_TAG_INFO = "isPhotoTag";

    public static boolean isPhotoVoice(int state) {
        return (state & PHOTO_HAS_VOICE) == PHOTO_HAS_VOICE;
    }

    public static boolean isPhotoTag(int state) {
        return (state & PHOTO_HAS_TAG) == PHOTO_HAS_TAG;
    }

    public static int setPhotoVoice(int state) {
        return state | PHOTO_HAS_VOICE;
    }

    public static int setPhotoTag(int state) {
        return state | PHOTO_HAS_TAG;
    }

    public static boolean isStatusVoice(int state) {
        return (state & STATUS_HAS_VOICE) == STATUS_HAS_VOICE;
    }

    public static int setStatusVoice(int state) {
        return state | STATUS_HAS_VOICE;
    }
    
    public static int setUrlVoice(int state) {
        return state | URL_HAS_VOICE;
    }

}
