package com.campus.transfer;


import com.campus.dao.ModuleDAO;
import com.campus.model.Module;

import net.paoding.rose.scanning.context.RoseAppContext;

/** 
 * @author meng.liu 
 * @date 2014-3-25 下午7:59:08 
 */
public class DaoModule {
	
	public static void main(String[] args) {
		RoseAppContext rose = new RoseAppContext();
		
		final ModuleDAO moduleDAO = rose.getBean(ModuleDAO.class);
		
		int beginIndex = Integer.parseInt(args[0]);
		int endIndex = Integer.parseInt(args[1]);
		
		System.out.println(String.format("begin to insert data to module, beginIndex[%d],endIndex[%d]",beginIndex,endIndex));
		
		try {
			for(int schoolId = beginIndex; schoolId <= endIndex; schoolId++) {
				//---------------树洞-------------------------------------------
				Module module = new Module();
				module.setSchoolId(schoolId);
				module.setName("口土木曹");
				module.setLogo("");
				module.setType(1);
				module.setSummary("我们是不吐槽会死星人！！");
				module.setMeta("");
				module.setPosition(1);
				module.setDefineType(1);
				module.setAnonymous(true);
				
				moduleDAO.createModule(module);
				
				System.out.println("insert 树洞 module successfully for school " + schoolId);
				
				//--------------------杂绘---------------------------------------
				module.setSchoolId(schoolId);
				module.setName("校园生活");
				module.setLogo("");
				module.setType(1);
				module.setSummary("这里没有花花世界，但有你身边的精彩生活。");
				module.setMeta("");
				module.setPosition(2);
				module.setDefineType(1);
				module.setAnonymous(false);
				
				moduleDAO.createModule(module);
				
				System.out.println("insert 大杂绘 module successfully for school " + schoolId);
				
				//-------------------跳蚤-------------------------------------------
				
				module.setSchoolId(schoolId);
				module.setName("校园.jpg");
				module.setLogo("");
				module.setType(2);
				module.setSummary("拍不尽身边美丽，赏不完校园芳华。");
				module.setMeta("");
				module.setPosition(3);
				module.setDefineType(1);
				module.setAnonymous(false);
				
				moduleDAO.createModule(module);
				
				System.out.println("insert 校园.jpg module successfully for school " + schoolId);
				
				//------------------图贴--------------------------------------------
				module.setSchoolId(schoolId);
				module.setName("跳蚤市场");
				module.setLogo("");
				module.setType(1);
				module.setSummary("这是一个神奇的版块...");
				module.setMeta("");
				module.setPosition(4);
				module.setDefineType(1);
				module.setAnonymous(false);
				
				moduleDAO.createModule(module);
				
				System.out.println("insert 跳蚤市场 module successfully for school " + schoolId);
				
				Thread.sleep(10);
				
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		
	}

}
