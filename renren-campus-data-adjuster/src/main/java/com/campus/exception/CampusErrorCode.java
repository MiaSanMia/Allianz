package com.campus.exception;

/**
 * @author lei.xu1
 * @date 2014年3月6日
 * @desc service中错误码（2000~3000），异常信息需要使用方填写
 * 暂时觉得这个类不需要用枚举限制msg
 * @author Wang Shufeng [shufeng.wang@renren-inc.com]
 */

public class CampusErrorCode {
	
	/**
	 * db 异常
	 */
	public static final int DB_ERROR = 2001;
	
	/**
	 * 资源不存在，比如帖子、相册、模块 ...
	 */
	public static final int ENTYR_NOT_EXIST = 2002;
	
	/**
	 * 内容太长
	 */
	public static final int CONTENT_TOO_LONG = 2003;
	
	/**
	 * 内容不能为空
	 */
	public static final int CONTENT_NOT_EMPTY = 2004;
	
	/**
	 * 没有权限操作，比如没有权限查看匿名帖子
	 */
	public static final int NO_RIGHT = 2005;
	
	/**
	 * 违禁
	 */
	public static final int FORBID = 2006;
	
	/**
	 * 频率过快
	 */
	public static final int FAST_OVER_COUNT = 2007;
	
	/**
	 * 频繁发布相同的内容
	 */
	public static final int SAME_OVER_COUNT = 2008;
	
	/**
	 * user服务出错
	 */
	public static final int USER_ERROR = 2009;
    
    /**
     * 参数错误
     */
	public static final int ARGS_WRONG = 2010;
	
	/**
     * cache服务异常:网络问题
     */
    public static final int CACHE_NETWORK = 2011;
    
    /**
     * cache服务异常:server问题
     */
    public static final int CACHE_SERVER = 2012;
    
    /**
     * cache服务异常:其他问题
     */
    public static final int CACHE_OTHER = 2013;
    
    /**
     * cache服务异常:其他问题
     */
    public static final int ILLEGAL_PARAM = 2014;
    
    /**
     * 该模块不属于任何学校
     */
    public static final int MODULE_NOT_BELONG_TO_ANY_SCHOOL = 2101;
    
    /**
     * 非本校学生
     */
    public static final int NOT_BELONG_TO_THIS_SCHOOL = 2102;

    /**
     * 人品服务调用异常
     */
    public static final int RP_SERVICE_ERROR = 2015;
    
    /**
	 * 标题太长
	 */
	public static final int TITLE_TOO_LONG = 2016;
	
	/**
	 * 帖子中图片数量太多
	 */
	public static final int TOO_MANY_IMGS = 2017;
	
	/**
	 * 没有获取到用户的真实id，请检查匿名i的是否有误
	 */
	public static final int REAL_USER_ID_NOT_FOUND = 2018;
	
	/**
	 * 没有获取到用户的匿名id，请确认该用户是否创建匿名信息
	 */
	public static final int ANONY_USER_ID_NOT_FOUND = 2019;
	
}
