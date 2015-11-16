package com.renren.ugc.comment.statistics;


public enum RecordType {

    COMMENT_API, // comment center api invocation
    EXTERNAL_API, // comment center invoke external api (such as WUserAdapter or Like service)
    GET_ENTRY, // comment center invoke external ugc get api (such as getting a blog or getting a share)
    GET_ENTRY_CACHE, // the cache for invoking external ugc get api (such as getting a blog or getting a share)
    COMMENT_CACHE, // comment center's default cache (currently it's the first page comment list cache)
    COMMENT_REDIS_CACHE, // redis cache for head and tail comment list retrieval
    COMMENT_DB, // comment center's internal db access
    INTERNAL_API;// to note local function 
    

    public boolean isForCache() {
        return (this == GET_ENTRY_CACHE || this == COMMENT_CACHE || this == COMMENT_REDIS_CACHE);
    }
}
