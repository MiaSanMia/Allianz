package com.renren.ugc.comment.strategy;

import java.util.List;

import com.renren.ugc.comment.model.CommentPackage;

/**
 * 针对于这次调用的Strategy。在xoa2 server的时候进行interceptor的配置用。
 *
 * @author Liu-Yao
 *
 */
public class ForInvokeStrategy extends CommentStrategy {
	List<CommentPackage> packageList ;

	public List<CommentPackage> getPackageList() {
		return packageList;
	}

	public void setPackageList(List<CommentPackage> packageList) {
		this.packageList = packageList;
	}
	
}
