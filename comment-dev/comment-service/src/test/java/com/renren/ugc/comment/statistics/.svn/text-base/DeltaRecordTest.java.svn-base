package com.renren.ugc.comment.statistics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DeltaRecordTest {

    @Test
    public void testToString() {
        DeltaRecord dr = new DeltaRecord("test", RecordType.COMMENT_API, 15, 0, 3, 2, 432, 60);
        assertEquals(
                "test:count=15,max(ms)=60,avg(ms)=28,timeout=3,timeout-rate(%%)=2000,exception=2,exception-rate(%%)=1333",
                dr.toString());


    }
}
