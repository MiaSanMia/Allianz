package com.renren.ugc.comment.xoa2.util;

import com.renren.ugc.comment.model.Comment;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * User: jiankuan
 * Date: 9/12/13
 * Time: 18:01
 * CommentIdDescComparator Unit test case
 */
public class CommentIdDescComparatorTest {

    @Test
    public void testCompare() {
        TreeSet<Comment> set = new TreeSet<Comment>(new CommentIdDescComparator());
        Comment c1 = new Comment();
        c1.setId(1234);

        Comment c2 = new Comment();
        c2.setId(6789);

        Comment c3 = new Comment();
        c3.setId(2678);

        Comment c4 = new Comment();
        c4.setId(4210);

        set.add(c1);
        set.add(c2);
        set.add(c3);
        set.add(c4);

        List<Comment> expected = new ArrayList<Comment>(4);
        expected.add(c2);
        expected.add(c4);
        expected.add(c3);
        expected.add(c1);

        Iterator<Comment> it1 = set.iterator();
        Iterator<Comment> it2 = expected.iterator();

        while (it1.hasNext() && it2.hasNext()) {
            Comment cA = it1.next();
            Comment cB = it2.next();
            Assert.assertEquals(cB.getId(), cA.getId());
        }
    }
}
