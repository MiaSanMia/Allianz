package com.renren.ugc.comment.util;

import java.io.IOException;

import net.paoding.rose.web.Invocation;

import org.json.JSONObject;

/**
 * output ajax fail message
 */
public class CommentControllerUtils {

    public static void ajaxFail(Invocation inv, String msg) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("code", "1");
            obj.put("msg", msg);
            inv.getResponse().getWriter().print(obj);
        } catch (IOException e) {
        }
    }

    public static JSONObject ajaxFail(Invocation inv, int code, String msg) {
        JSONObject obj = new JSONObject();
        // obj.put("code", String.valueOf(code));
        obj.put("code",
            String.valueOf(ErrorCodeAdapter.getAdapterErrorCode(code)));
        obj.put("msg", msg);
        return obj;
    }

    public static void ajaxFail(Invocation inv, String msg, Throwable t) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("code", "1");
            obj.put("msg", msg);
            String expMsg;
            if (t.getMessage() != null) {
                expMsg =
                        t.getClass().getCanonicalName() + ": " + t.getMessage();
            } else {
                expMsg = t.getClass().getCanonicalName();
            }
            obj.put("exception", expMsg);
            inv.getResponse().getWriter().print(obj);
        } catch (IOException e) {
        }
    }

    public static String ajaxFail(String msg) {
        JSONObject obj = new JSONObject();
        obj.put("code", "1");
        obj.put("msg", msg);

        return "@" + obj.toString();
    }

    public static String ajaxFail(int code, String msg) {

        JSONObject obj = new JSONObject();

        obj.put("code",
            String.valueOf(ErrorCodeAdapter.getAdapterErrorCode(code)));
        obj.put("msg", msg);

        return "@" + obj.toString();
    }

    public static String ajaxFail(String msg, Throwable t) {
        JSONObject obj = new JSONObject();
        obj.put("code", "1");
        obj.put("msg", msg);
        String expMsg;
        if (t.getMessage() != null) {
            expMsg = t.getClass().getCanonicalName() + ": " + t.getMessage();
        } else {
            expMsg = t.getClass().getCanonicalName();
        }
        obj.put("exception", expMsg);
        return "@" + obj.toString();
    }

}
