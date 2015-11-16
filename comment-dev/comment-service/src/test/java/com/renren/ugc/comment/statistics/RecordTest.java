package com.renren.ugc.comment.statistics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RecordTest {

	@Test
	public void testInvokeDone() {
        Record r = new Record(RecordType.COMMENT_API, "dummy");
		
		r.invokeDone(25);
		r.invokeDone(30);
		r.invokeDone(90);
		
		DeltaRecord dr = r.getDelta();
		assertEquals("dummy", dr.getName());
		assertEquals(90, dr.getMaxTime());
		assertEquals(3, dr.getCount());
		assertEquals(0, dr.getTimeoutCount());
		assertEquals(0, dr.getExceptionCount());
		assertEquals(48, dr.getAvgTime());
		assertEquals(0, dr.getTimeoutRate());
		assertEquals(0, dr.getExceptionRate());
		
		r.invokeDone(70);
		r.invokeDone(25);
		r.invokeDone(42);
		r.invokeDone(46);
		
		dr = r.getDelta();
		assertEquals("dummy", dr.getName());
		assertEquals(70, dr.getMaxTime());
		assertEquals(4, dr.getCount());
		assertEquals(0, dr.getTimeoutCount());
		assertEquals(0, dr.getExceptionCount());
		assertEquals(45, dr.getAvgTime());
		assertEquals(0, dr.getTimeoutRate());
		assertEquals(0, dr.getExceptionRate());
	}
	
	@Test
	public void testFailure() {
		
        Record r = new Record(RecordType.COMMENT_API, "dummy");
		
		r.invokeDone(25);
		r.invokeDone(600);
		r.invokeException(90);
		
		DeltaRecord dr = r.getDelta();
		assertEquals("dummy", dr.getName());
		assertEquals(600, dr.getMaxTime());
		assertEquals(3, dr.getCount());
		assertEquals(1, dr.getTimeoutCount());
		assertEquals(1, dr.getExceptionCount());
		assertEquals(238, dr.getAvgTime());
        assertEquals(3333, dr.getTimeoutRate());
        assertEquals(3333, dr.getExceptionRate());
		
		r.invokeException(276);
		r.invokeDone(29);
		r.invokeDone(451);
		r.invokeDone(66);
		
		dr = r.getDelta();
		assertEquals("dummy", dr.getName());
		assertEquals(451, dr.getMaxTime());
		assertEquals(4, dr.getCount());
		assertEquals(2, dr.getTimeoutCount());
		assertEquals(1, dr.getExceptionCount());
		assertEquals(205, dr.getAvgTime());
        assertEquals(5000, dr.getTimeoutRate());
        assertEquals(2500, dr.getExceptionRate());
	}
	
	@Test
	public void testZeroCount() {
        Record r = new Record(RecordType.COMMENT_API, "dummy");
		DeltaRecord dr = r.getDelta();
		assertEquals("dummy", dr.getName());
		assertEquals(0, dr.getMaxTime());
		assertEquals(0, dr.getCount());
		assertEquals(0, dr.getTimeoutCount());
		assertEquals(0, dr.getExceptionCount());
		assertEquals(-1, dr.getAvgTime());
		assertEquals(-1, dr.getTimeoutRate());
		assertEquals(-1, dr.getExceptionRate());
	}
}
