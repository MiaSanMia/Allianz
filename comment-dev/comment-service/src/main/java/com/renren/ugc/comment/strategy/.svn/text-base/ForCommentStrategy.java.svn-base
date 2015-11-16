package com.renren.ugc.comment.strategy;


/**
 * 针对批量创建接口产生的类
 * 
 * 原有的Stragegy逻辑是：在invoke调用service的时候会取到CommentStrategy的config配置
 * 来决定这次调用来用哪些过滤器，而且方法参数最后一个必须是CommentStrategy类型。而在servie进行处理
 * 的时候CommentStrateg又跟comment唯一对应。导致客户端必须传一个list《CommentStrategy》参数。
 * 这样的话invoke不成功。所以才有现在的结构，在批量创建接口的方法里吧CommentStrategy进行语义上的拆分，
 * 更容易理解。
 * 
 * 这个类是用来做唯一对应Comment的strate的，不加任何冗余字段，只是在之后的调用中进行语义区分。
 * 
 * @author Liu-Yao
 *
 */
public class ForCommentStrategy extends CommentStrategy {

}
