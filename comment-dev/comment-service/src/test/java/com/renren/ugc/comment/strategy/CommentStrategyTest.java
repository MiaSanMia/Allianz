package com.renren.ugc.comment.strategy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.renren.ugc.comment.strategy.CommentConfig;
import com.renren.ugc.comment.strategy.CommentStrategy;

public class CommentStrategyTest {

    @Test
    public void testSettingsWithConfig() {
        CommentStrategy strategy = new CommentStrategy();
        CommentConfig config = new CommentConfig();
        strategy.setConfig(config);

        config.setShouldAudit(true);
        assertTrue(strategy.shouldAudit());

        strategy.setShouldAudit(false);
        assertFalse(strategy.shouldAudit());

        strategy.setShouldAudit(true);
        assertTrue(strategy.shouldAudit());

        config.setReplaceUbb(true);
        assertTrue(strategy.isReplaceUbb());

        strategy.setReplaceUbb(false);
        assertFalse(strategy.isReplaceUbb());

        strategy.setReplaceUbb(true);
        assertTrue(strategy.isReplaceUbb());
    }
}
