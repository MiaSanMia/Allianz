package com.renren.ugc.comment.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.renren.ugc.comment.xoa2.CommentType;

public class IDGeneratorTest {

    @Test
    public void testGenId() {
        CommentType type = CommentType.Status;
        long id1 = IDGenerator.nextCommentId(type);
        long id2 = IDGenerator.nextCommentId(type);
        long time = System.currentTimeMillis();

        long diff = id2 - id1;
        assertEquals(1, diff);
        int typeCode = (int) (id1 >> 54);
        assertEquals(CommentType.Status.getValue(), typeCode);
        assertEquals(time >> 5, (id1 >> 18) & 0x0000000FFFFFFFFFL);
    }

    @Test
    public void testGenIdWithExternalId() {
        long externalId = 14254999763L;
        CommentType type = CommentType.Status;
        long id = IDGenerator.nextCommentId(type, externalId);
        assertEquals(0x0640000351AA08D3L, id);
    }
}
