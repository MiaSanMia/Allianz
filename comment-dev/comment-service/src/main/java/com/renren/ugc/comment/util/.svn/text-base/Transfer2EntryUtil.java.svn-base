package com.renren.ugc.comment.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;

import com.renren.app.share.IShareConstants;
import com.renren.app.share.model.Share;
import com.renren.privacy.permission.define.PrivacySourceControlType;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.model.album.Album;
import com.renren.ugc.model.album.Photo;
import com.renren.ugc.model.album.PhotoStateUtil;
import com.renren.ugc.model.shortvideo.constants.IShortvideoServiceConstants;
import com.renren.xoa2.api.campus.model.CampusFeed;
import com.renren.xoa2.api.campus.model.Posts;
import com.renren.xoa2.api.campus.model.TopAd;
import com.renren.xoa2.api.ugc.status.model.Doing;
import com.renren.xoa2.ugc.shortvideo.model.Shortvideo;
import com.xiaonei.antispam.AntiSpamUtils;
import com.xiaonei.commons.gid.util.G;
import com.xiaonei.commons.gid.util.Type;
import com.xiaonei.platform.component.xfeed.helper.FeedDefinition;

/**
 * @author wangxx
 * 
 *         from all kinds of model to "entry"
 */
public class Transfer2EntryUtil {
	
	private static final Logger logger = Logger.getLogger(Transfer2EntryUtil.class);

    public static Entry statusTransfer2EntryUtil(Doing doing, Entry entry, Comment c) {

        if (doing == null) {
            return null;
        }

        if (entry == null) {
            entry = new Entry();
        }

        //以后有需要继续加
        Map<String, String> pros = new HashMap<String, String>();
        pros.put(EntryConfig.ENTRY_CONTENT, doing.getContent());
        pros.put(EntryConfig.ENTRY_CONTROL, String.valueOf(doing.getSourceControl()));
        pros.put(EntryConfig.ENTRY_CREATE_TIME, String.valueOf(doing.getDoTime()));
        if (entry.getId() <= 0) {
            entry.setId(doing.getId());
        }

        if (entry.getOwnerId() <= 0) {
            entry.setOwnerId(doing.getUserId());
        }
        //502是原发状态的新鲜事类型，505是转发状态的新鲜事类型
        int stype = FeedDefinition.STATUS_UPDATE;
        long rootId = doing.getRootId();
        if (rootId != 0) {
            stype = 505;
        }
        //被评论的用户是page的话，设置为page的新鲜事类型
        if (G.isTypeOf(entry.getOwnerId(), Type.PAGE)) {
            stype = FeedDefinition.PAGE_STATUS_UPDATE;
        }
        pros.put(EntryConfig.ENTRY_STYPE, String.valueOf(stype));
        entry.setEntryProps(pros);
        entry.setType(CommentType.Status.toString().toLowerCase());

        return entry;

    }

    public static Entry blogTransfer2EntryUtil(com.renren.ugc.model.blog.Entry blogEntry,
            Entry entry, int entryOwnerId, long entryId) {

        if (blogEntry == null) {
            return null;
        }

        if (entry == null) {
            entry = new Entry();
        }

        //以后有需要继续加
        Map<String, String> pros = new HashMap<String, String>();
        pros.put(EntryConfig.ENTRY_TITLE, blogEntry.getTitle());
        //pros.put(EntryConfig.ENTRY_IS_PUBLIC,String.valueOf(blogEntry.getSourceControl() == PrivacySourceControlType.Open.getType()));
        pros.put(EntryConfig.ENTRY_CONTROL, blogEntry.getSourceControl() + "");
        pros.put(EntryConfig.ENTRY_CREATE_TIME, blogEntry.getTime().getTime() + "");
        entry.setEntryProps(pros);
        if (entry.getId() <= 0) {
            entry.setId(entryId);
        }
        if (entry.getOwnerId() <= 0) {
            entry.setOwnerId(entryOwnerId);
        }
        entry.setType(CommentType.Blog.toString().toLowerCase());

        return entry;

    }

