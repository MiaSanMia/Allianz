package com.renren.ugc.comment.statistics;

/**
 * Record one statistics data. This class is not thread safe
 * 
 * @author jiankuan.xing
 * 
 */
public class Record {

    /**
     * name
     */
    private final String name;

    /**
     * record type
     */
    private final RecordType type;

    /**
     * invoke count
     */
    private long count;

    /**
     * invoke count of last delta
     */
    private long prevCount;
    
    /**
     * cache miss count
     */
    private long missCount;
    
    /**
     * cache miss count of last delta
     */
    private long prevMissCount;

    /**
     * total exec time
     */
    private long totalTime;

    /**
     * total exec time of last delta
     */
    private long prevTotalTime;

    /**
     * max exec time
     */
    private long maxTime;

    /**
     * Exception count
     */
    private long exceptionCount;

    /**
     * exception count of last delta
     */
    private long prevExceptionCount;

    /**
     * Timeout count
     */
    private long timeoutCount;

    /**
     * timeout count of last delta
     */
    private long prevTimeoutCount;

    /**
     * Timeout threshold, now is 250ms
     */
    private static final int timeoutThreshold = 250;
    /**
     * Redis cache threshold, only 10ms
     */
    private static final int cacheTimeoutThreshold = 10;

    public Record(RecordType type, String name) {
        this.type = type;
        this.name = name;
    }
    
    public boolean hasNewData(){
        return (0 == count);
    }

    public void invokeDone(long time) {
        count++;
        totalTime += time;
        if (time > maxTime) {
            maxTime = time;
        }

        if (time > timeoutThreshold) {
            timeoutCount++;
        }
    }
    
    public void invokeDoneForCache(long time) {
        count++;
        totalTime += time;
        if (time > maxTime) {
            maxTime = time;
        }

        if (time > cacheTimeoutThreshold) {
            timeoutCount++;
        }
    }
    

    public void invokeMiss(long time) {
        invokeDone(time);
        missCount++;
    }
    
    public void invokeMissForCache(long time) {
        invokeDoneForCache(time);
        missCount++;
    }
    

    public void invokeException(long time) {
        invokeDone(time);
        exceptionCount++;
    }

    public RecordType getType() {
        return type;
    }

    public DeltaRecord getDelta() {
        DeltaRecord dr = new DeltaRecord(name, type, count - prevCount, missCount
                - prevMissCount, timeoutCount
                - prevTimeoutCount, exceptionCount - prevExceptionCount, totalTime - prevTotalTime,
                maxTime);
        prevCount = count;
        prevMissCount = missCount;
        prevTimeoutCount = timeoutCount;
        prevExceptionCount = exceptionCount;
        prevTotalTime = totalTime;
        maxTime = 0;

        return dr;
    }
}
