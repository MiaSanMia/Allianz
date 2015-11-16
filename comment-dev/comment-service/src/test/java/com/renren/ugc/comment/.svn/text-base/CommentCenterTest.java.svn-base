package com.renren.ugc.comment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentListResult;
import com.renren.ugc.comment.model.CommentVoiceInfo;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.service.CommentCenter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.QueryOrder;
import com.renren.ugc.comment.test.util.DBInitializer;
import com.renren.ugc.comment.xoa2.CommentType;

public class CommentCenterTest {

    Logger logger = Logger.getLogger(this.getClass());

    private static long entryId = 6809537162L;

    /**
     * use this method to ensure each test method use a different entryId
     * 
     * @return
     */
    private static long nextEntryId() {
        return ++entryId;
    }

    @BeforeClass
    public static void setUp() throws FileNotFoundException {
        DBInitializer.init();
    }

    /**
     * test create, get list, remove, recover, and update a comment
     */
    @Test
    public void testManipulateComment() throws InterruptedException {

        int actorId = 501522889;
        long entryId = nextEntryId();
        int entryOwnerId = 501522889;

        logger.debug("comment.cache.disabled property is "
                + System.getProperty("comment.cache.disabled"));

        // create
        Comment comm = new Comment();
        comm.setContent("this is a test comment");
        Entry entry = new Entry();
        entry.setId(entryId);
        entry.setOwnerId(entryOwnerId);
        entry.setName("test");
        comm.setEntry(entry);
        comm.setWhipserToId(0);
        CommentStrategy strategy = new CommentStrategy().setSendNotice(true).setReplaceUbb(true).setFeedType(
                217).setSourceId(576928345L).setUserIp("1.2.3.4");

        Map<String, String> params = new HashMap<String, String>();
        params.put("stype", "217");
        params.put("sourceId", "576928345");
        strategy.setParams(params);

        logger.debug("=============create a comment============");
        Comment result = CommentCenter.getInstance().create(CommentType.Dummy, actorId, entryId,
                entryOwnerId, comm, strategy);
        long commentId = result.getId();
        assertEquals(entryId, strategy.getEntry().getId());
        assertEquals(entryOwnerId, strategy.getEntry().getOwnerId());
        assertEquals("this is a test comment", result.getContent());

        // get
        logger.debug("=============get the created comment============");
        CommentStrategy strategyGet = new CommentStrategy();
        result = CommentCenter.getInstance().get(CommentType.Dummy, actorId, entry.getId(),
                entry.getOwnerId(), commentId, strategyGet);
        assertEquals("this is a test comment", result.getContent());
        Entry entryGet = strategyGet.getEntry();
        assertEquals(entryId, entryGet.getId());
        assertEquals(entryOwnerId, entryGet.getOwnerId());
        assertEquals(commentId, result.getId());
        assertNotNull(result.getOriginalContent()); // BIZFRAMEWORK-148 original content should not be null

        assertEquals("hdn321/20121213/1730/h_tiny_Dh32_5c7c000022a51376.jpg",
                result.getAuthorHead());

        // update
        logger.debug("==============update the comment=============");
        Comment newComment = new Comment();
        newComment.setContent("this is the updated comment");
        boolean updated = CommentCenter.getInstance().update(CommentType.Dummy, actorId,
                entry.getId(), entry.getOwnerId(), commentId, newComment, new CommentStrategy());
        assertTrue(updated);

        logger.debug("==============get the comment after update============");
        result = CommentCenter.getInstance().get(CommentType.Dummy, actorId, entry.getId(),
                entry.getOwnerId(), commentId, new CommentStrategy());
        assertEquals("this is the updated comment", result.getContent());
        assertEquals(commentId, result.getId());

        // remove
        logger.debug("=============remove created comment============");
        boolean removed = CommentCenter.getInstance().remove(CommentType.Dummy, actorId,
                entry.getId(), entry.getOwnerId(), commentId, strategy);
        assertTrue(removed);

        // get
        logger.debug("=============get the comment after remove===========");
        result = CommentCenter.getInstance().get(CommentType.Dummy, actorId, entry.getId(),
                entry.getOwnerId(), commentId, new CommentStrategy());
        assertNull(result);

        // recover
        logger.debug("=============recover the comment==============");
        boolean recovered = CommentCenter.getInstance().recover(CommentType.Dummy, actorId,
                entry.getId(), entry.getOwnerId(), commentId, new CommentStrategy());
        assertTrue(recovered);

        // get
        logger.debug("=============get the comment after recover==============");
        result = CommentCenter.getInstance().get(CommentType.Dummy, actorId, entry.getId(),
                entry.getOwnerId(), commentId, new CommentStrategy());
        assertNotNull(result);
    }

