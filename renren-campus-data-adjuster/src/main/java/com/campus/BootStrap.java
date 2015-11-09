package com.campus;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.campus.mbean.CampusMonitorClient;
import com.campus.mbean.TripodMonitor;
import com.sun.jdmk.comm.HtmlAdaptorServer;

/** 
 * @author meng.liu 
 * @date 2014-4-5 下午3:32:52 
 */
public class BootStrap {
	
	private static Log logger = LogFactory.getLog(BootStrap.class);
	
	public static void main(String[] args) {
		
		if(logger.isDebugEnabled()) {
			logger.debug("campus bootStrap begin to start up------------------------");
		}
		
		CampusMonitorClient campusTairClient = new CampusMonitorClient();
		TripodMonitor tripodMonitor = new TripodMonitor();
		try {
			MBeanServer server = ManagementFactory.getPlatformMBeanServer();
		    
	        // 将Hello这个类注入到MBeanServer中，注入需要创建一个ObjectName类
	        ObjectName helloName = new ObjectName("renren.campus:name=campusMonitor");  
	        server.registerMBean(campusTairClient, helloName);
	        
	        ObjectName tripodName = new ObjectName("renren.campus:name=tripodMonitor");
	        server.registerMBean(tripodMonitor, tripodName);
	        
	        //创建一个AdaptorServer，这个类将决定MBean的管理界面，这里用最普通的Html型界面。AdaptorServer其实也是一个MBean
	        ObjectName adapterName = new ObjectName("CampusMonitorAgent:name=htmladapter,port=8082");
	        HtmlAdaptorServer adapter = new HtmlAdaptorServer();
	        server.registerMBean(adapter, adapterName);

	        adapter.start();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
        
        if (logger.isDebugEnabled()) {
            logger.info(String.format("CampusMBean started successfully"));
        }
		
	}

}
