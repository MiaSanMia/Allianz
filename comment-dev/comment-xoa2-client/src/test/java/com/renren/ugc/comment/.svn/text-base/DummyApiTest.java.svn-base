package com.renren.ugc.comment;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import com.renren.ugc.comment.xoa2.*;
import org.junit.Test;

import com.renren.xoa2.ErrorInfo;

public class DummyApiTest {

    private static CommentXoa2Client COMMENT_CLIENT = new CommentXoa2Client();

    @Test
    public void testCreateDummy() {
        long entryId = 3453658807l;
        int actorId = 16544;
        int entryOwnerId = 16544;
        String content = "haha, this is a test";

        CreateCommentResponse resp = COMMENT_CLIENT.createComment(CommentType.Dummy, actorId,
                entryId, entryOwnerId, content, -1, null);

        if (resp.isSetComment()) {
            System.out.println("comment created: " + resp.getComment().getContent());
        } else {
            ErrorInfo err = resp.getBaseRep().getErrorInfo();
            System.out.println("error, code:" + err.getCode() + ", msg:" + err.getMsg());
        }
    }

    @Test
    public void testCreateDummyCommentWithEmojiContent() throws UnsupportedEncodingException {

        long entryId = 3453658808l;
        int actorId = 16544;
        int entryOwnerId = 16544;

        byte[] arr = { (byte) 0xF0, (byte) 0x9F, (byte) 0x92, (byte) 0x94 };
        String content = "This is an emoji " + new String(arr, "UTF8");

        CreateCommentResponse resp = COMMENT_CLIENT.createComment(CommentType.Dummy, actorId,
                entryId, entryOwnerId, content, -1, null);

        if (resp.isSetComment()) {
            System.out.println("comment created: " + resp.getComment().getContent());
        } else {
            ErrorInfo err = resp.getBaseRep().getErrorInfo();
            System.out.println("error, code:" + err.getCode() + ", msg:" + err.getMsg());
        }
    }

    @Test
    public void testGetDummyHeadAndTailComment() {
        long entryId = 3453658807l;
        int actorId = 16544;
        int entryOwnerId = 16544;
        GetHeadAndTailCommentListResponse resp = COMMENT_CLIENT.getHeadAndTailCommentList(
                CommentType.Dummy, actorId, entryId, entryOwnerId, 2, 3, true);
        if (resp.isSetHeadCommentList() || resp.isSetTailCommentList()) {
            System.out.println("get head comments: " + resp.getHeadCommentListSize()
                    + "; tail comments:" + resp.getTailCommentListSize());

        } else {
            ErrorInfo err = resp.getBaseRep().getErrorInfo();
            System.out.println("error, code:" + err.getCode() + ", msg:" + err.getMsg());
        }
    }


    @Test
    public void testGetHeadAndTailCommentList() {
        // Note this entry id is only used for testing get head and tail. DONOT create/remove comment for this entry.
        long entryId = 3453658810l;
        int actorId = 16544;
        int entryOwnerId = 16544;

        // test desc
        GetHeadAndTailCommentListResponse resp = COMMENT_CLIENT.getHeadAndTailCommentList(
                CommentType.Dummy, actorId, entryId, entryOwnerId, 1, 1, true);

        assertEquals(1, resp.getHeadCommentListSize());
        assertEquals(1, resp.getTailCommentListSize());
        Comment head = resp.getHeadCommentList().get(0);
        Comment tail = resp.getTailCommentList().get(0);
        assertEquals(29373335759660032l, head.getId());
        assertEquals(29373335760970754l, tail.getId());

        // test asc
        resp = COMMENT_CLIENT.getHeadAndTailCommentList(
                CommentType.Dummy, actorId, entryId, entryOwnerId, 1, 1, false);

        assertEquals(1, resp.getHeadCommentListSize());
        assertEquals(1, resp.getTailCommentListSize());
        head = resp.getHeadCommentList().get(0);
        tail = resp.getTailCommentList().get(0);
        assertEquals(29373335759660032l, head.getId());
        assertEquals(29373335760970754l, tail.getId());

        // test desc with limit 3
        resp = COMMENT_CLIENT.getHeadAndTailCommentList(
                CommentType.Dummy, actorId, entryId, entryOwnerId, 3, 3, true);

        assertEquals(3, resp.getHeadCommentListSize());
        assertEquals(3, resp.getTailCommentListSize());
        Comment head1 = resp.getHeadCommentList().get(0);
        Comment head2 = resp.getHeadCommentList().get(1);
        Comment head3 = resp.getHeadCommentList().get(2);
        Comment tail1 = resp.getTailCommentList().get(0);
        Comment tail2 = resp.getTailCommentList().get(1);
        Comment tail3 = resp.getTailCommentList().get(2);
        assertEquals(29373335760446464l, head1.getId());
        assertEquals(29373335760184320l, head2.getId());
        assertEquals(29373335759660032l, head3.getId());
        assertEquals(29373335760970754l, tail1.getId());
        assertEquals(29373335760970753l, tail2.getId());
        assertEquals(29373335760970752l, tail3.getId());

        // test asc with limit 3
        resp = COMMENT_CLIENT.getHeadAndTailCommentList(
                CommentType.Dummy, actorId, entryId, entryOwnerId, 3, 3, false);

        assertEquals(3, resp.getHeadCommentListSize());
        assertEquals(3, resp.getTailCommentListSize());
        head1 = resp.getHeadCommentList().get(0);
        head2 = resp.getHeadCommentList().get(1);
        head3 = resp.getHeadCommentList().get(2);
        tail1 = resp.getTailCommentList().get(0);
        tail2 = resp.getTailCommentList().get(1);
        tail3 = resp.getTailCommentList().get(2);
        assertEquals(29373335759660032l, head1.getId());
        assertEquals(29373335760184320l, head2.getId());
        assertEquals(29373335760446464l, head3.getId());
        assertEquals(29373335760970752l, tail1.getId());
        assertEquals(29373335760970753l, tail2.getId());
        assertEquals(29373335760970754l, tail3.getId());
    }