    @Test
    public void testManipulateMultipleList() throws InterruptedException {

        int actorId = 501522889;
        long entryId = nextEntryId();
        int entryOwnerId = 501522889;

        Entry entry = new Entry();
        entry.setId(entryId);
        entry.setOwnerId(entryOwnerId);

        Comment comm1 = new Comment();
        comm1.setContent("I create the first comment");
        entry.setName("test");
        comm1.setEntry(entry);
        comm1.setWhipserToId(0);

        Comment comm2 = new Comment();
        comm2.setContent("You make another test comment");
        comm2.setEntry(entry);
        comm2.setWhipserToId(0);

        CommentStrategy strategy = new CommentStrategy().setSendNotice(true).setReplaceUbb(true).setFeedType(
                217).setSourceId(576928345L).setUserIp("1.2.3.4");

        CommentStrategy queryStrategy = new CommentStrategy();
        queryStrategy.setQueryOffset(0);
        queryStrategy.setQueryLimit(2);
        queryStrategy.setQueryOrder(QueryOrder.DESC);

        logger.debug("=============create two comments============");
        Comment res1 = CommentCenter.getInstance().create(CommentType.Dummy, actorId, entryId,
                entryOwnerId, comm1, strategy);
        Comment res2 = CommentCenter.getInstance().create(CommentType.Dummy, actorId, entryId,
                entryOwnerId, comm2, strategy);

        logger.debug("=============get comments list============");
        strategy.setQueryLimit(10);
        strategy.setQueryOrder(QueryOrder.DESC);
//        List<Comment> results = CommentCenter.getInstance().getListByEntry(CommentType.Dummy,
//                actorId, entryId, entryOwnerId, strategy);
        CommentListResult result = CommentCenter.getInstance().getListByEntry(CommentType.Dummy,
              actorId, entryId, entryOwnerId, strategy);
        List<Comment> results = result.getCommentLists();
        assertEquals(2, results.size());
        Iterator<Comment> it = results.iterator();
        assertEquals(res2.getId(), it.next().getId());
        assertEquals(res1.getId(), it.next().getId());

        // remove
        logger.debug("=============remove two comments============");
        boolean removed1 = CommentCenter.getInstance().remove(CommentType.Dummy, actorId,
                entry.getId(), entry.getOwnerId(), res1.getId(), strategy);
        assertTrue(removed1);
        boolean removed2 = CommentCenter.getInstance().remove(CommentType.Dummy, actorId,
                entry.getId(), entry.getOwnerId(), res2.getId(), strategy);
        assertTrue(removed2);
    }

    @Test
    public void testTooLongComment() throws InterruptedException {
        logger.debug("create a content with invalid length");

        int actorId = 501522889;
        long entryId = nextEntryId();
        int entryOwnerId = 501522889;
        Entry entry = new Entry();
        entry.setId(entryId);
        entry.setOwnerId(entryOwnerId);

        Comment comm = new Comment();

        entry.setName("test");
        comm.setEntry(entry);
        comm.setWhipserToId(0);

        CommentStrategy strategy = new CommentStrategy().setSendNotice(true).setReplaceUbb(true).setFeedType(
                217).setSourceId(576928345L);

        // length is beyond the max length of open commment box type (600chr)
        logger.debug("====create a comment with too long content====");
        comm.setContent("this is a test comment this is a test comment "
                + "this is a test comment this is a test comment "
                + "this is a test comment this is a test comment "
                + "this is a test comment this is a test comment "
                + "this is a test comment this is a test comment "
                + "this is a test comment this is a test comment "
                + "this is a test comment this is a test comment "
                + "this is a test comment this is a test comment "
                + "this is a test comment this is a test comment"
                + "this is a test comment this is a test comment"
                + "this is a test comment this is a test comment"
                + "this is a test comment this is a test comment"
                + "this is a test comment this is a test comment"
                + "this is a test comment this is a test comment");
        try {
            CommentCenter.getInstance().create(CommentType.OpenCommentBoxF, actorId, entryId,
                    entryOwnerId, comm, strategy);
            fail("the content length is not checked");
        } catch (UGCCommentException e) {
            logger.debug("the content length is checked: " + e.getMessage());
        }
    }

