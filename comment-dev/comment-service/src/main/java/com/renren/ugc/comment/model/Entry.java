package com.renren.ugc.comment.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 被评论的实体, 内部使用
 * 
 * @author lvenle
 */
public class Entry implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;

    private int ownerId;

    private String name;

    private String ownerName;

    private String ownerHead;

    private String type;

    public Entry() {
    }

    public Entry(long id, int ownerId, String type) {
        this.id = id;
        this.ownerId = ownerId;
        this.type = type;
        entryProps = new HashMap<String,String>();
    }

    public Entry(com.renren.ugc.comment.xoa2.Entry entry) {
        if (entry != null) {
            this.setId(entry.getId());
            this.setName(entry.getName());
            this.setOwnerId(entry.getOwnerId());
            this.setOwnerName(entry.getOwnerName());
            this.setEntryProps(entry.getEntryProps());
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerHead() {
        return ownerHead;
    }

    public void setOwnerHead(String ownerHead) {
        this.ownerHead = ownerHead;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * entry自定义的参数
     */
    private Map<String, String> entryProps;

    public Map<String, String> getEntryProps() {
        return entryProps;
    }

    public void setEntryProps(Map<String, String> entryProps) {
        this.entryProps = entryProps;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Entry)) {
            return false;
        }

        Entry that = (Entry) obj;
        if (this == that) {
            return true;
        } else {
            if (this.id == that.id && this.ownerId == that.ownerId && this.type.equals(that.type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int code = (int) this.id ^ this.ownerId;
        if (this.type != null) {
            code ^= this.type.hashCode();
        }

        return code;
    }
}