    @Test
    public void testGetHeadAndTailCommentListWithOverlappedSection() {

        // Note this entry id is only used for testing get head and tail with overlapped.
        // DONOT create/remove comment for this entry.
        // This comment list contains 4 comments.
        long entryId = 3453658811l;
        int actorId = 16544;
        int entryOwnerId = 16544;

        // test desc case, the overlapped comments will be put in tail comment list
        GetHeadAndTailCommentListResponse resp = COMMENT_CLIENT.getHeadAndTailCommentList(
                CommentType.Dummy, actorId, entryId, entryOwnerId, 3, 3, true);

        assertEquals(1, resp.getHeadCommentListSize());
        assertEquals(3, resp.getTailCommentListSize());
        Comment c1 = resp.getTailCommentList().get(0);
        Comment c2 = resp.getTailCommentList().get(1);
        Comment c3 = resp.getTailCommentList().get(2);
        Comment c4 = resp.getHeadCommentList().get(0);

        assertEquals(29373389186180097l, c1.getId());
        assertEquals(29373389186180096l, c2.getId());
        assertEquals(29373389185655808l, c3.getId());
        assertEquals(29373388740797440l, c4.getId());

        // test asc case, the overlapped comments will be put in head comment list
        resp = COMMENT_CLIENT.getHeadAndTailCommentList(
                CommentType.Dummy, actorId, entryId, entryOwnerId, 3, 3, false);

        assertEquals(3, resp.getHeadCommentListSize());
        assertEquals(1, resp.getTailCommentListSize());
        c1 = resp.getHeadCommentList().get(0);
        c2 = resp.getHeadCommentList().get(1);
        c3 = resp.getHeadCommentList().get(2);
        c4 = resp.getTailCommentList().get(0);
        assertEquals(29373388740797440l, c1.getId());
        assertEquals(29373389185655808l, c2.getId());
        assertEquals(29373389186180096l, c3.getId());
        assertEquals(29373389186180097l, c4.getId());

        // test desc case, the overlapped with limit 4
        resp = COMMENT_CLIENT.getHeadAndTailCommentList(
                CommentType.Dummy, actorId, entryId, entryOwnerId, 4, 4, true);

        assertEquals(0, resp.getHeadCommentListSize());
        assertEquals(4, resp.getTailCommentListSize());
        c1 = resp.getTailCommentList().get(0);
        c2 = resp.getTailCommentList().get(1);
        c3 = resp.getTailCommentList().get(2);
        c4 = resp.getTailCommentList().get(3);

        assertEquals(29373389186180097l, c1.getId());
        assertEquals(29373389186180096l, c2.getId());
        assertEquals(29373389185655808l, c3.getId());
        assertEquals(29373388740797440l, c4.getId());

        // test asc case, the overlapped with limit 4
        resp = COMMENT_CLIENT.getHeadAndTailCommentList(
                CommentType.Dummy, actorId, entryId, entryOwnerId, 4, 4, false);

        assertEquals(4, resp.getHeadCommentListSize());
        assertEquals(0, resp.getTailCommentListSize());
        c1 = resp.getHeadCommentList().get(0);
        c2 = resp.getHeadCommentList().get(1);
        c3 = resp.getHeadCommentList().get(2);
        c4 = resp.getHeadCommentList().get(3);
        assertEquals(29373388740797440l, c1.getId());
        assertEquals(29373389185655808l, c2.getId());
        assertEquals(29373389186180096l, c3.getId());
        assertEquals(29373389186180097l, c4.getId());
    }

    @Test
    public void testGetHeadAndTailFromEmptyCommentList() {
        // Note this entry id is only used for testing get head and tail with overlapped.
        // DONOT create/remove comment for this entry.
        // This comment list contains 0 comments.
        long entryId = 3453658812l;
        int actorId = 16544;
        int entryOwnerId = 16544;

        // test desc case, the overlapped comments will be put in tail comment list
        GetHeadAndTailCommentListResponse resp = COMMENT_CLIENT.getHeadAndTailCommentList(
                CommentType.Dummy, actorId, entryId, entryOwnerId, 3, 3, true);

        assertEquals(0, resp.getHeadCommentListSize());
        assertEquals(0, resp.getTailCommentListSize());
    }


    @Test
    public void testPing() {
        PingResponse resp = COMMENT_CLIENT.ping("hello");
        String content = resp.getContent();
        assertEquals("OK:hello", content);
    }
}
