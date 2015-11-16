package com.renren.ugc.comment.cache;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.renren.tair.util.TairManagerFactory;
import com.renren.ugc.comment.tair.TairCacheManagerImpl;
import com.taobao.tair.DataEntry;
import com.taobao.tair.Result;
import com.taobao.tair.impl.DefaultTairManager;

public class TairKeyValueTest {
	
	private DefaultTairManager tairManager;
	
	@Before
	public void init(){
		tairManager = TairManagerFactory.getDefaultTairManager("group_2");
	}
	
	public void keySetter(){
		String key = getTairKey();
		String value = "";
		tairManager.put(67, key, value);
	}
	
	@Test
	public void testValue() {
		Result<DataEntry> result = tairManager.get(67, getTairKey());
		if (result.isSuccess()) {// 能够确保result不会为null
			// todo成功打印日志
			DataEntry dataEntry = result.getValue();
			if (dataEntry != null) {
				System.out.println(dataEntry.getValue());
				Assert.assertEquals("on", dataEntry.getValue());
			} else {
			}
		} else {
		}
	}
	
	private String getTairKey(){
		return TairCacheManagerImpl.SWITCH_ON_OFF + "kafka";
	}

}
