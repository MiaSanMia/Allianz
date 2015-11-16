package com.renren.ugc.comment.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.renren.ugc.comment.model.Flag;

public class FlagTest {

    @Test
    public void testFlag() {
        Flag f = new Flag();
        f.setUseExtent();
        assertEquals(1, f.getValue());

        f.setUseVoice();
        assertEquals(3, f.getValue());

        f.unsetUseExtent();
        assertEquals(2, f.getValue());

        Flag f2 = new Flag(3);
        assertTrue(f2.isUseExtent());
        assertTrue(f2.isUseVoice());
    }

}
