/**
 * 
 */
package com.renren.pillar.common.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhang.liang
 * @createTime 2014-5-15 上午11:08:14
 * 
 */
public class BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;

    /** log在客户端生成的时间 目前都是精确到分钟*/
    private Date logTime;

    /** eg. share-web share-xoa share-xoa like-web like-xoa */
    private String business;

    /** server ip */
    private String ip;

    private Date createTime;

    private int enable = 1;

    /** other info , can record anything you want */
    private String note;

    
    private int count;
    
    /** a property for query , not in db */
    private Date afterTime;

    
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public LogType getLogType() {
        return null;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getAfterTime() {
        return afterTime;
    }

    public void setAfterTime(Date afterTime) {
        this.afterTime = afterTime;
    }

    
    public int getCount() {
        return count;
    }

    
    public void setCount(int count) {
        this.count = count;
    }
    
    
}