    public static Entry shareTransfer2EntryUtil(Share share, Entry entry) {

        if (share == null) {
            return null;
        }

        if (entry == null) {
            entry = new Entry();
        }

        //以后有需要继续加
        Map<String, String> pros = new HashMap<String, String>();
        pros.put(EntryConfig.ENTRY_TITLE, share.getTitle());
        //pros.put(EntryConfig.ENTRY_IS_PUBLIC,"true");
        pros.put(EntryConfig.ENTRY_CONTROL, PrivacySourceControlType.Open.getType() + "");
        pros.put(EntryConfig.ENTRY_CREATE_TIME, share.getCreationDate().getTime() + "");
        pros.put(EntryConfig.ENTRY_URL_MD5, share.getUrlMd5() + "");
        pros.put(EntryConfig.ENTRY_SHARE_RESOURCE_USER_ID,
                Integer.toString(share.getResourceUserId()));
        pros.put(EntryConfig.ENTRY_SHARE_RESOURCE_ID, Long.toString(share.getResourceId()));
        pros.put(EntryConfig.ENTRY_SHARE_THUMB_URL, share.getThumbUrl() + "");
        String title = share.getTitle();
        if (StringUtils.isBlank(title) && share.getType() == IShareConstants.URL_TYPE_PHOTO) {
        	//照片没有标题的话，用album的标题
            if (share.getMetaMap() != null && !StringUtils.isEmpty(share.getMetaMap().get(CommentCenterConsts.SHARE_METADATA_ALBUMNAME_KEY))) {
                title = share.getMetaMap().get(CommentCenterConsts.SHARE_METADATA_ALBUMNAME_KEY);
            } else {
                title = "照片";
            }
        }
        pros.put(EntryConfig.ENTRY_SHARE_TITLE, title + "");
        pros.put(EntryConfig.ENTRY_SHARE_TYPE, Integer.toString(share.getType()));
        pros.put(EntryConfig.ENTRY_SHARE_URL, share.getUrl() + "");
        pros.put(EntryConfig.ENTRY_SHARE_VIRTUAL_ID, Long.toString(share.getVirtualId()));
        if (share.getId() > 0) {
            entry.setId(share.getId());
        }
        if (share.getUserId() > 0) {
            entry.setOwnerId(share.getUserId());
        }
        entry.setEntryProps(pros);

        return entry;

    }

    public static Entry photoTransfer2EntryUtil(Photo photo, Entry entry, CommentStrategy strategy,
            int entryOwnerId, long entryId) {

        if (photo == null) {
            return null;
        }

        if (entry == null) {
            entry = new Entry();
        }

        //以后有需要继续加
        Map<String, String> pros = new HashMap<String, String>();
        pros.put(EntryConfig.ENTRY_TITLE, photo.getTitle());
        pros.put(EntryConfig.ENTRY_CONTROL, photo.getControl() + "");
        pros.put(EntryConfig.ENTRY_CREATE_TIME, photo.getTime().getTime() + "");
        //photo with list比较特殊，放在comment strategy里
        if (photo.getPhotoWithList() != null && PhotoStateUtil.isHasWith(photo)) {
            strategy.setPhotoWiths(photo.getPhotoWithList());
        }
        pros.put(EntryConfig.ENTRY_IS_HEAD_FEED,
                String.valueOf(PhotoStateUtil.isWithHeadFeed(photo)));
        pros.put(EntryConfig.ENTRY_IS_MULTI, String.valueOf(PhotoStateUtil.isMultiPhoto(photo)));
        pros.put(EntryConfig.ENTRY_IS_TAG, String.valueOf(PhotoStateUtil.isWithTag(photo)));
        pros.put(EntryConfig.ENTRY_IS_WITH, String.valueOf(PhotoStateUtil.isHasWith(photo)));
        pros.put(EntryConfig.ENTRY_IS_708Feed, String.valueOf(PhotoStateUtil.isWith708Feed(photo)));

        pros.put(EntryConfig.ENTRY_HEADURL, photo.getHeadUrl());
        pros.put(EntryConfig.ENTRY_LARGEURL, photo.getLargeUrl());

        Album album = photo.getAlbum();
        if (album != null) {
            //photo的加密是由album来决定的
            pros.put(EntryConfig.ENTRY_IS_PASSWORD_PROTECTED,
                    String.valueOf(album.isPasswordProtected()));
            pros.put(EntryConfig.ENTRY_PARENT_CREATE_TIME, album.getTime().getTime() + "");
            pros.put(EntryConfig.ENTRY_PARENT_ID, album.getId() + "");
            pros.put(EntryConfig.ENTRY_PARENT_IS_PASSWORD_PROTECTED,
                    String.valueOf(album.isPasswordProtected()));
            pros.put(EntryConfig.ENTRY_PARENT_CONTROL, album.getControl() + "");
            pros.put(EntryConfig.ENTRY_PARENT_NAME,
                    AntiSpamUtils.getInstance().htmlSecurityEscape(album.getName()));
        }
        //获取"足迹"信息
        if (strategy.getParams() != null
                && strategy.getParams().containsKey(EntryConfig.ENTRY_HAS_ZUJI)) {
            strategy.setZujiComment(true);
            pros.put(EntryConfig.ENTRY_ZUJI_ID, strategy.getParams().get(EntryConfig.ENTRY_ZUJI_ID));
            pros.put(EntryConfig.ENTRY_ZUJISITE_NAME,
                    strategy.getParams().get(EntryConfig.ENTRY_ZUJISITE_NAME));
            pros.put(EntryConfig.ENTRY_ZUJISITE_ID,
                    strategy.getParams().get(EntryConfig.ENTRY_ZUJISITE_ID));
            pros.put(EntryConfig.ENTRY_ZUJIITEM_ID,
                    strategy.getParams().get(EntryConfig.ENTRY_ZUJIITEM_ID));
        }
        entry.setEntryProps(pros);

        if (entry.getId() <= 0) {
            entry.setId(entryId);
        }
        if (entry.getOwnerId() <= 0) {
            entry.setOwnerId(entryOwnerId);
        }
        //获取子相册id
        pros.put(EntryConfig.ENTRY_CHILD_PARENT_ID, photo.getChildAlbum() + "");
        //统一输出为小写字母
        entry.setType(CommentType.Photo.toString().toLowerCase());

        return entry;

    }

