package com.renren.ugc.comment.interceptor.impl;

import com.renren.ugc.comment.service.CommentLogicAdapter;

/**
 * @author wangxx
 * 
 *         评论需要推送给"搜索"业务
 */
@Deprecated
public class PushSearchClientInterceptor extends CommentLogicAdapter {

//    private Logger logger = Logger.getLogger(this.getClass());
//
//    private static ProducerClient producerClient = null;
//
//    private static Map<CommentType, String> searchPrefixMap = new HashMap<CommentType, String>();
//
//    static {
//        producerClient = ProducerClient.getInstance();
//        try {
//            InputStream in = PushSearchClientInterceptor.class.getResourceAsStream("producer.properties");
//            producerClient.init(in);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //init map
//        searchPrefixMap.put(CommentType.Album, "album_comment");
//        searchPrefixMap.put(CommentType.Photo, "photo_comment");
//
//    }
//
//    @Override
//    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
//            Comment comment, CommentStrategy strategy) {
//
//        this.doSearch(type, comment, entryOwnerId, entryId, actorId, strategy);
//
//        return null; // as an interceptor, returned value is of no use
//    }
//
//    private void doSearch(final CommentType type, final Comment comment, final int entryOwnerId,
//            final long entryId, final int actorId, final CommentStrategy strategy) {
//
//        CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.CALL_SEARCH){
//            private static final long serialVersionUID = 1L;
//            @Override
//            protected Void doCall() throws Exception {
//                //1.判断likePrefixMap有没有前缀
//                String prefix = searchPrefixMap.get(type);
//
//                if (prefix == null || "".equals(prefix)) {
//                    logger.warn("found  no match prefix in searchPrefixMap type = " + type);
//                    return null;      
//                }
//                producerClient.sendProduceDataById(prefix, entryOwnerId, new Gson().toJson(comment));
//                return null;
//            }
//        });
//        
//        /*AsynJobService.asynRun(new Runnable() {
//
//            @Override
//            public void run() {
//
//                //1.判断likePrefixMap有没有前缀
//                String prefix = searchPrefixMap.get(type);
//
//                if (prefix == null || "".equals(prefix)) {
//                    logger.warn("found  no match prefix in searchPrefixMap type = " + type);
//                    return;
//                }
//                producerClient.sendProduceDataById(prefix, entryOwnerId, new Gson().toJson(comment));
//
//            }
//        });*/
//    }

}
