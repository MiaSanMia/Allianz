package com.renren.ugc.comment.interceptor.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Test;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.strategy.CommentConfig;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.xoa2.CommentType;

public class CheckLengthInterceptorTest {

    private CheckLengthInterceptor interceptor = new CheckLengthInterceptor();

    @Test
    public void testCheckLength() {
        CommentConfig config = new CommentConfig();
        config.setMaxContentLength(100);

        CommentStrategy strategy = new CommentStrategy();
        strategy.setConfig(config);

        String content = new String("hello, this is a test comment with a relative long length "
                + ", in this test the max content length is 100 characters");

        Comment comment = new Comment();
        comment.setContent(content);

        // test create
        try {
            interceptor.create(CommentType.Dummy, 0, 0, 0, comment, strategy);
            fail("the exception is not thrown");
        } catch (UGCCommentException e) {
            // the exception is expected
        }

        // test update
        try {
            interceptor.update(CommentType.Dummy, 0, 0, 0, 0, comment, strategy);
            fail("the exception is not thrown");
        } catch (UGCCommentException e) {
            // the exception is expected
        }
    }

    @Test
    public void testCheckExtContentLength() {
        CommentConfig config = new CommentConfig();
        config.setUseExtContent(true);

        CommentStrategy strategy = new CommentStrategy();
        strategy.setConfig(config);

        // the max length MEDIUMTEXT type
        byte[] bytes = new byte[16777215];
        Arrays.fill(bytes, (byte) 48);
        String content = new String(bytes);

        Comment comment = new Comment();
        comment.setContent(content);

        // should pass
        interceptor.create(CommentType.Dummy, 0, 0, 0, comment, strategy);

        // one more byte than the MEDIUMTEXT type
        bytes = new byte[16777215 + 1];
        Arrays.fill(bytes, (byte) 48);
        content = new String(bytes);
        comment.setContent(content);

        // test create
        try {
            interceptor.create(CommentType.Dummy, 0, 0, 0, comment, strategy);
            fail("the exception is not thrown");
        } catch (UGCCommentException e) {
            // the exception is expected
        }

        // test update
        try {
            interceptor.update(CommentType.Dummy, 0, 0, 0, 0, comment, strategy);
            fail("the exception is not thrown");
        } catch (UGCCommentException e) {
            // the exception is expected
        }
    }

    @Test
    public void testTrimContent() {
        CommentConfig config = new CommentConfig();
        config.setMaxContentLength(100);
        config.setTrimWhenContentTooLong(true);

        CommentStrategy strategy = new CommentStrategy();
        strategy.setConfig(config);

        String content = new String("hello, this is a test comment with a relative long length "
                + ", in this test the max content length is 100 characters");

        Comment comment = new Comment();
        comment.setContent(content);

        // test create
        interceptor.create(CommentType.Dummy, 0, 0, 0, comment, strategy);
        assertEquals(content.substring(0, 100), comment.getContent());

        // test update
        Comment comment2 = new Comment();
        comment2.setContent(content);
        interceptor.update(CommentType.Dummy, 0, 0, 0, 0, comment2, strategy);
        assertEquals(content.substring(0, 100), comment2.getContent());

    }

}