    public static Entry albumTransfer2EntryUtil(Album album, Entry entry, long entryId,
            int entryOwnerId,Comment comment, CommentStrategy strategy) {
        
        if (album == null) {
            return null;
        }

        if (entry == null) {
            entry = new Entry();
        }

        //以后有需要继续加
        Map<String, String> pros = new HashMap<String, String>();
        pros.put(EntryConfig.ENTRY_TITLE, album.getName() + "");
        pros.put(EntryConfig.ENTRY_CREATE_TIME, album.getTime().getTime() + "");
        pros.put(EntryConfig.ENTRY_CONTROL, album.getControl() + "");
        pros.put(EntryConfig.ENTRY_IS_PASSWORD_PROTECTED,
                String.valueOf(album.getPasswordProtected() > 0));
        pros.put(EntryConfig.ENTRY_HEADURL,album.getHeadUrl());
        entry.setEntryProps(pros);

        if (entry.getId() <= 0) {
            entry.setId(entryId);
        }
        if (entry.getOwnerId() <= 0) {
            entry.setOwnerId(entryOwnerId);
        }
        entry.setType(CommentType.Album.toString().toLowerCase());
        
        //关联评论
//        if(comment != null && comment.isLinked() && (strategy.getCommentLinkedInfos() == null || strategy.getCommentLinkedInfos().size() == 0)){
//            //被标识了关联评论，但是没有关联评论的信息,我们才会取
//            if(album.getParentAlbumId() != 0 && album.getParentAlbumId() != entryId){
//                CommentLinkedInfo info = new CommentLinkedInfo();
//                info.setEntryId(album.getParentAlbumId());
//                info.setEntryOwnerId(entryOwnerId);
//                info.setCommentType(CommentType.Album.getValue());
//                
//                List<CommentLinkedInfo> commentLinkedInfos = new ArrayList<CommentLinkedInfo>();
//                commentLinkedInfos.add(info);
//                strategy.setCommentLinkedInfos(commentLinkedInfos);
//            }
//        }
        
        //设置父相册id
         long parentEntryId = album.getParentAlbumId() != 0 && album.getParentAlbumId() != entryId ? album.getParentAlbumId() : entryId;
         strategy.setParentEntryId(parentEntryId);

        return entry;

    }
    
    public static Entry videoTransfer2EntryUtil(Shortvideo video,
            Entry entry, int entryOwnerId, long entryId) {

        if (video == null) {
            return null;
        }

        if (entry == null) {
            entry = new Entry();
        }

        //以后有需要继续加
        Map<String, String> pros = new HashMap<String, String>();
        pros.put(EntryConfig.ENTRY_TITLE, video.getTitle());
        pros.put(EntryConfig.ENTRY_HEADURL,StringUtils.isEmpty(video.getTinyThumbUrl()) ? "":video.getTinyThumbUrl());
        pros.put(EntryConfig.ENTRY_URL, String.format(IShortvideoServiceConstants.TERMINAL_FORMAT_URL_STRING, entryOwnerId,entryId));
        entry.setEntryProps(pros);
        if (entry.getId() <= 0) {
            entry.setId(entryId);
        }
        if (entry.getOwnerId() <= 0) {
            entry.setOwnerId(entryOwnerId);
        }
        entry.setType(CommentType.ShortVideo.toString().toLowerCase());

        return entry;

    }
    