    @Test
    public void testManipulateExtContent() {
        int actorId = 501522889;
        long entryId = nextEntryId();
        int entryOwnerId = 501522889;
        Entry entry = new Entry();
        entry.setId(entryId);
        entry.setOwnerId(entryOwnerId);
        entry.setName("test");

        Comment comm = new Comment();
        comm.setEntry(entry);
        comm.setWhipserToId(0);

        /**
         * construct a content with 6000 characters
         */
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 600; i++) {
            sb.append("abcdefghi");
        }
        String content = sb.toString();
        comm.setContent(content);

        // Dummy type supports ext content
        logger.debug("=============create an ext comment============");
        Comment result = CommentCenter.getInstance().create(CommentType.Dummy, actorId, entryId,
                entryOwnerId, comm, new CommentStrategy());
        long commentId = result.getId();
        assertEquals(content, result.getContent());

        logger.debug("=============get the created ext comment============");
        result = CommentCenter.getInstance().get(CommentType.Dummy, actorId, entry.getId(),
                entry.getOwnerId(), commentId, new CommentStrategy());
        assertEquals(content, result.getContent());
        assertEquals(commentId, result.getId());

        // remove
        logger.debug("=============remove the created ext comment============");
        boolean removed = CommentCenter.getInstance().remove(CommentType.Dummy, actorId,
                entry.getId(), entry.getOwnerId(), commentId, new CommentStrategy());
        assertTrue(removed);

        // get
        logger.debug("=============get the ext comment after remove===========");
        result = CommentCenter.getInstance().get(CommentType.Dummy, actorId, entry.getId(),
                entry.getOwnerId(), commentId, new CommentStrategy());
        assertNull(result);

        // recover
        logger.debug("=============recover the ext comment==============");
        boolean recovered = CommentCenter.getInstance().recover(CommentType.Dummy, actorId,
                entry.getId(), entry.getOwnerId(), commentId, new CommentStrategy());
        assertTrue(recovered);

