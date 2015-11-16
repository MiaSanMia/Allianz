package com.renren.ugc.benchmark;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.log4j.Logger;

import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.comment.xoa2.CreateCommentRequest;
import com.renren.ugc.comment.xoa2.CreateCommentResponse;
import com.renren.ugc.comment.xoa2.GetCommentListRequest;
import com.renren.ugc.comment.xoa2.GetCommentListResponse;
import com.renren.ugc.comment.xoa2.IXCommentCenter;
import com.renren.xoa2.client.ServiceFactory;

/**
 * The comment center xoa2 api client sampler used by JMeter.
 * 
 * @author jiankuan.xing
 * 
 */
public class CommentCenterSampler extends AbstractJavaSamplerClient {

    private Logger logger = Logger.getLogger(this.getClass());

    private IXCommentCenter xcc;

    enum Mode {
        READ, WRITE
    }

    private Mode mode = Mode.READ;

    private int timeout = 250;

    private String xoa2ServerAddr = "ONLINE";

    @Override
    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument("comment.mode", mode.toString());
        params.addArgument("comment.xoa2.timeout", String.valueOf(timeout));
        params.addArgument("comment.xoa2.addr", xoa2ServerAddr);
        return params;
    }

    @Override
    public void setupTest(JavaSamplerContext context) {
        setupValues(context);
        logger.info("setup comment center xoa2 test");
        listParams();
        if (!xoa2ServerAddr.equalsIgnoreCase("ONLINE")) {
            System.setProperty("comment.ugc.xoa2.renren.com", xoa2ServerAddr);
        }
        xcc = ServiceFactory.getService(IXCommentCenter.class, timeout);
    }

    private void listParams() {
        logger.info("test params:\n" + "serverAddr: " + xoa2ServerAddr + ","
                + "xoa2 connect timeout:" + String.valueOf(timeout) + "ms," + "mode: "
                + mode.toString());

    }

    private void setupValues(JavaSamplerContext context) {
        xoa2ServerAddr = context.getParameter("comment.xoa2.addr");

        try {
            int tempTimeout = context.getIntParameter("comment.xoa2.timeout");
            if (tempTimeout > 250) {
                timeout = tempTimeout;
            }
        } catch (NumberFormatException e) {
            logger.warn("invalid timeout: " + context.getParameter("comment.xoa2.timeout")
                    + ", ignored");
        }

        try {
            mode = Mode.valueOf(context.getParameter("comment.mode"));
        } catch (IllegalArgumentException e) {
            logger.warn("invalid mode: " + context.getParameter("comment.mode") + ", ignored");
        }

    }

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult result = new SampleResult();

        if (mode == Mode.WRITE) {
            result.setSampleLabel("write comment");
            result.sampleStart();
            boolean created = testCreateComment();
            result.sampleEnd();
            result.setSuccessful(created);
        } else {
            result.setSampleLabel("read comment");
            result.sampleStart();
            boolean got = testGetCommentList();
            result.sampleEnd();
            result.setSuccessful(got);
        }
        return result;
    }

    private boolean testGetCommentList() {
        CommentType type = CommentType.Dummy;
        int actorId = 501522889;
        long entryId = 6829537162L;
        int entryOwnerId = 501522889;
        GetCommentListRequest req = new GetCommentListRequest();
        req.setActorId(actorId);
        req.setType(type);
        req.setEntryId(entryId);
        req.setEntryOwnerId(entryOwnerId);
        req.setBorderCommentId(0);
        req.setDesc(false);
        req.setLimit(30);

        try {
            GetCommentListResponse resp = xcc.getCommentList(req);
            return resp.isSetCommentList();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean testCreateComment() {
        CommentType type = CommentType.Dummy;
        String content = "This is the test comment from the jmeter sampler";
        int actorId = 501522889;
        long entryId = 6829537162L;
        int entryOwnerId = 501522889;
        CreateCommentRequest req = new CreateCommentRequest();
        req.setActorId(actorId);
        req.setType(type);
        req.setEntryId(entryId);
        req.setEntryOwnerId(entryOwnerId);
        req.setContent(content);

        try {
            CreateCommentResponse resp = xcc.createComment(req);
            return resp.isSetComment();

        } catch (Exception e) {
            return false;
        }

    }
}
