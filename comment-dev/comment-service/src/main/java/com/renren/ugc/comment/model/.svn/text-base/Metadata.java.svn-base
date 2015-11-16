package com.renren.ugc.comment.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * the meta data of a comment. This data is used to store additional
 * key/value pairs that need to be attached to a comment.
 * 
 * @author jiankuan.xing
 * 
 */
public class Metadata implements Serializable {

    private static final long serialVersionUID = -3701783173329976712L;

    /**
     * key for comment's author head
     */
    public static final String COMMENT_AUTHOR_HEAD = "comment.author.head";

    /**
     * key for comment's author name
     */
    public static final String COMMENT_AUTHOR_NAME = "comment.author.name";

    private static Logger logger = Logger.getLogger(Metadata.class);

    private Map<String, String> metadata = new HashMap<String, String>();

    public Metadata() {
    }

    public Metadata(String metadataStr) {
        decode(metadataStr);
    }

    public void put(String key, String value) {
        metadata.put(key, value);
    }

    public String get(String key) {
        return metadata.get(key);
    }

    public void putAll(Map<String, String> params) {
        if (params == null) {
            return;
        }

        for (String key : params.keySet()) {
            metadata.put(key, params.get(key));
        }
    }

    public int size() {
        return metadata.size();
    }

    /**
     * Encode the metada to a string by url encoding strategy
     */
    public String encode() {

        if (metadata.isEmpty()) {
            return "";
        }

        Set<String> keySet = metadata.keySet();
        StringBuilder sb = new StringBuilder();
        try {
            for (String key : keySet) {
                sb.append(URLEncoder.encode(key, "utf-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(metadata.get(key), "utf-8"));
                sb.append("&");
            }

        } catch (UnsupportedEncodingException e) {
            // should never happen
            e.printStackTrace();
        }

        sb.setLength(sb.length() - 1); // trim the last "&"
        return sb.toString();
    }

    public void decode(String metadataStr) {
        if (!metadata.isEmpty()) {
            metadata.clear();
        }

        if (metadataStr == null || metadataStr.length() == 0) {
            return;
        }

        try {
            String[] sections = metadataStr.split("&");
            for (String section : sections) {
                int sepPos = section.indexOf("=");
                if (sepPos == -1 || sepPos == 0) {
                    logger.warn("Ignore invalid metadata string: " + metadataStr);
                    continue;
                }

                String key = section.substring(0, sepPos);
                String value = section.substring(sepPos + 1);
                metadata.put(URLDecoder.decode(key, "utf-8"), URLDecoder.decode(value, "utf-8"));
            }
        } catch (UnsupportedEncodingException e) {
            // should never happen
            e.printStackTrace();
        }
    }
    
    public Map<String, String> toInterfaceMetadata(){
        return metadata;
    }
    
    public boolean containsKey(String key) {
        return metadata.containsKey(key);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Metadata)) {
            return false;
        }

        Metadata that = (Metadata) obj;
        return this.metadata.equals(that.metadata);
    }
}