    public static Entry postsTransfer2EntryUtil(Posts posts,
            Entry entry, int entryOwnerId, long entryId) {

        if (posts == null) {
            return null;
        }

        if (entry == null) {
            entry = new Entry();
        }

        //以后有需要继续加
        Map<String, String> pros = new HashMap<String, String>();
        
        pros.put(EntryConfig.ENTRY_URL, String.format(CommentCenterConsts.POST_TERMINAL_FORMAT_URL_STRING, entryOwnerId,entryId));
        //学校id
        pros.put(EntryConfig.ENTRY_SCHOOL_ID, posts.getSchoolId()+"");
        //模块id
        pros.put(EntryConfig.ENTRY_PARENT_ID, posts.getModuleId()+"");
        //帖子的创建时间
        pros.put(EntryConfig.ENTRY_CAMPUS_POST_CREATETIME, posts.getCreateTime()+"");
        //从summary中取出第一张图片的url
        String summary = posts.getSummary();
        pros.put(EntryConfig.ENTRY_HEADURL,  ImgJsonUtil.getCampusPic(summary));
        pros.put(EntryConfig.ENTRY_CONTENT,  ImgJsonUtil.getCampusText(summary));
        
        //因为校园广场帖子没有title，所以需要截断content里面的内容作为tiltle，以便发送@
        if("".equals(posts.getTitle())){
        	if(pros.get(EntryConfig.ENTRY_CONTENT).length()<=6){
        		pros.put(EntryConfig.ENTRY_TITLE,pros.get(EntryConfig.ENTRY_CONTENT));
        	}else {
        		pros.put(EntryConfig.ENTRY_TITLE,pros.get(EntryConfig.ENTRY_CONTENT).substring(0, 5)+"...");
        	}
        }else{
        	 pros.put(EntryConfig.ENTRY_TITLE, posts.getTitle());
        }   
        entry.setEntryProps(pros);
        if (entry.getId() <= 0) {
            entry.setId(entryId);
        }
        if (entry.getOwnerId() <= 0) {
            entry.setOwnerId(entryOwnerId);
        }
        entry.setType(CommentType.CampusPost.toString().toLowerCase());
        
        

        return entry;

    }
    
    public static Entry albumPostsTransfer2EntryUtil(com.renren.xoa2.api.campus.model.Album album,
            Entry entry, int entryOwnerId, long entryId) {

        if (album == null) {
            return null;
        }

        if (entry == null) {
            entry = new Entry();
        }

        //以后有需要继续加
        Map<String, String> pros = new HashMap<String, String>();
        pros.put(EntryConfig.ENTRY_TITLE, album.getDescription());
        pros.put(EntryConfig.ENTRY_URL, String.format(CommentCenterConsts.ALBUM_POST_TERMINAL_FORMAT_URL_STRING, entryOwnerId,entryId));
        //学校id
        pros.put(EntryConfig.ENTRY_SCHOOL_ID, album.getSchoolId()+"");
        //模块id
        pros.put(EntryConfig.ENTRY_PARENT_ID, album.getModuleId()+"");
        //帖子的创建时间
        pros.put(EntryConfig.ENTRY_CAMPUS_POST_CREATETIME, album.getCreateTime()+"");
        //取出封面url
        String mainUrl = album.getMainUrl();
        pros.put(EntryConfig.ENTRY_HEADURL,  mainUrl);
        entry.setEntryProps(pros);
        if (entry.getId() <= 0) {
            entry.setId(entryId);
        }
        if (entry.getOwnerId() <= 0) {
            entry.setOwnerId(entryOwnerId);
        }
        entry.setType(CommentType.CampusPost.toString().toLowerCase());
        
        

        return entry;

    }
    
    public static Entry topPostsTransfer2EntryUtil(TopAd topAd,
            Entry entry, int entryOwnerId, long entryId, CommentStrategy commentStrategy) {

        if (topAd == null) {
            return null;
        }

        if (entry == null) {
            entry = new Entry();
        }

        //以后有需要继续加
        Map<String, String> pros = new HashMap<String, String>();
        pros.put(EntryConfig.ENTRY_TITLE, topAd.getTitle());
        if("".equals(topAd.getTitle())){
        	if(pros.get(EntryConfig.ENTRY_CONTENT).length()<=6){
        		pros.put(EntryConfig.ENTRY_TITLE,pros.get(EntryConfig.ENTRY_CONTENT));
        	}else {
        		pros.put(EntryConfig.ENTRY_TITLE,pros.get(EntryConfig.ENTRY_CONTENT).substring(0, 5)+"...");
        	}
        }else{
        	 pros.put(EntryConfig.ENTRY_TITLE, topAd.getTitle());
        }  
        pros.put(EntryConfig.ENTRY_URL, String.format(CommentCenterConsts.TOP_POST_TERMINAL_FORMAT_URL_STRING,entryId));
        //置顶帖子的学校id需要从参数中取的
        pros.put(EntryConfig.ENTRY_SCHOOL_ID, commentStrategy.getSchoolId()+"");
        //取出封面url
        pros.put(EntryConfig.ENTRY_HEADURL,  StringUtils.isNotEmpty(topAd.getTitleImageUrl()) ? topAd.getTitleImageUrl() : "");
        pros.put(EntryConfig.ENTRY_CONTENT,  StringUtils.isNotEmpty(topAd.getContent()) ? topAd.getContent() : "");
        entry.setEntryProps(pros);
        if (entry.getId() <= 0) {
            entry.setId(entryId);
        }
        if (entry.getOwnerId() <= 0) {
            entry.setOwnerId(entryOwnerId);
        }
        entry.setType(CommentType.CampusTop.toString().toLowerCase());

        return entry;
    }
    
