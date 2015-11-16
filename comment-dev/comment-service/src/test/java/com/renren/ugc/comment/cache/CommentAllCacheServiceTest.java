package com.renren.ugc.comment.cache;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * 标注:{@link CommentAllCacheServiceTest} 评论中心cache测试类
 * 
 * @author xinyan.yang@renren-inc.com
 * 
 * @date 2013-1-31 20:43:19
 * 
 */
public class CommentAllCacheServiceTest {

    CommentCacheService service = CommentAllCacheService.getInstance();

    long entryId = 361463487;

    int entryOwnerId = 942596819;

    int type = CommentType.Dummy.getValue();

    String order = "ASC";

    int limit = 51;

    int borderCommentId = 30;

    int id = 123456789;

    int count = 1;

    Comment comment = new Comment();

    List<Comment> list = new ArrayList<Comment>();

    Logger logger = Logger.getLogger(this.getClass());

    @Before
    public void setUp() throws Exception {
        comment.setId(id);
        list.add(comment);
    }

    @Test
    public void testGetCommeListByEntry() {
        logger.debug("---------保存评论列表缓存-----------");
        service.setCommentListByEntryCache(entryId, entryOwnerId, type, order, limit, list);

        logger.debug("-----------获取评论列表缓存-------------");
        List<Comment> list = service.getCommentListByEntryCache(entryId, entryOwnerId, type, order,
                limit);
        assertEquals("list.size() == 1", 1, list.size());
        assertEquals("id = 123456789", id, list.get(0).getId());
    }

    @Test
    public void testGetCommeListByEntryWithBorderId() {
        logger.debug("-------------保存分页评论列表缓存------------");
        service.setCommentListByEntryCache(entryId, entryOwnerId, type, order, limit,
                borderCommentId, list);

        logger.debug("-------------获取分页评论列表缓存-------------");
        List<Comment> list = service.getCommentListByEntryCache(entryId, entryOwnerId, type, order,
                limit, borderCommentId);
        assertEquals("list.size() == 1", 1, list.size());
        assertEquals("id = 123456789", id, list.get(0).getId());
    }

    @Test
    public void testGetLatestEntryComment() {
        logger.debug("------------------保存最新的一条评论缓存----------");
        service.setLatestEntryCommentCache(entryId, entryOwnerId, type, comment);

        logger.debug("------------获取最新评论缓存-----------");
        Comment comment = service.getLatestEntryCommentCache(entryId, entryOwnerId, type);
        assertEquals("id = 123456789", id, comment.getId());
    }

    @Test
    public void testGetOldestEntryComment() {
        logger.debug("------------保存最旧的一条评论缓存------------");
        service.setOldestEntryCommentCache(entryId, entryOwnerId, type, comment);

        logger.debug("------------获取最旧的一条评论缓存--------------");
        Comment comment = service.getOldestEntryCommentCache(entryId, entryOwnerId, type);
        assertEquals("id = 123456789", id, comment.getId());
    }

    @Test
    public void testGetCount() {
        logger.debug("--------------设置计数器------------------");
        service.setCount(entryId, entryOwnerId, type, count);

        logger.debug("-------------获取计数器-------------");
        long count = service.getCount(entryId, entryOwnerId, type);
        assertEquals("count = 123456789", 1, count);
    }

    @Test
    public void testInc() {
        logger.debug("---------------计数器增1---------------");
        long count = service.incCount(entryId, entryOwnerId, type, 1);
        assertEquals("count 2", 2, count);
        logger.debug("----------------计数器减1---------------");
        count = service.incCount(entryId, entryOwnerId, type, -1);
        assertEquals("count 1", 1, count);
    }

    @Test
    public void testRemoveCache() {
        logger.debug("-----------------清空cache----------------");
        service.removeCacheByEntry(entryId, entryOwnerId, type);
        Comment comment = service.getLatestEntryCommentCache(entryId, entryOwnerId, type);
        Assert.assertTrue("comment == null", comment == null);
        comment = service.getOldestEntryCommentCache(entryId, entryOwnerId, type);
        Assert.assertTrue("comment == null", comment == null);
        List<Comment> list = service.getCommentListByEntryCache(entryId, entryOwnerId, type, order,
                limit);
        Assert.assertTrue("普通评论列表list == null", list == null);

        list = service.getCommentListByEntryCache(entryId, entryOwnerId, type, order, limit,
                borderCommentId);
        Assert.assertTrue("分页评论列表list == null", list == null);
    }
}
