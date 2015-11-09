package com.campus.dao;

import java.util.List;

import com.campus.model.Module;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

/** 
 * 管理版块的DAO
 * @author meng.liu 
 * @date 2014-3-5 下午8:15:00 
 */
@DAO(catalog = "mpage")
public interface ModuleDAO {
	
	public static String TB_NAME = "module";

	public static String FIELDS_OUT = "id,school_id,name,logo,type,define_type,anonymous,summary,create_time,meta,position";
	
	public static String FIELDS_IN = "school_id,name,logo,type,define_type,anonymous,summary,create_time,meta,position";
	
	/**
     * 根据版块id，查询一个版块信息
     * 
     * @param id
     * @return
     */
    @SQL("select " + FIELDS_OUT + " from " + TB_NAME + " where id=:id")
    public Module getModuleById(@SQLParam("id") int id);
    
    /**
     * 查询某个学校的所有版块信息
     * 
     * @param schoolId
     * @return
     */
    @SQL("select " + FIELDS_OUT + " from " + TB_NAME + " where school_id=:schoolId")
    public List<Module> getModulesBySchoolId(@SQLParam("schoolId") int schoolId);
    
    /***
     * 创建一个版块
     * 
     * @param Module module
     * @return
     */
    @SQL("insert into "
            + TB_NAME
            + " ("
            + FIELDS_IN
            + ")"
            + " values (:module.schoolId,:module.name,:module.logo,:module.type,:module.defineType,:module.anonymous,:module.summary,now(),:module.meta,:module.position)")
    public void createModule(@SQLParam("module") Module module);
    
    /***
     * 删除一个版块
     * 
     * @param id
     * @return
     */
    @SQL("delete from " + TB_NAME + " where id=:id")
    public void deleteModule(@SQLParam("id") int id);
    
    /** 
     * 编辑一个版块的信息
     * @param id
     * @param module
     */
    @SQL("update " + TB_NAME + " set name =:module.name ,logo=:module.logo , type=:module.type , summary=:module.summary , meta=:module.meta , position=:module.position where id = :id")
    public void updateModuleInfo(@SQLParam("id") int id,@SQLParam("module") Module module);

}
