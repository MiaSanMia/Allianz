package com.renren.ugc.comment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import com.renren.ugc.comment.xoa2.Comment;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.comment.xoa2.CreateCommentResponse;
import com.renren.ugc.comment.xoa2.FeedCommentInfo;
import com.renren.ugc.comment.xoa2.FeedCommentResult;
import com.renren.ugc.comment.xoa2.GetCommentsForFeedCountResponse;
import com.renren.ugc.comment.xoa2.GetCommentsForFeedResponse;
import com.renren.ugc.comment.xoa2.IXCommentCenter;
import com.renren.xoa2.ErrorInfo;

@Ignore
public class FeedApiTest {
    
    static IXCommentCenter xcc = null;

    private static CommentXoa2Client COMMENT_CLIENT = new CommentXoa2Client();

    @Test
    public void testGetCommentsForFeed() {

        int feedid = 1;
        List<FeedCommentInfo> infos = new ArrayList<FeedCommentInfo>();
        
        FeedCommentInfo info = new FeedCommentInfo();
        info.setEntryId(6908996919l);
        info.setEntryOwnerId(454926876);
        info.setType(CommentType.Photo);
        info.setFeedid(feedid++);
        //status
        FeedCommentInfo info2 = new FeedCommentInfo();
        info2.setEntryId(5014420847L);
        info2.setEntryOwnerId(454926876);
        info2.setType(CommentType.Status);
        info2.setFeedid(feedid++);
        
        //blog
        FeedCommentInfo info4 = new FeedCommentInfo();
        info4.setEntryId(914537041L);
        info4.setEntryOwnerId(454926876);
        info4.setType(CommentType.Blog);
        info4.setFeedid(feedid++);

        //album
        //commentcenter6
        FeedCommentInfo info6 = new FeedCommentInfo();
        info6.setEntryId(928852809L);
        info6.setEntryOwnerId(454926876);
        info6.setType(CommentType.Album);
        info6.setFeedid(feedid++);

        infos.add(info);
        infos.add(info2);
        infos.add(info4);
        infos.add(info6);
       
        int actorId = 454926876;
        
        GetCommentsForFeedCountResponse resp = COMMENT_CLIENT.getCommentsForFeedCount(infos, actorId);

        if (resp.isSetResults()) {
            Assert.assertNotNull(resp.getResults());
            
            Assert.assertEquals(resp.getResults().size(), infos.size());
            
            Iterator<Map.Entry<Long,Integer>> iter = resp.getResults().entrySet().iterator();
     
           Assert.assertEquals(8,iter.next().getValue().intValue());
           Assert.assertEquals(4,iter.next().getValue().intValue());
           Assert.assertEquals(3,iter.next().getValue().intValue());
           Assert.assertEquals(2,iter.next().getValue().intValue());
                
        } else {
            ErrorInfo err = resp.getBaseRep().getErrorInfo();
            System.out.println("error, code:" + err.getCode() + ", msg:" + err.getMsg());
        }
    }

    @Test
    public void testGetCommentsFor() throws UnsupportedEncodingException {

        int feedid = 1;
        List<FeedCommentInfo> infos = new ArrayList<FeedCommentInfo>();
        
        FeedCommentInfo info = new FeedCommentInfo();
        info.setEntryId(6908996919l);
        info.setEntryOwnerId(454926876);
        info.setHeadLimit(3);
        info.setTailLimit(2);
        info.setType(CommentType.Photo);
        info.setFeedid(feedid++);
        //status
        FeedCommentInfo info2 = new FeedCommentInfo();
        info2.setEntryId(5014420847L);
        info2.setEntryOwnerId(454926876);
        info2.setHeadLimit(2);
        info2.setTailLimit(3);
        info2.setType(CommentType.Status);
        info2.setFeedid(feedid++);
        
        //blog
        FeedCommentInfo info4 = new FeedCommentInfo();
        info4.setEntryId(914537041L);
        info4.setEntryOwnerId(454926876);
        info4.setHeadLimit(3);
        info4.setTailLimit(2);
        info4.setType(CommentType.Blog);
        info4.setFeedid(feedid++);

        //album
        //commentcenter6
        FeedCommentInfo info6 = new FeedCommentInfo();
        info6.setEntryId(928852809L);
        info6.setEntryOwnerId(454926876);
        info6.setHeadLimit(3);
        info6.setTailLimit(2);
        info6.setType(CommentType.Album);
        info6.setFeedid(feedid++);

        infos.add(info);
        infos.add(info2);
        infos.add(info4);
        infos.add(info6);
       
        int actorId = 454926876;
        
        GetCommentsForFeedResponse resp = COMMENT_CLIENT.getCommentsForFeed(infos, actorId);

        if (resp.isSetResults()) {
            Assert.assertNotNull(resp.getResults());
            
            Assert.assertEquals(resp.getResults().size(), infos.size());
            
            Iterator<Map.Entry<Long,FeedCommentResult>> iter = resp.getResults().entrySet().iterator();
            
                
                FeedCommentResult result = iter.next().getValue();
                Assert.assertEquals(8,result.totalCount);
                Assert.assertEquals(3, result.getHeadCommentList().size());
                Assert.assertEquals(2, result.getTailCommentList().size());
                
                FeedCommentResult result1 = iter.next().getValue();
                Assert.assertEquals(4,result1.totalCount);
                Assert.assertEquals(1, result1.getHeadCommentList().size());
                Assert.assertEquals(3, result1.getTailCommentList().size());
                
                FeedCommentResult result2 = iter.next().getValue();
                Assert.assertEquals(3,result2.totalCount);
                Assert.assertEquals(1, result2.getHeadCommentList().size());
                Assert.assertEquals(2, result2.getTailCommentList().size());
                
                FeedCommentResult result3 = iter.next().getValue();
                Assert.assertEquals(2,result3.totalCount);
                Assert.assertEquals(0, result3.getHeadCommentList().size());
                Assert.assertEquals(2, result3.getTailCommentList().size());

        } else {
            ErrorInfo err = resp.getBaseRep().getErrorInfo();
            System.out.println("error, code:" + err.getCode() + ", msg:" + err.getMsg());
        }
    }


}