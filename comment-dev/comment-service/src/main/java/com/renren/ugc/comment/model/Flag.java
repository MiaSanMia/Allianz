package com.renren.ugc.comment.model;

import java.io.Serializable;

/**
 * the flag used for MySQL storage. flag is a bitset value that is stored
 * into MySQL as a whole unsigned int value
 * 
 * @author jiankuan.xing
 * 
 */
public class Flag implements Serializable {

    private static final long serialVersionUID = 3832675332172870082L;

    private int bitsetValue = 0;

    public Flag() {
    }

    public Flag(int value) {
        bitsetValue = value;
    }

    /**
     * whether to use extent comment table to store long content
     */
    private static final int USE_EXTENT = 0x00000001;

    /**
     * whether to use voice comment table
     */
    private static final int USE_VOICE = 0x00000002;

    /**
     * whether is old data from ugc , because the primary key id 's
     * generation is different between old and new data
     */
    private static final int OLD_UGC = 0x00000004;
    
    /**
     * whether is the data from share , copy from "blogcomment"
     *  if comment is from share, you should filter body
     */
    private static final int FROM_SHARE = 1 << 3;
    
    /**
     * whether is old data from wap 
     */
    private static final int FROM_WAP = 1 << 4;
    
    /**
     * whether is the data linked 
     */
    private static final int USE_LINKED = 1 << 5;
    
    private boolean usePhotoTag;

    public void setUseExtent() {
        bitsetValue |= USE_EXTENT;
    }

    public boolean isUseExtent() {
        return (bitsetValue & USE_EXTENT) > 0;
    }

    public void unsetUseExtent() {
        bitsetValue &= ~USE_EXTENT;
    }

    public void setUseVoice() {
        bitsetValue |= USE_VOICE;
    }

    public boolean isUseVoice() {
        return (bitsetValue & USE_VOICE) > 0;
    }

    public void unsetUseVoice() {
        bitsetValue &= ~USE_VOICE;
    }

    public void setOldUgc() {
        bitsetValue |= OLD_UGC;
    }

    public boolean isOldUgc() {
        return (bitsetValue & OLD_UGC) > 0;
    }

    public void unsetOldUgc() {
        bitsetValue &= ~OLD_UGC;
    }
    
    public void setFromShare() {
        bitsetValue |= FROM_SHARE;
    }

    public boolean isFromShare() {
        return (bitsetValue & FROM_SHARE) > 0;
    }
    
    public void setFromWap() {
        bitsetValue |= FROM_WAP;
    }

    public boolean isFromWap() {
        return (bitsetValue & FROM_WAP) > 0;
    }
    
    public void setUseLinked() {
        bitsetValue |= USE_LINKED;
    }

    public boolean isUseLinked() {
        return (bitsetValue & USE_LINKED) > 0;
    }

    public int getValue() {
        return bitsetValue;
    }

    
    public boolean isUsePhotoTag() {
        return usePhotoTag;
    }

    
    public void setUsePhotoTag(boolean usePhotoTag) {
        this.usePhotoTag = usePhotoTag;
    }

    
    public int getBitsetValue() {
        return bitsetValue;
    }

    
    public void setBitsetValue(int bitsetValue) {
        this.bitsetValue = bitsetValue;
    }
    
    
    
}
