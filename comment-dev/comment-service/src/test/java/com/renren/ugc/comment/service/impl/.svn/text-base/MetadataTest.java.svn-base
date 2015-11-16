package com.renren.ugc.comment.service.impl;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.renren.ugc.comment.model.Metadata;

public class MetadataTest {

    @Test
    public void testGenMetadata() {

        assertEquals("", new Metadata(null).encode());
        Metadata metadata = new Metadata();
        metadata.putAll(new HashMap<String, String>());
        assertEquals("", metadata.encode());

        Map<String, String> params = new HashMap<String, String>();
        params.put("key1", "value1");
        params.put("key2", "value2 and value3 *+/");
        metadata.putAll(params);
        String metadataStr = metadata.encode();
        assertEquals("key2=value2+and+value3+*%2B%2F&key1=value1", metadataStr);

        assertEquals(0, new Metadata("").size());
        assertEquals(0, new Metadata(null).size());
        Metadata metadata2 = new Metadata(metadataStr);
        assertEquals(metadata, metadata2);
    }
}
