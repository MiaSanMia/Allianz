package com.renren.ugc.comment.util;

import org.apache.commons.lang.StringUtils;


/**
 * @author wangxx
 *
 * 用来评论内容的截取
 */
public class LengthUtil {
    
    /**
     * 截取评论的长度，并保持分界线的表情和链接
     *
     * @param body
     * @param cutSize
     * @return
     */

    public static String cutString(String body, int cutSize) {
        if (body == null || body.length() <= cutSize) {
            return body;
        }
        int needIndex = 0;
        int curIndex = 0;
        while (needIndex < cutSize && curIndex < body.length()) {
            char ch = body.charAt(curIndex);
            switch (ch) {
            case '<': {
                if (body.substring(curIndex, curIndex + 4).equals("<img")) {
                    int faceLength = getFaceLength(body.substring(curIndex,
                            body.length()), "<img", "/>");
                    if (faceLength != 0) {
                        needIndex += 10;
                        curIndex += faceLength;
                        break;
                    }

                } else if (body.substring(curIndex, curIndex + 2).equals("<a")) {
                    int[] intArr = getLinkLength(body.substring(curIndex, body
                            .length()), "<a", ">", "</a>");
                    if (intArr != null) {
                        needIndex += intArr[0];
                        curIndex += intArr[1];
                        break;
                    }
                }
            }

            default: {
                needIndex++;
                curIndex++;
                break;
            }
            }
        }
        if (curIndex < body.length()) {
            return body.substring(0, curIndex) + "...";
        }
        return body;
    }
    
    /**
     * 取一个表情的占用长度
     *
     * @param body
     * @param begTag
     * @param endTag
     * @return
     */

    public static int getFaceLength(String body, String begTag, String endTag) {
        if (StringUtils.contains(body, begTag)
                && StringUtils.contains(body, endTag)) {
            return StringUtils.indexOf(body, endTag) + endTag.length()
                    - StringUtils.indexOf(body, begTag);
        }
        return 0;

    }
    
    /**
     * 取链接的长度
     *
     * @param body
     * @param begTag
     * @param midTag
     * @param endTag
     * @return
     */

    public static int[] getLinkLength(String body, String begTag,
            String midTag, String endTag) {
        if (StringUtils.contains(body, begTag)
                && StringUtils.contains(body, midTag)
                && StringUtils.contains(body, endTag)) {
            int[] intArr = new int[2];
            intArr[0] = StringUtils.indexOf(body, endTag) - midTag.length()
                    - StringUtils.indexOf(body, midTag);
            intArr[1] = StringUtils.indexOf(body, endTag) + endTag.length()
                    - StringUtils.indexOf(body, begTag);
            return intArr;
        }

        return null;

    }

}
