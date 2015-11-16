package com.renren.ugc.comment.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.renren.ugc.comment.model.OneMinsData;
import com.renren.ugc.comment.model.OneLineData;
import com.renren.ugc.comment.model.SearchResult;
import com.renren.ugc.comment.util.FieldMapGetterUtil;
import com.renren.ugc.comment.util.FieldMapSetterUtil;


/**
 * @author wangxx
 * 
 *  数据处理中心
 */
public class DataCenter {

    //private static List<OneMinsData> allDatas = new CopyOnWriteArrayList<OneMinsData>();
    private static List<OneMinsData> allDatas = new ArrayList<OneMinsData>();

    //key为日期，value为这一天的有没有解析
    private static Map<String,Boolean> hasDataMaps = new HashMap<String,Boolean>();

    public static void setIntoList(List<OneMinsData> datas,String date,long dateIndex){

        //这里需要同步
        if(hasDataMaps.get(date) == null){
            synchronized(DataCenter.class){
                if(hasDataMaps.get(date) == null){
                    //1.找到插入的位置
                    int index = searchLastEqualOrSmaller(allDatas,dateIndex,0,allDatas.size()-1);
                    //2.插入
                    allDatas.addAll(index+1, datas);
                    //3.更新map,这一天的已经解析了
                    hasDataMaps.put(date, Boolean.TRUE);
                }
            }
        }
    }

