package com.renren.ugc.comment.feed;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;


/**
 * @author wangxx
 *
 * fast json中，并不是所有的评论信息都存在redis中
 * 与{@link:FeedCommentPropertyFilter}不同的是，这个filter会保存originalContent,过滤掉content
 * 
 */
public class FeedOriginalCommentPropertyFilter implements PropertyFilter {
    
    private static FeedOriginalCommentPropertyFilter instance = new FeedOriginalCommentPropertyFilter();
    
    /**
     * 保存不需要转化成json的字段名字
     */
    private Set<String> excludeNames = new HashSet<String>();
    
    private FeedOriginalCommentPropertyFilter(){
        excludeNames.add("entry");
        excludeNames.add("content");
        excludeNames.add("metadata");
        excludeNames.add("likeInfo");
    }
    
    public static FeedOriginalCommentPropertyFilter getInstance(){
        return instance;
    }
    
    public boolean apply(Object source, String name, Object value) {
        
       if(excludeNames != null && excludeNames.contains(name)){
           return false;
       }
        
        return true;
    }
           
}
