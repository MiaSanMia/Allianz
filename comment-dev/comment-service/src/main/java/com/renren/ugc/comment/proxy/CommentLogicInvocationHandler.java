package com.renren.ugc.comment.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.cache.CommentFirstPageCacheService;
import com.renren.ugc.comment.service.CommentLogic;
import com.renren.ugc.comment.strategy.CommentConfig;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.ForInvokeStrategy;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * The invocation handler for comment logic interceptor mechanism. Each
 * method of the {@link com.renren.ugc.comment.service.CommentLogic
 * CommentLogic} could have a couple of pre- and post-interceptors, which
 * are specified in the
 * {@link com.renren.ugc.comment.strategy.CommentConfig CommentConfig} per
 * comment business type.<br>
 * 
 * You can ignore the interceptors by setting in the
 * {@link com.renren.ugc.comment.strategy.CommentStrategy CommentStrategy}
 * object the current invocation "internal". This is useful when you want
 * to directly invoke the method of
 * {@link com.renren.ugc.comment.service.CommentLogic CommentLogic}
 * 
 * 
 * @author jiankuan.xing
 * 
 */
public class CommentLogicInvocationHandler implements InvocationHandler {

    private static final Logger logger = Logger.getLogger(CommentLogicInvocationHandler.class);

    private Map<String, Object> object_map = new ConcurrentHashMap<String, Object>();

    private Map<String, Method> method_map = new ConcurrentHashMap<String, Method>();

    private Object target;

    private Object target2; // tmp code 

    public Object bind(Object target) {
        this.target = target;
        return Proxy.newProxyInstance(
        //被代理类的ClassLoader
                target.getClass().getClassLoader(),
                //要被代理的接口,本方法返回对象会自动声称实现了这些接口
                target.getClass().getInterfaces(),
                //代理处理器对象
                this);
    }

    /**
     * 拦截
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {

        if (params.length == 0) {
            throw new RuntimeException(
                    "the params for comment center api doesn't have necessary params");
        }

        //get the first param (CommentType) and last param (CommentStrategy)
        if (params[0] == null) {
            throw new RuntimeException("the first param must be comment type");
        }

        if (!params[0].getClass().equals(CommentType.class)) {
            throw new RuntimeException("the first parameter must be comment type");
        }
        CommentType type = (CommentType) params[0];

        Object target = this.target;

        if (!params[params.length - 1].getClass().equals(CommentStrategy.class) &&
        		!params[params.length - 1].getClass().equals(ForInvokeStrategy.class)) {
            throw new RuntimeException("the last parameter must be comment strategy");
        }

        // inject comment logic
        CommentStrategy strategy = (CommentStrategy) params[params.length - 1];
        strategy.setCommentLogic((CommentLogic) target);
        strategy.setCommentCacheService(CommentFirstPageCacheService.getInstance());

        // inject comment config
        CommentConfig config = CommentConfig.getCommentConfig(type);
        strategy.setConfig(config);

        // when config is not found or the invocation is internal, ignore interceptors
        if (config == null || strategy.isInternal()) {
            try {
                return method.invoke(target, params);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }

        // pre-interceptors
        for (Class<? extends CommentLogic> interceptorClz : config.getPreInterceptors()) {
            intercept(interceptorClz, method, params);
        }

        // method invocation
        Object result = null;
        try {
            result = method.invoke(target, params);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }

        // set the result so the post-interceptors can use it
        strategy.setReturnedValue(result);

        // post-interceptors
        for (Class<? extends CommentLogic> interceptorClz : config.getPostInterceptors()) {
            intercept(interceptorClz, method, params);
        }

        // the result returned by interceptor is useless
        return result;
    }

    private void intercept(Class<? extends CommentLogic> classs, Method method, Object[] params)
            throws Throwable {
        Object interceptor = object_map.get(classs.getName());
        Method interceptorMethod = method_map.get(classs.getName() + "." + method.getName());
        if (interceptor == null) {
            try {
                interceptor = Class.forName(classs.getName()).newInstance();
                object_map.put(classs.getName(), interceptor);
            } catch (Exception e) {
                logger.error("", e);
                return;
            }
        }
        if (interceptorMethod == null) {
            Method[] ms = classs.getMethods();
            for (Method method2 : ms) {
                if (method2.getName().equals(method.getName())) {
                    interceptorMethod = method2;
                    method_map.put(classs.getName() + "." + method.getName(), interceptorMethod);
                    break;
                }
            }
        }
        if (interceptor != null && interceptorMethod != null) {
            try {
                long t1 = System.currentTimeMillis();
                
                interceptorMethod.invoke(interceptor, params);
                
                if(logger.isDebugEnabled()){
                    long t2 = System.currentTimeMillis();
                    logger.debug("time cost|interceptor:"+interceptor.toString()+"|method:"+interceptorMethod.getName()+"|time:"+(t2-t1));
                }
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }
    }
}
