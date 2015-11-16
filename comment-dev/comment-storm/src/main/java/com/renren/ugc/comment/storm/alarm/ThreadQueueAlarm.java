package com.renren.ugc.comment.storm.alarm;

import java.util.Date;

import com.renren.ugc.comment.storm.model.CommentAccessLogEntry;
import com.renren.ugc.comment.storm.utils.Constants;
import com.renren.ugc.comment.storm.utils.TimeUtils;

public class ThreadQueueAlarm extends BaseAlarm{

	@Override
	public void shouldSendAlarm() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getAvgThreshold() {
		return 0;
	}

	@Override
	public int getMaxThreshold() {
		return 0;
	}

	@Override
	public int getExceptionThreshold() {
		return 0;
	}

	@Override
	public int getCountThreshold() {
		return 200;
	}

	@Override
	public int getTimoutThreshold() {
		return 0;
	}

}
