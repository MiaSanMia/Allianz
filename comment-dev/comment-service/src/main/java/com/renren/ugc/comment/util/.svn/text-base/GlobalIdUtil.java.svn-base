package com.renren.ugc.comment.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.xoa2.CommentType;
import com.xiaonei.commons.gid.util.G;
import com.xiaonei.commons.gid.util.Type;


public class GlobalIdUtil {
    
    private static Logger logger = Logger.getLogger(GlobalIdUtil.class);
    
    private static final Map<CommentType,Type> globalMaps = new HashMap<CommentType,Type>();
    
    static {
        globalMaps.put(CommentType.Blog, Type.BLOG);
        globalMaps.put(CommentType.Photo, Type.PHOTO);
        globalMaps.put(CommentType.Album, Type.ALBUM);
        globalMaps.put(CommentType.Status, Type.STATUS);
        globalMaps.put(CommentType.Share, Type.SHARE);
    }
    
    public static long getGlobalId(final CommentType type,final long blogId) {
        long globalId = 0l;
        
        if(globalMaps == null || globalMaps.get(type) == null){
           logger.error("GlobalIdUtil found no such type:"+type.getValue());
        }
        try {
            globalId = G.id(blogId, globalMaps.get(type));
        } catch (Exception e) {
            logger.error("GlobalIdUtil error|type:"+type.getValue()+"|msg:"+e.getMessage());
        }
        return globalId;
    }

}
