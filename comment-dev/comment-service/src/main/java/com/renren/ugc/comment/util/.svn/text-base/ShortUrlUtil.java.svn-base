package com.renren.ugc.comment.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.renren.app.shorturl.xoa.api.ShortUrlService;
import com.renren.shorturl.local.config.ShortUrlConfigUtil;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.statistics.StatisticsHelper;
import com.renren.ugc.model.shorturl.FORMAT_TYPE;
import com.renren.ugc.model.shorturl.ResultWrapper;
import com.renren.ugc.model.shorturl.ShortUrlPackageBuilder;
import com.renren.xoa.lite.ServiceFactories;
import com.renren.xoa.lite.ServiceFuture;

/**
 * @author wangxx
 * 
 *         短连接支持类
 * 
 */
public class ShortUrlUtil {

    private Logger logger = Logger.getLogger(this.getClass());

    private ShortUrlService shortUrlService = null;

    private static ShortUrlUtil instance = new ShortUrlUtil();

    private ShortUrlUtil() {
        shortUrlService = ServiceFactories.getFactory().getService(ShortUrlService.class);
    }

    public static ShortUrlUtil getInstance() {
        return instance;
    }

    /**
     * @param content
     * @return 对应的短域名字符串
     * 
     *         得到对应的短域名字符串,一般情况下是在存db之前调用
     */
    public String getEncryptedContent(String content) {

        if (StringUtils.isBlank(content) || !ShortUrlConfigUtil.containsUrl(content)) {
            return content;
        }

        String result = null;
        long start = System.nanoTime();
        boolean success = false;
        try {

            ServiceFuture<String> sf = shortUrlService.getEncryptedContent(null, content);
            result = (String) XoaClientAdapter.doSubmit2(sf, XoaClientAdapter.TIME_OUT_MILLES);
            success = true;
        } catch (Exception e) {
            logger.error("ShortUrlUtil getEncryptedContent error | content:" + content, e);
        }
        long end = System.nanoTime();
        StatisticsHelper.invokeGetEncrpyptedShortUrl((end - start)
                / StatisticsHelper.NANO_TO_MILLIS, success);

        content = StringUtils.isBlank(result) ? content : result;
        return content;
    }

    /**
     * @param content
     * @return 带有链接的html
     * 
     *         得到带有链接的url,一般情况下是返回给前端之前调用
     */
    public void getOriginalContent(Comment comment) {

        String content = comment.getContent();
        if (StringUtils.isBlank(content) || !ShortUrlConfigUtil.containsShortUrl(content)) {
            return;
        }

        ShortUrlPackageBuilder spb = new ShortUrlPackageBuilder();
        spb.setEncryptedHrefHtml(true);
        String result = null;
        long start = System.nanoTime();
        boolean success = false;
        try {
            ServiceFuture<String> sf = shortUrlService.getOriginalContent(getPackagStyle(spb),
                    content);
            result = (String) XoaClientAdapter.doSubmit2(sf, XoaClientAdapter.TIME_OUT_MILLES);
            success = true;
        } catch (Exception e) {
            logger.error("ShortUrlUtil getOriginalContent error | content:" + content, e);
        }
        long end = System.nanoTime();
        StatisticsHelper.invokeGetShortUrlOriginalContent((end - start)
                / StatisticsHelper.NANO_TO_MILLIS, success);

        comment.setContent(StringUtils.isBlank(result) ? content : result);

    }

    /**
     * @param content
     * @return 带有链接的html
     * 
     *         批量得到带有链接的url,一般情况下是返回给前端之前调用
     */
    public void getBatchOriginalContent(List<Comment> comments) {

        if (comments == null || comments.size() == 0) {
            return;
        }

        //1.filter comment
        List<String> shortUrls = new ArrayList<String>();

        for (Comment c : comments) {
            if (ShortUrlConfigUtil.containsShortUrl(c.getContent())) {
                shortUrls.add(c.getContent());
            }
        }

        if (shortUrls.size() == 0) {
            return;
        }

        //2.get originUrlBatch
        ShortUrlPackageBuilder spb = new ShortUrlPackageBuilder();
        spb.setEncryptedHrefHtml(true);
        long start = System.nanoTime();
        boolean success = false;
        try {
            ServiceFuture<ResultWrapper> sf = shortUrlService.batchGetOriginalContent(null,
                    shortUrls, FORMAT_TYPE.ENCRYPTED_HREF_HTML);
            ResultWrapper result = (ResultWrapper) XoaClientAdapter.doSubmit2(sf,
                    XoaClientAdapter.TIME_OUT_MILLES);

            if (result == null) {
                return;
            }
            //3.replace
            Map<String, String> map = result.getResultMap();
            if (map != null) {
                for (int i = 0; i < comments.size(); ++i) {
                    Comment c = comments.get(i);
                    if (c != null) {
                        String url = map.get(c.getContent());
                        if (url != null) {
                            c.setContent(url);
                        }
                    }
                }
            }
            success = true;
        } catch (Exception e) {
            logger.error("ShortUrlUtil batchGetOriginalContent error :", e);
        }
        long end = System.nanoTime();
        StatisticsHelper.invokeGetShortUrlOriginalContentBatch((end - start)
                / StatisticsHelper.NANO_TO_MILLIS, success);

    }

    private int getPackagStyle(ShortUrlPackageBuilder spb) {
        if (spb.isEncryptedHrefHtml()) {
            return ShortUrlPackageBuilder.Style.EncryptedHrefHtml.getValue();
        } else if (spb.isEncrytedPureText()) {
            return ShortUrlPackageBuilder.Style.EncrytedPureText.getValue();
        } else {
            return ShortUrlPackageBuilder.Style.PureText.getValue();
        }
    }
}
