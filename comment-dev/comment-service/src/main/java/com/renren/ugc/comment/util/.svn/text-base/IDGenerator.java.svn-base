package com.renren.ugc.comment.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.xoa2.CommentType;

/**
 * Comment Id generator. A comment Id consists of type + current millis +
 * part of current host's IP + a sequence number that's unique within a
 * short time. This class also support the the comment id passed in by
 * outside, which keep unique only within one type. For details, see
 * {@link http://wiki.d.xiaonei.com/pages/viewpage.action?pageId=19010801}
 * 
 * @author jiankuan.xing
 * 
 */
public class IDGenerator {

    public static IDGenerator instance = new IDGenerator();
    
    private static Logger logger = Logger.getLogger(IDGenerator.class);

    /**
     * generate a comment id with format <type (10bit)><time (36bit)><IP
     * byte (8bit)><seq (10bit)>
     */
    public static long nextCommentId(CommentType type) {
        return instance.nextId(type);
    }

    /**
     * generate a comment id with format <type (10bit)><externalId (54bit)>
     */
    public static long nextCommentId(CommentType type, long externalId) {
        return instance.nextId(type, externalId);
    }

    private AtomicInteger currSeq = new AtomicInteger(0);

    /**
     * max value of sequence number
     */
    private final int SEQ_MAX = 1024; // 2^10

    private AtomicLong lastGenTime = new AtomicLong(0);

    private Random r = new Random();

    /**
     * a sub part of an IP
     */
    private int pos;

    private IDGenerator() {
        initPos();
    }

    private void initPos() {

        InetAddress addr;
        try {
            addr = InetAddress.getLocalHost();
            if (addr.isLoopbackAddress()) {
                pos = r.nextInt(256); // 0x0 ~ 0xFF
            } else {
                // without '& 0xFF', the byte's high bit is considered to symbol
                pos = (addr.getAddress()[3] & 0xFF);
            }
        } catch (UnknownHostException e) {
            pos = r.nextInt(256); // 0x0 ~ 0xFF
        }

        pos <<= 10;
    }

    private long nextId(CommentType type) {

        long typeCode = (long) type.getValue();

        // currently the system time millis are 41bit or 42bit
        long time = System.currentTimeMillis();
        if (time < 0x20000000000L) { // 41bit
            time >>= 5;
        } else { // assume it's 42 bit, unless this code is in use after hundreds of years
            time >>= 6;
        }

        long lastTime = lastGenTime.getAndSet(time);
        if (time > lastTime) {
            currSeq.set(0);
        }

        int seq = currSeq.getAndIncrement();

        if (seq >= SEQ_MAX) {
            // TODO 可以改成等待一段时间
            String msg = String.format(
                    "whitin about 64ms the curr seq is max than %d, may be is too fast~ ", SEQ_MAX);
            logger.error(msg);
            throw new RuntimeException(msg);
        }

        return ((typeCode << 54) | (time << 18) | pos | seq);
    }

    private long nextId(CommentType type, long externalId) {
        long typeCode = (long) type.getValue();
        typeCode <<= 54;
        return (typeCode | externalId);
    }
}