    //确保date的数据有了，date格式如下:2013-11-11
    public static void ensureHasThisDayData(String date){

        if(StringUtils.isEmpty(date)){
            return;
        }

        if(hasDataMaps.get(date) == null){
            //kao,没有数据，解析，干活
            List<OneMinsData> datas = generateOneDayData(date);
            //fill进去
            //由于涉及到查找，所以这里要设置一个index
            String allDate = date + " 00:00:00";
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date fdate = format.parse(allDate);
                //set to list
                setIntoList(datas,date,fdate.getTime());

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    //分析一行数据
    public static OneLineData generateOneLineData(String s){
        OneLineData data = new OneLineData();

        String[] values = s.split(",");
        if(values == null){
            return data;
        }
        for(int i = 0 ; i < values.length;++i){
            String[] onedatas = values[i].split("=");
            if(onedatas != null && onedatas.length == 2){
                FieldMapSetterUtil.setValue(onedatas[0], onedatas[1], data);
            }
        }
        return data;
    }


    /**
     * @param fileName
     * @return
     * 
     *  根据文件名解析一天的数据放在一个list中
     *  这个list保证了有序
     */
    private static List<OneMinsData> generateOneDayData(String fileDate){

        // String fileName = "/home/wangxx/fun/comment-statistics.log_2013-11-11";

        //保存一天的OneMinsData
        List<OneMinsData> dayList = new ArrayList<OneMinsData>();
        DateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = dayFormat.format(new Date());
        
        //String filePrefix = "/home/wangxx/fun/comment-statistics.log";
        String filePrefix = "/data/logs/xoa2/comment-statistics.log";

        String fileName = "";
        if(!fileDate.equals(nowDate)){
            fileName = filePrefix + "_" + fileDate;
        } else {
            fileName = filePrefix;
        }
        
        File file = new File(fileName);

        try{
            InputStreamReader read = new InputStreamReader (new FileInputStream(file),"utf-8");
            BufferedReader in = new BufferedReader(read);
            String s = new String();
            int k = 0;
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss"); 
            //long beginTime = format.parse("2013-01-01-00:00:00").getTime();
            Map<String,OneLineData> dataMaps =  null;

            while((s = in.readLine())!= null){
                if(s.contains("INFO")){
                    //处理时间
                    int index1 = s.indexOf('[');
                    if(index1 == -1) continue;
                    int index2 = s.indexOf(']');
                    if(index2 == -1) continue;
                    String dateStr = s.substring(index1+1, index2);

                    try{
                        long nowTime = format.parse(dateStr).getTime();

                        //更换dataMaps
                        OneMinsData minsData = new OneMinsData(nowTime,dateStr);
                        dataMaps = minsData.getDataMaps();
                        //加入到list中
                        dayList.add(minsData);

                    } catch(Exception e){
                        e.printStackTrace();
                    }
                } else if(!StringUtils.isEmpty(s)){
                    //处理数据
                    String filedata = s.trim();
                    String[] datas = filedata.split(":");
                    if(datas != null&& datas.length == 2){
                        String key = datas[0];
                        try{
                            OneLineData oneLineData = generateOneLineData(datas[1]);

                            //set 进去
                            if(dataMaps != null){
                                dataMaps.put(key, oneLineData);
                            }

                        } catch (Exception e){
                            e.printStackTrace();
                            continue;
                        }

                    }
                }
            }
            in.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        return dayList;
    }

    public static List<OneMinsData> getList(long begin,long end){

        int index1 = searchLastEqualOrSmaller(allDatas,begin,0,allDatas.size()-1);

        int index2 = searchFirstEqualOrLarger(allDatas,end,0,allDatas.size()-1);

        return allDatas.subList(index1, index2);
    }

    /**
     * @param datas
     * @param key
     * @param begin
     * @param end
     * @return
     * 
     *  查找到第一个大于或等于key的元素
     */
    private static int searchFirstEqualOrLarger(List<OneMinsData> datas, long key, int begin, int end) {
        if(datas == null || datas.size() == 0){
            return 0;
        }
        while (end - begin >= 0) {
            int middle = (begin + end) / 2;
            if (datas.get(middle).getNowTime() >= key) end = middle - 1;
            else begin = middle + 1;
        }
        return begin;
    }

    /**
     * @param datas
     * @param key
     * @param begin
     * @param end
     * @return
     * 
     * 查找到最后一个小于或者等于key的元素
     */
    private static int searchLastEqualOrSmaller(List<OneMinsData> datas, long key, int begin, int end) {
        if(datas == null || datas.size() == 0){
            return -1;
        }
        while (end - begin >= 0) {
            int middle = (begin + end) / 2;
            if (datas.get(middle).getNowTime()  <= key) begin = middle + 1;
            else end = middle - 1;
        }
        return end;
    }

    //返回的目标格式如下:[{"PV":1,"views":3,"day":"01-01","UV":2},{"PV":11,"views":33,"day":"01-02","UV":22}]
    public static SearchResult generateJSData(long begin,long end,long diff1,long diff2,long diff3,String queryJiekou,String queryZhibiao){

        SearchResult sr = new SearchResult();
        JSONArray array = new JSONArray();
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("HH:mm");
        long wucha = 60 * 1000;
        int max = 0;
        int value = 0;
        int index = 0;
        
        while(begin < end){
            JSONObject json = new JSONObject();
            //turn to date
            date.setTime(begin);
            json.put("day", format.format(date));
            //第一条
            if(diff1 != 0){
                index = searchLastEqualOrSmaller(allDatas,begin+diff1,0,allDatas.size()-1);
                if(index != -1 && Math.abs(allDatas.get(index).getNowTime() - begin - diff1) < wucha){
                    value = getValue(index,queryJiekou,queryZhibiao);
                    max = max < value ? value : max;
                    json.put("count1", value);
                } else if(index < allDatas.size() - 1 && Math.abs(allDatas.get(index+1).getNowTime() -  begin - diff1) < wucha){
                    //还有可能是index+1
                    value = getValue(index+1,queryJiekou,queryZhibiao);
                    max = max < value ? value : max;
                    json.put("count1", value);
                }
            }
            //第2条
            if(diff2 != 0){
                index = searchLastEqualOrSmaller(allDatas,begin+diff2,0,allDatas.size()-1);
                if(index != -1 && Math.abs(allDatas.get(index).getNowTime() - begin - diff2) < wucha){
                    value = getValue(index,queryJiekou,queryZhibiao);
                    max = max < value ? value : max;
                    json.put("count2", value);
                } else if(index < allDatas.size() - 1 && Math.abs(allDatas.get(index+1).getNowTime() -  begin - diff2) < wucha){
                    value = getValue(index+1,queryJiekou,queryZhibiao);
                    max = max < value ? value : max;
                    json.put("count2", value);
                }
            }
            //第3条
            if(diff3 != 0){
                index = searchLastEqualOrSmaller(allDatas,begin+diff3,0,allDatas.size()-1);
                if(index != -1 && Math.abs(allDatas.get(index).getNowTime() - begin - diff3) < wucha){
                    value = getValue(index,queryJiekou,queryZhibiao);
                    max = max < value ? value : max;
                    json.put("count3", value);
                } else if(index < allDatas.size() - 1 && Math.abs(allDatas.get(index+1).getNowTime() -  begin - diff3) < wucha){
                    value = getValue(index+1,queryJiekou,queryZhibiao);
                    max = max < value ? value : max;
                    json.put("count3", value);
                }
            }

            begin += 60 * 1000;
            array.put(json);
        }
        
        sr.setArray(array);
        sr.setMax(max);
        
        return sr;
}
    
    private static int getValue(int index,String queryJiekou,String queryZhibiao){
        if( allDatas.get(index) == null || allDatas.get(index).getDataMaps() == null){
            return 0;
        }
        OneLineData data = allDatas.get(index).getDataMaps().get(queryJiekou);
        if(data != null){
            return FieldMapGetterUtil.getValue(queryZhibiao, data);
        }
        return 0;
    }


public static void printAll(){

    for(OneMinsData data:allDatas){
        System.out.println("key="+data.getNowDate());
        System.out.println("value="+data.getDataMaps());
    }
}


}
