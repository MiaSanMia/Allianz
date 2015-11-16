/**
 * 
 */
package com.renren.pillar.common.model;

/**
 * @author zhang.liang
 * @createTime 2014-5-21 下午6:34:36
 * 
 */
public class AccessLog extends BaseModel {
 
    private static final long serialVersionUID = 1L;

    private String responseCode;
    
    private String url;
    
    public String getResponseCode() {
        return responseCode;
    }

    
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    
    public String getUrl() {
        return url;
    }

    
    public void setUrl(String url) {
        this.url = url;
    }

     
}
