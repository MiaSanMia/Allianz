package com.renren.ugc.comment.util;

import org.apache.log4j.Logger;

import com.renren.privacy.permission.define.PrivacySourceControlType;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.xoa2.CommentType;

public class EntryConfigUtil {
    
    private static Logger logger = Logger.getLogger(EntryConfigUtil.class);
    
    private static String getMap(CommentStrategy commentStrategy,String key){
        
        if (commentStrategy == null
                || commentStrategy.getEntry() == null
                || commentStrategy.getEntry().getEntryProps() == null
                || !commentStrategy.getEntry().getEntryProps()
                        .containsKey(key)) {
            return "";
        }
        return commentStrategy.getEntry().getEntryProps().get(key);
    }
    
 public static Long getLong(CommentStrategy commentStrategy,String key){
        
        long result = 0L;
        
        try{
            result = Long.parseLong(getMap(commentStrategy,key));
        } catch(Exception e){
           // throw new UGCCommentException("EntryConfigUtil getLong error|key:" + key + "|value:"+getMap(commentStrategy,key));
           // logger.error("EntryConfigUtil getLong error|key:" + key + "|value:"+getMap(commentStrategy,key),e);
        }
        
        return result;
 }
 
 public static Integer getInt(CommentStrategy commentStrategy,String key){
     
     Integer result = 0;
     
     try{
         result = Integer.parseInt(getMap(commentStrategy,key));
     } catch(Exception e){
         //throw new UGCCommentException("EntryConfigUtil getInt error|key:" + key+"|value:"+getMap(commentStrategy,key));
         logger.error("EntryConfigUtil getInt error|key:" + key + "|value:"+getMap(commentStrategy,key),e);
     }
     
     return result;
 }
 
 public static boolean getBoolean(CommentStrategy commentStrategy,String key){
 
     Boolean result = false;
     
     try{
         result = Boolean.parseBoolean(getMap(commentStrategy,key));
     } catch(Exception e){
        // throw new UGCCommentException("EntryConfigUtil getBoolean error|key:" + key+"|value:"+getMap(commentStrategy,key));
         logger.error("EntryConfigUtil getBoolean error|key:" + key + "|value:"+getMap(commentStrategy,key),e);
     }
     
     return result;
 }
 
 public static String getString(CommentStrategy commentStrategy,String key){
     
     String result = "";
     
     result = getMap(commentStrategy,key);
     
     return result;
 }
    
    private static boolean isPublic(int control){
        
        return control == PrivacySourceControlType.Open.getType();
    }
    
public static boolean isOnlySelf(int control){
        
        return control == PrivacySourceControlType.OnlySlef.getType();
    }

/**
 * @param type
 * @param comment
 * @return
 * 
 *  各个业务对于是否公开的定义都不一样
 */
public static boolean getIsPublic(CommentType type,CommentStrategy commentStrategy){
    
    boolean isPublic = false;
    switch(type){
        case Photo:
            //查看album的权限
            isPublic = isPublic(getInt(commentStrategy,EntryConfig.ENTRY_PARENT_CONTROL)) && !getBoolean(commentStrategy,EntryConfig.ENTRY_PARENT_IS_PASSWORD_PROTECTED);
            //查看photo的权限
            if(isPublic){
                isPublic = isPublic(getInt(commentStrategy,EntryConfig.ENTRY_CONTROL)) &&  !getBoolean(commentStrategy,EntryConfig.ENTRY_IS_PASSWORD_PROTECTED);
            }
            break;
        case Album:
            isPublic = isPublic(getInt(commentStrategy,EntryConfig.ENTRY_CONTROL)) && !getBoolean(commentStrategy,EntryConfig.ENTRY_IS_PASSWORD_PROTECTED);
            break;
         default:
                isPublic =  isPublic(getInt(commentStrategy,EntryConfig.ENTRY_CONTROL));
    }
    
    return isPublic;
}

public static boolean getParentIsPublic(CommentType type,CommentStrategy commentStrategy){
    
    boolean isPublic = false;
    switch(type){
        case Photo:
            //查看album的权限
            isPublic = isPublic(getInt(commentStrategy,EntryConfig.ENTRY_PARENT_CONTROL)) && !getBoolean(commentStrategy,EntryConfig.ENTRY_PARENT_IS_PASSWORD_PROTECTED);
            break;
         default:
                isPublic =  isPublic(getInt(commentStrategy,EntryConfig.ENTRY_CONTROL));
    }
    
    return isPublic;
}

    
    
    

}
