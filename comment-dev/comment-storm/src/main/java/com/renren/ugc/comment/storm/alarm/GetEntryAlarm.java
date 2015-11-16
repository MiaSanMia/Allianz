package com.renren.ugc.comment.storm.alarm;

import java.util.Date;

import com.renren.ugc.comment.storm.model.CommentAccessLogEntry;
import com.renren.ugc.comment.storm.utils.Constants;
import com.renren.ugc.comment.storm.utils.TimeUtils;

public class GetEntryAlarm extends BaseAlarm {

	@Override
	public void shouldSendAlarm() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getAvgThreshold() {
		return Constants.ALARM_API_AVG_THRESHOLD;
	}

	@Override
	public int getMaxThreshold() {
		return Constants.ALARM_API_MAX_THRESHOLD;
	}

	@Override
	public int getExceptionThreshold() {
		return Constants.ALARM_API_EXCEPTION_RATE_THRESHOLD;
	}

	@Override
	public int getCountThreshold() {
		return 0;
	}

	@Override
	public int getTimoutThreshold() {
		return Constants.ALARM_API_TIMEOUT_RATE_THRESHOLD;
	}

}
