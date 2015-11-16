package com.renren.ugc.comment.util;

import java.util.HashMap;
import java.util.Map;

import com.renren.ugc.comment.model.OneLineData;


public class FieldMapSetterUtil {
    
    private static Map<String,AbstractFieldSetter> maps = null;
    
    static {
        maps = new HashMap<String,AbstractFieldSetter>();
        maps.put("count", new CountFieldSetter());
        maps.put("max(ms)", new MaxFieldSetter());
        maps.put("avg(ms)", new AvgFieldSetter());
        maps.put("timeout", new TimeoutFieldSetter());
        maps.put("timeout-rate(%%)", new TimeOutRateFieldSetter());
        maps.put("exception", new ExceptionFieldSetter());
        maps.put("exception-rate(%%)", new ExceptionRateFieldSetter());
        maps.put("miss", new MissFieldSetter());
        maps.put("miss-rate(%%)", new MissRateFieldSetter());
    }
    
    public static void setValue(String key,String value,OneLineData data){
        
        AbstractFieldSetter setter = maps.get(key);
        
        if(setter != null){
            setter.setValue(value, data);
        }
    }
    
    
    static abstract class AbstractFieldSetter {

        public int getIntValue(String rawValue) {
            int value = 0;
            try{
                value =  Integer.valueOf(rawValue);
            } catch(Exception e){
                e.printStackTrace();
            }
            return value;
        }

        public abstract void setValue(String rawValue, OneLineData data);
    }
    
    static class CountFieldSetter extends AbstractFieldSetter{
        @Override
        public void setValue(String rawValue, OneLineData data) {
                    data.setCount(getIntValue(rawValue));
            }
        }
    
    static class MaxFieldSetter extends AbstractFieldSetter{
        @Override
        public void setValue(String rawValue, OneLineData data) {
                    data.setMax(getIntValue(rawValue));
            }
        }
    
    static class AvgFieldSetter extends AbstractFieldSetter{
        @Override
        public void setValue(String rawValue, OneLineData data) {
                    data.setAvg(getIntValue(rawValue));
            }
        }
    
    static class TimeoutFieldSetter extends AbstractFieldSetter{
        @Override
        public void setValue(String rawValue, OneLineData data) {
                    data.setTimeout(getIntValue(rawValue));
            }
        }
    
    static class TimeOutRateFieldSetter extends AbstractFieldSetter{
        @Override
        public void setValue(String rawValue, OneLineData data) {
                    data.setTimeoutRate(getIntValue(rawValue));
            }
        }
    
    static class ExceptionFieldSetter extends AbstractFieldSetter{
        @Override
        public void setValue(String rawValue, OneLineData data) {
                    data.setException(getIntValue(rawValue));
            }
        }
    
    static class ExceptionRateFieldSetter extends AbstractFieldSetter{
        @Override
        public void setValue(String rawValue, OneLineData data) {
                    data.setExceptionRate(getIntValue(rawValue));
            }
        }
    
    static class MissFieldSetter extends AbstractFieldSetter{
        @Override
        public void setValue(String rawValue, OneLineData data) {
                    data.setMiss(getIntValue(rawValue));
            }
        }
    
    static class MissRateFieldSetter extends AbstractFieldSetter{
        @Override
        public void setValue(String rawValue, OneLineData data) {
                    data.setMissRate(getIntValue(rawValue));
            }
        }
        

}