        // get
        logger.debug("=============get the ext  comment after recover==============");
        result = CommentCenter.getInstance().get(CommentType.Dummy, actorId, entry.getId(),
                entry.getOwnerId(), commentId, new CommentStrategy());
        assertEquals(content, result.getContent());
    }

    @Test
    public void testGetCommentListWithBorderIdAndLimit() throws InterruptedException {
        int actorId = 501522889;
        long entryId = nextEntryId();
        int entryOwnerId = 501522889;
        CommentType type = CommentType.Dummy;

        Entry entry = new Entry();
        entry.setId(entryId);
        entry.setOwnerId(entryOwnerId);

        CommentStrategy strategy = new CommentStrategy();

        List<Long> commentIdList = new ArrayList<Long>(10);
        for (int i = 0; i < 10; i++) {
            Comment comm = new Comment();
            comm.setContent("I create the first comment");
            entry.setName("test");
            comm.setEntry(entry);
            comm.setWhipserToId(0);
            Comment result = CommentCenter.getInstance().create(type, actorId, entryId,
                    entryOwnerId, comm, strategy);
            commentIdList.add(result.getId());
            Thread.sleep(20); // sleep for a while to ensure the query order is correct whose order is by timestamp
        }

        logger.debug("prepare comments: " + commentIdList);

        logger.debug("================query by asc from the start================");
        CommentStrategy queryStrategy = new CommentStrategy().setQueryLimit(5).setQueryOrder(
                QueryOrder.ASC);
//        List<Comment> commentList = CommentCenter.getInstance().getListByEntry(type, actorId,
//                entryId, entryOwnerId, queryStrategy);
        CommentListResult result = CommentCenter.getInstance().getListByEntry(type, actorId,
              entryId, entryOwnerId, queryStrategy);
        List<Comment> commentList = result.getCommentLists();
        List<Long> ids = commentsToIds(commentList);
        logger.debug("query result: " + ids);
        assertEquals(5, commentList.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(commentIdList.get(i).longValue(), ids.get(i).longValue());
        }

        logger.debug("================query by desc from the start================");
        queryStrategy = new CommentStrategy().setQueryLimit(5).setQueryOrder(QueryOrder.DESC);
//        commentList = CommentCenter.getInstance().getListByEntry(type, actorId, entryId,
//                entryOwnerId, queryStrategy);
        result = CommentCenter.getInstance().getListByEntry(type, actorId, entryId,
                entryOwnerId, queryStrategy);
        commentList = result.getCommentLists();
        ids = commentsToIds(commentList);
        logger.debug("query result: " + ids);
        assertEquals(5, commentList.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(commentIdList.get(9 - i).longValue(), ids.get(i).longValue());
        }

        // get from the 3rd comment id with limit=5, ASC
        logger.debug("================query by asc with paging===============");
        queryStrategy = new CommentStrategy().setQueryBorderID(commentIdList.get(2)).setQueryLimit(
                5).setQueryOrder(QueryOrder.ASC);
//        commentList = CommentCenter.getInstance().getListByEntry(type, actorId, entryId,
//                entryOwnerId, queryStrategy);
        result = CommentCenter.getInstance().getListByEntry(type, actorId, entryId,
                entryOwnerId, queryStrategy);
        commentList = result.getCommentLists();
        ids = commentsToIds(commentList);
        logger.debug("query result: " + ids);
        assertEquals(5, commentList.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(commentIdList.get(i + 2).longValue(), ids.get(i).longValue());
        }

        // get from the 2nd comment id with limit=3, DESC
        logger.debug("================query by desc with paging===============");
        queryStrategy.setQueryBorderID(commentIdList.get(1)).setQueryLimit(3).setQueryOrder(
                QueryOrder.DESC);
//        commentList = CommentCenter.getInstance().getListByEntry(type, actorId, entryId,
//                entryOwnerId, queryStrategy);
        result = CommentCenter.getInstance().getListByEntry(type, actorId, entryId,
                entryOwnerId, queryStrategy);
        commentList = result.getCommentLists();
        ids = commentsToIds(commentList);
        logger.debug("query result: " + ids);
        assertEquals(2, commentList.size());
        for (int i = 0; i < 2; i++) {
            assertEquals(commentIdList.get(1 - i).longValue(), commentList.get(i).getId());
        }
    }

    /**
     * generate the id list based on the comment list
     */
    private List<Long> commentsToIds(List<Comment> comments) {
        List<Long> ids = new ArrayList<Long>();
        for (Comment comment : comments) {
            ids.add(comment.getId());
        }
        return ids;
    }

    @Test
    public void testGetMultiComments() {
        int actorId = 501522889;
        long entryId = nextEntryId();
        int entryOwnerId = 501522889;

        Entry entry = new Entry();
        entry.setId(entryId);
        entry.setOwnerId(entryOwnerId);

        Comment comm1 = new Comment();
        comm1.setContent("comment 1");
        comm1.setEntry(entry);

        Comment comm2 = new Comment();
        comm2.setContent("comment 2");
        comm2.setEntry(entry);

        Comment comm3 = new Comment();
        comm3.setContent("comment 3");
        comm3.setEntry(entry);

        CommentStrategy strategy = new CommentStrategy();
        Comment res1 = CommentCenter.getInstance().create(CommentType.Dummy, actorId, entryId,
                entryOwnerId, comm1, strategy);
        Comment res2 = CommentCenter.getInstance().create(CommentType.Dummy, actorId, entryId,
                entryOwnerId, comm2, strategy);
        Comment res3 = CommentCenter.getInstance().create(CommentType.Dummy, actorId, entryId,
                entryOwnerId, comm3, strategy);

        List<Long> commentIds = new ArrayList<Long>();
        commentIds.add(res1.getId());
        commentIds.add(res3.getId());
        Map<Long, Comment> commentMap = CommentCenter.getInstance().getMulti(CommentType.Dummy,
                actorId, entryId, entryOwnerId, commentIds, strategy);

        assertEquals(2, commentIds.size());
        Comment c1 = commentMap.get(res1.getId());
        Comment c3 = commentMap.get(res3.getId());
        assertEquals(c1.getContent(), res1.getContent());
        assertEquals(c3.getContent(), res3.getContent());

        // test the case that the result should be an empty map
        commentIds.clear();
        commentIds.add((long) 1);
        commentIds.add((long) 2);
        Map<Long, Comment> commentMap2 = CommentCenter.getInstance().getMulti(CommentType.Dummy,
                actorId, entryId, entryOwnerId, commentIds, strategy);
        assertEquals(0, commentMap2.size());
    }

    @Test
    public void testManipulateVoiceComment() {
        int actorId = 501522889;
        long entryId = nextEntryId();
        int entryOwnerId = 501522889;
        
        CommentVoiceInfo vi = new CommentVoiceInfo();
        String url = "http://fmn.rrimg.com/fmn060/audio/20130922/1045/m_kZEh_05340000d5af118f.mp3";
        vi.setVoiceUrl(url);
        vi.setVoiceRate(16000);
        vi.setVoiceSize(4536);
        vi.setVoiceLength(1);
        
        Comment comm = new Comment();
        comm.setContent("This is a voice comment");
        comm.setVoiceInfo(vi);
        
        Entry entry = new Entry();
        entry.setId(entryId);
        entry.setOwnerId(entryOwnerId);

        CommentStrategy strategy = new CommentStrategy();
        strategy.setEntry(entry);

        Comment createResult = CommentCenter.getInstance().create(CommentType.Dummy, actorId,
                entryId, entryOwnerId, comm, strategy);
        assertNotNull(createResult);
        assertTrue(createResult.isVoiceComment());
        CommentVoiceInfo resultVi = createResult.getVoiceInfo();
        assertNotNull(resultVi);
        assertEquals(url, resultVi.getVoiceUrl());
        assertEquals(16000, resultVi.getVoiceRate());
        assertEquals(4536, resultVi.getVoiceSize());
        assertEquals(1, resultVi.getVoiceLength());
        assertEquals(0, resultVi.getVoicePlayCount());

        Comment getResult = CommentCenter.getInstance().get(CommentType.Dummy, actorId, entryId,
                entryOwnerId, createResult.getId(), strategy);
        assertNotNull(getResult);
        assertTrue(getResult.isVoiceComment());
        resultVi = createResult.getVoiceInfo();
        assertNotNull(resultVi);
        assertEquals(url, resultVi.getVoiceUrl());
        assertEquals(16000, resultVi.getVoiceRate());
        assertEquals(4536, resultVi.getVoiceSize());
        assertEquals(1, resultVi.getVoiceLength());
        assertEquals(0, resultVi.getVoicePlayCount());

        int newPlayCount = CommentCenter.getInstance().increaseVoiceCommentPlayCount(
                CommentType.Dummy, actorId, entryId, entryOwnerId, createResult.getId(), 3,
                strategy);
        assertEquals(3, newPlayCount);

        Comment getResult2 = CommentCenter.getInstance().get(CommentType.Dummy, actorId, entryId,
                entryOwnerId, createResult.getId(), strategy);
        assertNotNull(getResult2);
        assertTrue(getResult2.isVoiceComment());
        resultVi = getResult2.getVoiceInfo();
        assertNotNull(resultVi);
        assertEquals(url, resultVi.getVoiceUrl());
        assertEquals(16000, resultVi.getVoiceRate());
        assertEquals(4536, resultVi.getVoiceSize());
        assertEquals(1, resultVi.getVoiceLength());
        assertEquals(3, resultVi.getVoicePlayCount());
    }
}
