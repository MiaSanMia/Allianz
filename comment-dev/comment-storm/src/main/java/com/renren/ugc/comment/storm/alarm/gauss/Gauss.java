package com.renren.ugc.comment.storm.alarm.gauss;
import java.util.List;


/**   用于计算 数值的均值，方差 ，计算结果是否符合预期。
 * @author 樊城晨
 *
 */
public class Gauss {
		/**		计算数值的均值
		 * @param input
		 * @return
		 */
		public static double getEx(List<Integer> input){
			int sum = 0;
			
			for(Integer i:input){
				sum+=i;
			}
			// 原来可能input 是 0长度的 会被当做除数 
			return input.size() ==0? 0: (double)sum/input.size();
		}
		
		/** 计算数值 的标准差
		 * @param input
		 * @return
		 */
		public static double getDx(List<Integer> input){
//			获得均值
			double ex = getEx(input);
//			sum 为方差
			double sum = 0;
			for(Integer i: input){
				double down = i - ex;
				sum+=Math.pow(down,2);
			}
//			开平方获得标准差
			return Math.pow(sum/input.size(),0.5);
		}
		
		/**
		 * 
		 * @param input
		 * @param currentValue
		 * @return false 不正常 true 正常 
		 */
		public static boolean check(List<Integer> input,int currentValue){
			double ex = getEx(input);
			double dx = getDx(input);
//			变成标准 高斯分布  
			double x = (currentValue-ex)/dx;
			double probability = Cal.Ni(x);
			if(probability>0.99)
				return false;
			else return true;
		}
		
		/**
		 * @param vo  保存 相应的 均值 方差。
		 * @param currentValue
		 * @return  false 不正常 true 正常 
		 */
		public static boolean check(GaussVO vo , int currentValue){
			if(vo == null) 
					throw new IllegalArgumentException("no gauss vo !!!");
			double x = (currentValue-vo.getEx())/vo.getDx();
			double probability = Cal.Ni(x);
			if(probability>0.99)
				return false;
			else return true;
		}
		
		public static void main(String[] args){
			GaussVO vo = new GaussVO();
			vo.setEx(0.17416415846416660000);
			vo.setDx(0.47452935892416650000);
			System.out.println(check(vo,2));
//			int a = 5;
//			int b = 48;
//			System.out.println((double)a/b);
		}
		
}
