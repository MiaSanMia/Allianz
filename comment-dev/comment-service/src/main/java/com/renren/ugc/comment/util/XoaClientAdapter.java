package com.renren.ugc.comment.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.renren.xoa.lite.ServiceFuture;
import com.renren.xoa.lite.ServiceFutureHelper;

public class XoaClientAdapter {

    protected static Log logger = LogFactory.getLog(XoaClientAdapter.class);

    public static final long TIME_OUT_MILLES = 1000;

    /*public static String doSubmit(ServiceFuture<String> sf) throws UGCCommentException {
        return doSubmit(sf, TIME_OUT_MILLES);
    }

    public static String doSubmit(ServiceFuture<String> sf, long waitTime)
            throws UGCCommentException {

        try {
            return ServiceFutureHelper.execute(sf, waitTime);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
        return null;
    }*/

    /*public static void doAsyncSubmit(final ServiceFuture<String> sf, final String methodName) {
        sf.addListener(new ServiceFutureListener() {

            @Override
            public void operationComplete(ServiceFuture<?> future) throws Exception {
                if (sf.isSuccess()) {
                    //这个信息可能有用
                    String result = (String) future.getContent();
                    if (logger.isDebugEnabled()) {
                        logger.debug(result);
                    }
                } else {
                    logger.error(String.format("Failed to invoke %s", methodName), sf.getCause());
                }
            }
        });
        sf.submit();
    }*/

    public static Object doSubmit2(ServiceFuture<?> sf) {
        return doSubmit2(sf, TIME_OUT_MILLES);
    }

    public static Object doSubmit2(ServiceFuture<?> sf, long timeout) {

        try {
            return ServiceFutureHelper.execute(sf, timeout);
        } catch (Exception e) {
            logger.error("error happened XoaClientAdapter execute",e);
        }
        return null;
    }
}