    public static Entry campusExcellentTransfer2EntryUtil(CampusFeed campusFeed,
            Entry entry, int entryOwnerId, long entryId) {

        if (campusFeed == null) {
            return null;
        }

        if (entry == null) {
            entry = new Entry();
        }
        
        //这里的content是校园主页新鲜事的xml
        int feedType = campusFeed.getSType();
        long resourceId = campusFeed.getSourceId();
        
        String feedStrType = AlbumPostUtil.getFeedStringType(feedType);
        
        //解析新鲜事xml,我们需要取出以下字段:
        //1.resourceOwnerId 2.title 3.img
        Document document = AlbumPostUtil.getFeedContentDocument(campusFeed);
        if(StringUtils.isNotBlank(feedStrType) || document == null){
	        Map<String, String> pros = new HashMap<String, String>();
		        String title = AlbumPostUtil.getFeedContentByPath(document, feedStrType, AlbumPostUtil.TITLE_PATH);
		        String resourceOwnerStr = AlbumPostUtil.getFeedContentByPath(document, feedStrType, AlbumPostUtil.RESOURCE_OWNERID_PATH);
		        int resourceOwnerId = 0;
		        try{
		        	resourceOwnerId = Integer.parseInt(resourceOwnerStr);
		        } catch (Exception e){
		        	logger.error("getResourceOwnerId error,str = " + resourceOwnerStr);
		        }
		        String imgUrl = AlbumPostUtil.getFeedContentByPath(document, feedStrType, AlbumPostUtil.THUMBURL_PATH);
		        //新鲜事名字
		        String shareName = AlbumPostUtil.getFeedContentByPath(document, AlbumPostUtil.SHARE_NAME_PATH);
		        //新鲜事时间戳
		        String shareTime = AlbumPostUtil.getFeedContentByPath(document, AlbumPostUtil.SHARE_TIME_PATH);
		        //新鲜事img
		        String shareImg = AlbumPostUtil.getFeedContentByPath(document, AlbumPostUtil.SHARE_IMG_PATH);
		        
		        pros.put(EntryConfig.ENTRY_SHARE_RESOURCE_ID, resourceId+"");
		        pros.put(EntryConfig.ENTRY_SHARE_RESOURCE_USER_ID, resourceOwnerId+"");
		        pros.put(EntryConfig.ENTRY_HEADURL, imgUrl);
		        pros.put(EntryConfig.ENTRY_CAMPUS_SHARE_TYPE, feedType+"");
		        pros.put(EntryConfig.ENTRY_TITLE, StringUtils.isEmpty(title) ? "本校热门分享": title );
		        pros.put(EntryConfig.ENTRY_URL, String.format(CommentCenterConsts.EXCELLENT_POST_TERMINAL_FORMAT_URL_STRING,entryOwnerId,entryId));
		        //学校id
		        pros.put(EntryConfig.ENTRY_SCHOOL_ID, entryOwnerId+"");
		        pros.put(EntryConfig.ENTRY_CAMPUS_SHARE_NAME, shareName);
		        pros.put(EntryConfig.ENTRY_CAMPUS_SHARE_TIME, shareTime);
		        pros.put(EntryConfig.ENTRY_CAMPUS_SHARE_IMG, shareImg);
		        entry.setEntryProps(pros);
        } else {
        	logger.error("parseCampusFeedError,feedContent = " + campusFeed.getContent() + ",stype = " + feedType);
        }
        if (entry.getId() <= 0) {
            entry.setId(entryId);
        }
        if (entry.getOwnerId() <= 0) {
            entry.setOwnerId(entryOwnerId);
        }
        entry.setType(CommentType.CampusExcellent.toString().toLowerCase());

        return entry;
    }

}
