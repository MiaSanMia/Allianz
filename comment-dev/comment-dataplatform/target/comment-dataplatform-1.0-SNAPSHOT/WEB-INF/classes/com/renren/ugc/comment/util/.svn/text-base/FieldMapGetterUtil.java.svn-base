package com.renren.ugc.comment.util;

import java.util.HashMap;
import java.util.Map;

import com.renren.ugc.comment.model.OneLineData;


public class FieldMapGetterUtil {

    private static Map<String,AbstractFieldGetter> maps = null;

    static {
        maps = new HashMap<String,AbstractFieldGetter>();
        maps.put("count", new CountFieldGetter());
        maps.put("max(ms)", new MaxFieldGetter());
        maps.put("avg(ms)", new AvgFieldGetter());
        maps.put("timeout", new TimeoutFieldGetter());
        maps.put("timeout-rate(%%)", new TimeOutRateFieldGetter());
        maps.put("exception", new ExceptionFieldGetter());
        maps.put("exception-rate(%%)", new ExceptionRateFieldGetter());
        maps.put("miss", new MissFieldGetter());
        maps.put("miss-rate(%%)", new MissRateFieldGetter());
    }

    public static int getValue(String key,OneLineData data){

        AbstractFieldGetter getter = maps.get(key);

        if(getter != null){
            return getter.getValue(data);
        }
        return 0;
    }

    static abstract class AbstractFieldGetter {
        public abstract int getValue(OneLineData data);
    }

    static class CountFieldGetter extends AbstractFieldGetter{

        @Override
        public int getValue(OneLineData data) {
            return data.getCount();
        }

    }

    static class MaxFieldGetter extends AbstractFieldGetter{
        @Override
        public int getValue(OneLineData data) {
            return data.getMax();
        }
    }

    static class AvgFieldGetter extends AbstractFieldGetter{
        @Override
        public int getValue(OneLineData data) {
            return data.getAvg();
        }
    }

    static class TimeoutFieldGetter extends AbstractFieldGetter{
        @Override
        public int getValue(OneLineData data) {
            return data.getTimeout();
        }
    }

    static class TimeOutRateFieldGetter extends AbstractFieldGetter{
        @Override
        public int getValue(OneLineData data) {
            return data.getTimeoutRate();
        }
    }

    static class ExceptionFieldGetter extends AbstractFieldGetter{
        @Override
        public int getValue(OneLineData data) {
            return data.getException();
        }
    }

    static class ExceptionRateFieldGetter extends AbstractFieldGetter{
        @Override
        public int getValue(OneLineData data) {
            return data.getExceptionRate();
        }
    }

    static class MissFieldGetter extends AbstractFieldGetter{
        @Override
        public int getValue(OneLineData data) {
            return data.getMiss();
        }
    }

    static class MissRateFieldGetter extends AbstractFieldGetter{
        @Override
        public int getValue(OneLineData data) {
            return data.getMissRate();
        }
    }

}
