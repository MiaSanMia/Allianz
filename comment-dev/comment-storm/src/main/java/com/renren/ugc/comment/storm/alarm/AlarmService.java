package com.renren.ugc.comment.storm.alarm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.renren.ugc.comment.storm.alarm.gauss.Gauss;
import com.renren.ugc.comment.storm.alarm.gauss.GaussVO;
import com.renren.ugc.comment.storm.alarm.strategy.AlarmConfig;
import com.renren.ugc.comment.storm.dao.ErrorLogDAO;
import com.renren.ugc.comment.storm.dao.GaussDAO;

/** 
 * @author meng.liu 
 * @date 2014-5-27 下午3:01:23 
 */
public class AlarmService {
	
//  标识系统是否初始化完毕的一个参数，false表示没有初始化完毕 
	private static boolean initialized = false;
	
	private static boolean isCalculating = false;
	private static Object object = new Object();
	
	private static final Log logger = LogFactory.getLog(AlarmService.class);
	
	/**
	 * 存储每个要监控信息对应的Gauss模型,用于计算是否需要报警
	 * key包含信息：business, hour(24小时制), message+location(需要考虑)
	 */
	private static Map<String, GaussVO> serverStandard = new HashMap<String, GaussVO>();
	
	private static Queue<Boolean> resultQueue = new LinkedList<Boolean>();
	private static final int CLEAN_QUEUE_SIZE = 4;
	
	private static final int HOURS_ONE_DAY = 24;
	
	public static void dailyCalculation() {
		long beginTime = System.currentTimeMillis();
		long lastTime = beginTime;
//		获取库中所有的需要监控的机器
		List<String> businessList = GaussDAO.loadAllBusiness();
		
		List<String> allMonitorKeys = getAllMonitorKeysF24Hours(businessList);
		
		for (String key : allMonitorKeys) {
//			初始化 map
			serverStandard.put(key, iniGaussVO(key));
			logger.info("++calculate gaussinfo key =" + key + " time:"
				    + "cost:"
					+ (System.currentTimeMillis() - lastTime) / 1000);
			
			lastTime = System.currentTimeMillis();
		}
//		计算完值之后， 存db做持久化处理
		saveOrUpdateDB();

	}

	/**
	 * 通过gauss模型计算当前的异常数是否需要报警
	 * @param message
	 * @param location
	 * @param business
	 * @param config
	 * @param count
	 * @return
	 */
	public static boolean isNeedAlarm(String message, String location, String business,
			AlarmConfig config, int count) {
		
//		如果系统没有被初始化，则返回false ，表示正常
		if (!initialized) {
			
			logger.info("not initialized yet =========================");
			if(!isCalculating) {
				synchronized (object) {
					if(!isCalculating) {
						isCalculating = true;
						new Thread(new Runnable() {
							public void run() {
//								start();
								dailyCalculation();
								initialized = true;
							}
						}).start();
					}
				}
			}
			
			return false;
		}
		
		int currentHour = new Date().getHours();
		String key = business + "_" + currentHour;
		
		logger.info(String.format("check value begin, key[%s], count[%d]", key, count));
//		得到map中的模型
		
		GaussVO gauss = serverStandard.get(key);
		
//		如果没有，有可能是新加进去的机器，不用管，照样返回正常
		if(gauss == null) {
			logger.error("no gauss vo");
			return false;
		}
		
		logger.info(String.format("get gaussVO,key[%s],ex[%.3f],dx[%.3f]", key,gauss.getEx(),gauss.getDx()));
		
		logger.info("++check result:"+!Gauss.check(gauss, count));
		return !Gauss.check(gauss, count);

	}
	
	public static void start() {
		if (!initialized) {
			System.out.println("starting gausssssssssssssssssssssssss");
			startCalculation();
		}
		logger.info("started gauss");
		initialized = true;
	}
	
