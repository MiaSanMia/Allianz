package com.renren.ugc.comment.storm.alarm.gauss;
/**  计算概率密度值
 * @author 樊城晨
 *
 */
public class Cal
{
   
	 private static double Fi_erf_6(double x){
		 double a=Math.abs(x);
		 return 0.5*(1+erf_6(a/Math.sqrt(2)));
	 }
	 private static double erf_6(double x){
		double a[]={0.070523084,0.0422820123,0.0092705272,0.0001520143,0.0002765672,0.0000430638};
		double t=0;
		for(int i=0;i<6;i++){
			t=t+a[i]*Math.pow(x, i+1);
		}
		return 1-Math.pow(1+t, -16);
	 }
	 
	 /**
	  * 正态分布 不正常概率
	  * 
	  * @param x
	  * @return
	  */
	 public static double Ni(double x){
		 return x==0?0.5:(x>0?Fi_erf_6(x):1-Fi_erf_6(x));
	 }
	 
	 private static void printTable(){
		 for(double i=0;i<3;i+=0.1){
			 for(double j=0;j<0.1;j+=0.01){
			  if(i==0){
  				 if(j==0)
  					 System.out.print("     "+j+"                ");
  				 else if(j<=0.09)
  				   System.out.print((double)Math.round(j*100)/100+"                ");
  				 else
  					 System.out.print("  ");
			    }
			  }
			 if(i==0)
			 {
				 System.out.println();
				 System.out.print("     -------------------------------------------------------" +
				 		"--------------------------------------------------------------------------" +
				 		"--------------------------------------------------------------------------");  
			 }
			 System.out.println("   |");
			 System.out.print((double)Math.round(i*10)/10+"| ");
			 for(double j=0;j<0.1;j+=0.01){
			   if(i==0&&j==0)
			  	 System.out.print(Ni(i+j)+"                ");
			   else if(j<=0.09)
				   System.out.print(Ni(i+j)+" ");
				 else
					 System.out.print("  ");
			 }
			 System.out.println();
		 }
	 }
	 
	 public static void main(String[] args){
		 
  	 printTable();
   }
}
