package com.renren.ugc.comment.cache;

import org.junit.Test;

import com.renren.tair.ext.TairByteManagerImpl;
import com.renren.tair.util.TairGroup;
import com.renren.tair.area.misc.Area;
import com.renren.tair.util.TairManagerFactory;
import com.taobao.tair.DataEntry;
import com.taobao.tair.Result;
import com.taobao.tair.ResultCode;
import com.taobao.tair.impl.DefaultTairManager;

/**
 * @author xiaoqiang
 *
 */
public class CommentTairCacheServiceTest {
	
	@Test
	public void switchOnoffTest() {
		DefaultTairManager tairManager = TairManagerFactory.getDefaultTairManager(TairGroup.MISC);  //注意这里只能使用TairByteManagerImpl，不能用其他的manager
	    String key = "onoff_kafka";
	    String value = "on";

	    ResultCode rc = tairManager.put(Area.SWITCH_ON_OFF, key, value);
	    if (rc.isSuccess()) {
	      //todo： put数据成功
	    } else {
	      //todo： put数据失败，可以调用rc.getMessage();
	    }
	    
	    Result<DataEntry> result = tairManager.get(Area.SWITCH_ON_OFF, key);
	    if (result.isSuccess()) {
			DataEntry dataEntry = result.getValue();
			if (dataEntry != null) {
				System.out.println(dataEntry.getValue());
			} else {
			    //todo： key不存在
			}
	    } else {
	      //todo： get失败，错误码result.getRc().getCode(); 错误信息result.getRc().getMessage();

	    }
	}

}
