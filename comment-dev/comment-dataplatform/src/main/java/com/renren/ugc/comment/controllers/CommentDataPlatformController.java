package com.renren.ugc.comment.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.renren.ugc.comment.model.OneMinsData;
import com.renren.ugc.comment.model.OneLineData;
import com.renren.ugc.comment.model.SearchResult;
import com.renren.ugc.comment.service.DataCenter;
import com.renren.ugc.comment.util.FieldMapSetterUtil;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;

@Path("")
public class CommentDataPlatformController {
    
    private Logger logger = Logger.getLogger(this.getClass());
    
    @Get("ping")
    public String ping(Invocation inv) {
        return "@commentdataplatform controller is working";
    }
    
    @Get("index")
    public String index(Invocation inv){
        Date date = new Date();
        
        java.text.DateFormat format = new java.text.SimpleDateFormat( "HH:mm:ss");  
        String formatDate = format.format(date);
        
        inv.addModel("beginDate",formatDate);
        inv.addModel("endDate",formatDate);
        
        JSONArray array = new JSONArray();
        
        inv.addModel("arr", array);
        
        return "index";
    }
    
    //我们会"多"返回前一分钟和后一分钟的
    @Get("search")
    public String search(Invocation inv,@Param("beginDate") String beginDateStr, @Param("endDate") String endDateStr,
            @Param("cDate1") String cDate1, @Param("cDate2") String cDate2,@Param("cDate3") String cDate3,@Param("queryJiekou") String queryJiekou,@Param("queryZhibiao") String queryZhibiao){
        
        //1.解析得到小时，分，秒
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        String nowDate = dayFormat.format(new Date());
        if(StringUtils.isEmpty(cDate1)){
            //2.取当期时间
            cDate1 = nowDate;
        }
        
        //解析数据
        DataCenter.ensureHasThisDayData(cDate1);
        DataCenter.ensureHasThisDayData(cDate2);
        DataCenter.ensureHasThisDayData(cDate3);
        
        //3.取数据,02:10:30变成02:10:00
        String begindate1 = beginDateStr.substring(0, 5) + ":00";
        String enddate1 = endDateStr.substring(0, 5) + ":59";
        //基准数据为1970年1月1日的数据
        String beginStr0 = "1970-01-01" + " " +  begindate1;
        String endStr0 = "1970-01-01" + " " +  enddate1;
        
        String beginStr1 = cDate1.trim() + " " + begindate1;
        String beginStr2 = cDate2.trim() + " " + begindate1;
        String beginStr3 = cDate3.trim() + " " + begindate1;
        
        try {
            Date bdate0 = format.parse(beginStr0);
            Date edate0 = format.parse(endStr0);
            
            long diff1 = 0;
            if(!StringUtils.isEmpty(beginStr1)){
                Date bdate1 = format.parse(beginStr1);
                diff1 = bdate1.getTime() - bdate0.getTime();
            }
            
            long diff2 = 0;
            if(!StringUtils.isEmpty(cDate2)){
                Date bdate2 = format.parse(beginStr2);
                diff2 = bdate2.getTime() - bdate0.getTime();
            }
            long diff3 = 0;
            if(!StringUtils.isEmpty(cDate3)){
                Date bdate3 = format.parse(beginStr3);
                diff3 = bdate3.getTime() - bdate0.getTime();
            }
            
            
            SearchResult sr = DataCenter.generateJSData(bdate0.getTime(),edate0.getTime(),diff1,diff2,diff3,queryJiekou,queryZhibiao);
            
            inv.addModel("arr", sr.getArray());
            inv.addModel("beginDate",beginDateStr);
            inv.addModel("endDate",endDateStr);
            inv.addModel("max",sr.getMax());
            
            return "index";
            
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
            return "@error";
    }
    
    public static void main(String[] args){
        
       
        System.out.println("^_^");
    }
    
    
    
}
