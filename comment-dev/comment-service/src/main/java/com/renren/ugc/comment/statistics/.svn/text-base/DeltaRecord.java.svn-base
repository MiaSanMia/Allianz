package com.renren.ugc.comment.statistics;

public class DeltaRecord {

    private final String name;

    private final RecordType type;

    private final long count;

    private final long missCount;

    private final long timeoutCount;

    private final long exceptionCount;

    private final long totalTime;

    private final long maxTime;
    
    /** 用来统计比率的基数 */
    private static int RATE_BASE = 100 * 100;

    public DeltaRecord(String name, RecordType type, long count, long missCount,
            long timeoutCount, long exceptionCount,
            long totalTime, long maxTime) {
        this.name = name;
        this.type = type;
        this.count = count;
        this.missCount = missCount;
        this.timeoutCount = timeoutCount;
        this.exceptionCount = exceptionCount;
        this.totalTime = totalTime;
        this.maxTime = maxTime;
    }

    public String getName() {
        return name;
    }

    public RecordType getType() {
        return type;
    }

    public long getCount() {
        return count;
    }

    public long getMissCount() {
        return missCount;
    }

    public long getTimeoutCount() {
        return timeoutCount;
    }

    public long getExceptionCount() {
        return exceptionCount;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public long getMissRate() {
        if (count == 0) {
            return -1;
        }
        return missCount * RATE_BASE / count;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public long getAvgTime() {
        if (count == 0) {
            return -1;
        }
        return totalTime / count;
    }

    public long getTimeoutRate() {
        if (count == 0) {
            return -1;
        }
        return timeoutCount * RATE_BASE / count;
    }

    public long getExceptionRate() {
        if (count == 0) {
            return -1;
        }
        return exceptionCount * RATE_BASE / count;
    }

    @Override
    public String toString() {
        if (this.type.isForCache()) {
            return String.format(
                    "%s:count=%d,miss=%d,miss-rate(%%%%)=%d,max(ms)=%d,avg(ms)=%d,timeout=%d,timeout-rate(%%%%)=%d,exception=%d,exception-rate(%%%%)=%d",
                    name, count, missCount, getMissRate(), maxTime, getAvgTime(), timeoutCount,
                    getTimeoutRate(), exceptionCount, getExceptionRate());
        } else {
            return String.format(
                "%s:count=%d,max(ms)=%d,avg(ms)=%d,timeout=%d,timeout-rate(%%%%)=%d,exception=%d,exception-rate(%%%%)=%d",
                name, count, maxTime, getAvgTime(), timeoutCount, getTimeoutRate(), exceptionCount,
                getExceptionRate());
        }

    }
}
