package com.renren.ugc.comment.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.renren.ugc.comment.xoa2.CommentType;

public class UrlMdsUtilTest {

	@Test
    public void testGetUrlMd5() {
        CommentType type = CommentType.Blog;
        int entryOwnerId = 471859607;
    	long entryId = 908910889;
        String expected = "http://blog.renren.com/GetEntry.do?id=" + entryId + "&owner=" + entryOwnerId;
        String actual = UrlMd5Util.getUrl(type, entryOwnerId, entryId);
        assertEquals(expected, actual);
        
        type = CommentType.Photo;
        entryOwnerId = 471859607;
    	entryId = 861121913;
        expected = "http://photo.renren.com/photo/" + entryOwnerId + "/photo-" + entryId;
        actual = UrlMd5Util.getUrl(type, entryOwnerId, entryId);
        assertEquals(expected, actual);
        
        type = CommentType.Album;
        entryOwnerId = 471859607;
    	entryId = 861121913;
        expected = "http://photo.renren.com/photo/" + entryOwnerId + "/album-" + entryId;
        actual = UrlMd5Util.getUrl(type, entryOwnerId, entryId);
        assertEquals(expected, actual);
        
        type = CommentType.Status;
        entryOwnerId = 471859607;
    	entryId = 861121913;
        expected = "http://status.renren.com/" + entryOwnerId + "/" + entryId;
        actual = UrlMd5Util.getUrl(type, entryOwnerId, entryId);
        assertEquals(null, actual);
        assertEquals(null, UrlMd5Util.getUrlMd5(actual));
    }
}