	/**
	 * 加载并计算出所有需要监控的key的Gauss模型
	 */
	private static void startCalculation() {
		
		List<String> businessList = GaussDAO.loadAllBusiness();
		if(businessList == null || businessList.size() == 0) {
			System.out.println("load businessList failed");
			return;
		}
		
		////每个要监控的key，后面加上小小时数（即每个key后面加24个小时数组成 24 * size个key），对每小时的异常数值分别求gauss模型
		List<String> allMonitorKeys = getAllMonitorKeysF24Hours(businessList);
		
		List<GaussVO> gaussInfoList = GaussDAO.loadAllGaussInfo();
		
		for(String key:allMonitorKeys) {
			for(GaussVO gauss:gaussInfoList) {
				if(key.equals(gauss.getKey())) {
					serverStandard.put(key, gauss);
					break;
				}
			}
		}
		logger.info("serverStandard load success , size = " + serverStandard.size());
		
	}
	
	/**
	 * 计算出某key的gaussInfo
	 * @param key
	 * @return
	 */
	private static GaussVO iniGaussVO(String key) {
		
		List<Integer> activeValues = new ArrayList<Integer>();
		try {
			activeValues = ErrorLogDAO.queryMessageCount(key);
		} catch (Exception e) {
			logger.error("queryMessageCount failed key = " + key + "\n" + e.getMessage(), e);
		}

		for (Iterator<Integer> it = activeValues.iterator(); it.hasNext();) {
			int activeValue = (Integer) it.next();
//			如果返回 false  表示这条数据不正常
//			if(!Gauss.check(activeValues, activeValue))
//				清理脏数据，把不正常的数据删掉
//				it.remove();
			
			resultQueue.offer(Gauss.check(activeValues, activeValue));
			if (resultQueue.size() == CLEAN_QUEUE_SIZE + 1)
				resultQueue.poll();

			// 检验结果
			int count = 1;
			boolean flag = true;
			for (Boolean q : resultQueue) {
				if (!q && count == CLEAN_QUEUE_SIZE) {
					flag = false;
					break;
				} else if (!q)
					count++;
			}
			// 清理脏数据
			if (!flag)
				it.remove();
		}
//		現在得到的 activeValues 是被清理过的，理论上来讲他是一个正常的数据集，没有异常数据
		GaussVO vo = new GaussVO();
		vo.setDx(Gauss.getDx(activeValues));
		vo.setEx(Gauss.getEx(activeValues));
		return vo;
		
	}
	
	/**
	 *    将map中的值做持久话处理，便于系统热启动
	 */
	private static void saveOrUpdateDB(){
//		遍历 map中的所有信息
		for( Iterator<Entry<String, GaussVO>> it = serverStandard.entrySet().iterator();it.hasNext();){
			try{
					Entry<String, GaussVO> entry = it.next();
		//			初始化存储模型
					GaussVO info = new GaussVO();
					info.setKey(entry.getKey());
					info.setDx(entry.getValue().getDx());
					info.setEx(entry.getValue().getEx());
		//			到db中去取这条信息，如果存在则更新，不存在则删除
					if(GaussDAO.queryGaussInfo(info).size() != 0){
						GaussDAO.updateGaussInfo(info);
					}else{
						GaussDAO.insertGaussInfo(info);
					}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
	}
	}
	
	private static List<String> getAllMonitorKeysF24Hours(List<String> businessList) {
		
	        ////每个要监控的key，后面加上小小时数（即每个key后面加24个小时数组成 24 * size个key），对每小时的异常数值分别求gauss模型
			List<String> allMonitorKeys = new ArrayList<String>(businessList.size() * HOURS_ONE_DAY);
			for(String business:businessList) {
				for(int i=0;i<HOURS_ONE_DAY;i++) {
					allMonitorKeys.add(business + "_" + i);
				}
			}
			return allMonitorKeys;
		
	}
	
	public static void main(String[] args) {
		
		System.out.println(new Date().getHours());
		
		double i = 6.666;
		
		System.out.println(String.format("[%.2f]", i));
		
	}

}
