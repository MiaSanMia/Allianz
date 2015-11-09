package com.campus.exception;

import com.campus.exception.CampusException;
/**
 * 操作cache时抛出的异常。
 *
 * @author Wang Shufeng [shufeng.wang@renren-inc.com]
 * @since Mar 12, 2014
 */
public class CacheException extends CampusException {
    
    /**
     * 
     */
    private static final long serialVersionUID = 5186431757076183043L;

    public CacheException(int errorCode, String message, Throwable cause) {
        super(message, cause);
    }

    public CacheException(int errorCode, String message) {
        super(message);
    }
}
